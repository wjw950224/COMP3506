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
                    assertEquals(element, testCube.get(i, j, k));
                }
            }
        }
    }

    @Test//(timeout=500)
    public void testBigInput2() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 25; j++) {
                for (int k = 0; k < 1; k++) {
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
    public void testBigInput5() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 250; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    testCube.add(i, j, k, element);
                    assertEquals(element, testCube.get(i, j, k));
                }
            }
        }
    }

    @Test//(timeout=500)
    public void testBigInput6() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 250; j++) {
                for (int k = 0; k < 10; k++) {
                    testCube.add(i, j, k, element);
                    assertEquals(element, testCube.get(i, j, k));
                }
            }
        }
    }
}
