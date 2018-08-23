package comp3506.assn1.adts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.util.Iterator;

public class MyBoundedCubeTest {
    @Test(timeout=500)
    public void testGetWithOneElement() {
        Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
        Object element = new Object();
        for (int i = 0; i < 20000; i++) {
            testCube.add(1, 1, 1, element);
        }
        IterableQueue<Object> queue = testCube.getAll(1, 1, 1);
        assertThat("Returned queue was wrong size.", queue.size(), is(equalTo(20000)));
    }
}
