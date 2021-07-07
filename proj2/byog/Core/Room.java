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
     * Return negative number if this's x position < other's x position.
     * Return 0 if this's x position == other's x position.
     * Return positive number if this's x position > other's x position.
     */
    @Override
    public int compareTo(Room other) {
        return this.getPosition().getX() - other.getPosition().getX();
    }
}
