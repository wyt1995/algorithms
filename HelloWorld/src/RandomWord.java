import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int index = 0;
        String champion = "";
        while (!StdIn.isEmpty()) {
            String next = StdIn.readString();
            boolean choose = StdRandom.bernoulli(1 / (index + 1.0));
            if (choose) {
                champion = next;
            }
            index += 1;
        }
        System.out.println(champion);
    }
}
