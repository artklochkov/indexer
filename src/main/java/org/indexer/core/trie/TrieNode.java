package org.indexer.core.trie;

import net.jcip.annotations.ThreadSafe;
import org.indexer.core.index.PostingList;

import java.io.Serializable;
import java.util.*;

/**
 * Represents node for Trie data structure.
 */
@ThreadSafe
public class TrieNode implements Serializable {

    /**
     * This symbol described by unicode only for internal purpuses.
     * So it is quite unlikely that we will met it somewhere else.
     */
    private static final char EMPTY_NODE_SYMBOL = '\uffff';

    /**
     * This node's value char
     */
    private final char value;

    /**
     * Child nodes of this node. Can use array instead,
     * if symbols will be from narrow diapason.
     */
    private Map<Character, TrieNode> childs = new TreeMap<Character, TrieNode>();

    /**
     * Flag, represents terminal nodes, i.e. nodes,
     * which is an end of one of the inserted patterns.
     */
    private boolean isTerminal = false;

    /**
     * default constructor to use for root creating only.
     */
    TrieNode() {
        value = '\uffff';
    }

    /**
     * Creates node with specified char value. Most common constructor.
     * @param value
     */
    TrieNode(Character value) {
        this.value = value;
    }

    /**
     * Value getter method
     * @return this node char value
     */
    public Character getValue() {
        return value;
    }

    /**
     * Searching for child with the given value on edge. Add child if didn't find one.
     * @param value
     * @return child node if found, null otherwise.
     */
    public synchronized TrieNode getOrAddChild(Character value) {
        if (childs.containsKey(value)) {
            return childs.get(value);
        } else {
            TrieNode node = new TrieNode(value);
            childs.put(value, node);
            return node;
        }
    }

    /**
     * Returns terminal flag.
     * @return isTerminal flag.
     */
    public boolean isTerminal() {
        return isTerminal;
    }

    /**
     * Makes node terminal. Used in pattern insertions.
     * @return isTerminal flag.
     */
    public void setTerminal() {
        isTerminal = true;
    }

    /**
     * Returns child on the specified edge.
     *
     * @param value
     *        value on edge
     * @return child if found, or null otherwise
     */
    public TrieNode getChild(Character value) {
        return childs.get(value);
    }

    /**
     *
     * @return all childs of this node
     */
    public Collection<TrieNode> getChilds() {
        return childs.values();
    }
}