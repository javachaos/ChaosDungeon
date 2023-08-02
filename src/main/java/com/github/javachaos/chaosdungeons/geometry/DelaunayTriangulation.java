package com.github.javachaos.chaosdungeons.geometry;

import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;
import com.github.javachaos.chaosdungeons.geometry.polygons.Triangle;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple helper class for constructing delaunay triangulations.
 */
public class DelaunayTriangulation {

  /**
   * Bowyer–Watson algorithm.
   *
   * @param points points representing a polygon, orientation is assumed clockwise
   * @return a new list of triangles representing a delaunay triangulation
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
          triangle.setBad(true);
          edges.add(new Edge(triangle.getA(), triangle.getB()));
          edges.add(new Edge(triangle.getB(), triangle.getC()));
          edges.add(new Edge(triangle.getC(), triangle.getA()));
        }
      }
      triangles.removeIf(Triangle::isBad);
      for (int i = 0; i < edges.size(); i++) {
        for (int j = i + 1; j < edges.size(); j++) {
          Edge e1 = edges.get(i);
          Edge e2 = edges.get(j);
          if (e1.equals(e2)) {
            e1.setBadEdge(true);
            e2.setBadEdge(true);
          }
        }
      }
      edges.removeIf(Edge::isBad);

      // Create new triangles from the edges and the current point
      for (Edge edge : edges) {
        triangles.add(new Triangle(edge.getA(), edge.getB(), point));
      }
    }
    // Remove triangles that contain vertices of the super triangle
    triangles.removeIf(triangle -> triangle.containsVertex(p1) || triangle.containsVertex(p2)
        || triangle.containsVertex(p3));

    return triangles;
  }

  /**
   * Create a constrained delauney triangulation.
   *
   * @param triangulation the list of triangles to constrain.
   * @param polygon the polygon of edge constraints
   * @return the list of edges for the final triangulation
   */
  public static List<Edge> constrainDelaunayTriangulation(List<Triangle> triangulation,
                                                          Vertex polygon) {
    List<Edge> originalEdges = polygon.getEdges();
    List<Edge> badEdges = Edge.convexHull(polygon);
    badEdges.removeAll(originalEdges);
    List<Point2D> badPoints = new ArrayList<>();
    for (Edge be : badEdges) {
      for (Edge oe : badEdges) {
        if (be.getA() == oe.getA()) {
          badPoints.add(be.getA());
        }
        if (be.getB() == oe.getB()) {
          badPoints.add(be.getB());
        }
      }
    }
    polygon.removeAll(badPoints);

    //TODO figure this shit out more...
    // 1. We are trying to remove hull points create
    // the convex hull with the new hull and then repeat this function to remove
    // bad edges, the problem seems recursive in nature, but maybe it can be done
    // without recursion.
    // 2. We check if any two edges from the set difference between the original edge list
    // and the convex hull share the same points, these are the points we must remove and
    // recompute
    // the new convex hull.
    // 3. Once we have these new edges, compute the set different between the original edges
    // and this new hull
    // list. We do this until the set difference between the hull and the original list is
    // empty.
    // seems like it should work, need to implement and test.

    Set<Edge> constrainedTriangulation = new LinkedHashSet<>(originalEdges);

    for (Triangle triangle : triangulation) {
      constrainedTriangulation.addAll(triangle.getEdges());
    }
    for (Edge e : constrainedTriangulation) {
      for (Edge o : originalEdges) {
        if (e.intersects(o)) {
          e.setBadEdge(true);
        }
      }
    }

    badEdges.forEach(constrainedTriangulation::remove);
    constrainedTriangulation.removeIf(Edge::isBad);
    return new ArrayList<>(constrainedTriangulation);
  }

}
