package com.github.javachaos.chaosdungeons.geometry;

import java.awt.geom.Point2D;
import java.util.*;

public class DelaunayTriangulation {

    /**
     * Bowyer–Watson algorithm
     *
     * @param points
     * @return
     */
    public static List<Triangle> delaunayTriangulation(List<Point2D> points) {

        // Create a super triangle that covers all the points
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        for (Point2D point : points) {
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
        }

        double delta = Math.max(maxX - minX, maxY - minY) * 10.0;
        Point2D p1 = new Point2D.Double(minX - delta, minY - delta);
        Point2D p2 = new Point2D.Double(maxX + delta, minY - delta);
        Point2D p3 = new Point2D.Double(minX + (maxX - minX) / 2.0, maxY + delta);

        Triangle superTriangle = new Triangle(p1, p2, p3);
        List<Triangle> triangles = new ArrayList<>();
        triangles.add(superTriangle);

        // Incrementally add each point to the triangulation
        for (Point2D point : points) {
            List<Edge> edges = new ArrayList<>();

            // Find all edges affected by adding the current point
            for (Triangle triangle : triangles) {
                if (triangle.contains(point)) {
                    triangle.badTriangle = true;
                    edges.add(new Edge(triangle.a, triangle.b));
                    edges.add(new Edge(triangle.b, triangle.c));
                    edges.add(new Edge(triangle.c, triangle.a));
                }
            }
            triangles.removeIf(t -> t.badTriangle);
            for (int i = 0; i < edges.size(); i++) {
                for (int j = i + 1; j < edges.size(); j++) {
                    Edge e1 = edges.get(i);
                    Edge e2 = edges.get(j);
                    if (e1.equals(e2)) {
                        e1.badEdge = true;
                        e2.badEdge = true;
                    }
                }
            }
            edges.removeIf(e -> e.badEdge);

            // Create new triangles from the edges and the current point
            for (Edge edge : edges) {
                triangles.add(new Triangle(edge.a, edge.b, point));
            }
        }
        // Remove triangles that contain vertices of the super triangle
        triangles.removeIf(triangle -> triangle.containsVertex(p1) || triangle.containsVertex(p2)
                || triangle.containsVertex(p3));

        return triangles;
    }

    public static List<Edge> constrainDelaunayTriangulation(List<Triangle> triangulation, Vertex polygon) {
        List<Edge> originalEdges = polygon.getEdges();
        List<Edge> badEdges = Edge.convexHull(polygon);
        badEdges.removeAll(originalEdges);
        List<Point2D> badPoints = new ArrayList<>();
        for (Edge be : badEdges) {
            for (Edge oe : badEdges) {
                if (be.a == oe.a) {
                    badPoints.add(be.a);
                }
                if (be.b == oe.b) {
                    badPoints.add(be.b);
                }
            }
        }
        polygon.removeAll(badPoints);
//        if (badPoints.size() > 3) {
//            constrainDelaunayTriangulation(triangulation,
//                    polygon);
//
//            //TODO figure this shit out more...
//            // 1. We are trying to remove hull points create
//            // the convex hull with the new hull and then repeat this function to remove
//            // bad edges, the problem seems recursive in nature, but maybe it can be done
//            // without recursion.
//            // 2. We check if any two edges from the set difference between the original edge list
//            // and the convex hull share the same points, these are the points we must remove and recompute
//            // the new convex hull.
//            // 3. Once we have these new edges, compute the set different between the original edges and this new hull
//            // list. We do this until the set difference between the hull and the original list is empty.
//            // seems like it should work, need to implement and test.
//        }
        Set<Edge> constrainedTriangulation = new LinkedHashSet<>(originalEdges);

        for (Triangle triangle : triangulation) {
            constrainedTriangulation.addAll(triangle.getEdges());
        }
        for (Edge e : constrainedTriangulation) {
            for (Edge o : originalEdges) {
                if (e.intersects(o)) {
                    e.badEdge = true;
                }
            }
        }

        badEdges.forEach(constrainedTriangulation::remove);
        constrainedTriangulation.removeIf(e -> e.badEdge);
        return new ArrayList<>(constrainedTriangulation);
    }

}
