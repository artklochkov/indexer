package org.indexer.core.trie.visitors;

import org.indexer.core.trie.TrieNodeWrapper;

/**
 * Part of the Visitor pattern. This interface gives the ability to accumulate
 * results in visitor's internal data structure.
 */
public interface AccumulatingVisitor {

    /**
     * visit method (Visitor pattern)
     * @param node
     */
    public void visit(TrieNodeWrapper node);

    /**
     *
     * @return results accumulated in this Visitor
     */
    public Object getResults();
}
