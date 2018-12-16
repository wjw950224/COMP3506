package comp3506.assn2.utils;

/**
 * Trie implementation for quick searching stop words
 *
 * Memory usage: O(n)
 * n: number of characters stored in this trie
 * At worst case, all word has no same prefix
 *
 * @author Youwen Mao
 */

public class Lookups {

    LinkedList head; //first level elements

    /**
     * Lookups Node
     */
    public class LookupsNode {
        char c;
        public boolean isWord;
        public LinkedList children;
        public int len;
    }
    /**
     * Initialization
     */
    public Lookups() {
        head = new LinkedList();
    }

    /**
     * Add new word into this trie
     *
     * Run-time: O(n)
     * n: length of this character
     * There is no word has the same prefix with new word,
     *
     * @param word
     */
    public void add(String word) {
        LinkedList current = head;
        Lookups.LookupsNode tmp;
        Lookups.LookupsNode top = null;
        int length = word.length();
        for (int i = 0; i < length; i++) {
            if ((tmp = find(current, word.charAt(i))) != null) {
                if (i == 0) {
                    top = tmp;
                }
                if (i == (length - 1)) {
                    tmp.isWord = true;
                } else {
                    if (tmp.children == null) {
                        tmp.children = new LinkedList();
                    }
                    current = tmp.children;
                }
            } else {
                Lookups.LookupsNode newNode = new Lookups.LookupsNode();
                if (i == 0) {
                    top = newNode;
                }
                newNode.c = word.charAt(i);
                current.add(newNode);
                if (i == (length - 1)) {
                    newNode.isWord = true;
                } else {
                    newNode.children = new LinkedList();
                    current = newNode.children;
                }
            }
        }
        if (top.len < length) {
            top.len = length;
        }
    }

    /**
     *
     * Check if this char exist in this trie
     *
     * Run-time: O(n)
     * n: number of children of parent char
     * If there is no such a child in parent char or the child matches at the end of list, it will go through all children.
     *
     * @param list parent char
     * @param c character is looking for
     * @return char node if result found, null otherwise
     */
    public LookupsNode find(LinkedList list, char c) {
        if (list.head != null) {
            Node current = list.head;
            while (current != null) {
                if (((Lookups.LookupsNode)(current.elem)).c == c) {
                    return (Lookups.LookupsNode)(current.elem);
                }
                current = current.next;
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * Get the list of first level
     *
     * Run-time: O(1)
     * head can be access directly
     *
     * @return LinkedList of all first level elements
     */
    public LinkedList getHead() {
        return this.head;
    }
}
