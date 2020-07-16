/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

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
            cmp = Double.compare(node.p.x(), p.x());
        } else {
            cmp = Double.compare(node.p.y(), p.y());
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
            cmp = Double.compare(node.p.x(), p.x());
        } else {
            cmp = Double.compare(node.p.y(), p.y());
        }

        if (cmp <= 0) {
            return contains(node.left, p);
        } else {
            return contains(node.right, p);
        }
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        return new ArrayList<>();
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return null;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        KdTree kd = new KdTree();
        // kd.insert(new Point2D(1, 1));
        // kd.insert(new Point2D(1, 2));

        kd.insert(new Point2D(1, 2));
        kd.insert(new Point2D(1, 2));
        kd.insert(new Point2D(3, 4));
        kd.insert(new Point2D(3, 4));
        kd.insert(new Point2D(5, 4));
        kd.insert(new Point2D(5, 6));
        kd.insert(new Point2D(10, 10));
        kd.insert(new Point2D(-2, -3));
        kd.insert(new Point2D(1, 2));

        System.out.println(kd.size());
        System.out.println(kd.contains(new Point2D(1, 2)));
        System.out.println(kd.contains(new Point2D(3, 4)));
        System.out.println(kd.contains(new Point2D(5, 4)));
        System.out.println(kd.contains(new Point2D(-2, -4)));
        System.out.println(kd.contains(new Point2D(-2, -3)));
        System.out.println(kd.contains(new Point2D(10, 11)));
        System.out.println(kd.contains(new Point2D(10, 10)));

    }
    
}
