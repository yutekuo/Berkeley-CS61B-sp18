package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int size; // Size of the grid.
    private int numberOfOpenSites;
    private boolean[][] isSiteOpen; // For each site in the grid, is it open?
    private WeightedQuickUnionUF sites; // Total: N * N sites + 2 virtual sites.
    private final int topVirtualSite;
    private final int bottomVirtualSite;
    private WeightedQuickUnionUF sitesWithTopVirtual; // Total: N * N sites + 1 virtual site.


    /** Create N-by-N grid, with all sites initially blocked. */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than zero.");
        }

        size = N;
        numberOfOpenSites = 0;

        isSiteOpen = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                isSiteOpen[i][j] = false;
            }
        }

        sites = new WeightedQuickUnionUF(N * N + 2);
        topVirtualSite = N * N;
        bottomVirtualSite = N * N + 1;
        sitesWithTopVirtual = new WeightedQuickUnionUF(N * N + 1);
    }

    /** Convert coordinate (x, y) to an integer. */
    private int xyTo1D(int row, int col) {
        return row * size + col;
    }

    /** Returns true if row and col are valid inputs, false otherwise. */
    private boolean areInputsValid(int row, int col) {
        return row <= size - 1 && col <= size - 1 && row >= 0 && col >= 0;
    }

    /** Open the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        if (!areInputsValid(row, col)) {
            throw new IndexOutOfBoundsException(
                    "The row and column indices must be integers between 0 and N − 1.");
        }

        if (isOpen(row, col)) {
            return;
        }

        isSiteOpen[row][col] = true;
        numberOfOpenSites++;
        int currentSite = xyTo1D(row, col);

        // Virtual top site connected to all open items in top row.
        if (currentSite <= size - 1) {
            sites.union(topVirtualSite, currentSite);
            sitesWithTopVirtual.union(topVirtualSite, currentSite);
        }

        // Virtual bottom site connected to all open items in bottom row.
        if (currentSite >= size * (size - 1)) {
            sites.union(bottomVirtualSite, currentSite);
        }

        // If current site's upper site is open, union them.
        if (areInputsValid(row - 1, col)) {
            if (isOpen(row - 1, col)) {
                sites.union(currentSite, xyTo1D(row - 1, col));
                sitesWithTopVirtual.union(currentSite, xyTo1D(row - 1, col));
            }
        }

        // If current site's lower site is open, union them.
        if (areInputsValid(row + 1, col)) {
            if (isOpen(row + 1, col)) {
                sites.union(currentSite, xyTo1D(row + 1, col));
                sitesWithTopVirtual.union(currentSite, xyTo1D(row + 1, col));
            }
        }

        // If current site's left site is open, union them.
        if (areInputsValid(row, col - 1)) {
            if (isOpen(row, col - 1)) {
                sites.union(currentSite, xyTo1D(row, col - 1));
                sitesWithTopVirtual.union(currentSite, xyTo1D(row, col - 1));
            }
        }

        // If current site's right site is open, union them.
        if (areInputsValid(row, col + 1)) {
            if (isOpen(row, col + 1)) {
                sites.union(currentSite, xyTo1D(row, col + 1));
                sitesWithTopVirtual.union(currentSite, xyTo1D(row, col + 1));
            }
        }
    }

    /** Is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        if (!areInputsValid(row, col)) {
            throw new IndexOutOfBoundsException(
                    "The row and column indices must be integers between 0 and N − 1.");
        }
        return isSiteOpen[row][col];
    }

    /** Is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        if (!areInputsValid(row, col)) {
            throw new IndexOutOfBoundsException(
                    "The row and column indices must be integers between 0 and N − 1.");
        }

        return sitesWithTopVirtual.connected(xyTo1D(row, col), topVirtualSite);
    }

    /** Number of open sites. */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /** Does the system percolate? */
    public boolean percolates() {
        return sites.connected(topVirtualSite, bottomVirtualSite);
    }

    /** Use for unit testing (not required) */
    public static void main(String[] args) {
        Percolation p = new Percolation(10);
        p.open(-1, 5);
    }
}
