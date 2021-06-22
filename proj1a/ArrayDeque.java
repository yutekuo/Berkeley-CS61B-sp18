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
    /*
    public ArrayDeque(ArrayDeque other) {
        items = (T[]) new Object[other.items.length];
        size = other.size;
        nextFirst = other.nextFirst;
        nextLast = other.nextLast;
        System.arraycopy(other.items, 0, items, 0, items.length);
    }
    */

    /** Returns the index immediately “before” a given index for a given array arr. */
    private int resizeHelper(T[] arr, int index) {
        index = index - 1;
        if (index < 0) {
            index = arr.length - 1;
        }
        return index;
    }

    /**
     * Invariants:
     * 1. When addOne(nextLast) == nextFirst, we need to resize the array.
     * 2. The size of new array is twice than that of the old array.
     * 3. nextLast will be same in both arrays.
     * 4. Start copying the elements of old array to new array from minusOne(nextLast).
     * 5. After we copy the last element of old array to new array, the new nextFirst will
     *    be minusOne(the new index of the last element).
     */
    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];
        int oldIndex = minusOne(nextLast);
        int newIndex = oldIndex;
        for (int i = 0; i < size; i++) {
            newArray[newIndex] = items[oldIndex];
            oldIndex = minusOne(oldIndex);
            newIndex = resizeHelper(newArray, newIndex);
        }
        nextFirst = newIndex;
        items = newArray;
    }
    /** Returns true if addOne(nextLast) == nextFirst. */
    private boolean isFull() {
        return addOne(nextLast) == nextFirst;
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
        if (isFull()) {
            resize(items.length * 2);
        }
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
        if (isFull()) {
            resize(items.length * 2);
        }
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
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

    /** Returns the usage ratio of an array. */
    private double calcUsageRatio() {
        double usageRatio = (double) size / items.length;
        return usageRatio;
    }

    /**
     * Invariants:
     * 1. When usage ratio of an array of length 16 or more is below 25%, halve it.
     * 2. Start copying elements from addOne(nextFirst) of larger array to index 1
     *    of the smaller array.
     * 3. After copying elements, set new nextFirst to 0 and new nextLast to size + 1.
     */
    private void downsize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];
        int index = addOne(nextFirst);
        for (int i = 0; i < size; i++) {
            newArray[i + 1] = items[index];
            index = addOne(index);
        }
        items = newArray;
        nextFirst = 0;
        nextLast = size + 1;
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

        if (items.length >= 16 && calcUsageRatio() < 0.25) {
            downsize(items.length / 2);
        }

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

        if (items.length >= 16 && calcUsageRatio() < 0.25) {
            downsize(items.length / 2);
        }

        return item;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index) {
        if (isEmpty()) {
            return null;
        }

        int itemIndex = addOne(nextFirst);
        for (int i = 0; i < index; i++) {
            itemIndex = addOne(itemIndex);
        }

        return items[itemIndex];
    }
}
