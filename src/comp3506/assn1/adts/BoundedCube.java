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
	private int cellNum;
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
		this.cellNum = 0;
		this.cube = new Node[height];
		for (int i = 0; i < height; i++) {
		    cube[i] = new Node(breadth);
		    for (int j = 0; j < breadth; j++) {
		        cube[i].next[j] = new Node(length);
		        for (int k = 0; k < length; k++) {
                    cube[i].next[j].next[k] = new Node(0);
                    cube[i].next[j].next[k].cells = new TraversableQueue<T>();
                }
            }
        }
	}

    private class Node<T> {
        //int coordinator;
        Node[] next;
        IterableQueue<T> cells;

        public Node(int size)
        {
            //coordinator = position;
            next = new Node[size];
            //children = new Node[size];
            //cells = new TraversableQueue<T>();
        }
    }


    @Override
    public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
        this.cube[z].next[y].next[x].cells.enqueue(element);
    }

    @Override
    public T get(int x, int y, int z) throws IndexOutOfBoundsException {
	    T element = (T) this.cube[z].next[y].next[x].cells.iterator().next();
        //this.cube[z].next[y].next[x].cells.iterator().remove();
	    return element;
    }

    @Override
    public IterableQueue<T> getAll(int x, int y, int z) throws IndexOutOfBoundsException {
        return (IterableQueue<T>) this.cube[z].next[y].next[x].cells;
    }

    @Override
    public boolean isMultipleElementsAt(int x, int y, int z) throws IndexOutOfBoundsException {
        Iterator<T> itr = (Iterator<T>) this.cube[z].next[y].next[x].cells.iterator();
        itr.next();
	    if (itr.hasNext()) {
	        return true;
        }
        return false;
    }

    @Override
    public boolean remove(int x, int y, int z, T element) throws IndexOutOfBoundsException {
	    for (int i = 0; i < this.cube[z].next[y].next[x].cells.size(); i++) {
	        if (this.cube[z].next[y].next[x].cells.iterator().next().equals(element)) {
                this.cube[z].next[y].next[x].cells.iterator().remove();
            }
        }
        return false;
    }

    @Override
    public void removeAll(int x, int y, int z) throws IndexOutOfBoundsException {
        this.cube[z].next[y].next[x].cells = new TraversableQueue<T>();
    }

    @Override
    public void clear() {
        for (int i = 0; i < height; i++) {
            cube[i] = new Node(breadth);
            for (int j = 0; j < breadth; j++) {
                cube[i].next[j] = new Node(length);
                for (int k = 0; k < length; k++) {
                    cube[i].next[j].next[k] = new Node(0);
                    cube[i].next[j].next[k].cells = new TraversableQueue<T>();
                }
            }
        }
    }
}

