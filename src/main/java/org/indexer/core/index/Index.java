package org.indexer.core.index;

import net.jcip.annotations.ThreadSafe;
import org.indexer.core.cache.CachedPostingListManager;
import org.indexer.core.trie.Trie;

import java.io.Serializable;
import java.util.*;

/**
 * Index, built upon the underlined tries structures. Gives the API to
 * build an index for a collection of documents and give an answers
 * about occurences in O(sizeof(answer)) time.
 */
@ThreadSafe
public class Index implements Serializable {

    private final Trie suffixesTrie = new Trie();
    private final Trie wordsTrie = new Trie();
    private final CachedPostingListManager cachedPostingListManager = new CachedPostingListManager();

    /**
     * Inserts pattern with to the specified position. Can be useful if
     * we can add resources other than files on disk, for example. Or, if we want to use
     * different methods to read pattern from file.
     *
     * @param pattern    - pattern to insert
     * @param documentId - id of the document
     * @param position   - position in the document
     */
    public void insert(String pattern, int documentId, long position) {
        for (int i = 0; i < pattern.length(); ++i) {
            suffixesTrie.addPattern(pattern.substring(i));
        }
        wordsTrie.addPattern(new StringBuilder(pattern).reverse().toString());
        cachedPostingListManager.addToPostingList(pattern, new PostingNode(documentId, position));
    }

    /**
     * @param pattern - part of the word to search for
     * @return returns all the indexed words, containing pattern
     */
    private List<String> findWordsByPattern(String pattern) {
        List<String> suffixes = suffixesTrie.getPatternsWithPrefix(pattern);
        List<String> words = new ArrayList<>();
        for (String suffix : suffixes) {
            List<String> reversedWords = wordsTrie.getPatternsWithPrefix(new StringBuilder(suffix).reverse().toString());
            for (String reversedWord : reversedWords) {
                words.add(new StringBuilder(reversedWord).reverse().toString());
            }
        }
        return words;
    }

    /**
     * gives all the words containing pattern, with their positions in
     * collection of documents.
     *
     * @param pattern - pattern to search for
     * @return - all the words containing pattern, with their positions in
     * collection of documents.
     */
    public Map<String, Collection<PostingNode>> getPostingNodes(String pattern) {
        List<String> words = findWordsByPattern(pattern);
        Map<String, Collection<PostingNode>> resultMap = new HashMap<>();
        for (String word : words) {
            resultMap.put(word, cachedPostingListManager.getPostingList(word));
        }
        return resultMap;
    }
}
