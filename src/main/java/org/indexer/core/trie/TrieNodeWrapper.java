package org.indexer.core.trie;

import org.indexer.core.trie.visitors.AccumulatingVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Wrapper for TrieNode. Useful for algorithms like DFS,
 * where it is necessary to store not only node on stack,
 * but also node's depth in Trie.
 */
public class TrieNodeWrapper {

    /**
     * internal TrieNode
     */
    private final TrieNode node;

    /**
     * Depth of the node in Trie
     */
    private final int depth;

    public TrieNodeWrapper(TrieNode node, int depth) {
        this.node = node;
        this.depth = depth;
    }

    public TrieNode getNode() {
        return node;
    }

    public int getDepth() {
        return depth;
    }

    /**
     * Part of the Visitor pattern. Give the ability to
     * implement different algorithms on Trie, without
     * necessarity to add additional information to Node.
     *
     * @param visitor - specific visitor
     */
    public void accept(AccumulatingVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Returns internal node's character value as String
     *
     * @return internal node's character value as String
     */
    public String getStringRepresentation() {
        return String.valueOf(node.getValue());
    }

    /**
     * Check that the node is terminal, i.e. is the end of
     * one of the patterns in Trie.
     *
     * @return flag, indicating is node internal or not.
     */
    public boolean isTerminal() {
        return node.isTerminal();
    }

    /**
     * Get childs of the wrapped node, wrapped into TrieNodeWrapper.
     *
     * @return childs of the wrapped node, wrapped into TrieNodeWrapper.
     */
    public Collection<TrieNodeWrapper> getChilds() {
        List<TrieNode> childNodesList = new ArrayList<>(node.getChilds());
        Collections.reverse(childNodesList);
        List<TrieNodeWrapper> childNodeWrappers = new ArrayList<>(childNodesList.size());
        for (TrieNode child : childNodesList) {
            childNodeWrappers.add(new TrieNodeWrapper(child, depth + 1));
        }
        return childNodeWrappers;
    }

    /**
     *
     * @return flag, indicating that internal node has childs, or not
     */
    public boolean hasChilds() {
        return !node.getChilds().isEmpty();
    }
}
