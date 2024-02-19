package com.github.javachaos.chaosdungeons.geometry;

import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.geometry.math.Hull;
import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;
import com.github.javachaos.chaosdungeons.geometry.polygons.Triangle;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple helper class for constructing delaunay triangulations.
 */
@SuppressWarnings("all")
public class DelaunayTriangulation {

  /**
   * Bowyer–Watson algorithm.
   *
   * @param points points representing a polygon, orientation is assumed clockwise
   * @return a new list of triangles representing a delaunay triangulation
   */
  public static List<Triangle> delaunayTriangulation(Set<Polygon.Point> points) {

    MinMaxPoints result = getMinMaxPoints(points);
    Polygon.Point p1 = new Polygon.Point((float) (result.minX() - result.delta()), (float) (result.minY() - result.delta()));
    Polygon.Point p2 = new Polygon.Point((float) (result.maxX() + result.delta()), (float) (result.minY() - result.delta()));
    Polygon.Point p3 = new Polygon.Point((float) (result.minX() + (result.maxX() - result.minX()) / 2.0), (float) (result.maxY() + result.delta()));

    Triangle superTriangle = new Triangle(p1, p2, p3);
    List<Triangle> triangles = new ArrayList<>();
    triangles.add(superTriangle);

    // Incrementally add each point to the triangulation
    for (Polygon.Point point : points) {
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

  private static MinMaxPoints getMinMaxPoints(Set<Polygon.Point> points) {
    // Create a super triangle that covers all the points
    double maxX = Double.MIN_VALUE;
    double maxY = Double.MIN_VALUE;
    double minX = Double.MAX_VALUE;
    double minY = Double.MAX_VALUE;
    for (Polygon.Point point : points) {
      maxX = Math.max(maxX, point.x());
      maxY = Math.max(maxY, point.y());
      minX = Math.min(minX, point.x());
      minY = Math.min(minY, point.y());
    }

    double delta = Math.max(maxX - minX, maxY - minY) * 10.0;
    return new MinMaxPoints(maxX, maxY, minX, minY, delta);
  }

  private record MinMaxPoints(double maxX, double maxY, double minX, double minY, double delta) {
  }

  /**
   * Create a constrained delauney triangulation.
   *
   * @param triangulation the list of triangles to constrain.
   * @param polygon the polygon of edge constraints
   * @return the list of edges for the final triangulation
   */
  public static List<Edge> constrainDelaunayTriangulation(List<Triangle> triangulation,
                                                          Polygon polygon) {
    Set<Edge> originalEdges = polygon.getEdges();
    List<Edge> badEdges = Hull.convexHull(polygon);
    badEdges.removeAll(originalEdges);
    List<Polygon.Point> badPoints = new ArrayList<>();
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

    // Use edge flipping technique by sloan

    Set<Edge> constrainedTriangulation = new LinkedHashSet<>(originalEdges);

    for (Triangle triangle : triangulation) {
      constrainedTriangulation.addAll(triangle.getEdges());
    }
    for (Edge e : constrainedTriangulation) {
      for (Edge o : originalEdges) {
        if (e.strictlyIntersects(o)) {
          e.setBadEdge(true);
        }
      }
    }

    badEdges.forEach(constrainedTriangulation::remove);
    constrainedTriangulation.removeIf(Edge::isBad);
    return new ArrayList<>(constrainedTriangulation);
  }

}
