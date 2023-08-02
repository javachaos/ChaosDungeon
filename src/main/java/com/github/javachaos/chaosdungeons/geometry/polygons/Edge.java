package com.github.javachaos.chaosdungeons.geometry.polygons;

import static com.github.javachaos.chaosdungeons.geometry.math.LinearMath.checkIntersection;
import static com.github.javachaos.chaosdungeons.geometry.math.LinearMath.epsPtEquals;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Edge class, defined by two points (a, b).
 */
@SuppressWarnings("unused")
public class Edge {
  
  private Point2D pointA;
  private Point2D pointB;
  boolean badEdge;

  public Edge(Point2D a, Point2D b) {
    this.pointA = a;
    this.pointB = b;
  }

  /**
   * Helper function to create edges from a list of points.
   *
   * @param points list of points to create edges from, orientation is assumed to be clockwise.
   * @return the new list of edges.
   */
  @SuppressWarnings("unused")
  private static List<Edge> createEdges(List<Point2D> points) {
    List<Edge> edges = new ArrayList<>();
    int n = points.size();
    for (int i = 0; i < n - 1; i++) {
      edges.add(new Edge(points.get(i), points.get(i + 1)));
    }
    edges.add(new Edge(points.get(n - 1), points.get(0))); // Closing edge
    return edges;
  }

  public void setA(Point2D pointA) {
    this.pointA = pointA;
  }

  public void setB(Point2D pointB) {
    this.pointB = pointB;
  }

  public Point2D getA() {
    return pointA;
  }

  public Point2D getB() {
    return pointB;
  }

  public void setBadEdge(boolean isBad) {
    this.badEdge = isBad;
  }

  public boolean isBad() {
    return badEdge;
  }

  /**
   * Checks if this edge intersects with the edge e.
   *
   * @param e the other edge.
   * @return true if this edge and e are intersecting
   */
  public boolean intersects(Edge e) {
    return checkIntersection(this, e);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pointA, pointB);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Edge other = (Edge) obj;
    return (epsPtEquals(pointA, other.pointA) && epsPtEquals(pointB, other.pointB))
        || (epsPtEquals(pointA, other.pointB) && epsPtEquals(pointB, other.pointA));
  }

  @Override
  public String toString() {
    return "u[ " + pointA.getX() + ", " + pointA.getY() + "]"
        + " v[" + pointB.getX() + ", " + pointB.getY() + "]";
  }

}