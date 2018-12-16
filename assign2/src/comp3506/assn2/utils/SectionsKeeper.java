package comp3506.assn2.utils;

/**
 * Collection of section
 *
 * Memory usage: O(n)
 * n: number of section reference stored in this collection
 *
 */
public class SectionsKeeper {
    private LinkedList[] keeper;

    /**
     * Initializer
     *
     * Run-time: O(1)
     * Consistent size of Linkedlist
     *
     */
    public SectionsKeeper() {
        keeper = new LinkedList[10];
        for (int i = 0; i < 10; i++) {
            keeper[i] = new LinkedList();
        }
    }


    /**
     * Add a new section
     *
     * Run-time: O(1)
     * New section will be added at the end of the list
     *
     * @param tail section
     * @param section name
     * @param line of section name
     * @return section to be added
     */
    public Section addSection(Section tail, String section, int line) {
        tail.setNext(new Section());
        int hash = getHash(section);
        this.keeper[hash].add(tail.getNext());
        tail.getNext().setLast(tail);
        tail.getNext().setTitle(section);
        tail.getNext().setLine(line);
        tail.getNext().setContents(new LinkedList());
        return tail.getNext();
    }

    /**
     * Get section by name
     *
     * Run-time: O(n)
     * n: number of section stored in this collection
     * At worst case, all section are in same list. The target section is at the end of the list. Therefore all section will be checked.
     *
     * @param section name
     * @return Section
     */
    public Section getSection(String section) {
        int hash = getHash(section);
        Node start = this.keeper[hash].head;
        while (start != null) {
            if (((Section)(start.elem)).getTitle().equals(section)) {
                return (Section)(start.elem);
            }
            start = start.next;
        }
        return null;
    }

    /**
     * Get hash number by giving section name
     *
     * Run-time: O(logn)
     * n: number of section stored in this collection
     * Not every characters will be calculated
     *
     * @param name of section
     * @return int Hash code
     */
    private int getHash(String name) {
        int hash = 0;
        for (int i = 0; i < name.length(); i += 5) {
            hash += name.charAt(i);
        }
        return hash%10;
    }
}
