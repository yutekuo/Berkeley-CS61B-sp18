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
}
