import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        int minLength = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                minLength = Math.min(minLength, bfs1.distTo(i) + bfs2.distTo(i));
            }
        }
        return minLength == Integer.MAX_VALUE ? -1 : minLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        int minLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                int pathLen = bfs1.distTo(i) + bfs2.distTo(i);
                if (pathLen < minLength) {
                    minLength = pathLen;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    private boolean checkIter(Iterable<Integer> v) {
        if (v == null) {
            throw new IllegalArgumentException();
        }
        return v.iterator().hasNext();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w;
    // -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (!checkIter(v) || !checkIter(w)) {
            return -1;
        }

        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        int minLength = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                minLength = Math.min(minLength, bfs1.distTo(i) + bfs2.distTo(i));
            }
        }
        return minLength == Integer.MAX_VALUE ? -1 : minLength;
    }

    // a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!checkIter(v) || !checkIter(w)) {
            return -1;
        }

        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        int minLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                int pathLen = bfs1.distTo(i) + bfs2.distTo(i);
                if (pathLen < minLength) {
                    minLength = pathLen;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
