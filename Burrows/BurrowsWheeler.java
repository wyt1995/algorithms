import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(input);
        int n = input.length();
        char[] t = new char[n];
        int first = 0;
        for (int i = 0; i < n; i++) {
            if (suffixArray.index(i) == 0) {
                first = i;
            }
            t[i] = input.charAt((suffixArray.index(i) - 1 + n) % n);
        }
        BinaryStdOut.write(first);
        for (char c : t) {
            BinaryStdOut.write(c, 8);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] t = BinaryStdIn.readString().toCharArray();
        int n = t.length;

        int[] count = new int[R +1];
        for (int i = 0; i < n; i++) {
            count[t[i]+1]++;
        }
        for (int r = 0; r < R; r++) {
            count[r+1] += count[r];
        }

        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            next[count[t[i]]++] = i;
        }

        int p = first;
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(t[next[p]]);
            p = next[p];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
