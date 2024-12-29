import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private final String str;
    private final int len;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        str = s;
        len = s.length();
        index = new int[len];
        for (int i = 0; i < len; i++) {
            index[i] = i;
        }
        sortCircularSuffixArray();
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= len) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return index[i];
    }

    private void sortCircularSuffixArray() {
        Comparator<Integer> comparator = (a, b) -> {
            for (int i = 0; i < len; i++) {
                char c1 = str.charAt((a + i) % len);
                char c2 = str.charAt((b + i) % len);
                if (c1 < c2) return -1;
                if (c1 > c2) return 1;
            }
            return 0;
        };

        Integer[] indexArray = new Integer[len];
        for (int i = 0; i < len; i++) {
            indexArray[i] = i;
        }
        Arrays.sort(indexArray, comparator);
        for (int i = 0; i < len; i++) {
            index[i] = indexArray[i];
        }
    }

    public static void main(String[] args) {
        CircularSuffixArray suffixArray = new CircularSuffixArray("ABRACADABRA!");
        System.out.println("string length: " + suffixArray.length());
        for (int i = 0; i < suffixArray.length(); i++) {
            System.out.print(suffixArray.index(i) + " ");
        }
        System.out.println();
    }
}
