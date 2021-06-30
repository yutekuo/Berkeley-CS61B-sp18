public class OffByN implements CharacterComparator {
    private int difference;
    /** A single argument constructor which takes an integer. */
    public OffByN(int N) {
        difference = N;
    }

    /** Returns true for characters that are off by N. */
    @Override
    public boolean equalChars(char x, char y) {
        return x - y == difference || x - y == -difference;
    }
}
