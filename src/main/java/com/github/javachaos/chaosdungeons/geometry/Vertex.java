package com.github.javachaos.chaosdungeons.geometry;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private double x;
    private double y;
    private Vertex next;
    private Vertex previous;
    private boolean isAcute;
    private double angle;
    private int index;

    public Vertex() {
    }

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vertex(Point2D p) {
        this(p.getX(), p.getY());
    }

    public Vertex(List<Point2D> points) {
        if (points == null) {
            throw new NullPointerException("Points list was null.");
        }
        if (points.size() < 3) {
            throw new IllegalArgumentException("Not enough points.");
        }
        Point2D first = points.get(0);
        this.x = first.getX();
        this.y = first.getY();
        this.index = 0;
        next = null; // Initialize to null, to be set later
        previous = null; // Initialize to null, to be set later

        Vertex prevVertex = this;
        for (int i = 1; i < points.size(); i++) {
            Point2D point = points.get(i);
            Vertex vertex = new Vertex(point.getX(), point.getY());
            vertex.index = i;
            prevVertex.setNext(vertex);
            vertex.setPrevious(prevVertex);
            prevVertex = vertex;
        }

        // Complete the circular link
        this.setPrevious(prevVertex);
        prevVertex.setNext(this);
        calculateAngles(this);
    }

    public List<Edge> getEdges() {
        List<Edge> e = new ArrayList<>();
        Vertex current = this.next;
        while (current != this) {
            e.add(new Edge(current.getPoint(), current.next.getPoint()));
            current = current.next;
        }
        e.add(new Edge(current.getPoint(), current.next.getPoint()));
        return e;
    }

    public boolean isAcute() {
        return isAcute;
    }

    public double getAngle() {
        return angle;
    }

    public void setNext(Vertex next) {
        this.next = next;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
    }

    public Point2D getPoint() {
        return new Point2D.Double(x, y);
    }

    // Function to calculate the angle between three vertices
    public double calculateAngle() {
        if (this.next == null || this.previous == null) {
            throw new IllegalArgumentException("Invalid vertex or insufficient vertices in the polygon.");
        }
        Vertex c = this.next;
        Vertex a = this.previous;

        double x1 = this.x - a.x;
        double y1 = this.y - a.y;
        double x2 = this.x - c.x;
        double y2 = this.y - c.y;

        Point2D v1 = new Point2D.Double(x1, y1);//unit length
        Point2D v2 = new Point2D.Double(x2, y2);//unit length

        double dot = dotProduct(v1, v2);

        if (epsEquals(dot, 1.0)) {//orthonormal
            return 90.0;
        }

        double magnitude1 = v1.distance(0,0);
        double magnitude2 = v2.distance(0,0);

        double angle = 0.0;

        double acos = Math.acos(dot / (magnitude1 * magnitude2));
        int con = concavity();
        if (con == 1) {
            angle = acos + (Math.PI);
        }
        if (con == 0) {
            return 0.0;
        }
        if (con == -1) {
            angle = acos;
        }
        //System.out.println(this + " Angle: " + Math.toDegrees(angle));
        // Convert angle from radians to degrees
        return Math.toDegrees(angle);
    }

    /**
     * Get the concavity of the vertex b
     * @return 0 if the point is co-linear with its neighbours
     *         1 if the point is concave
     *         -1 if the point is convex
     */
    public int concavity() {
        Vertex b = this;
        Vertex a = this.previous;
        Vertex c = this.next;
        double o = calculateDeterminant(b);
        double det = calculateDeterminant(a, b, c);
        if (epsEquals(det, 0.0)) {
            return 0;
        }
        if ((det < 0.0 && o < 0.0) || (det > 0.0 && o > 0.0)) {
            return -1;
        }
        if (!sameSign(det, o)) {
            return 1;
        }
        return 0;
    }

    /**
     * Fast determinant of 3 points.
     * @param a point 1 (prev)
     * @param b point 2 (current point)
     * @param c point 3 (next)
     * @return the determinant
     */
    public static double calculateDeterminant(Vertex a, Vertex b, Vertex c) {
        return ((b.x - a.x) * (c.y - a.y)) - ((c.x - a.x) * (b.y - a.y));
    }

    /**
     * Test if the polygon formed by this doubly linked list backed
     * vertex has the edge e.
     *
     * @param e the edge {@link com.github.javachaos.chaosdungeons.geometry.Edge}
     * @return true if this polygon contains the edge
     */
    public boolean has(Edge e) {
        Vertex tempStart = this.next;
        if (e.equals(new Edge(this.getPoint(), this.next.getPoint()))
                || e.equals(new Edge(this.next.getPoint(), this.getPoint()))) {
            return true;
        }
        while (tempStart != this) {
            if (e.equals(new Edge(tempStart.getPoint(), tempStart.next.getPoint()))
                    || e.equals(new Edge(tempStart.next.getPoint(), tempStart.getPoint()))) {
                return true;
            }
            tempStart = tempStart.next;
        }
        return false;
    }

    @Override
    public String toString() {
        return index + " :[" + x + ", " + y + "]";
    }

    public Vertex find(Point2D a) {
        Vertex current = this.next;
        while (current != this) {
            if (current.getPoint().equals(a)) {
                return current;
            }
            current = current.next;
        }
        if (current.getPoint().equals(a)) {
            return current;
        }
        return null;
    }

    public Point2D get(int i) {
        return getVertex(i).getPoint();
    }

    public Vertex getVertex(int i) {
        int j = 0;
        Vertex current = this.next;
        while (j++ < i) {
            current = current.next;
        }
        return current;
    }

    public void print() {
        if (this.next == null) {
            System.out.println(this);
        }
        Vertex current = this.next;
        do {
            System.out.println(current);
            current = current.next;
        } while (current != this.next);
    }

    public void add(Vertex newVertex) {
        if (this.next == null) {
            this.next = newVertex;
            this.next.next = this.next;
            this.next.previous = this.next;
        } else {
            Vertex last = this.next;
            while (last.next != this.next) {
                last = last.next;
            }
            last.next = newVertex;
            newVertex.next = this.next;
            newVertex.previous = last;
            this.next.previous = newVertex;
        }
    }

    public void add(Point2D point) {
        Vertex newVertex = new Vertex(point);
        add(newVertex);
    }

    public void remove(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Index " + i + " out of bounds.");
        }
        Vertex current = this.next;
        do {
            if (current.index != i) {
                if (this.previous != null) {
                    this.previous.next = current.next;
                    if (current == this.next) {
                        this.next = current.next;
                    }
                } else {
                    this.next = current.next;
                }
                break;
            }
            this.previous = current;
            current = current.next;
        } while (current != this.next);
        updateIndicies();
    }

    private void updateIndicies() {
        if (this.next == null) {
            return;
        }
        Vertex current = this.next;
        int i = 0;
        do {
            current.index = i++;
            current = current.next;
        } while (current != this.next);
    }

    public Vertex getNext() {
        return next;
    }

    public Vertex getPrevious() {
        return previous;
    }

    public List<Point2D> getPoints() {
        List<Point2D> pts = new ArrayList<>();
        Vertex current = this.next;
        pts.add(getPoint());
        while (current != this) {
            pts.add(current.getPoint());
            current = current.next;
        }
        return pts;
    }

    /**
     * TODO test this
     * @param point
     */
    public void remove(Point2D point) {
        Vertex current = this;
        while (current.next != this) {
            if (current.next.getPoint().equals(point)) {
                // Found the vertex to remove
                current.next = current.next.next; // Skip the node we wish to remove
                current.next.previous = current;
                return; // Exit the method, no more processing required
            }
            current.previous = current;
            current = current.next;
        }
        updateIndicies();
    }

    public void removeAll(List<Point2D> badPoints) {
        badPoints.forEach(this::remove);
    }

    public int size() {
        int count = 0;
        Vertex current = this;
        do {
            count++;
            current = current.next;
        } while (current != this);
        return count;
    }

    ///////////////////////////////////////// Helper functions ////////////////////////////////////////////

    private static void calculateAngles(Vertex vertices) {
        Vertex current = vertices.next;
        while (current != vertices) {
            current.angle = current.calculateAngle();
            current.isAcute = current.angle < 180.0;
            current = current.next;
        }
        current.angle = current.calculateAngle();
        current.isAcute = current.angle < 180.0;
    }

    private static boolean epsEquals(double d1, double d2) {
        final double EPSILON = 1e-9;
        return Math.abs(d1 - d2) < EPSILON;
    }

    private static double calculateDeterminant(Vertex vertices) {
        double determinant = 0.0;
        Vertex current = vertices.next;
        while (current != vertices) {
            determinant += current.x * current.next.y - current.next.x * current.y;
            current = current.next;
        }
        determinant += current.x * current.next.y - current.next.x * current.y;
        return determinant;
    }

    private static boolean sameSign(double num1, double num2) {
        if (num1 == 0 || num2 == 0) {
            return num1 == num2; // Zero is considered to have the same sign as zero
        } else {
            return (num1 > 0 && num2 > 0) || (num1 < 0 && num2 < 0);
        }
    }

    /**
     * Helper function to calculate the dot product of two 2D vectors.
     *
     */
    private static double dotProduct(Point2D a, Point2D b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

}
