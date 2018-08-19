package comp3506.assn1.adts;

import java.util.Iterator;

public class TraversableQueue<T> implements IterableQueue<T> {
	private LinkedNode<T> head;
	private LinkedNode<T> tail;
	private int size = 0;
	
	private class LinkedNode<T> {
		LinkedNode<T> next;
		LinkedNode<T> previous;
		T element;
	}
	
	private class Itr<T> implements Iterator<T> {
		private LinkedNode<T> current;
		private int index = 0;
		
		private Itr() {
			LinkedNode<T> first = new LinkedNode<T>();
			this.current = first;
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
		public T next() {
			if (index != 0) {
				this.current = current.next;
			}
			index++;
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
		if (size == 0) {
			newNode.previous = null;
			newNode.next = null;
			this.head = newNode;
			this.tail = newNode;
		} else {
			newNode.previous = this.tail;
			newNode.next = null;
			this.tail.next = newNode;
			this.tail = newNode;
		}
		this.size++;
	}
	

	@Override
	public T dequeue() throws IndexOutOfBoundsException {
		LinkedNode<T> tempNode = this.head;
		this.head = tempNode.next;
		this.head.previous = null;
		this.size--;
		return tempNode.element;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public Iterator<T> iterator() {
		Itr<T> itr = new Itr<T>();
		return itr;
	}




}
