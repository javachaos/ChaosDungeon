package com.github.javachaos.chaosdungeons.geometry;

import java.awt.geom.Point2D;
import java.util.*;

public class Edge {
    Point2D a, b;
    boolean badEdge;

    Edge(Point2D a, Point2D b) {
        this.a = a;
        this.b = b;
    }

    public static boolean checkIntersection(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        double orientation1 = (p2.getY() - p1.getY()) * (p3.getX() - p2.getX()) - (p2.getX() - p1.getX()) * (p3.getY() - p2.getY());
        double orientation2 = (p2.getY() - p1.getY()) * (p4.getX() - p2.getX()) - (p2.getX() - p1.getX()) * (p4.getY() - p2.getY());
        double orientation3 = (p4.getY() - p3.getY()) * (p1.getX() - p4.getX()) - (p4.getX() - p3.getX()) * (p1.getY() - p4.getY());
        double orientation4 = (p4.getY() - p3.getY()) * (p2.getX() - p4.getX()) - (p4.getX() - p3.getX()) * (p2.getY() - p4.getY());

        return (orientation1 * orientation2 < 0) && (orientation3 * orientation4 < 0);
    }

    public static boolean checkIntersection(Edge a, Edge b) {
        return checkIntersection(a.a, a.b, b.a, b.b);
    }

    public boolean intersects(Edge e) {
        return checkIntersection(this, e);
//        double x1 = a.getX();
//        double y1 = a.getY();
//        double x2 = b.getX();
//        double y2 = b.getY();
//        double x3 = e.a.getX();
//        double y3 = e.a.getY();
//        double x4 = e.b.getX();
//        double y4 = e.b.getY();
//        double t = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4) / (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
//        double u = (x1 - x3) * (y1 - y2) - (y1 - y3) * (x1 - x2) / (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
//        return (t >= 0.0 && t <= 1.0) && (u >= 0.0 && u <= 1.0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    public static boolean epsEquals(double d1, double d2) {
        final double EPSILON = 1e-6;
        return Math.abs(d1 - d2) < EPSILON;
    }

    public static boolean epsPtEquals(Point2D p1, Point2D p2) {
        return epsEquals(p1.getX(), p2.getX()) && epsEquals(p1.getY(), p2.getY());
    }

    // Orientation of three points (p, q, r)
    private static int orientation(Point2D p, Point2D q, Point2D r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0;  // Collinear
        return (val > 0) ? -1 : 1; // Clockwise or Counterclockwise
    }

    /**
     * Function to find the convex hull using Graham Scan algorithm O(n log n)
     * @param points point cloud
     * @return points of the convex hull
     */
    public static List<Point2D> convexHullPoints(List<Point2D> points) {
        int n = points.size();
        if (n <= 3) return points; // Convex hull is the same as the original points

        // Find the point with the lowest y-coordinate (and leftmost if tie)
        Point2D lowestPoint = points.get(0);
        for (int i = 1; i < n; i++) {
            Point2D current = points.get(i);
            if (current.getY() < lowestPoint.getY() ||
                    (current.getY() == lowestPoint.getY() && current.getX() < lowestPoint.getX())) {
                lowestPoint = current;
            }
        }

        // Sort points by polar angle with respect to the lowest point
        Point2D finalLowestPoint = lowestPoint;
        points.sort(Comparator.comparingDouble(p -> {
            double angle = Math.atan2(p.getY() - finalLowestPoint.getY(), p.getX() - finalLowestPoint.getX());
            return angle < 0 ? angle + 2 * Math.PI : angle;
        }));

        // Build the convex hull using a stack
        Stack<Point2D> stack = new Stack<>();
        stack.push(points.get(0));
        stack.push(points.get(1));

        for (int i = 2; i < n; i++) {
            while (!stack.isEmpty() &&
                    orientation(
                            stack.get(stack.size() - 2),
                            stack.peek(),
                            points.get(i)) <= 0) {
                stack.pop();
            }
            stack.push(points.get(i));
        }

        // Convert stack to list and return the convex hull
        return new ArrayList<>(stack);
    }

    public static List<Edge> convexHull(Vertex polygon) {
        return convexHull(polygon.getPoints());
    }

    // Function to find the convex hull using Graham Scan algorithm
    public static List<Edge> convexHull(List<Point2D> points) {
        // Find the point with the lowest y-coordinate (and leftmost if tie)
        Point2D lowestPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point2D current = points.get(i);
            if (current.getY() < lowestPoint.getY() ||
                    (current.getY() == lowestPoint.getY() && current.getX() < lowestPoint.getX())) {
                lowestPoint = current;
            }
        }
        Stack<Point2D> stack = new Stack<>();
        stack.addAll(convexHullPoints(points));

        // Convert stack to list of edges and return the convex hull
        List<Edge> convexHullEdges = new ArrayList<>();
        Point2D lastPoint = stack.pop();
        while (!stack.isEmpty()) {
            Point2D currentPoint = stack.pop();
            convexHullEdges.add(new Edge(currentPoint, lastPoint));
            lastPoint = currentPoint;
        }
        convexHullEdges.add(new Edge(lowestPoint, lastPoint)); // Add the edge to the lowest point
        return convexHullEdges;
    }

    // Helper function to create edges from a list of points
    private static List<Edge> createEdges(List<Point2D> points) {
        List<Edge> edges = new ArrayList<>();
        int n = points.size();
        for (int i = 0; i < n - 1; i++) {
            edges.add(new Edge(points.get(i), points.get(i + 1)));
        }
        edges.add(new Edge(points.get(n - 1), points.get(0))); // Closing edge
        return edges;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge other = (Edge) obj;
        return (epsPtEquals(a, other.a) && epsPtEquals(b, other.b)) || (epsPtEquals(a, other.b) && epsPtEquals(b, other.a));
    }

    @Override
    public String toString() {
        return "u[ " + a.getX() + ", " + a.getY() + "] v[" + b.getX() + ", " + b.getY() + "]";
    }

}