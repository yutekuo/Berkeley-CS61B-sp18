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
    private final int roomMaxSize = 15;
    private final int maxRoomNumbers = 20;

    public World(int w, int h, long s) {
        width = w;
        height = h;
        seed = s;
        random = new Random(seed);
        world = new TETile[width][height];
    }

    /**
     * Adds random number of rooms to the world. There should be 10 ~ maxRoomNumbers rooms
     * in the world.
     */
    public void addManyRooms() {
        int numberOfRooms = RandomUtils.uniform(random, 10, maxRoomNumbers + 1);
        for (int i = 0; i < numberOfRooms; i++) {
            addOneRoom();
        }
    }

    /**
     * Adds a rectangle room with random width and height to a random location in the world.
     *
     * @return the room that is added to the world.
     *
     * A room should have both width and height greater than or equal to 4 and smaller than
     * or equal to roomMaxSize.
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
        Position roomPosition = getRandomPosition();
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
        if (widthBound > roomMaxSize) {
            widthBound = roomMaxSize;
        }
        return RandomUtils.uniform(random, 4, widthBound + 1);
    }

    /** Return a random integer length which can be used as the height of a room. */
    private int getRandomRoomHeight(int heightBound) {
        if (heightBound > roomMaxSize) {
            heightBound = roomMaxSize;
        }
        return RandomUtils.uniform(random, 4, heightBound + 1);
    }

    /** Return a random position. */
    private Position getRandomPosition() {
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
