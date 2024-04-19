import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        checkPoints(points);
        int n = points.length;
        if (n < 4) {
            return;
        }

        Point[] sortedPoints = new Point[n];
        System.arraycopy(points, 0, sortedPoints, 0, n);
        Arrays.sort(sortedPoints);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int m = k + 1; m < n; m++) {
                        double s1 = sortedPoints[i].slopeTo(sortedPoints[j]);
                        double s2 = sortedPoints[i].slopeTo(sortedPoints[k]);
                        double s3 = sortedPoints[i].slopeTo(sortedPoints[m]);
                        if (s1 == s2 && s2 == s3) {
                            LineSegment line = new LineSegment(sortedPoints[i], sortedPoints[m]);
                            segments.add(line);
                        }
                    }
                }
            }
        }
    }

    private static void checkPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        System.out.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
