/**
 * This deque must use arrays as the core data structure.
 * For this implementation, your operations are subject to the following rules:
 * 1. add and remove must take constant time, except during resizing operations.
 * 2. get and size must take constant time.
 * 3. The starting size of your array should be 8.
 * 4. The amount of memory that your program uses at any given time must be
 *    proportional to the number of items. For example, if you add 10,000 items
 *    to the deque, and then remove 9,999 items, you shouldn’t still be using an
 *    array of length 10,000ish. For arrays of length 16 or more, your usage factor
 *    should always be at least 25%. For smaller arrays, your usage factor can be
 *    arbitrarily low.
 */
public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    /** Creates an empty array deque. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    /** Creates a deep copy of other. */
    public ArrayDeque(ArrayDeque other) {
        items = (T[]) new Object[other.items.length];
        size = other.size;
        nextFirst = other.nextFirst;
        nextLast = other.nextLast;
        System.arraycopy(other.items, 0, items, 0, items.length);
    }

    /** Returns the index immediately “before” a given index. */
    private int minusOne(int index) {
        index = index - 1;
        if (index < 0) {
            index = items.length - 1;
        }
        return index;
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        items[nextFirst] = item;
        size = size + 1;
        nextFirst = minusOne(nextFirst);
    }

    /** Returns the index immediately “after” a given index. */
    private int addOne(int index) {
        index = index + 1;
        if (index > items.length - 1) {
            index = 0;
        }
        return index;
    }

    /**  Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        items[nextLast] = item;
        size = size + 1;
        nextLast = addOne(nextLast);
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        int index = addOne(nextFirst); //Current "First".
        for (int i = 0; i < size; i++) {
            System.out.print(items[index] + " ");
            index = addOne(index);
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque. If no such item exists,
     * returns null.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int currentFirst = addOne(nextFirst);
        T item = items[currentFirst];
        size = size - 1;
        items[currentFirst] = null;
        nextFirst = currentFirst;
        return item;
    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists,
     * returns null.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int currentLast = minusOne(nextLast);
        T item = items[currentLast];
        size = size - 1;
        items[currentLast] = null;
        nextLast = currentLast;
        return item;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index) {
        return items[index];
    }
}
