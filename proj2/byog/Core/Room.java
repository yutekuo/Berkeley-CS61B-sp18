package byog.Core;

public class Room {
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
}
