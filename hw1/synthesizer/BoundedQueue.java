package synthesizer;
import java.util.Iterator;

public interface BoundedQueue<T> extends Iterable<T> {
    /** Returns size of the buffer. */
    int capacity();

    /** Returns number of items currently in the buffer. */
    int fillCount();

    /** Adds item x to the end. */
    void enqueue(T x);

    /** Deletes and returns item from the front. */
    T dequeue();

    /** Returns (but do not deletes) item from the front. */
    T peek();

    /** Is the buffer empty (fillCount equals zero)? */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /** Is the buffer full (fillCount is same as capacity)? */
    default boolean isFull() {
        return fillCount() == capacity();
    }

    @Override
    Iterator<T> iterator();
}
