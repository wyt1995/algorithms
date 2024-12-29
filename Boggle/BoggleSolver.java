import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private final Trie trie;
    private Set<String> validWords;

    private final int[] rowDir = {-1, -1, -1, 0, 0, 1, 1, 1};
    private final int[] colDir = {-1, 0, 1, -1, 1, -1, 0, 1};

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trie = new Trie();
        for (String word : dictionary) {
            trie.insert(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int m = board.rows();
        int n = board.cols();
        boolean[][] marked = new boolean[m][n];
        validWords = new HashSet<>();
        StringBuilder word = new StringBuilder();
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                dfs(board, row, col, marked, word);
            }
        }
        return validWords;
    }

    private void dfs(BoggleBoard board, int row, int col, boolean[][] marked, StringBuilder word) {
        if (row < 0 || row >= board.rows() || col < 0 || col >= board.cols()) {
            return;
        }
        if (marked[row][col]) {
            return;
        }
        String letter;
        char c = board.getLetter(row, col);
        if (c == 'Q') {
            letter = "QU";
        } else {
            letter = String.valueOf(c);
        }

        int len = word.length();
        word.append(letter);
        if (!trie.startsWith(word.toString())) {
            word.setLength(len);
            return;
        }
        if (word.length() >= 3 && trie.search(word.toString())) {
            validWords.add(word.toString());
        }

        marked[row][col] = true;
        for (int i = 0; i < rowDir.length; i++) {
            int nextRow = row + rowDir[i];
            int nextCol = col + colDir[i];
            dfs(board, nextRow, nextCol, marked, word);
        }
        marked[row][col] = false;
        word.setLength(len);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!trie.search(word)) {
            return 0;
        }
        int len = word.length();
        if (len < 3) return 0;
        if (len <= 4) return 1;
        if (len == 5) return 2;
        if (len == 6) return 3;
        if (len == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
