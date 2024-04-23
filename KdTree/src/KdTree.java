import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private BSTNode root;
    private int size;

    private static class BSTNode {
        Point2D point;
        BSTNode left;
        BSTNode right;
        RectHV rect;
        boolean isXCor;

        public BSTNode(Point2D point, RectHV rect, boolean isXCor) {
            this.point = point;
            this.left = null;
            this.right = null;
            this.rect = rect;
            this.isXCor = isXCor;
        }

        private RectHV leftRect() {
            assert isXCor;
            return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
        }

        private RectHV rightRect() {
            assert isXCor;
            return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        }

        private RectHV topRect() {
            assert !isXCor;
            return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
        }

        private RectHV bottomRect() {
            assert !isXCor;
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
        }

        public RectHV smallerRect() {
            if (isXCor) {
                return leftRect();
            } else {
                return bottomRect();
            }
        }

        public RectHV largerRect() {
            if (isXCor) {
                return rightRect();
            } else {
                return topRect();
            }
        }
    }

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private static void checkNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    public void insert(Point2D p) {
        checkNull(p);
        root = insertHelper(p, root, null, true);
    }

    private BSTNode insertHelper(Point2D point, BSTNode node, BSTNode parent, boolean isSmaller) {
        // base case
        if (node == null) {
            size += 1;
            if (parent == null) {
                RectHV original = new RectHV(0, 0, 1, 1);
                return new BSTNode(point, original, true);
            } else if (isSmaller) {
                return new BSTNode(point, parent.smallerRect(), !parent.isXCor);
            } else {
                return new BSTNode(point, parent.largerRect(), !parent.isXCor);
            }
        }

        // no duplicate points
        if (point.equals(node.point)) {
            return node;
        }

        // compare root with point
        int cmp;
        if (node.isXCor) {
            cmp = Double.compare(point.x(), node.point.x());
        } else {
            cmp = Double.compare(point.y(), node.point.y());
        }

        if (cmp < 0) {
            node.left = insertHelper(point, node.left, node, true);
        } else {
            node.right = insertHelper(point, node.right, node, false);
        }
        return node;
    }

    public boolean contains(Point2D p) {
        checkNull(p);
        return containsHelper(root, p);
    }

    private boolean containsHelper(BSTNode node, Point2D point) {
        if (node == null) {
            return false;
        }
        if (node.point.equals(point)) {
            return true;
        }

        int cmp;
        if (node.isXCor) {
            cmp = Double.compare(point.x(), node.point.x());
        } else {
            cmp = Double.compare(point.y(), node.point.y());
        }

        if (cmp < 0) {
            return containsHelper(node.left, point);
        } else {
            return containsHelper(node.right, point);
        }
    }

    public void draw() {
        drawHelper(root);
    }

    private void drawHelper(BSTNode node) {
        if (node == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.point.x(), node.point.y());

        if (node.isXCor) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }

        drawHelper(node.left);
        drawHelper(node.right);
    }

    /**
     * To find all points contained in a given query rectangle, start at the root
     * and recursively search for points in both subtrees using the following pruning rule:
     * if the query rectangle does not intersect the rectangle corresponding to a node,
     * there is no need to explore that node (or its subtrees).
     * A subtree is searched only if it might contain a point contained in the query rectangle.
     */
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        List<Point2D> result = new ArrayList<>();
        searchRange(root, rect, result);
        return result;
    }

    private void searchRange(BSTNode node, RectHV rect, List<Point2D> points) {
        if (node == null || !rect.intersects(node.rect)) {
            return;
        }
        if (rect.contains(node.point)) {
            points.add(node.point);
        }
        if (node.left != null && rect.intersects(node.left.rect)) {
            searchRange(node.left, rect, points);
        }
        if (node.right != null && rect.intersects(node.right.rect)) {
            searchRange(node.right, rect, points);
        }
    }

    public Point2D nearest(Point2D p) {
        checkNull(p);
        return nearestHelper(root, p, null, Double.POSITIVE_INFINITY);
    }

    private Point2D nearestHelper(BSTNode node, Point2D query, Point2D nearest, double distSoFar) {
        //  if the closest point discovered so far is closer than the distance between the query point
        //  and the rectangle corresponding to a node, then there is no need to explore that node
        if (node == null || distSoFar < node.rect.distanceSquaredTo(query)) {
            return nearest;
        }

        double distToRoot = query.distanceSquaredTo(node.point);
        if (distToRoot < distSoFar) {
            nearest = node.point;
            distSoFar = distToRoot;
        }

        BSTNode first, second;
        double xDiff = query.x() - node.point.x();
        double yDiff = query.y() - node.point.y();
        if ((node.isXCor && xDiff < 0) || (!node.isXCor && yDiff < 0)) {
            first = node.left;
            second = node.right;
        } else {
            first = node.right;
            second = node.left;
        }
        nearest = nearestHelper(first, query, nearest, distSoFar);
        nearest = nearestHelper(second, query, nearest, query.distanceSquaredTo(nearest));
        return nearest;
    }

    public static void main(String[] args) {
        KdTree kd = new KdTree();
        kd.insert(new Point2D(0.0, 0.75));
        kd.insert(new Point2D(0.25, 0.5));
        kd.insert(new Point2D(0.875, 0.875));
        kd.insert(new Point2D(0.125, 0.625));
        kd.insert(new Point2D(1.0, 0.375));
        Point2D query = new Point2D(0.75, 1.0);
        Point2D nearest = kd.nearest(query);
        System.out.println("Nearest point: " + nearest);
        System.out.println("Nearest distance: " + nearest.distanceSquaredTo(query));
    }
}
