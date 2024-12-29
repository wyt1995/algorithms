import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int size;
    private final int trials;
    private final double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        this.size = n;
        this.trials = trials;
        this.thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(size);
            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(size) + 1;
                int col = StdRandom.uniformInt(size) + 1;
                percolation.open(row, col);
            }
            thresholds[i] = (double) percolation.numberOfOpenSites() / (size * size);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    // test client
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java-algs4 PercolationStats n T");
            return;
        }
        int n = Integer.parseInt(args[0]);
        int trial = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trial);
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo()
                + ", " + stats.confidenceHi() + "]");
    }
}
