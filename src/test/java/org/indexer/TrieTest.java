package org.indexer;

import org.indexer.core.trie.Trie;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Trie class tests
 */
public class TrieTest extends Assert {

    Trie trie = new Trie();
    private static final String TEST_PATTERN = "abracadabra";
    List<String> suffixes = new ArrayList<>(TEST_PATTERN.length());

    @Before
    public void initTrieForTests() {
        for (int i = 0; i < TEST_PATTERN.length(); ++i) {
            suffixes.add(TEST_PATTERN.substring(i));
        }
        for (String suffix : suffixes) {
            trie.addPattern(suffix);
        }
        Collections.sort(suffixes);
    }

    @Test
    public void testNodesNumber() {
        assertEquals(54, trie.calculateNodes());
    }

    @Test
    public void testTerminalNodesNumber() {
        assertEquals(11, trie.calculateTerminalNodes());
    }

    @Test
    public void suffixOrderTest() {
        List<String> triePatterns = trie.getPatternsWithPrefix("");
        assertEquals(suffixes.size(), triePatterns.size());
        for (int i = 0; i < suffixes.size(); ++i) {
            assertEquals(suffixes.get(i), triePatterns.get(i));
        }
    }

    @Test
    public void patternSearchTest() {
        List<String> patterns = trie.getPatternsWithPrefix("abra");
        assertEquals("abra", patterns.get(0));
        assertEquals("abracadabra", patterns.get(1));

        patterns = trie.getPatternsWithPrefix("ra");
        assertEquals("ra", patterns.get(0));
        assertEquals("racadabra", patterns.get(1));
    }
}
