/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    private class Node {
        private Point2D p;
        private boolean splitByX;
        private Node left;
        private Node right;
        private int count;

        private Node(Point2D p, boolean splitByX, int count) {
            this.p = p;
            this.splitByX = splitByX;
            this.count = count;
        }
    }

    private Node root;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) return 0;
        return node.count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        root = insert(root, true, p);
    }

    private Node insert(Node node, boolean splitByX, Point2D p) {
        if (node == null) {
            return new Node(p, splitByX, 1);
        }

        if (node.p.equals(p)) {
            node.p = p;
            return node;
        }

        int cmp = 0;
        if (node.splitByX) {
            cmp = Double.compare(p.x(), node.p.x());
        } else {
            cmp = Double.compare(p.y(), node.p.y());
        }

        if (cmp <= 0) {
            node.left = insert(node.left, !node.splitByX, p);
        } else {
            node.right = insert(node.right, !node.splitByX, p);
        }

        node.count = 1 + size(node.left) + size(node.right);

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) {
            return false;
        }

        if (node.p.equals(p)) {
            return true;
        }

        int cmp = 0;
        if (node.splitByX) {
            cmp = Double.compare(p.x(), node.p.x());
        } else {
            cmp = Double.compare(p.y(), node.p.y());
        }

        if (cmp <= 0) {
            return contains(node.left, p);
        } else {
            return contains(node.right, p);
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, RectHV parentRect) {
        if (node == null) {
            return;
        }

        RectHV leftTreeRect, rightTreeRect;
        if (node.splitByX) {
            leftTreeRect = new RectHV(parentRect.xmin(), parentRect.ymin(), node.p.x(), parentRect.ymax());
            rightTreeRect = new RectHV(node.p.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
        } else {
            leftTreeRect = new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), node.p.y());
            rightTreeRect = new RectHV(parentRect.xmin(), node.p.y(), parentRect.xmax(), parentRect.ymax());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();

        StdDraw.setPenRadius(0.005);
        if (node.splitByX) {
            StdDraw.setPenColor(StdDraw.BOOK_RED);
        } else {
            StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        }

        Point2D rightMin = new Point2D(rightTreeRect.xmin(), rightTreeRect.ymin());
        Point2D leftMax = new Point2D(leftTreeRect.xmax(), leftTreeRect.ymax());
        rightMin.drawTo(leftMax);

        draw(node.left, leftTreeRect);
        draw(node.right, rightTreeRect);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        return range(root, new RectHV(0, 0, 1, 1), rect);
    }

    private Iterable<Point2D> range(Node node, RectHV parentRect, RectHV rect) {
        ArrayList<Point2D> result = new ArrayList<>();

        if (node == null) {
            return result;
        }

        if (rect.contains(node.p)) {
            result.add(node.p);
        }

        RectHV leftTreeRect, rightTreeRect;
        if (node.splitByX) {
            leftTreeRect = new RectHV(parentRect.xmin(), parentRect.ymin(), node.p.x(), parentRect.ymax());
            rightTreeRect = new RectHV(node.p.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
        } else {
            leftTreeRect = new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), node.p.y());
            rightTreeRect = new RectHV(parentRect.xmin(), node.p.y(), parentRect.xmax(), parentRect.ymax());
        }

        if (leftTreeRect.intersects(rect)) {
            Iterable<Point2D> leftResult = range(node.left, leftTreeRect, rect);
            for (Point2D p : leftResult) {
                result.add(p);
            }
        }
        if (rightTreeRect.intersects(rect)) {
            Iterable<Point2D> rightResult = range(node.right, rightTreeRect, rect);
            for (Point2D p : rightResult) {
                result.add(p);
            }
        }

        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return nearest(root, new RectHV(0, 0, 1, 1), p, null);
    }

    private Point2D nearest(Node node, RectHV parentRect, Point2D p, Point2D min) {
        if (node == null) {
            return min;
        }

        RectHV leftTreeRect, rightTreeRect;
        if (node.splitByX) {
            leftTreeRect = new RectHV(parentRect.xmin(), parentRect.ymin(), node.p.x(), parentRect.ymax());
            rightTreeRect = new RectHV(node.p.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
        } else {
            leftTreeRect = new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), node.p.y());
            rightTreeRect = new RectHV(parentRect.xmin(), node.p.y(), parentRect.xmax(), parentRect.ymax());
        }

        Point2D newMin;
        if (min == null) {
            newMin = node.p;
        } else {
            newMin = node.p.distanceSquaredTo(p) < min.distanceSquaredTo(p) ? node.p : min;
        }

        boolean leftCheckFirst = leftTreeRect.distanceSquaredTo(p) < rightTreeRect.distanceSquaredTo(p);
        Node nodeToCheck1 = leftCheckFirst ? node.left : node.right;
        Node nodeToCheck2 = !leftCheckFirst ? node.left : node.right;

        RectHV rectToCheck1 = leftCheckFirst ? leftTreeRect : rightTreeRect;
        RectHV rectToCheck2 = !leftCheckFirst ? leftTreeRect : rightTreeRect;

        Point2D nearestFirst = nearest(nodeToCheck1, rectToCheck1, p, newMin);
        if (nearestFirst.distanceSquaredTo(p) < rectToCheck2.distanceSquaredTo(p)) {
            return nearestFirst;
        } else {
            return nearest(nodeToCheck2, rectToCheck2, p, nearestFirst);
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        KdTree kd = new KdTree();

        kd.insert(new Point2D(0.25, 0.25));
        kd.insert(new Point2D(0.20, 0.70));
        kd.insert(new Point2D(0.80, 0.30));
        kd.insert(new Point2D(0.85, 0.85));

        System.out.println(kd.range(new RectHV(0.1, 0.1, 0.9, 0.9)));
        System.out.println(kd.range(new RectHV(0.1, 0.1, 0.5, 0.9)));

    }
    
}
