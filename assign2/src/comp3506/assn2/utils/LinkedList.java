package comp3506.assn2.utils;

/**
 * Linked list
 *
 * Memory usage: O(n)
 * n: number of objects this linked list stored
 * Every object were stored only once
 *
 * @param <T>
 */

public class LinkedList<T> {
    Node head;
    Node tail;

    /**
     * Add new object into list
     *
     * Run-time: O(1)
     * The object will be added at tail, tail can be directly accessed.
     *
     * @param elem to be added
     */
    public void add(T elem) {
        if (tail != null) {
            tail.next = new Node();
            tail.next.last = tail;
            tail = tail.next;
            tail.elem = elem;
        } else {
            head = new Node();
            tail = head;
            head.elem = elem;
        }
    }

    /**
     * Return the head of this list
     *
     * Run-time: O(1)
     * Head can be directly accessed.
     *
     * @return Node
     */
    public Node getHead() {
        return this.head;
    }

    /**
     * Return the tail of this list
     *
     * Run-time: O(1)
     * Tail can be directly accessed.
     *
     * @return Node
     */
    public Node getTail() {
        return this.tail;
    }

}
