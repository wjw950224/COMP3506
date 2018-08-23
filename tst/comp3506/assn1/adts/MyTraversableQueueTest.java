package comp3506.assn1.adts;

import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MyTraversableQueueTest {
    @Test(timeout=500)
    public void testEmptyQueueSize() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        assertThat("An empty queue does not have a size of 0.", testQueue.size(), is(equalTo(0)));
    }

    @Test(timeout=500)
    public void testBigElementQueueSize() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        for (int i = 0; i < 20000; i++) {
            testQueue.enqueue(new Object());
        }
        assertThat("A queue with 20000 elements does not have a size of 20000.", testQueue.size(), is(equalTo(20000)));
    }

    @Test(timeout=500)
    public void testDequeueMultiElement() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        for (int i = 0; i < 20000; i++) {
            testQueue.enqueue(new Object());
        }
        while(testQueue.size() > 0) {
            testQueue.dequeue();
        }
        assertThat("Dequeue empty queue return null.", testQueue.dequeue(), is(equalTo(null)));
    }

    @Test(timeout=500)
    public void testFirstNext() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o = new Object();
        testQueue.enqueue(o);
        assertThat("Return first element when first call next.", testQueue.iterator().next(), is(equalTo(o)));
    }

    @Test(timeout=500)
    public void testEmptyNext() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o = new Object();
        testQueue.enqueue(o);
        Iterator itr = testQueue.iterator();
        itr.next();
        assertThat("Return first element when first call next.", itr.next(), is(equalTo(null)));
    }
}
