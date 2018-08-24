package comp3506.assn1.adts;

import java.util.Iterator;

public class TraversableQueue<T> implements IterableQueue<T> {
	private LinkedNode<T> head;
	private LinkedNode<T> tail;
	private int size = 0;
	//private int lastSize = 0;

	private class LinkedNode<T> {
		LinkedNode<T> next = null;
		LinkedNode<T> previous = null;
		T element = null;
	}
	
	private class Itr implements Iterator<T> {
        private int lastSize;
		private LinkedNode<T> current;
		private LinkedNode<T> initNode;
		public Itr() {
			initNode = new LinkedNode<T>();
			initNode.next = head;
			initNode.previous = null;
			this.current = initNode;
			head.previous = initNode;
			lastSize = size;
		}
		
		@Override
		public boolean hasNext() {
			if (this.current.next != null) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public T next() throws java.util.NoSuchElementException {
			/**
            if (size() < lastSize) {
                for (int i = 0; i < lastSize - size(); i++) {
                    this.current = this.current.previous;
                }
                lastSize = size();
            }
            if (size() == 0) {
                this.current = this.initNode;
                this.initNode.next = head;
                return null;
            }**/
		    if (hasNext()){
                this.current = this.current.next;
                return this.current.element;
            }

            return null;
        }

        /**
		@Override
		public void remove() {
			if (this.current.next == null) {
			    return;
            }
            if (this.current.previous != null) {
                this.current.previous.next = this.current.next;
                this.current.next.previous = this.current.previous;
                this.current = this.current.next;
                return;
            }
			this.current = null;
		}**/
		
	}
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
		//this.lastSize = this.size - 1;
	}
	

	@Override
	public T dequeue() throws IndexOutOfBoundsException {
		if (size == 0) {
			return null;
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
        //this.lastSize = this.size + 1;
		return element;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public Iterator<T> iterator() {
		return new Itr();
	}
}
