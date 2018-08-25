package comp3506.assn1.adts;
import java.util.Iterator;

/**
 * A three-dimensional data structure that holds items in a positional relationship to each other.
 * Each cell in the data structure can hold multiple items.
 * A bounded cube has a specified maximum size in each dimension.
 * The root of each dimension is indexed from zero.
 * 
 * @author Jingwei WANG
 *
 * @param <T> The type of element held in the data structure.
 */
public class BoundedCube<T> implements Cube<T> {
	private int length;
	private int breadth;
	private int height;
	@SuppressWarnings("rawtypes")
	private Node[] cube;


	/**
	 * @param length  Maximum size in the 'x' dimension.
	 * @param breadth Maximum size in the 'y' dimension.
	 * @param height  Maximum size in the 'z' dimension.
	 * @throws IllegalArgumentException If provided dimension sizes are not positive.
	 */
	@SuppressWarnings("unchecked")
	public BoundedCube(int length, int breadth, int height) throws IllegalArgumentException {
        if (length < 0 || breadth < 0|| height < 0) {
            throw new IndexOutOfBoundsException();
        }
		this.length = length;
		this.breadth = breadth;
		this.height = height;
		this.cube = new Node[height];
		for (int i = 0; i < height; i++) {
		    cube[i] = new Node<Object>(10, 0);
		    cube[i].coordinator = i;
		    for (int j = 0; j < 5; j++) {
		        cube[i].next[j] = new Node<Object>(10, 0);
		        cube[i].next2[j] = new Node<Object>(10, 0);
		        for (int k = 0; k < 5; k++) {
                    cube[i].next[j].next[k] = new Node<Object>(0, 0);
                    cube[i].next[j].next2[k] = new Node<Object>(0, 0);
                    cube[i].next2[j].next[k] = new Node<Object>(0, 0);
                    cube[i].next2[j].next2[k] = new Node<Object>(0, 0);
                }
            }
        }
	}

    @SuppressWarnings("hiding")
	private class Node<T> {
        int coordinator;
        int nextNodeSize;
        int next2NodeSize;
        int usedNode;
        int usedNode2;
        @SuppressWarnings("rawtypes")
		Node[] next;
        @SuppressWarnings("rawtypes")
		Node[] next2;
        IterableQueue<T> cells;

        Node(int size, int position)
        {
            coordinator = position;
            nextNodeSize = size/2;
            if (size%2 != 0) {
                next2NodeSize = size/2 + 1;
            } else {
                next2NodeSize = size/2;
            }

            usedNode = 0;
            usedNode2 = 0;
            next = new Node[nextNodeSize];
            next2 = new Node[next2NodeSize];
        }

        @SuppressWarnings("unchecked")
		IterableQueue<T> getCells(int x, int y) {
        	Node<T> yNode;
        	Node<T> xNode;
        	if (y > breadth/2) {
        		for (int i = 0; i < usedNode2; i++) {
        			yNode = next2[i];
                    //Node<T> yNode = next[i];
                    if (yNode.coordinator == y) {
                    	if (x > length/2) {
                    		for (int j = 0; j < yNode.usedNode2; j++) {
                    			xNode = next2[j];
                                //xNode = yNode.next[j];
                                if (xNode.coordinator == x) {
                                    return (IterableQueue<T>) xNode.cells;
                                }
                            }
                    	} else {
                    		for (int j = 0; j < yNode.usedNode; j++) {
                    			xNode = yNode.next[j];
                                //xNode = yNode.next[j];
                                if (xNode.coordinator == x) {
                                    return (IterableQueue<T>) xNode.cells;
                                }
                            }
                    	}
                    	
                    }
                }
        	} else {
        		for (int i = 0; i < usedNode; i++) {
        			yNode = next[i];
                    //Node<T> yNode = next[i];
                    if (yNode.coordinator == y) {
                    	if (x > length/2) {
                    		for (int j = 0; j < yNode.usedNode2; j++) {
                    			xNode = next2[j];
                                //xNode = yNode.next[j];
                                if (xNode.coordinator == x) {
                                    return (IterableQueue<T>) xNode.cells;
                                }
                            }
                    	} else {
                    		for (int j = 0; j < yNode.usedNode; j++) {
                    			xNode = yNode.next[j];
                                //xNode = yNode.next[j];
                                if (xNode.coordinator == x) {
                                    return (IterableQueue<T>) xNode.cells;
                                }
                            }
                    	}
                    	
                    }
                }
        	}
            
            return null;
        }

        @SuppressWarnings("unchecked")
		void updateNode() {
            if (usedNode >= nextNodeSize) {
                nextNodeSize *= 2;
                @SuppressWarnings("rawtypes")
				Node[] newNext = new Node[nextNodeSize];

                for (int i = 0; i < usedNode; i++) {
                    newNext[i] = next[i];
                }
                for (int i = usedNode; i < nextNodeSize; i++) {
                    newNext[i] = new Node<Object>(10, 0);
                    newNext[i].next = new Node[10];
                    for (int j = 0; j < 10; j++) {
                        newNext[i].next[j] = new Node<Object>(0,0);
                    }
                }
                next = newNext;
            }
            
            if (usedNode2 >= next2NodeSize) {
                next2NodeSize *= 2;
                @SuppressWarnings("rawtypes")
				Node[] newNext2 = new Node[next2NodeSize];

                for (int i = 0; i < usedNode; i++) {
                    newNext2[i] = next[i];
                }
                for (int i = usedNode2; i < next2NodeSize; i++) {
                    newNext2[i] = new Node<Object>(10, 0);
                    newNext2[i].next = new Node[10];
                    for (int j = 0; j < 10; j++) {
                        newNext2[i].next[j] = new Node<Object>(0,0);
                    }
                }
                next2 = newNext2;
            }
        }
    }

    /**
     * Add an element at a fixed position.
     * 
     * Run-time: O(n)
     *
     *
     * @param element The element to be added at the indicated position.
     * @param x X Coordinate of the position of the element.
     * @param y Y Coordinate of the position of the element.
     * @param z Z Coordinate of the position of the element.
     * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
     */
    @SuppressWarnings("unchecked")
	@Override
    public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
    	Node<T> yNode;
    	Node<T> xNode;
        if (x > this.length || y > this.breadth || z > this.height) {
            throw new IndexOutOfBoundsException();
        }
        if (this.cube[z].getCells(x, y) != null) {
            this.cube[z].getCells(x, y).enqueue(element);
            return;
        }
	    Node<T> zNode = this.cube[z];
        zNode.updateNode();
	    zNode.coordinator = z;
	    if (y > breadth/2) {
    		yNode = zNode.next2[zNode.usedNode2];
    		zNode.usedNode2++;
    	} else {
    		yNode = zNode.next[zNode.usedNode];
    		zNode.usedNode++;
    	}
	    //yNode = zNode.next[zNode.usedNode];
	    yNode.updateNode();
	    yNode.coordinator = y;
	    if (x > length/2) {
    		xNode = yNode.next2[yNode.usedNode2];
    		yNode.usedNode2++;
    	} else {
    		xNode = yNode.next[yNode.usedNode];
    		yNode.usedNode++;
    	}
	    //Node<T> xNode = yNode.next[yNode.usedNode];
        xNode.cells = new TraversableQueue<T>();
	    xNode.coordinator = x;
	    xNode.cells.enqueue(element);
	    
	    
    }

    /**
     * Return the 'oldest' element at the indicated position.
     * 
     * Run-time: O(n)
     *
     *
     * @param x X Coordinate of the position of the element.
     * @param y Y Coordinate of the position of the element.
     * @param z Z Coordinate of the position of the element.
     * @return 'Oldest' element at this position or null if no elements at the indicated position.
     * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
     */
    @SuppressWarnings("unchecked")
	@Override
    public T get(int x, int y, int z) throws IndexOutOfBoundsException {
        if (x > this.length || y > this.breadth || z > this.height) {
            throw new IndexOutOfBoundsException();
        }
        if (this.cube[z].getCells(x, y) == null) {
            return null;
        }
        return (T) this.cube[z].getCells(x, y).iterator().next();
    }

    /**
     * Return all the elements at the indicated position.
     * 
     * Run-time: O(n)
     *
     * @param x X Coordinate of the position of the element(s).
     * @param y Y Coordinate of the position of the element(s).
     * @param z Z Coordinate of the position of the element(s).
     * @return An IterableQueue of all elements at this position or null if no elements at the indicated position.
     * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
     */
    @SuppressWarnings("unchecked")
	@Override
    public IterableQueue<T> getAll(int x, int y, int z) throws IndexOutOfBoundsException {
        if (x > this.length || y > this.breadth || z > this.height) {
            throw new IndexOutOfBoundsException();
        }
        return this.cube[z].getCells(x, y);
    }

    /**
     * Indicates whether there are more than one elements at the indicated position.
     * 
     * Run-time: O(n)
     *
     * @param x X Coordinate of the position of the element(s).
     * @param y Y Coordinate of the position of the element(s).
     * @param z Z Coordinate of the position of the element(s).
     * @return true if there are more than one elements at the indicated position, false otherwise.
     * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
     */
    @Override
    public boolean isMultipleElementsAt(int x, int y, int z) throws IndexOutOfBoundsException {
        if (x > this.length || y > this.breadth || z > this.height) {
            throw new IndexOutOfBoundsException();
        }
        @SuppressWarnings("unchecked")
		Iterator<T> itr = (Iterator<T>) this.cube[z].getCells(x, y).iterator();
        itr.next();
	    if (itr.hasNext()) {
	        return true;
        }
        return false;
    }

    /**
     * Removes the specified element at the indicated position.
     * 
     * Run-time: O(n)
     *
     * @param element The element to be removed from the indicated position.
     * @param x X Coordinate of the position.
     * @param y Y Coordinate of the position.
     * @param z Z Coordinate of the position.
     * @return true if the element was removed from the indicated position, false otherwise.
     * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
     */
    @Override
    public boolean remove(int x, int y, int z, T element) throws IndexOutOfBoundsException {
        if (x > this.length || y > this.breadth || z > this.height) {
            throw new IndexOutOfBoundsException();
        }
	    boolean result = false;
        IterableQueue<T> cells;
        IterableQueue<T> tempCells = new TraversableQueue<>();
	    if (this.getAll(x,y,z) == null) {
	        return false;
        } else {
	        cells = this.getAll(x,y,z);
        }
	    for (int i = 0; i < getAll(x, y, z).size(); i++) {
	        T object = cells.dequeue();
	        if (!object.equals(element)) {
	            tempCells.enqueue(object);
            } else {
	            result = true;
            }
        }
        for (int i = 0; i < tempCells.size(); i++) {
            T object = tempCells.dequeue();
            cells.enqueue(object);
        }
        return result;
    }

    /**
     * Removes all elements at the indicated position.
     * 
     * Run-time: O(n)
     *
     * @param x X Coordinate of the position.
     * @param y Y Coordinate of the position.
     * @param z Z Coordinate of the position.
     * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
     */
    @Override
    public void removeAll(int x, int y, int z) throws IndexOutOfBoundsException {
        if (x > this.length || y > this.breadth || z > this.height) {
            throw new IndexOutOfBoundsException();
        }
	    int size = this.getAll(x,y,z).size();
        for (int i = 0; i < size; i++) {
            this.getAll(x,y,z).dequeue();
        }
    }

    /**
     * 
     * Removes all elements stored in the cube.
     * 
     * Run-time: O(1)
     */
    @SuppressWarnings("unchecked")
	@Override
    public void clear() {
        for (int i = 0; i < height; i++) {
            cube[i] = new Node<Object>(10, 0);
            cube[i].coordinator = i;
            for (int j = 0; j < 5; j++) {
                cube[i].next[j] = new Node<Object>(10, 0);
                cube[i].next2[j] = new Node<Object>(10, 0);
                for (int k = 0; k < 5; k++) {
                    cube[i].next[j].next[k] = new Node<Object>(0, 0);
                    cube[i].next[j].next2[k] = new Node<Object>(0, 0);
                    cube[i].next2[j].next[k] = new Node<Object>(0, 0);
                    cube[i].next2[j].next2[k] = new Node<Object>(0, 0);
                }
            }
        }
    }
}

/**
 * 
 * 
 * 
 * 
 */

