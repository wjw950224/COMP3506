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
                    cube[i].next[j].next[k].cells = new TraversableQueue<T>();
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
            //Node zNode = this.cube[z];
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

        public int getIndex(int number) {
            for (int i = 0; i < usedNode; i++) {
                if (coordinator == number) {
                    return i;
                }
            }
            return -1;
        }
    }


    @Override
    public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
        if (this.cube[z].getCells(x, y) != null) {
            this.cube[z].getCells(x, y).enqueue(element);
            return;
        }
	    Node zNode = this.cube[z];
	    zNode.coordinator = z;
	    Node yNode = zNode.next[zNode.usedNode];
	    yNode.coordinator = y;
	    Node xNode = yNode.next[yNode.usedNode];
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
	    for (int i = 0; i < getAll(x, y, z).size(); i++) {
	        if (getAll(x, y, z).iterator().next().equals(element)) {
                getAll(x, y, z).iterator().remove();
            }
        }
        return false;
    }

    @Override
    public void removeAll(int x, int y, int z) throws IndexOutOfBoundsException {
	    int yIndex = this.cube[z].getIndex(y);
	    int xIndex = this.cube[z].next[yIndex].getIndex(z);
        this.cube[z].next[yIndex].next[xIndex].cells = new TraversableQueue<T>();
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

