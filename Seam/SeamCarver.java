import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private final Picture picture;
    private int width;
    private int height;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture cannot be null");
        }
        // create new picture without mutation
        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
        this.energy = new double[width][height];
    }

    // current picture
    public Picture picture() {
        Picture newPicture = new Picture(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newPicture.set(x, y, picture.get(x, y));
            }
        }
        return newPicture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        double BORDER = 1000.0;
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("x or y out of bounds");
        }
        if (energy[x][y] != 0.0) {

        } else if (isBorder(x, y)) {
            energy[x][y] = BORDER;
        } else {
            energy[x][y] = dualGradient(x, y);
        }
        return energy[x][y];
    }

    private boolean isBorder(int x, int y) {
        return x == 0 || x == width - 1 || y == 0 || y == height - 1;
    }

    private double dualGradient(int x, int y) {
        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        Color top = picture.get(x, y - 1);
        Color bottom = picture.get(x, y + 1);
        int xRed = left.getRed() - right.getRed();
        int xGreen = left.getGreen() - right.getGreen();
        int xBlue = left.getBlue() - right.getBlue();
        int yRed = top.getRed() - bottom.getRed();
        int yGreen = top.getGreen() - bottom.getGreen();
        int yBlue = top.getBlue() - bottom.getBlue();
        double xGradient = Math.pow(xRed, 2) + Math.pow(xGreen, 2) + Math.pow(xBlue, 2);
        double yGradient = Math.pow(yRed, 2) + Math.pow(yGreen, 2) + Math.pow(yBlue, 2);
        return Math.sqrt(xGradient + yGradient);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[width];
        int[][] edgeTo = new int[width][height];
        double[][] dist = new double[width][height];
        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                dist[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        // DAG shortest paths
        int[] paths = { -1, 0, 1 };
        for (int x = 0; x < width - 1; x++) {
            int nextX = x + 1;
            for (int y = 0; y < height; y++) {
                for (int offset : paths) {
                    int nextY = y + offset;
                    if (nextY < 0 || nextY >= height) continue;
                    relax(dist, edgeTo, x, y, nextX, nextY, false);
                }
            }
        }

        // find shortest path
        double minDist = dist[width - 1][0];
        int index = 0;
        for (int y = 1; y < height; y++) {
            if (dist[width - 1][y] < minDist) {
                minDist = dist[width - 1][y];
                index = y;
            }
        }
        for (int x = width - 1; x >= 0; x--) {
            seam[x] = index;
            index = edgeTo[x][index];
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height];
        int[][] edgeTo = new int[width][height];
        double[][] dist = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 1; y < height; y++) {
                dist[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        int[] paths = { -1, 0, 1 };
        for (int y = 0; y < height - 1; y++) {
            int nextY = y + 1;
            for (int x = 0; x < width; x++) {
                for (int offset : paths) {
                    int nextX = x + offset;
                    if (nextX < 0 || nextX >= width) continue;
                    relax(dist, edgeTo, x, y, nextX, nextY, true);
                }
            }
        }

        double minDist = dist[0][height - 1];
        int index = 0;
        for (int x = 1; x < width; x++) {
            if (dist[x][height - 1] < minDist) {
                minDist = dist[x][height - 1];
                index = x;
            }
        }
        for (int y = height - 1; y >= 0; y--) {
            seam[y] = index;
            index = edgeTo[index][y];
        }
        return seam;
    }

    private void relax(double[][] dist, int[][] edge, int x0, int y0, int x1, int y1, boolean isX) {
        if (dist[x1][y1] > dist[x0][y0] + energy(x1, y1)) {
            dist[x1][y1] = dist[x0][y0] + energy(x1, y1);
            edge[x1][y1] = isX ? x0 : y0;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        isValidSeam(seam, width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height - 1; y++) {
                if (y >= seam[x]) {
                    picture.setRGB(x, y, picture.getRGB(x, y + 1));
                }
            }
        }
        height--;
        energy = new double[width][height];
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        isValidSeam(seam, height, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width - 1; x++) {
                if (x >= seam[y]) {
                    picture.setRGB(x, y, picture.getRGB(x + 1, y));
                }
            }
        }
        width--;
        energy = new double[width][height];
    }

    private void isValidSeam(int[] seam, int len, int limit) {
        if (seam == null || seam.length != len || limit <= 1) {
            throw new IllegalArgumentException("Invalid seam");
        }
        for (int i = 0; i < len; i++) {
            if (seam[i] < 0 || seam[i] >= limit) {
                throw new IllegalArgumentException("Invalid seam");
            }
        }
        for (int i = 0; i < len - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("Invalid seam");
            }
        }
    }
}
