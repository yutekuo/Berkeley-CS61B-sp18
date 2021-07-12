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
    private final int maxRoomSize = 13;
    private final int minRoomNumbers = 15;
    private final int maxRoomNumbers = 24;
    private Position playerPosition;
    private int playerRoomNumber;
    private Position doorPosition;

    public World(int w, int h, long s) {
        width = w;
        height = h;
        seed = s;
        random = new Random(seed);
        world = new TETile[width][height];
    }

    public void addDoor() {
        int doorRoomNumber = getDoorRoomNumber();
        getDoorPosition(existingRooms.get(doorRoomNumber));
        world[doorPosition.getX()][doorPosition.getY()] = Tileset.LOCKED_DOOR;
    }

    private void getDoorPosition(Room room) {
        int leftX = room.getPosition().getX();
        int bottomY = room.getPosition().getY();
        int rightX = leftX + room.getWidth() - 1;
        int topY = bottomY + room.getHeight() - 1;
        int x = RandomUtils.uniform(random, leftX + 1, rightX);
        int y = RandomUtils.uniform(random, bottomY + 1, topY);
        int side = random.nextInt(10) % 4;
        switch (side) {
            case 0: // up
                if (topY == height - 1 && world[x][topY - 1] == Tileset.FLOOR) {
                    doorPosition = new Position(x, topY);
                    break;
                } else if (world[x][topY + 1] == Tileset.NOTHING
                        && world[x][topY - 1] == Tileset.FLOOR) {
                    doorPosition = new Position(x, topY);
                    break;
                }
                getDoorPosition(room);
                break;
            case 1: // down
                if (bottomY == 0 && world[x][bottomY + 1] == Tileset.FLOOR) {
                    doorPosition = new Position(x, bottomY);
                    break;
                } else if (world[x][bottomY - 1] == Tileset.NOTHING
                        && world[x][bottomY + 1] == Tileset.FLOOR) {
                    doorPosition = new Position(x, bottomY);
                    break;
                }
                getDoorPosition(room);
                break;
            case 2: // left
                if (leftX == 0 && world[leftX + 1][y] == Tileset.FLOOR) {
                    doorPosition = new Position(leftX, y);
                    break;
                } else if (world[leftX - 1][y] == Tileset.NOTHING
                        && world[leftX + 1][y] == Tileset.FLOOR) {
                    doorPosition = new Position(leftX, y);
                    break;
                }
                getDoorPosition(room);
                break;
            case 3: // right
                if (rightX == width - 1 && world[rightX - 1][y] == Tileset.FLOOR) {
                    doorPosition = new Position(rightX, y);
                    break;
                } else if (world[rightX + 1][y] == Tileset.NOTHING
                        && world[rightX - 1][y] == Tileset.FLOOR) {
                    doorPosition = new Position(rightX, y);
                    break;
                }
                getDoorPosition(room);
                break;
            default:
        }
    }

    private int getDoorRoomNumber() {
        int doorRoomNumber = RandomUtils.uniform(random, 0, existingRooms.size());
        if (doorRoomNumber == playerRoomNumber) {
            return getDoorRoomNumber();
        }
        return doorRoomNumber;
    }

    /**
     * Move player's position according to the input.
     * If input is 'W': move up
     *             'A': move left
     *             'S': move down
     *             'D': move right.
     */
    public void move(char input) {
        int x = playerPosition.getX();
        int y = playerPosition.getY();
        if (input == 'W') {
            if (world[x][y + 1] != Tileset.FLOOR) {
                return;
            }
            playerPosition = new Position(x, y + 1);
            world[x][y] = Tileset.FLOOR;
            world[x][y + 1] = Tileset.PLAYER;
        } else if (input == 'A') {
            if (world[x - 1][y] != Tileset.FLOOR) {
                return;
            }
            playerPosition = new Position(x - 1, y);
            world[x][y] = Tileset.FLOOR;
            world[x - 1][y] = Tileset.PLAYER;
        } else if (input == 'S') {
            if (world[x][y - 1] != Tileset.FLOOR) {
                return;
            }
            playerPosition = new Position(x, y - 1);
            world[x][y] = Tileset.FLOOR;
            world[x][y - 1] = Tileset.PLAYER;
        } else if (input == 'D') {
            if (world[x + 1][y] != Tileset.FLOOR) {
                return;
            }
            playerPosition = new Position(x + 1, y);
            world[x][y] = Tileset.FLOOR;
            world[x + 1][y] = Tileset.PLAYER;
        }
    }

    /** Randomly adds a player to the world. */
    public void addPlayer() {
        playerRoomNumber = RandomUtils.uniform(random, 0, existingRooms.size());
        playerPosition = getPlayerPosition();
        world[playerPosition.getX()][playerPosition.getY()] = Tileset.PLAYER;
    }

    /**
     * Returns the position of a FLOOR in the room with index playerRoomNumber
     * in the existing room list.
     */
    private Position getPlayerPosition() {
        Room room = existingRooms.get(playerRoomNumber);
        int roomX = room.getPosition().getX();
        int roomY = room.getPosition().getY();
        int x = RandomUtils.uniform(random, roomX + 1, roomX + room.getWidth() - 1);
        int y = RandomUtils.uniform(random, roomY + 1, roomY + room.getHeight() - 1);
        if (world[x][y] == Tileset.FLOOR) {
            return new Position(x, y);
        }
        return getPlayerPosition();
    }

    /** Adds hallways to all of the nearby rooms in the existing room list. */
    public void addHallways() {
        sortExistingRooms();
        for (int i = 0; i < existingRooms.size() - 1; i++) {
            Room currentRoom = existingRooms.get(i);
            int currentRoomX = currentRoom.getPosition().getX();
            int currentRoomY = currentRoom.getPosition().getY();
            int x1 = RandomUtils.uniform(random, currentRoomX + 1,
                    currentRoomX + currentRoom.getWidth() - 1);
            int y1 = RandomUtils.uniform(random, currentRoomY + 1,
                    currentRoomY + currentRoom.getHeight() - 1);
            Position p1 = new Position(x1, y1);

            Room nextRoom = existingRooms.get(i + 1);
            int nextRoomX = nextRoom.getPosition().getX();
            int nextRoomY = nextRoom.getPosition().getY();
            int x2 = RandomUtils.uniform(random, nextRoomX + 1,
                    nextRoomX + nextRoom.getWidth() - 1);
            int y2 = RandomUtils.uniform(random, nextRoomY + 1,
                    nextRoomY + nextRoom.getHeight() - 1);
            Position p2 = new Position(x2, y2);

            drawHallway(p1, p2);
        }
    }

    /** Given two arbitrary positions, draw a hallway to connect them. */
    private void drawHallway(Position p1, Position p2) {
        if (p1.getX() == p2.getX()) {
            drawVerticalHallway(p1, p2);
        } else if (p1.getY() == p2.getY()) {
            drawHorizontalHallway(p1, p2);
        } else {
            drawLShapeHallway(p1, p2);
        }
    }

    /**
     * Draw a L-shape hallway, which can be used to connect two positions
     * that have different x- and y- coordinates.
     * Position p1's x coordinate will always be smaller than or equal to p2's.
     */
    private void drawLShapeHallway(Position p1, Position p2) {
        if (p1.getY() < p2.getY()) {
            Position bottomRightCorner = new Position(p2.getX(), p1.getY());
            drawHorizontalHallway(p1, bottomRightCorner);
            drawVerticalHallway(bottomRightCorner, p2);
            if (world[p2.getX() + 1][p1.getY() - 1] != Tileset.FLOOR) {
                world[p2.getX() + 1][p1.getY() - 1] = Tileset.WALL;
            }
        } else {
            Position bottomLeftCorner = new Position(p1.getX(), p2.getY());
            drawVerticalHallway(p1, bottomLeftCorner);
            drawHorizontalHallway(bottomLeftCorner, p2);
            if (world[p1.getX() - 1][p2.getY() - 1] != Tileset.FLOOR) {
                world[p1.getX() - 1][p2.getY() - 1] = Tileset.WALL;
            }
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
    private void drawVerticalHallway(Position p1, Position p2) {
        if (p1.getX() != p2.getX()) {
            throw new IllegalArgumentException("Position " + p1
                    + " and Position " + p2 + " should have same X-positions!");
        }

        if (p1.getY() > p2.getY()) {
            int dy = p1.getY() - p2.getY();
            for (int y = 0; y <= dy; y++) {
                world[p1.getX()][p2.getY() + y] = Tileset.FLOOR;
                if (world[p1.getX() - 1][p2.getY() + y] != Tileset.FLOOR) {
                    world[p1.getX() - 1][p2.getY() + y] = Tileset.WALL;
                }

                if (world[p1.getX() + 1][p2.getY() + y] != Tileset.FLOOR) {
                    world[p1.getX() + 1][p2.getY() + y] = Tileset.WALL;
                }
            }
        } else {
            int dy = p2.getY() - p1.getY();
            for (int y = 0; y <= dy; y++) {
                world[p1.getX()][p1.getY() + y] = Tileset.FLOOR;
                if (world[p1.getX() - 1][p1.getY() + y] != Tileset.FLOOR) {
                    world[p1.getX() - 1][p1.getY() + y] = Tileset.WALL;
                }

                if (world[p1.getX() + 1][p1.getY() + y] != Tileset.FLOOR) {
                    world[p1.getX() + 1][p1.getY() + y] = Tileset.WALL;
                }
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
    private void drawHorizontalHallway(Position p1, Position p2) {
        if (p1.getY() != p2.getY()) {
            throw new IllegalArgumentException("Position " + p1
                    + " and Position " + p2 + " should have same Y-positions!");
        }

        if (p1.getX() > p2.getX()) {
            int dx = p1.getX() - p2.getX();
            for (int x = 0; x <= dx; x++) {
                world[p2.getX() + x][p1.getY()] = Tileset.FLOOR;
                if (world[p2.getX() + x][p1.getY() - 1] != Tileset.FLOOR) {
                    world[p2.getX() + x][p1.getY() - 1] = Tileset.WALL;
                }

                if (world[p2.getX() + x][p1.getY() + 1] != Tileset.FLOOR) {
                    world[p2.getX() + x][p1.getY() + 1] = Tileset.WALL;
                }
            }
        } else {
            int dx = p2.getX() - p1.getX();
            for (int x = 0; x <= dx; x++) {
                world[p1.getX() + x][p1.getY()] = Tileset.FLOOR;
                if (world[p1.getX() + x][p1.getY() - 1] != Tileset.FLOOR) {
                    world[p1.getX() + x][p1.getY() - 1] = Tileset.WALL;
                }

                if (world[p1.getX() + x][p1.getY() + 1] != Tileset.FLOOR) {
                    world[p1.getX() + x][p1.getY() + 1] = Tileset.WALL;
                }
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
            if (r2X + r2.getWidth() - 1 >= r1X && r2Y + r2.getHeight() - 1 >= r1Y) {
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
