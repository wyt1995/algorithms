/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    private boolean sameAs(Point that) {
        return this.x == that.x && this.y == that.y;
    }

    private boolean horizontal(Point that) {
        if (this.sameAs(that)) {
            return false;
        }
        return this.y == that.y;
    }

    private boolean vertical(Point that) {
        if (this.sameAs(that)) {
            return false;
        }
        return this.x == that.x;
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (this.sameAs(that)) {
            return Double.NEGATIVE_INFINITY;
        } else if (vertical(that)) {
            return Double.POSITIVE_INFINITY;
        } else if (horizontal(that)) {
            return 0;
        } else {
            return (double) (that.y - this.y) / (double) (that.x - this.x);
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.sameAs(that)) {
            return 0;
        } else if (horizontal(that)) {
            return this.x < that.x ? -1 : 1;
        } else {
            return this.y < that.y ? -1 : 1;
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new SlopeComparator();
    }

    private class SlopeComparator implements Comparator<Point> {
        @Override
        public int compare(Point p1, Point p2) {
            double slope1 = slopeTo(p1);
            double slope2 = slopeTo(p2);
            return Double.compare(slope1, slope2);
        }
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 0);
        Point p3 = new Point(0, 1);
        Point p4 = new Point(2, 3);
        Point p5 = new Point(1, 1);

        // slope to
        System.out.println(p1.slopeTo(p2));
        System.out.println(p1.slopeTo(p3));
        System.out.println(p1.slopeTo(p4));
        System.out.println(p1.slopeTo(p5));

        // comparable
        System.out.println(p1.compareTo(p5));  // 0
        System.out.println(p1.compareTo(p3));  // p1 > p3, positive
        System.out.println(p1.compareTo(p4));  // p1 < p4, negative

        // comparator
        Comparator<Point> slopeCompare = p1.slopeOrder();
        System.out.println(slopeCompare.compare(p2, p4));  // 1
        System.out.println(slopeCompare.compare(p3, p4));  // -1
    }
}
