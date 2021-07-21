package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import static java.lang.Math.sqrt;

public class PercolationStats {
    private double[] threshold;
    /**
     * Perform T independent experiments on an N-by-N grid.
     * To estimate the percolation threshold, consider the following computational experiment:
     * 1. Initialize all sites to be blocked.
     * 2. Repeat the following until the system percolates:
     *    2-1. Choose a site uniformly at random among all blocked sites.
     *    2-2. Open the site.
     *    2-3. The fraction of sites that are opened when the system percolates provides
     *         an estimate of the percolation threshold.
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T must be greater than zero.");
        }
        threshold = new double[T];
        Percolation p = pf.make(N);
        int count = 0;
        while (count < T) {
            while (!p.percolates()) {
                int row = StdRandom.uniform(0, N);
                int col = StdRandom.uniform(0, N);
                if (p.isOpen(row, col)) {
                    continue;
                }
                p.open(row, col);
            }
            threshold[count] = (double) p.numberOfOpenSites() / (N * N);
            count++;
        }
    }

    /** Sample mean of percolation threshold. */
    public double mean() {
        return StdStats.mean(threshold);
    }

    /** Sample standard deviation of percolation threshold. */
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    /** Low endpoint of 95% confidence interval. */
    public double confidenceLow() {
        return mean() - (1.96 * stddev() / sqrt(threshold.length));
    }

    /** High endpoint of 95% confidence interval. */
    public double confidenceHigh() {
        return mean() + (1.96 * stddev() / sqrt(threshold.length));
    }
}
