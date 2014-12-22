package org.indexer.core.trie;

import net.jcip.annotations.ThreadSafe;
import org.indexer.core.trie.visitors.AccumulatingVisitor;
import org.indexer.core.trie.visitors.NodeCalculatingVisitor;
import org.indexer.core.trie.visitors.TerminalNodeCalculatingVisitor;
import org.indexer.core.trie.visitors.PatternsSearchVisitor;

import java.io.Serializable;
import java.util.*;

/**
 * Class representing Trie data structure.
 * (used with suffix links for example in Aho–Corasick algorithm)
 */
@ThreadSafe
public class Trie implements Serializable {

    /**
     * Root node of this trie.
     */
    private final TrieNode root = new TrieNode();

//    public TrieNode getRoot() {
//        return root;
//    }

    /**
     * Method for inserting pattern to the existing trie;
     *
     * @param pattern - patters to insert
     */
    public void addPattern(String pattern) {
        TrieNode currentNode = root;
        for (Character c : pattern.toCharArray()) {
            TrieNode child = currentNode.getOrAddChild(c);
            currentNode = child;
        }
        currentNode.setTerminal();
    }

    /**
     * Caclulating number of all nodes in trie.
     * Can be useful for collecting statistics.
     *
     * @return int - total number of nodes
     */
    public int calculateNodes() {
        return (Integer) preOrderTraverse(root, new NodeCalculatingVisitor());
    }

    /**
     * Caclulating number of terminal nodes, i.e. nodes
     * which are end of one of the inserted suffixes.
     *
     * @return int - number of terminal nodes
     */
    public int calculateTerminalNodes() {
        return (Integer) preOrderTraverse(root, new TerminalNodeCalculatingVisitor());
    }

    /**
     * Method made traversal around the trie.
     * Guaranted that each node will be visited and
     * that it will be visited only once.
     *
     * @param node - node to start traverse from.
     * @param accumulatingVisitor - visitor to call on each node.
     * @return Object - results, depends totally on visitor type.
     */
    private Object preOrderTraverse(TrieNode node, AccumulatingVisitor accumulatingVisitor) {
        Deque<TrieNodeWrapper> stack = new ArrayDeque<>();
        TrieNodeWrapper currentNodeWrapper = new TrieNodeWrapper(node, -1);
        pushChilds(stack, currentNodeWrapper);
        while (!stack.isEmpty()) {
            currentNodeWrapper = stack.pop();
            currentNodeWrapper.accept(accumulatingVisitor);
            pushChilds(stack, currentNodeWrapper);
        }
        return accumulatingVisitor.getResults();
    }

    /**
     * Add child nodes to stack. Method only used for internal purposes
     * to reduce the code length in preOrderTraverse method.
     *
     * @param stack - stack, containing nodes on the current path (see DFS algorithm description)
     * @param node - node, which child to push to stack
     */
    private void pushChilds(Deque<TrieNodeWrapper> stack, TrieNodeWrapper node) {
        for (TrieNodeWrapper child : node.getChilds()) {
            stack.push(child);
        }
    }

    /**
     * Returns all the patterns in this trie, which starts with prefix.
     *
     * @param prefix
     *        string prefix
     * @return List of pattern strings with prefix, containing in this trie.
     */
    public List<String> getPatternsWithPrefix(String prefix) {
        TrieNode node = getLastNodeForPrefix(prefix);
        List<String> fullResults = new ArrayList<>();
        if (node.isTerminal()) {
            fullResults.add(prefix);
        }
        List<String> results = (List<String>) preOrderTraverse(node, new PatternsSearchVisitor());
        for (String result : results) {
            fullResults.add(prefix + result);
        }
        return fullResults;
    }

    /**
     * Returns trie node, representing the last symbol of pattern
     *
     * @param pattern - pattern to search
     * @return trie node, representing the last symbol of pattern or null if
     * pattern didn't find.
     */
    private TrieNode getLastNodeForPrefix(String pattern) {
        TrieNode currentNode = root;
        for (Character c : pattern.toCharArray()) {
            if (currentNode != null) {
                currentNode = currentNode.getChild(c);
            } else {
                return null;
            }
        }
        return currentNode;
    }
}
