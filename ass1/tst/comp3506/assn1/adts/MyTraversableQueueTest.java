package comp3506.assn1.adts;
import org.junit.Test;
import java.util.Iterator;
import static org.junit.Assert.assertEquals;

public class MyTraversableQueueTest {
    @Test(timeout=500)
    public void testEmptyQueueSize() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        assertEquals(0, testQueue.size());
    }

    @Test(timeout=500)
    public void testBigElementQueueSize() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        for (int i = 0; i < 20000; i++) {
            testQueue.enqueue(new Object());
        }
        assertEquals(20000, testQueue.size());
    }

    @Test(timeout=500, expected = IndexOutOfBoundsException.class)
    public void testDequeueMultiElement() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        for (int i = 0; i < 20000; i++) {
            testQueue.enqueue(new Object());
        }
        while(testQueue.size() > 0) {
            testQueue.dequeue();
        }
        testQueue.dequeue();
    }

    @Test(timeout=500)
    public void testFirstNext() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o = new Object();
        testQueue.enqueue(o);
        assertEquals(o, testQueue.iterator().next());
    }

    @Test(timeout=500)
    public void testEmptyNext() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o = new Object();
        testQueue.enqueue(o);
        Iterator<Object> itr = testQueue.iterator();
        itr.next();
        assertEquals(null, itr.next());
    }

    @Test(timeout=500)
    public void testMultiElementNext() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        testQueue.enqueue(o1);
        testQueue.enqueue(o2);
        testQueue.enqueue(o3);
        Iterator<Object> itr = testQueue.iterator();
        assertEquals(o1, itr.next());
        assertEquals(o2, itr.next());
        assertEquals(o3, itr.next());
    }

    @Test(timeout=500)
    public void testMultiElementNext2() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        testQueue.enqueue(o1);
        Iterator<Object> itr = testQueue.iterator();
        assertEquals(o1, itr.next());
        testQueue.enqueue(o2);
        assertEquals(o2, itr.next());
        testQueue.enqueue(o3);
        assertEquals(o3, itr.next());
    }

    @Test//(timeout=500)
    public void testMultiElementNext3() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o1 = new Object();
        Object o2 = new Object();
        testQueue.enqueue(o1);
        Iterator<Object> itr = testQueue.iterator();
        assertEquals(o1, itr.next());
        testQueue.dequeue();
        assertEquals(null, itr.next());
        testQueue.enqueue(o2);
        assertEquals(null, itr.next());
    }



    @Test//(timeout=500)
    public void testSingleElementHasNext() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o1 = new Object();
        testQueue.enqueue(o1);
        Iterator<Object> itr = testQueue.iterator();
        assertEquals(o1, itr.next());
        assertEquals(false, itr.hasNext());
    }

    @Test//(timeout=500)
    public void testMultiElementHasNext() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        Object o4 = new Object();
        Object o5 = new Object();
        testQueue.enqueue(o1);
        testQueue.enqueue(o2);
        testQueue.enqueue(o3);
        testQueue.enqueue(o4);
        testQueue.enqueue(o5);
        Iterator<Object> itr = testQueue.iterator();
        itr.next();
        assertEquals(true, itr.hasNext());
        testQueue.dequeue();
        testQueue.dequeue();
        testQueue.dequeue();
        assertEquals(true, itr.hasNext());
        testQueue.dequeue();
        assertEquals(false, itr.hasNext());
    }

    @Test//(timeout=500)
    public void testMultiElementHasNext2() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        Object o4 = new Object();
        Object o5 = new Object();
        testQueue.enqueue(o1);
        testQueue.enqueue(o2);
        testQueue.enqueue(o3);
        testQueue.enqueue(o4);
        Iterator<Object> itr = testQueue.iterator();
        assertEquals(true, itr.hasNext());
        testQueue.dequeue();
        testQueue.dequeue();
        testQueue.dequeue();
        assertEquals(false, itr.hasNext());
        testQueue.enqueue(o5);
        assertEquals(true, itr.hasNext());
    }

    @Test//(timeout=500)
    public void testMultiElementHasNext3() {
        IterableQueue<Object> testQueue = new TraversableQueue<>();
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        Object o4 = new Object();
        Object o5 = new Object();
        testQueue.enqueue(o1);
        testQueue.enqueue(o2);
        testQueue.enqueue(o3);
        testQueue.enqueue(o4);
        Iterator<Object> itr = testQueue.iterator();
        assertEquals(true, itr.hasNext());
        testQueue.dequeue();
        testQueue.dequeue();
        testQueue.dequeue();
        itr.next();
        assertEquals(false, itr.hasNext());

        testQueue.enqueue(o5);
        itr.next();
        assertEquals(false, itr.hasNext());
    }
}
