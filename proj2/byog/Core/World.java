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

    public World(int w, int h, long s) {
        width = w;
        height = h;
        seed = s;
        random = new Random(seed);
        world = new TETile[width][height];
    }

    /**
     * Adds a rectangle room with random width and height to a random location in the world.
     *
     * A room should have both width and height greater than or equal to 4 and smaller than
     * or equal to roomMaxSize.
     * e.g., the smallest room. ('#' means wall and '.' means floor)
     *      ####
     *      #..#
     *      #..#
     *      ####
     */
    public void addOneRoom() {
        Position roomPosition = getRandomPosition();
        int roomWidth = getRandomRoomWidth(width - roomPosition.getX());
        int roomHeight = getRandomRoomHeight(height - roomPosition.getY());
        Room room = new Room(roomWidth, roomHeight, roomPosition);
        existingRooms.add(room);
        createOneRoom(roomWidth, roomHeight, roomPosition);
    }

    /** Create one room in the world. */
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
