package org.indexer.core.index;

import net.jcip.annotations.ThreadSafe;
import org.indexer.core.exception.PostingListLoadException;

import java.io.*;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * List, containing links to occurences of a particular word in collection of files.
 */
@ThreadSafe
public class PostingList implements Serializable {

    private static final AtomicLong UNIQUE_ID = new AtomicLong(0);
    private final long uid;
    volatile SortedSet<PostingNode> postingNodeList = new TreeSet<>();
    String postingListPath;
    private boolean loaded = true;

    public PostingList() {
        uid = UNIQUE_ID.incrementAndGet();
        postingListPath = System.getProperty("user.dir") + "/postings/" + "posting-" + uid;
    }

    public long getUid() {
        return uid;
    }

    public synchronized void add(PostingNode postingNode) {
        if (!loaded) {
            load();
        }
        postingNodeList.add(postingNode);
    }

    public synchronized Collection<PostingNode> getNodes() {
        if (!loaded) {
            load();
        }
        return new ConcurrentSkipListSet<>(postingNodeList);
    }

    private void makeDirectoryExists(String path) {
        int indexOfSlash = path.lastIndexOf("/");
        path = path.substring(0, indexOfSlash);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public synchronized void unload() {
        long unloaded = 0 - postingNodeList.size();
        if (loaded) {
            try {
                makeDirectoryExists(postingListPath);
                FileOutputStream fileOutputStream = new FileOutputStream(postingListPath);
                ObjectOutputStream objectStream = new ObjectOutputStream(fileOutputStream);
                objectStream.writeObject(postingNodeList);
                objectStream.flush();
                objectStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                throw new PostingListLoadException(e);
            }
            postingNodeList = null;
            loaded = false;
        }
    }

    public synchronized void load() {
        if (!loaded) {
            try {
                makeDirectoryExists(postingListPath);
                FileInputStream fileInputStream = new FileInputStream(postingListPath);
                ObjectInputStream objectStream = new ObjectInputStream(fileInputStream);
                postingNodeList = (SortedSet) objectStream.readObject();
                File loadedFrom = new File(postingListPath);
                loadedFrom.delete();
            } catch (IOException e) {
                throw new PostingListLoadException(e);
            } catch (ClassNotFoundException e) {
                throw new PostingListLoadException(e);
            }
            loaded = true;
        }
    }

    public synchronized int size() {
        if (!loaded) {
            load();
        }
        return postingNodeList.size();
    }
}
