/**
 * Your operations are subject to the following rules:
 * 1. add and remove operations must not involve any looping or recursion.
 *    A single such operation must take “constant time”,
 *    i.e. execution time should not depend on the size of the deque.
 * 2. get must use iteration, not recursion.
 * 3. size must take constant time.
 * 4. The amount of memory that your program uses at any given time must be proportional
 *    to the number of items. For example, if you add 10,000 items to the deque,
 *    and then remove 9,999 items, the resulting size should be more like a deque with 1 item
 *    than 10,000. Do not maintain references to items that are no longer in the deque.
 */
public class LinkedListDeque<T> {
    private class TNode {
        public T item;
        public TNode prev;
        public TNode next;

        public TNode(T i, TNode p, TNode n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private TNode sentinel;
    private int size;

    /** Creates an empty linked list deque. */
    public LinkedListDeque() {
        size = 0;
        sentinel = new TNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    /** Creates a deep copy of other. */
    public LinkedListDeque(LinkedListDeque other) {

    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        size = size + 1;
        sentinel.next = new TNode(item, sentinel, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        size = size + 1;
        sentinel.prev = new TNode(item, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
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
     * Prints the items in the deque from first to last,
     * separated by a space. Once all the items have been printed,
     * print out a new line.
     */
    public void printDeque() {
        TNode p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            System.out.print(p.item + " ");
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     */
    public T removeFirst() {
        return null;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     */
    public T removeLast() {
        return null;
    }

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists,
     * returns null. Must not alter the deque!
     */
    public T get(int index) {
        return null;
    }

    public T getRecursive(int index) {
        return null;
    }
}
