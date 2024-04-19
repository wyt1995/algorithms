import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();
    private final int len;

    public FastCollinearPoints(Point[] points) {
        checkPoints(points);
        len = points.length;
        if (len < 4) {
            return;
        }

        Point[] sortedPoints = new Point[len];
        System.arraycopy(points, 0, sortedPoints, 0, len);
        Arrays.sort(sortedPoints);

        for (int i = 0; i < len; i++) {
            if (i > len - 4) {
                return;
            }
            Point origin = sortedPoints[i];
            Point[] slopeOrder = new Point[len];
            System.arraycopy(sortedPoints, 0, slopeOrder, 0, len);
            Arrays.sort(slopeOrder, origin.slopeOrder());
            findCollinearPoints(origin, slopeOrder);
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

    private void findCollinearPoints(Point origin, Point[] slopeOrder) {
        int count = 0;
        double prevSlope = origin.slopeTo(slopeOrder[0]);

        for (int i = 1; i < len; i++) {
            double currSlope = origin.slopeTo(slopeOrder[i]);
            if (prevSlope == currSlope) {
                count += 1;
            } else {
                if (isCollinear(slopeOrder, i, count)) {
                    segments.add(new LineSegment(origin, slopeOrder[i - 1]));
                }
                prevSlope = currSlope;
                count = 1;
            }
        }

        if (isCollinear(slopeOrder, len, count)) {
            segments.add(new LineSegment(origin, slopeOrder[len - 1]));
        }
    }

    private boolean isCollinear(Point[] points, int index, int count) {
        if (count < 3) {
            return false;
        }
        Point origin = points[0];
        Point start = points[index - count];
        return origin.compareTo(start) < 0;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
