package com.github.javachaos.chaosdungeons.geometry;

import java.awt.geom.Point2D;
import java.util.List;

// Helper classes for Triangle and Edge
public class Triangle {
    Point2D a, b, c;

    boolean badTriangle;

    public Triangle(Point2D a, Point2D b, Point2D c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean contains(Point2D point) {
        Point2D A = a;
        Point2D B = b;
        Point2D C = c;
        Point2D D = point;

        double delta = A.getX() - D.getX();
        double gamma = A.getY() - D.getY();
        double epsilon = B.getX() - D.getX();
        double iota = B.getY() - D.getY();
        double phi = C.getX() - D.getX();
        double theta = C.getY() - D.getY();
        double[][] temp = {
                {A.getX() - D.getX(), A.getY() - D.getY(), delta * delta + gamma * gamma},
                {B.getX() - D.getX(), B.getY() - D.getY(), epsilon * epsilon + iota * iota},
                {C.getX() - D.getX(), C.getY() - D.getY(), phi * phi + theta * theta}
        };

        return Matrix3x3Det.compute(temp) > 1e-6;
    }

    public boolean contains(Edge e) {
        return e.equals(new Edge(a, b)) || e.equals(new Edge(b, c)) || e.equals(new Edge(a, c));
    }

    public boolean containsVertex(Point2D vertex) {
        return a.equals(vertex) || b.equals(vertex) || c.equals(vertex);
    }

    public List<Point2D> getPoints() {
        return List.of(a, b, c);
    }

    public List<Edge> getEdges() {
        return List.of(new Edge(a, b), new Edge(b, c), new Edge(a, c));
    }

}
