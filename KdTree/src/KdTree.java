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

    private static void checkNull(Object p) throws IllegalArgumentException {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    public void insert(Point2D p) {
        checkNull(p);
        root = insertHelper(p, root, null,true);
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
            cmp = Double.compare(node.point.x(), point.x());
        } else {
            cmp = Double.compare(node.point.y(), point.y());
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

    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }

    public Point2D nearest(Point2D p) {
        return null;
    }
}
