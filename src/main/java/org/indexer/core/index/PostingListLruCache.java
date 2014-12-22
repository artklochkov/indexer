package org.indexer.core.index;

import net.jcip.annotations.ThreadSafe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple implementation of LRU cache for posting lists.
 */
@ThreadSafe
public class PostingListLruCache {

    private static final long DEFAULT_MAX_NODES = 10000000;
    private static final AtomicLong requestNumber = new AtomicLong(0);
    private static final AtomicLong requestsPassed = new AtomicLong(0);
    private static final AtomicBoolean needToCompact = new AtomicBoolean(false);
    private final long maxNodes;
    private final int COMPACT_FREQUENCY = 1000;
    private final int MAX_UNLOAD_ITERATIONS = 100;
    private final ConcurrentMap<String, PostingList> wordsPostingList = new ConcurrentHashMap<>();
    ConcurrentMap<PostingList, Long> postingListTimestamp = new ConcurrentHashMap<>();
    ConcurrentNavigableMap<Long, PostingList> timestampToPostingList = new ConcurrentSkipListMap<>();

    public PostingListLruCache() {
        maxNodes = DEFAULT_MAX_NODES;
    }

    public PostingListLruCache(long maxNodes) {
        this.maxNodes = maxNodes;
    }

    /**
     * @param pattern     - pattern, to search posting list for
     * @param putIfAbsent - indicating put empty PostingList to map or not.
     * @return postingList for pattern
     */
    public PostingList getPostingList(String pattern, boolean putIfAbsent) {
        if (putIfAbsent) {
            wordsPostingList.putIfAbsent(pattern, new PostingList());
        }
        PostingList postingList = wordsPostingList.get(pattern);
        if (postingList == null) {
            return null;
        }

        synchronized (postingList) {
            Long timestamp = postingListTimestamp.get(postingList);
            if (timestamp != null) {
                timestampToPostingList.remove(timestamp);
            }
            timestamp = requestNumber.incrementAndGet();
            postingListTimestamp.put(postingList, timestamp);
            timestampToPostingList.put(timestamp, postingList);
        }

        synchronized (this) {
            requestsPassed.incrementAndGet();
            needToCompact.set(requestsPassed.compareAndSet(COMPACT_FREQUENCY, 0));
        }

        if (needToCompact.compareAndSet(true, false)) {
            if (getTotallyLoaded() > maxNodes) {
                compact();
            }
        }
        return postingList;
    }

    /**
     * Try to remove least recently used posting lists.
     */
    public synchronized void compact() {
        long toUnload = getTotallyLoaded();
        int counter = 0;
        while (toUnload > (maxNodes * 0.95) && (counter < MAX_UNLOAD_ITERATIONS)) {
            toUnload -= unloadLeastRecentlyUsed();
            ++counter;
        }
    }

    /**
     * unload one least recently used posting list.
     *
     * @return - number of nodes in removed list
     */
    private long unloadLeastRecentlyUsed() {
        Map.Entry<Long, PostingList> entry = timestampToPostingList.pollFirstEntry();
        if (entry == null) {
            return 0;
        }
        PostingList postingList = entry.getValue();

        Long timestamp = entry.getKey();
        synchronized (postingList) {
            if (timestamp.equals(postingListTimestamp.get(postingList))) {
                postingListTimestamp.remove(postingList);
            }
        }
        long unloaded = postingList.size();
        postingList.unload();
        return unloaded;
    }

    /**
     * @return returns an estimation of how many nodes are loaded now.
     */
    private long getTotallyLoaded() {
        long totallyLoaded = 0;
        for (PostingList postingList : timestampToPostingList.values()) {
            totallyLoaded += postingList.size();
        }
        return totallyLoaded;
    }
}
