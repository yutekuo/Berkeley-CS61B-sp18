package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.Random;

public class World {
    private TETile[][] world;
    private final int width;
    private final int height;
    private final long seed;
    private final Random random;
    private ArrayList<Room> existingRooms = new ArrayList<>();
    private final int minRoomSize = 4;
    private final int maxRoomSize = 14;
    private final int minRoomNumbers = 10;
    private final int maxRoomNumbers = 20;

    public World(int w, int h, long s) {
        width = w;
        height = h;
        seed = s;
        random = new Random(seed);
        world = new TETile[width][height];
    }

    /** Adds hallways to all of the nearby rooms in the existing room list. */
    public void addHallways() {
        sortExistingRooms();
        for (int i = 0; i < existingRooms.size() - 1; i++) {
            Room currentRoom = existingRooms.get(i);
            Room nextRoom = existingRooms.get(i + 1);
        }
    }

    /**
     * Draw a L-shape hallway, which can be used to connect two positions
     * that have different x- and y- coordinates.
     * Position p1's x coordinate will always be smaller than or equal to p2's.
     */
    public void drawLShapeHallway(Position p1, Position p2) {
        // L-shape hallway can have four cases, each locates at one of the four corners.
        if (p1.getY() < p2.getY()) {
            // Case 1: upper-left corner
            Position upperLeftCorner = new Position(p1.getX(), p2.getY());
            drawVerticalHallway(p1, upperLeftCorner);
            drawHorizontalHallway(upperLeftCorner, p2);
            world[p1.getX()][p2.getY() - 1] = Tileset.FLOOR;

            // Case 2: bottom-right corner
            Position bottomRightCorner = new Position(p2.getX(), p1.getY());
            drawHorizontalHallway(p1, bottomRightCorner);
            drawVerticalHallway(bottomRightCorner, p2);
            world[p2.getX() - 1][p1.getY()] = Tileset.FLOOR;
        } else {
            // Case 3: upper-right corner
            Position upperRightCorner = new Position(p2.getX(), p1.getY());
            drawHorizontalHallway(p1, upperRightCorner);
            drawVerticalHallway(upperRightCorner, p2);
            world[p2.getX() - 1][p1.getY()] = Tileset.FLOOR;

            // Case 4: bottom-left corner
            Position bottomLeftCorner = new Position(p1.getX(), p2.getY());
            drawVerticalHallway(p1, bottomLeftCorner);
            drawHorizontalHallway(bottomLeftCorner, p2);
            world[p1.getX()][p2.getY() + 1] = Tileset.FLOOR;
        }
    }

    /**
     * Draw a vertical hallway between position p1 and p2.
     * p1 and p2 should have same x-position.
     * e.g., a vertical hallway has width equals to 3, and one of p1 and p2
     * is the position of the top floor, while the other is the bottom floor.
     *          #.#
     *          #.#
     *          #.#
     */
    public void drawVerticalHallway(Position p1, Position p2) {
        if (p1.getX() != p2.getX()) {
            throw new IllegalArgumentException("Position " + p1
                    + " and Position " + p2 + " should have same X-positions!");
        }

        if (p1.getY() > p2.getY()) {
            int dy = p1.getY() - p2.getY();
            for (int y = 0; y <= dy; y++) {
                world[p1.getX()][p2.getY() + y] = Tileset.FLOOR;
                world[p1.getX() - 1][p2.getY() + y] = Tileset.WALL;
                world[p1.getX() + 1][p2.getY() + y] = Tileset.WALL;
            }
        } else {
            int dy = p2.getY() - p1.getY();
            for (int y = 0; y <= dy; y++) {
                world[p1.getX()][p1.getY() + y] = Tileset.FLOOR;
                world[p1.getX() - 1][p1.getY() + y] = Tileset.WALL;
                world[p1.getX() + 1][p1.getY() + y] = Tileset.WALL;
            }
        }
    }

    /**
     * Draw a horizontal hallway between position p1 and p2.
     * p1 and p2 should have same y-position.
     * e.g., a horizontal hallway has width equals to 3, and one of p1 and p2
     * is the position of the left floor, while the other is the right floor.
     *      #####
     *      .....
     *      #####
     */
    public void drawHorizontalHallway(Position p1, Position p2) {
        if (p1.getY() != p2.getY()) {
            throw new IllegalArgumentException("Position " + p1
                    + " and Position " + p2 + " should have same Y-positions!");
        }

        if (p1.getX() > p2.getX()) {
            int dx = p1.getX() - p2.getX();
            for (int x = 0; x <= dx; x++) {
                world[p2.getX() + x][p1.getY()] = Tileset.FLOOR;
                world[p2.getX() + x][p1.getY() - 1] = Tileset.WALL;
                world[p2.getX() + x][p1.getY() + 1] = Tileset.WALL;
            }
        } else {
            int dx = p2.getX() - p1.getX();
            for (int x = 0; x <= dx; x++) {
                world[p1.getX() + x][p1.getY()] = Tileset.FLOOR;
                world[p1.getX() + x][p1.getY() - 1] = Tileset.WALL;
                world[p1.getX() + x][p1.getY() + 1] = Tileset.WALL;
            }
        }
    }

    /** Sort existing room list according to each room's x position. */
    private void sortExistingRooms() {
        existingRooms.sort(Room::compareTo);
    }

    /**
     * Adds random number of rooms to the world. There should be minRoomNumbers ~
     * maxRoomNumbers rooms in the world.
     */
    public void addManyRooms() {
        int numberOfRooms = RandomUtils.uniform(random, minRoomNumbers, maxRoomNumbers + 1);
        for (int i = 0; i < numberOfRooms; i++) {
            addOneRoom();
        }
    }

    /**
     * Adds a rectangle room with random width and height to a random location in the world.
     *
     * A room should have both width and height greater than or equal to minRoomSize
     * and smaller than or equal to maxRoomSize.
     * e.g., the smallest room. ('#' means wall and '.' means floor)
     *      ####
     *      #..#
     *      #..#
     *      ####
     */
    private void addOneRoom() {
        Room room = roomGenerator();
        existingRooms.add(room);
        createOneRoom(room.getWidth(), room.getHeight(), room.getPosition());
    }

    /** Returns a room which doesn't overlap any room in the existing room list. */
    private Room roomGenerator() {
        Position roomPosition = getRandomRoomPosition();
        int roomWidth = getRandomRoomWidth(width - roomPosition.getX());
        int roomHeight = getRandomRoomHeight(height - roomPosition.getY());
        Room room = new Room(roomWidth, roomHeight, roomPosition);
        if (isOverlap(existingRooms, room)) {
            return roomGenerator();
        }
        return room;
    }

    /** Returns true if a room overlaps any room in roomList, false otherwise. */
    private static boolean isOverlap(ArrayList<Room> roomList, Room room) {
        for (Room r : roomList) {
            if (isNewRoomInOldRoom(r, room)) {
                return true;
            }

            if (isOldRoomInNewRoom(r, room)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if r2's bottom-left position is included in r1, false otherwise.
     * @param r1 the existing room.
     * @param r2 the new room.
     */
    private static boolean isNewRoomInOldRoom(Room r1, Room r2) {
        int r1X = r1.getPosition().getX();
        int r1Y = r1.getPosition().getY();
        int r2X = r2.getPosition().getX();
        int r2Y = r2.getPosition().getY();

        int dx = r2X - r1X;
        if (dx < 0 || dx > r1.getWidth()) {
            return false;
        }

        int dy = r2Y - r1Y;
        if (dy < 0 || dy > r1.getHeight()) {
            return false;
        }

        return true;
    }

    /**
     * Returns true if r1's bottom-left position is included in r2, false otherwise.
     * @param r1 the existing room.
     * @param r2 the new room.
     */
    private static boolean isOldRoomInNewRoom(Room r1, Room r2) {
        int r1X = r1.getPosition().getX();
        int r1Y = r1.getPosition().getY();
        int r2X = r2.getPosition().getX();
        int r2Y = r2.getPosition().getY();

        if (r2X >= r1X && r2X <= r1X + r1.getWidth() - 1 && r2Y < r1Y) {
            if (r2Y + r2.getHeight() - 1 >= r1Y) {
                return true;
            }
        }

        if (r2X < r1X && r2Y < r1Y) {
            if (r2X + r2.getHeight() - 1 >= r1X && r2Y + r2.getHeight() - 1 >= r1Y) {
                return true;
            }
        }

        if (r2X < r1X && r2Y <= r1Y + r1.getHeight() - 1 && r2Y >= r1Y) {
            if (r2X + r2.getWidth() - 1 >= r1X) {
                return true;
            }
        }

        return false;
    }

    /** Creates one room in the world. */
    private void createOneRoom(int roomWidth, int roomHeight, Position roomPosition) {
        // Fill in floors.
        for (int x = 1; x < roomWidth - 1; x++) {
            for (int y = 1; y < roomHeight - 1; y++) {
                world[roomPosition.getX() + x][roomPosition.getY() + y] = Tileset.FLOOR;
            }
        }

        // Fill in walls.
        for (int y = 0; y < roomHeight; y++) {
            world[roomPosition.getX()][roomPosition.getY() + y] = Tileset.WALL;
        }

        for (int y = 0; y < roomHeight; y++) {
            world[roomPosition.getX() + roomWidth - 1][roomPosition.getY() + y] = Tileset.WALL;
        }

        for (int x = 0; x < roomWidth; x++) {
            world[roomPosition.getX() + x][roomPosition.getY()] = Tileset.WALL;
        }

        for (int x = 0; x < roomWidth; x++) {
            world[roomPosition.getX() + x][roomPosition.getY() + roomHeight - 1] = Tileset.WALL;
        }
    }

    /** Return a random integer length which can be used as the width of a room. */
    private int getRandomRoomWidth(int widthBound) {
        if (widthBound > maxRoomSize) {
            widthBound = maxRoomSize;
        }
        return RandomUtils.uniform(random, minRoomSize, widthBound + 1);
    }

    /** Return a random integer length which can be used as the height of a room. */
    private int getRandomRoomHeight(int heightBound) {
        if (heightBound > maxRoomSize) {
            heightBound = maxRoomSize;
        }
        return RandomUtils.uniform(random, minRoomSize, heightBound + 1);
    }

    /** Return a random position of a room. */
    private Position getRandomRoomPosition() {
        int x = RandomUtils.uniform(random, 0, width - 3);
        int y = RandomUtils.uniform(random, 0, height - 3);
        return new Position(x, y);
    }

    /** Return a world. */
    public TETile[][] getWorld() {
        return world;
    }

    /** Fill the world with the the NOTHING tiles. */
    public void fillWithNothing() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
}
