package comp3506.assn1.adts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.util.Iterator;

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

    @Test(timeout=500)
    public void testBigInput() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 50; j++) {
                for (int k = 0; k < 10; k++) {
                    testCube.add(i, j, k, element);
                    assertThat("", testCube.get(i, j, k), is(equalTo(element)));
                }
            }

        }
    }

    @Test(timeout=500)
    public void testBigInput2() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 1; k++) {
                    testCube.add(i, j, k, element);
                    assertThat("", testCube.get(i, j, k), is(equalTo(element)));
                }
            }

        }
    }
}
