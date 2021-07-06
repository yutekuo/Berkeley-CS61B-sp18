package byog.Core;

public class Position {
    private int x;
    private int y;
    public Position(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }

    /** Return x position. */
    public int getX() {
        return x;
    }

    /** Return y position. */
    public int getY() {
        return y;
    }
}
