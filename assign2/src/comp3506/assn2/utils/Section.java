package comp3506.assn2.utils;

/**
 * Section Node
 *
 * Memory usage: O(1)
 * This class only store constant size of string and int, others are references to other object.
 *
 * @param <T>
 */

public class Section<T> {
    private Section next;
    private Section last;
    private LinkedList contents;
    private String title;
    private int line;

    /**
     * Add new content into Section
     *
     * Run-time: O(1)
     * As add() method has run time efficiency of 1, this method has O(1) as well
     *
     * @param content to be added
     * @return
     */
    public void addContent(T content) {
        this.contents.add(content);
    }

    /**
     * Get next section
     *
     * Run-time O(1)
     * Next section can be directly accessed
     *
     * @return Section
     */
    public Section getNext() {
        return this.next;
    }

    /**
     * Get previous section
     *
     * Run-time O(1)
     * Previous section can be directly accessed
     *
     * @return Section
     */
    public Section getLast() {
        return this.last;
    }

    /**
     * Return contents list of this section
     *
     * Run-time O(1)
     * Contents section can be directly accessed
     *
     * @return LinkedList
     */
    public LinkedList getContents() {
        return this.contents;
    }

    /**
     * Return title of this section
     *
     * Run-time O(1)
     * Title can be directly accessed
     *
     * @return String
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Return start row number of this section
     *
     * Run-time O(1)
     * Line can be directly accessed
     *
     * @return Integer
     */
    public int getLine() {
        return this.line;
    }

    /**
     * Set the next section
     *
     * Run-time O(1)
     * Next can be directly modified
     *
     * @param next section
     */
    public void setNext(Section next) {
        this.next = next;
    }

    /**
     * Set the last section
     *
     * Run-time O(1)
     * Last can be directly modified
     *
     * @param last section
     */
    public void setLast(Section last) {
        this.last = last;
    }

    /**
     * Set contents of this section
     *
     * Run-time O(1)
     * Contents can be directly modified
     *
     * @param contents of this section
     */
    public void setContents(LinkedList contents) {
        this.contents = contents;
    }

    /**
     * Set title of this section
     *
     * Run-time O(1)
     * Title can be directly modified
     *
     * @param title of this section
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set start row number of this section
     *
     * Run-time O(1)
     * Line can be directly modified
     *
     * @param line starting row number
     */
    public void setLine(int line) {
        this.line = line;
    }
}
