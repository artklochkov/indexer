package org.indexer.core.cache;

import org.indexer.core.index.PostingList;
import org.indexer.core.index.PostingNode;

import java.util.Collection;

/**
 * Main class for managing work our with PostingList.
 * Responsible for loading and saving posting lists,
 * as the resources exhausted
 */
public class CachedPostingListManager {

    /**
     * LruCache containing loaded posting lists.
     */
    private final PostingListLruCache postingListLruCache = new PostingListLruCache();

    /**
     * Gets copy of PostingNode Set containing in PostingList.
     *
     * @param pattern - pattern, to search posting list for
     * @return Collection of posting nodes in posting list for pattern
     */
    public Collection<PostingNode> getPostingList(String pattern) {
        PostingList postingList = postingListLruCache.getPostingList(pattern, false);
        return postingList.getNodes();
    }

    /**
     * Adds new posting node to existing posting list,
     * or create new PostingList if necessary.
     *
     * @param pattern     - pattern, to which posting list belongs to.
     * @param postingNode - new PostingNode to add
     */
    public void addToPostingList(String pattern, PostingNode postingNode) {
        PostingList postingList = postingListLruCache.getPostingList(pattern, true);
        postingList.add(postingNode);
    }
}
