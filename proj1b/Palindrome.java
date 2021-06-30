public class Palindrome {
    /**
     * Given a String, wordToDeque should return a Deque where the characters
     * appear in the same order as in the String.
     */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    /** Recursively checks whether a deque is a palindrome or not. */
    private boolean isPalindromeHelper(Deque<Character> deque) {
        if (deque.isEmpty() || deque.size() == 1) {
            return true;
        }

        if (deque.removeFirst() != deque.removeLast()) {
            return false;
        }

        return isPalindromeHelper(deque);
    }

    /** Returns true if the given word is a palindrome, and false otherwise. */
    public boolean isPalindrome(String word) {
        Deque<Character> deque = wordToDeque(word);
        return isPalindromeHelper(deque);
    }
}
