package comp3506.assn1.adts;

import java.util.Iterator;

/**
 * A three-dimensional data structure that holds items in a positional relationship to each other.
 * Each cell in the data structure can hold multiple items.
 * A bounded cube has a specified maximum size in each dimension.
 * The root of each dimension is indexed from zero.
 * 
 * @author 
 *
 * @param <T> The type of element held in the data structure.
 */
public class BoundedCube<T> implements Cube<T> {
	private int length;
	private int breadth;
	private int height;
	private Node[] cube;


	/**
	 * @param length  Maximum size in the 'x' dimension.
	 * @param breadth Maximum size in the 'y' dimension.
	 * @param height  Maximum size in the 'z' dimension.
	 * @throws IllegalArgumentException If provided dimension sizes are not positive.
	 */
	public BoundedCube(int length, int breadth, int height) throws IllegalArgumentException {
		this.length = length;
		this.breadth = breadth;
		this.height = height;
		this.cube = new Node[height];
		for (int i = 0; i < height; i++) {
		    cube[i] = new Node(10, 0);
		    cube[i].coordinator = i;
		    for (int j = 0; j < 10; j++) {
		        cube[i].next[j] = new Node(10, 0);
		        for (int k = 0; k < 10; k++) {
                    cube[i].next[j].next[k] = new Node(0, 0);
                }
            }
        }
	}

    private class Node<T> {
        int coordinator;
        int nextNodeSize;
        int usedNode;
        Node[] next;
        IterableQueue<T> cells;

        public Node(int size, int position)
        {
            coordinator = position;
            nextNodeSize = size;
            usedNode = 0;
            next = new Node[size];
        }

        public IterableQueue<T> getCells(int x, int y) {
            for (int i = 0; i < usedNode; i++) {
                Node yNode = next[i];
                if (yNode.coordinator == y) {
                    for (int j = 0; j < yNode.usedNode; j++) {
                        Node xNode = yNode.next[j];
                        if (xNode.coordinator == x) {
                            return (IterableQueue<T>) xNode.cells;
                        }
                    }
                }
            }
            return null;
        }

        public void updateNode() {
            if (usedNode >= nextNodeSize) {
                nextNodeSize *= 2;
                Node[] newNext = new Node[nextNodeSize];

                for (int i = 0; i < usedNode; i++) {
                    newNext[i] = next[i];
                }
                for (int i = usedNode; i < nextNodeSize; i++) {
                    newNext[i] = new Node(10, 0);
                    newNext[i].next = new Node[10];
                    for (int j = 0; j < 10; j++) {
                        newNext[i].next[j] = new Node(0,0);
                    }
                }
                next = newNext;

            }
        }
    }


    @Override
    public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
	    //this.cube[z].updateNode();
        if (this.cube[z].getCells(x, y) != null) {
            this.cube[z].getCells(x, y).enqueue(element);
            return;
        }
	    Node zNode = this.cube[z];
        zNode.updateNode();
	    zNode.coordinator = z;
	    Node yNode = zNode.next[zNode.usedNode];
	    yNode.updateNode();
	    yNode.coordinator = y;
	    Node xNode = yNode.next[yNode.usedNode];
        xNode.cells = new TraversableQueue<T>();
	    xNode.coordinator = x;

	    xNode.cells.enqueue(element);
	    zNode.usedNode++;
	    yNode.usedNode++;
    }

    @Override
    public T get(int x, int y, int z) throws IndexOutOfBoundsException {
        if (this.cube[z].getCells(x, y) == null) {
            return null;
        }
        return (T) this.cube[z].getCells(x, y).iterator().next();
    }

    @Override
    public IterableQueue<T> getAll(int x, int y, int z) throws IndexOutOfBoundsException {
        return this.cube[z].getCells(x, y);
    }

    @Override
    public boolean isMultipleElementsAt(int x, int y, int z) throws IndexOutOfBoundsException {
        Iterator<T> itr = (Iterator<T>) this.cube[z].getCells(x, y).iterator();
        itr.next();
	    if (itr.hasNext()) {
	        return true;
        }
        return false;
    }

    @Override
    public boolean remove(int x, int y, int z, T element) throws IndexOutOfBoundsException {
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

    @Override
    public void removeAll(int x, int y, int z) throws IndexOutOfBoundsException {
	    int size = this.getAll(x,y,z).size();
        for (int i = 0; i < size; i++) {
            this.getAll(x,y,z).dequeue();
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < height; i++) {
            cube[i] = new Node(10, 0);
            cube[i].coordinator = i;
            for (int j = 0; j < 10; j++) {
                cube[i].next[j] = new Node(10, 0);
                for (int k = 0; k < 10; k++) {
                    cube[i].next[j].next[k] = new Node(0, 0);
                    cube[i].next[j].next[k].cells = new TraversableQueue<T>();
                }
            }
        }
    }

}

