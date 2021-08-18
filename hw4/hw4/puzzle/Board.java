package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

import static java.lang.Math.abs;

public class Board implements WorldState {
    private static final int BLANK = 0;
    private int boardSize;
    private int[][] board;

    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j.
     */
    public Board(int[][] tiles) {
        boardSize = tiles.length;
        board = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank).
     */
    public int tileAt(int i, int j) {
        if (i < 0 || i > size() - 1 || j < 0 || j > size() - 1) {
            throw new IndexOutOfBoundsException("Both i and j should be between 0 and N − 1!");
        }
        return board[i][j];
    }

    /**
     * Returns the board size N.
     */
    public int size() {
        return boardSize;
    }

    /**
     * Returns the neighbors of the current board.
     *
     * @Source http://joshh.ug/neighbors.html
     */
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (abs(-bug + l11il) + abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    /**
     * Hamming estimate: The number of tiles in the wrong position.
     */
    public int hamming() {
        int wrong = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (board[i][j] == BLANK) {
                    continue;
                }
                if (board[i][j] != hammingHelper(i, j)) {
                    wrong++;
                }
            }
        }
        return wrong;
    }

    /**
     * Calculate the corresponding correct value at board[i][j].
     */
    private int hammingHelper(int i, int j) {
        return size() * i + j + 1;
    }

    /**
     *  Manhattan estimate: The sum of the Manhattan distances
     *  (sum of the vertical and horizontal distance) from the tiles to their goal positions.
     */
    public int manhattan() {
        int wrong = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                int value = board[i][j];
                if (value == BLANK) {
                    continue;
                }
                int realXPos = xPos(value);
                int realYPos = yPos(value);
                wrong += abs(realXPos - i);
                wrong += abs(realYPos - j);
            }
        }
        return wrong;
    }

    /**
     * Returns the real x-position of the value.
     */
    private int xPos(int value) {
        if (value % size() == 0) {
            return value / size() - 1;
        }
        return value / size();
    }

    /**
     * Returns the real y-position of the value.
     */
    private int yPos(int value) {
        int a = value % size();
        if (a == 0) {
            return size() - 1;
        }
        return a - 1;
    }

    /**
     * Estimated distance to goal. This method should simply return
     * the results of manhattan() when submitted to Gradescope.
     */
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * Returns true if this board's tile values are the same position as y's.
     */
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }

        if (y == null) {
            return false;
        }

        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;

        if (that.size() != this.size()) {
            return false;
        }

        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (this.board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Returns the string representation of the board.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
