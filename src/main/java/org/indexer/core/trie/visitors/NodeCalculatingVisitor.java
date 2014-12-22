package org.indexer.core.trie.visitors;

import org.indexer.core.trie.TrieNodeWrapper;

/**
 * Visitor implementation for calculating nodes in Trie.
 */
public class NodeCalculatingVisitor implements AccumulatingVisitor {

    int counter = 0;

    @Override
    public void visit(TrieNodeWrapper nodeWrapper) {
        ++counter;
    }

    @Override
    public Integer getResults() {
        return counter;
    }
}
