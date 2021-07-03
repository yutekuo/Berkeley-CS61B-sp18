package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void testBasics() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(4);
        assertTrue(arb.isEmpty());
        assertFalse(arb.isFull());
        arb.enqueue(10); // 10 null null null
        arb.enqueue(11); // 10 11 null null
        arb.enqueue(12); // 10 11 12 null
        arb.enqueue(13); // 10 11 12 13
        assertFalse(arb.isEmpty());
        assertTrue(arb.isFull());

        int actual = arb.dequeue(); // 11 12 13 null
        int expected = 10;
        assertEquals(expected, actual);

        int actual2 = arb.peek();
        int expected2 = 11;
        assertEquals(expected2, actual2);
        int expected3 = 3;
        assertEquals(expected3, arb.fillCount());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
