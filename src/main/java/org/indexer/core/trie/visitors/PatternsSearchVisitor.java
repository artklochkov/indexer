package org.indexer.core.trie.visitors;

import org.indexer.core.trie.TrieNodeWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor implementation for searching patterns in Trie.
 */
public class PatternsSearchVisitor implements AccumulatingVisitor {

    List<String> results = new ArrayList<>();
    StringBuilder currentPath = new StringBuilder();

    @Override
    public void visit(TrieNodeWrapper nodeWrapper) {
        currentPath.replace(nodeWrapper.getDepth(), currentPath.length(), nodeWrapper.getStringRepresentation());
        if (nodeWrapper.isTerminal()) {
            results.add(currentPath.toString());
        }
    }

    @Override
    public List<String> getResults() {
        return results;
    }
}
