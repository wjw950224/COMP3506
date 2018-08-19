package comp3506.assn1.adts;


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
	private Position[] positions;
	private int cellNum;

	/**
	 * 
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
		this.positions = new Position[20000];
	}

	private class Position {
		int x;
		int y;
		int z;
		TraversableQueue<T> elements;
	}
	@Override
	public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		positions[cellNum] = new Position();
		positions[cellNum].x = x;
		positions[cellNum].y = y;
		positions[cellNum].z = z;
		positions[cellNum].elements.enqueue(element);
		this.cellNum++;
	}

	@Override
	public T get(int x, int y, int z) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.cellNum; i++) {
			if (positions[i].x == x && positions[i].y == y && positions[i].z == z) {
				return positions[i].elements.iterator().next();
			}
		}
		return null;
	}

	@Override
	public IterableQueue<T> getAll(int x, int y, int z) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.cellNum; i++) {
			if (positions[i].x == x && positions[i].y == y && positions[i].z == z) {
				return positions[i].elements;
			}
		}
		return null;
	}

	@Override
	public boolean isMultipleElementsAt(int x, int y, int z) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.cellNum; i++) {
			if (positions[i].x == x && positions[i].y == y && positions[i].z == z) {
				return positions[i].elements.iterator().hasNext();
			}
		}
		return false;
	}

	@Override
	public boolean remove(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.cellNum; i++) {
			if (positions[i].x == x && positions[i].y == y && positions[i].z == z) {
				if (element.equals(positions[i].elements.iterator().next())) {
					positions[i].elements.iterator().remove();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void removeAll(int x, int y, int z) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.cellNum; i++) {
			if (positions[i].x == x && positions[i].y == y && positions[i].z == z) {
				for (int j = 0; j < positions[i].elements.size(); j++) {
					positions[i].elements.dequeue();
				}
			}
		}
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.positions = null;
	}
	
}
