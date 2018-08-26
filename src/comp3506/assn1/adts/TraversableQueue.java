package comp3506.assn1.adts;
import java.util.Iterator;

/**
 * Queue
 *
 * @author Jingwei WANG
 *
 * @param <T> The type of element held in the data structure.
 */
public class TraversableQueue<T> implements IterableQueue<T> {
	private LinkedNode<T> head;
	private LinkedNode<T> tail;
	private int size = 0;

	@SuppressWarnings("hiding")
	private class LinkedNode<T> {
		LinkedNode<T> next = null;
		@SuppressWarnings("unused")
		LinkedNode<T> previous = null;
		T element = null;
	}
	
	private class Itr implements Iterator<T> {
        private int lastSize;
		private LinkedNode<T> current;
		private LinkedNode<T> initNode;
		Itr() {
			this.initNode = new LinkedNode<T>();
			this.initNode.next = head;
			this.initNode.previous = null;
			this.current = initNode;
			head.previous = initNode;
			this.lastSize = size;
		}
		
		@Override
		public boolean hasNext() {
			if (size() < lastSize) {
				for (int i = 0; i < lastSize - size(); i++) {
				    if (size() <= 1) {
				        return false;
                    }
					this.current = this.current.next;
				}
				lastSize = size();
			}
			if (this.current.next != null) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public T next() throws java.util.NoSuchElementException {
			if (size() < lastSize) {
				for (int i = 0; i < lastSize - size(); i++) {
					if (size() < 1) {
						break;
					}
					this.current = this.current.next;
				}
				lastSize = size();
			}
		    if (hasNext()){
                this.current = this.current.next;
                return this.current.element;
            }
            return null;
        }
	}

    /**
     * Add a new element to the end of the queue.
     *
     * Run-time: O(1)
     *
     * @param element The element to be added to the queue.
     * @throws IllegalStateException Queue cannot accept a new element (e.g. queue space is full).
     */
	@Override
	public void enqueue(T element) throws IllegalStateException {
		LinkedNode<T> newNode = new LinkedNode<T>();
		newNode.element = element;
		newNode.next = null;
		if (size == 0) {
			newNode.previous = null;
			this.head = newNode;
		} else {
			newNode.previous = this.tail;
			this.tail.next = newNode;
		}
		this.tail = newNode;
		this.size++;
	}

    /**
     * Remove and return the element at the head of the queue.
     *
     * Run-time: O(1)
     *
     * @return Element at that was at the head of the queue.
     * @throws IndexOutOfBoundsException Queue is empty and nothing can be dequeued.
     */
	@Override
	public T dequeue() throws IndexOutOfBoundsException {
		if (size == 0) {
			throw new IndexOutOfBoundsException();
		}
		T element = this.head.element;
		if (this.head.next != null) {
			this.head = this.head.next;
			this.head.previous = null;
		} else {
			this.head = null;
			this.tail = null;
		}
		this.size--;
		return element;
	}

    /**
     * Return size of element.
     * 
     * Run-time: O(1)
     *
     * @return Number of elements in the queue.
     */
	@Override
	public int size() {
		return this.size;
	}

    /**
     * return iterator
     * 
     * Run-time: O(1)
     *
     * 
     * @return iterator.
     */
	@Override
	public Iterator<T> iterator() {
		return new Itr();
	}
}
