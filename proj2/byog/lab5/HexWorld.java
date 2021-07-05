package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    /** Shows the position of the lower left corner of the hexagon. */
    public static class Position {
        private int x;
        private int y;

        Position(int xPos, int yPos) {
            x = xPos;
            y = yPos;
        }
    }

    /**
     * Computes the width of row i for a size s hexagon.
     * @param s The size of the hex
     * @param i The row number where i = 0 is the bottom row
     * @return the width of row i
     *
     * The width of row i = s + 2 * i, for i = 0 ~ s-1;
     *                    = s + 2 * (2s - 1 - i), for i = s ~ 2s-1.
     */
    public static int hexRowWidth(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - i;
        }

        return s + 2 * effectiveI;
    }

    /**
     * Computes relative x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero. For example, if s = 3, and i = 2, this function
     * returns -2, because the row 2 up from the bottom starts 2 to the left
     * of the start position, e.g.
     *   xxxx
     *  xxxxxx
     * xxxxxxxx
     * xxxxxxxx <-- i = 2, starts 2 spots to the left of the bottom of the hex
     *  xxxxxx
     *   xxxx
     *
     * @param s size of the hexagon
     * @param i row num of the hexagon, where i = 0 is the bottom
     * @return the relative x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero.
     *
     * The relative x coordinate of the leftmost tile in the ith row is
     * -i, for i = 0 ~ s-1;
     * -(2s - 1 - i), for i = s ~ 2s-1.
     */
    public static int hexRowOffset(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - i;
        }

        return -effectiveI;
    }

    /** Adds a row of the same tile.
     * @param world the world to draw on
     * @param p the leftmost position of the row
     * @param width the number of tiles wide to draw
     * @param t the tile to draw
     */
    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int i = 0; i < width; i++) {
            int xPos = p.x + i;
            world[xPos][p.y] = t;
        }
    }

    /**
     * Adds a hexagon of side length s to a given position in the world.
     * @param world the world to draw on.
     * @param p the lower left corner of the hexagon.
     * @param s side length of a hexagon.
     * @param t the tile to draw.
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        /**
         * Hexagons have 2*s rows.
         * This code iterates up from the bottom row, which we call row 0.
         */
        for (int i = 0; i < 2 * s; i++) {
            int rowWidth = hexRowWidth(s, i);
            int rowXStart = p.x + hexRowOffset(s, i);
            int rowYPos = p.y + i;
            Position rowStartPos = new Position(rowXStart, rowYPos);
            addRow(world, rowStartPos, rowWidth, t);
        }
    }

    @Test
    public void testHexRowWidth() {
        assertEquals(4, hexRowWidth(4, 0));
        assertEquals(6, hexRowWidth(4, 1));
        assertEquals(8, hexRowWidth(4, 2));
        assertEquals(10, hexRowWidth(4, 3));
        assertEquals(10, hexRowWidth(4, 4));
        assertEquals(8, hexRowWidth(4, 5));
        assertEquals(6, hexRowWidth(4, 6));
        assertEquals(4, hexRowWidth(4, 7));
    }

    @Test
    public void testHexRowOffset() {
        assertEquals(0, hexRowOffset(3, 5));
        assertEquals(-1, hexRowOffset(3, 4));
        assertEquals(-2, hexRowOffset(3, 3));
        assertEquals(-2, hexRowOffset(3, 2));
        assertEquals(-1, hexRowOffset(3, 1));
        assertEquals(0, hexRowOffset(3, 0));
        assertEquals(0, hexRowOffset(2, 0));
        assertEquals(-1, hexRowOffset(2, 1));
        assertEquals(-1, hexRowOffset(2, 2));
        assertEquals(0, hexRowOffset(2, 3));
    }

    public static void main(String[] args) {
        // Initialize the tile rendering engine.
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // Initialize tiles.
        TETile[][] hexWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                hexWorld[x][y] = Tileset.NOTHING;
            }
        }
        addHexagon(hexWorld, new Position(25, 25), 4, Tileset.FLOWER);

        // Draw the world to the screen.
        ter.renderFrame(hexWorld);
    }
}
