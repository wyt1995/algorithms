import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF unionFind;
    private final WeightedQuickUnionUF withoutBottom;
    private final boolean[][] grid;
    private final int length;
    private final int size;
    private final int top;
    private final int bottom;
    private int openSites;
    private boolean percolates;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size must be positive");
        }

        this.length = n;
        this.size = n * n;
        this.grid = new boolean[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                grid[i][j] = false;
            }
        }
        this.top = 0;
        this.bottom = size + 1;
        this.openSites = 0;
        this.percolates = false;

        this.unionFind = new WeightedQuickUnionUF(size + 2);
        this.withoutBottom = new WeightedQuickUnionUF(size + 1);
        for (int i = 1; i <= length; i++) {
            withoutBottom.union(top, index(1, i));
            unionFind.union(top, index(1, i));
            unionFind.union(bottom, index(length, i));
        }
    }

    private void checkRep(int row, int col) {
        if (row < 1 || row > length || col < 1 || col > length) {
            throw new IllegalArgumentException("Invalid row or column");
        }
    }

    private int index(int row, int col) {
        checkRep(row, col);
        return (row - 1) * length + col;
    }

    private void connectNeighbors(int row, int col) {
        int[] rows = {-1, 0, 0, 1};
        int[] cols = {0, -1, 1, 0};
        for (int i = 0; i < rows.length; i++) {
            int r = row + rows[i];
            int c = col + cols[i];
            if (r < 1 || r > length || c < 1 || c > length) {
                continue;
            }
            if (isOpen(r, c)) {
                unionFind.union(index(r, c), index(row, col));
                withoutBottom.union(index(r, c), index(row, col));
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        grid[row - 1][col - 1] = true;
        openSites += 1;
        connectNeighbors(row, col);
        if (openSites >= length) {
            percolates = unionFind.find(top) == unionFind.find(bottom);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRep(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) {
            return false;
        }
        return withoutBottom.find(top) == withoutBottom.find(index(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolates;
    }
}
