package byog.Core;

public class Room implements Comparable<Room> {
    private final int width;
    private final int height;
    private final Position position; // The bottom-left position of the room

    public Room(int w, int h, Position p) {
        width = w;
        height = h;
        position = new Position(p.getX(), p.getY());
    }

    /** Return the width of a room. */
    public int getWidth() {
        return width;
    }

    /** Return the height of a room. */
    public int getHeight() {
        return height;
    }

    /** Return the bottom-left position of a room. */
    public Position getPosition() {
        return position;
    }

    /**
     * Return -1 if this's x position < other's x position.
     * Return -1 if this's x position == other's x position and
     * this's y position < other's y position.
     * Return 1 if this's x position > other's x position.
     */
    @Override
    public int compareTo(Room other) {
        int thisX = this.getPosition().getX();
        int thisY = this.getPosition().getY();
        int otherX = other.getPosition().getX();
        int otherY = other.getPosition().getY();

        if (thisX < otherX) {
            return -1;
        }

        if (thisX == otherX) {
            if (thisY < otherY) {
                return -1;
            }
        }

        return 1;
    }
}
