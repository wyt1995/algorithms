import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int size;
    private final int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.size = tiles.length;
        this.board = new int[size][size];  // create a copy of tiles
        for (int i = 0; i < size; i++) {
            System.arraycopy(tiles[i], 0, board[i], 0, size);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size).append("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(" ").append(board[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int goal = (i == j && i == size - 1) ? 0 : size * i + j + 1;
                if (board[i][j] != goal && board[i][j] != 0) {
                    count += 1;
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int diff = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int goal = (i == j && i == size - 1) ? 0 : size * i + j + 1;
                if (board[i][j] == goal || board[i][j] == 0) {
                    continue;
                }
                int x = Math.abs((board[i][j] - 1) / size - i);
                int y = Math.abs((board[i][j] - 1) % size - j);
                diff += x + y;
            }
        }
        return diff;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int goal = (i == j && i == size - 1) ? 0 : size * i + j + 1;
                if (board[i][j] != goal) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (this.getClass() == y.getClass()) {
            Board other = (Board) y;
            return this.size == other.size && Arrays.deepEquals(this.board, other.board);
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int r, c = 0;
        findZero:
        for (r = 0; r < size; r++) {
            for (c = 0; c < size; c++) {
                if (board[r][c] == 0) {
                    break findZero;
                }
            }
        }

        List<Board> neighbors = new ArrayList<>();
        int[] rows = {-1, 0, 1, 0};
        int[] cols = {0, -1, 0, 1};
        for (int i = 0; i < rows.length; i++) {
            int row = r + rows[i];
            int col = c + cols[i];
            if (outOfBounds(row, col)) {
                continue;
            }
            int[][] copy = copyTiles();
            copy[r][c] = board[row][col];
            copy[row][col] = 0;
            neighbors.add(new Board(copy));
        }
        return neighbors;
    }

    private boolean outOfBounds(int row, int col) {
        return row < 0 || row >= size || col < 0 || col >= size;
    }

    private int[][] copyTiles() {
        int[][] copy = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, size);
        }
        return copy;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twin = copyTiles();
        int r1 = 0, c1 = 0;
        int r2 = 0, c2 = 1;
        if (twin[r1][c1] == 0) {
            r1 = 1;
        } else if (twin[r2][c2] == 0) {
            r2 = 1;
            c2 = 0;
        }
        int temp = twin[r1][c1];
        twin[r1][c1] = twin[r2][c2];
        twin[r2][c2] = temp;
        return new Board(twin);
    }

    public static void main(String[] args) {
        int[][] tiles = {{1, 2, 3}, {4, 0, 5}, {7, 8, 6}};
        Board board = new Board(tiles);
        System.out.println(board);

        Iterable<Board> neighbors = board.neighbors();
        for (Board neighbor : neighbors) {
            System.out.println(neighbor);
        }
    }
}
