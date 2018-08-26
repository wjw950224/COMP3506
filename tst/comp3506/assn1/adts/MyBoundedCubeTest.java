package comp3506.assn1.adts;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MyBoundedCubeTest {
    /**
    @Test(timeout=500)
    public void testGetWithOneElement() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 20000; i++) {
            testCube.add(1, 1, 1, element);
        }
        IterableQueue<Object> queue = testCube.getAll(1, 1, 1);
        assertThat("Returned queue was wrong size.", queue.size(), is(equalTo(20000)));
    }**/

    @Test//(timeout=500)
    public void testBigInput() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 50; j++) {
                for (int k = 0; k < 10; k++) {
                    testCube.add(i, j, k, element);
                    
                }
            }
        }
        assertEquals(element, testCube.get(39, 49, 9));
    }

    @Test//(timeout=500)
    public void testBigInput2() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 80; i++) {
            for (int j = 0; j < 50; j++) {
                for (int k = 0; k < 10; k++) {
                    testCube.add(i, j, k, element);
                    //assertEquals(element, testCube.get(i, j, k));
                }
            }
        }
        assertEquals(element, testCube.get(79, 49, 9));
    }

    @Test//(timeout=500)
    public void testBigInput3() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 2661; i < 2700; i++) {
            for (int j = 3000; j < 3020; j++) {
                for (int k = 0; k < 10; k++) {
                    testCube.add(i, j, k, element);
                    assertEquals(element, testCube.get(i, j, k));
                }
            }
        }
        
    }
    
    /**
    @Test//(timeout=500)
    public void testBigInput3() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 20; j++) {
                for (int k = 0; k < 35; k++) {
                    testCube.add(i, j, k, element);
                    assertThat("", testCube.get(i, j, k), is(equalTo(element)));
                }
            }
        }
    }**/

    @Test//(timeout=500)
    public void testBigInput4() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 250000; i++) {
            testCube.add(1, 1, 1, element);
            assertEquals(element, testCube.get(1, 1, 1));
        }
    }

    @Test//(timeout=500)
    public void testRemoveSingleElement() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        Object element2 = new Object();
        Object element3 = new Object();
        testCube.add(1, 2, 3, element);
        testCube.add(1, 2, 3, element2);
        testCube.add(1, 2, 3, element3);
        assertEquals(false, testCube.remove(2,2,3,element3));
        assertEquals(true, testCube.remove(1,2,3,element2));
    }

    @Test//(timeout=500)
    public void testRemoveAllElement() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element1 = new Object();
        Object element2 = new Object();
        Object element3 = new Object();
        testCube.add(1, 2, 3, element1);
        testCube.add(1, 2, 3, element2);
        testCube.add(1, 2, 3, element3);
        testCube.removeAll(1,2,3);
        assertEquals(0, testCube.getAll(1,2,3).size());
    }

    @Test//(timeout=500)
    public void testClearCube() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 250; j++) {
                for (int k = 0; k < 10; k++) {
                    testCube.add(i, j, k, element);
                }
            }
        }
        testCube.clear();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 250; j++) {
                for (int k = 0; k < 10; k++) {
                    assertEquals(null, testCube.getAll(i,j,k));
                }
            }
        }
    }
    
    @Test//(timeout=500)
    public void testMutipleElement() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        testCube.add(1, 2, 3, element);
        assertEquals(false, testCube.isMultipleElementsAt(1, 2, 3));
        testCube.add(1, 2, 3, element);
        assertEquals(true, testCube.isMultipleElementsAt(1, 2, 3));
    }
    
    @Test(timeout=500, expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
        Object element = new Object();
        testCube.add(6, 6, 6, element);
    }
    
    @Test(timeout=500, expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
        Object element = new Object();
        testCube.add(4, 4, 4, element);
        testCube.get(6, 6, 6);
    }
    
    @Test(timeout=500, expected = IndexOutOfBoundsException.class)
    public void testGetAllOutOfBounds() {
        Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
        Object element = new Object();
        testCube.add(4, 4, 4, element);
        testCube.getAll(6, 6, 6);
    }
    
    @Test(timeout=500, expected = IndexOutOfBoundsException.class)
    public void testRemoveOutOfBounds() {
        Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
        Object element = new Object();
        testCube.add(4, 4, 4, element);
        testCube.remove(6, 6, 6, element);
    }
    
    @Test(timeout=500, expected = IndexOutOfBoundsException.class)
    public void testRemoveAllOutOfBounds() {
        Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
        Object element = new Object();
        testCube.add(4, 4, 4, element);
        testCube.removeAll(6, 6, 6);
    }
    
    @Test(timeout=500, expected = IndexOutOfBoundsException.class)
    public void testBoundedCubeOutOfBounds() {
        Cube<Object> testCube = new BoundedCube<>(-1, 5, 5);
    }
    
    @Test(timeout=500)
    public void testSeparateStore() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        testCube.add(4, 4, 4, element);
        testCube.add(2661, 4, 4, element);
        testCube.add(4, 1715, 4, element);
        testCube.add(4, 4, 15, element);
        testCube.add(2661, 1715, 4, element);
        testCube.add(2661, 4, 15, element);
        testCube.add(4, 1715, 15, element);
        testCube.add(2661, 1715, 15, element);
        assertEquals(element, testCube.get(4, 4, 4));
        assertEquals(element, testCube.get(2661, 4, 4));
        assertEquals(element, testCube.get(4, 1715, 4));
        assertEquals(element, testCube.get(4, 4, 15));
        assertEquals(element, testCube.get(2661, 1715, 4));
        assertEquals(element, testCube.get(2661, 4, 15));
        assertEquals(element, testCube.get(4, 1715, 15));
        assertEquals(element, testCube.get(2661, 1715, 15));
    }
}
