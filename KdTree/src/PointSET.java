import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private final Set<Point2D> points;

    public PointSET() {
        points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        checkNull(p);
        points.add(p);
    }

    public boolean contains(Point2D p) {
        checkNull(p);
        return points.contains(p);
    }

    private static void checkNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    public void draw() {
        for (Point2D p : points) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        List<Point2D> result = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                result.add(p);
            }
        }
        return result;
    }

    public Point2D nearest(Point2D p) {
        checkNull(p);
        Point2D nearest = null;
        double minDist = Double.POSITIVE_INFINITY;
        for (Point2D point : points) {
            if (point.distanceSquaredTo(p) < minDist) {
                minDist = point.distanceSquaredTo(p);
                nearest = point;
            }
        }
        return nearest;
    }
}
