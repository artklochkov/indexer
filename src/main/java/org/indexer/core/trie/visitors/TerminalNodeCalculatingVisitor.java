package org.indexer.core.trie.visitors;


import org.indexer.core.trie.TrieNodeWrapper;

/**
 * Visitor implementation for calculating terminal nodes (i.e. patterns) in Trie.
 */
public class TerminalNodeCalculatingVisitor implements AccumulatingVisitor {

    int counter = 0;

    @Override
    public void visit(TrieNodeWrapper nodeWrapper) {
        if (nodeWrapper.isTerminal()) {
            ++counter;
        }
    }

    @Override
    public Integer getResults() {
        return counter;
    }
}
