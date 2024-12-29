import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static final int R = 256;
    private static LinkedList<Character> alphabet;

    private static void init() {
        alphabet = new LinkedList<>();
        for (char i = 0; i < R; i++) {
            alphabet.add(i);
        }
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        init();
        String str = BinaryStdIn.readString();
        for (char c : str.toCharArray()) {
            int index = alphabet.indexOf(c);
            BinaryStdOut.write(index, 8);
            alphabet.remove(index);
            alphabet.add(0, c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        init();
        String str = BinaryStdIn.readString();
        char[] input = str.toCharArray();
        for (int i = 0; i < input.length; i++) {
            BinaryStdOut.write(alphabet.get(input[i]));
            char x = alphabet.remove(input[i]);
            alphabet.add(0, x);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
