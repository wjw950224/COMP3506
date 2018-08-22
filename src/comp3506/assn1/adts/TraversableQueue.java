package comp3506.assn1.adts;

import java.util.Iterator;

public class TraversableQueue<T> implements IterableQueue<T> {
	private LinkedNode<T> head;
	private LinkedNode<T> tail;
	private int size = 0;
	
	private class LinkedNode<T> {
		LinkedNode<T> next = null;
		LinkedNode<T> previous = null;
		T element = null;
	}
	
	private class Itr implements Iterator<T> {
		private LinkedNode<T> current;
		//private int index = 0;
		private LinkedNode<T> initNode;
		
		public Itr() {
			//LinkedNode<T> first = new LinkedNode<T>();
			//this.current = first;
			initNode = new LinkedNode<T>();
			initNode.next = head;
			initNode.previous = null;
			this.current = initNode;
		}
		
		@Override
		public boolean hasNext() {
			if (this.current.next.equals(head)) {
				if (this.current.next.next != null) {
					return true;
				} else {
					return false;
				}
			}
			if (this.current.next != null) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public T next() {
			this.current = this.current.next;
			return this.current.element;
		}
		
		@Override
		public void remove() {
			this.current.previous.next = this.current.next;
			this.current.next.previous = this.current.previous;
			this.current = this.current.next;
		}
		
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
	}
	

	@Override
	public T dequeue() throws IndexOutOfBoundsException {
		//this.head.next.previous = null;
		if (size == 0) {
			throw new IndexOutOfBoundsException("Empty Queue");
		}
		T element = this.head.element;
		if (this.head.next != null) {
			this.head = this.head.next;
			this.head.previous = null;
		} else {
			this.head = null;
		}
		this.size--;
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
