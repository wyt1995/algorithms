import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private final List<Set<String>> synsets;
    private final List<String> symbols;
    private final Map<String, List<Integer>> symbolMap;
    private final Digraph wordNet;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernymsFile) {
        if (synsetsFile == null || hypernymsFile == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }
        synsets = new ArrayList<Set<String>>();
        symbols = new ArrayList<>();
        symbolMap = new HashMap<String, List<Integer>>();
        readSynsets(synsetsFile);

        wordNet = new Digraph(synsets.size());
        readHypernyms(hypernymsFile);
        containsCycle();
        checkRoot();

        sap = new SAP(wordNet);
    }

    private void readSynsets(String synsetsFile) {
        In infile = new In(synsetsFile);
        while (!infile.isEmpty()) {
            String[] line = infile.readLine().split(",");
            int synsetId = Integer.parseInt(line[0]);
            symbols.add(line[1]);
            Set<String> nouns = new HashSet<String>();

            for (String word : line[1].split(" ")) {
                if (!symbolMap.containsKey(word)) {
                    symbolMap.put(word, new ArrayList<>());
                }
                symbolMap.get(word).add(synsetId);
                nouns.add(word);
            }
            synsets.add(nouns);
        }
    }

    private void readHypernyms(String hypernymsFile) {
        In infile = new In(hypernymsFile);
        while (!infile.isEmpty()) {
            String[] line = infile.readLine().split(",");
            int u = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int v = Integer.parseInt(line[i]);
                wordNet.addEdge(u, v);
            }
        }
    }

    private void containsCycle() {
        DirectedCycle dc = new DirectedCycle(wordNet);
        if (dc.hasCycle()) {
            throw new IllegalArgumentException("Word net has cycle");
        }
    }

    private void checkRoot() {
        int roots = 0;
        for (int i = 0; i < wordNet.V(); i++) {
            if (wordNet.outdegree(i) == 0) {
                roots++;
            }
        }
        if (roots != 1) {
            throw new IllegalArgumentException("Not a rooted DAG");
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return symbolMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Word cannot be null");
        }
        return symbolMap.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Nouns cannot be null");
        }
        return sap.length(symbolMap.get(nounA), symbolMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor
    // of nounA and nounB in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Nouns cannot be null");
        }
        return symbols.get(sap.ancestor(symbolMap.get(nounA), symbolMap.get(nounB)));
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println(wordNet.distance("cat", "horse"));
        System.out.println(wordNet.sap("cat", "horse"));
        System.out.println(wordNet.distance("apple", "art"));
        System.out.println(wordNet.sap("apple", "art"));
    }
}
