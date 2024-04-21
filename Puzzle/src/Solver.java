import java.util.Deque;
import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode solution;
    private final boolean solvable;

    /**
     * A search node consists of a board, the number of moves made to reach the board,
     * and the previous search node.
     */
    private static class SearchNode implements Comparable<SearchNode> {
        Board board;
        SearchNode prev;
        int moves;
        int manhattan;

        public SearchNode(Board board, SearchNode prev, int moves) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            this.manhattan = board.manhattan();
        }

        @Override
        public int compareTo(SearchNode other) {
            return this.moves - other.moves + this.manhattan - other.manhattan;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        solution = null;
        solvable = solve(initial);
    }

    private boolean solve(Board initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twin = new MinPQ<>();
        pq.insert(new SearchNode(initial, null, 0));
        twin.insert(new SearchNode(initial.twin(), null, 0));

        while (true) {
            SearchNode node = pq.delMin();
            SearchNode twinNode = twin.delMin();

            if (node.board.isGoal()) {
                solution = node;
                return true;
            }

            if (twinNode.board.isGoal()) {
                return false;
            }

            updatePQ(pq, node);
            updatePQ(twin, twinNode);
        }
    }

    private static void updatePQ(MinPQ<SearchNode> queue, SearchNode node) {
        Board board = node.board;
        int moves = node.moves;
        for (Board next : board.neighbors()) {
            if (node.prev == null || !next.equals(node.prev.board)) {
                queue.insert(new SearchNode(next, node, moves + 1));
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solvable ? solution.moves : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }
        Deque<Board> solutions = new LinkedList<>();
        SearchNode current = solution;
        while (current != null) {
            solutions.addFirst(current.board);
            current = current.prev;
        }
        return solutions;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
