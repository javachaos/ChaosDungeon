package com.github.javachaos.chaosdungeons.geometry.polygons;

import com.github.javachaos.chaosdungeons.collision.Polygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.javachaos.chaosdungeons.geometry.math.LinearMath.*;

/**
 * Edge class, defined by two points (a, b).
 */
@SuppressWarnings("unused")
public class Edge {
  
  private Polygon.Point pointA;
  private Polygon.Point pointB;
  boolean badEdge;

  public Edge(Polygon.Point a, Polygon.Point b) {
    this.pointA = a;
    this.pointB = b;
  }

  public Edge(float a, float b, float c, float d) {
    this.pointA = new Polygon.Point(a, b);
    this.pointB = new Polygon.Point(c, d);
  }

  /**
   * Helper function to create edges from a list of points.
   *
   * @param points list of points to create edges from, orientation is assumed to be clockwise.
   * @return the new list of edges.
   */
  @SuppressWarnings("unused")
  private static List<Edge> createEdges(List<Polygon.Point> points) {
    List<Edge> edges = new ArrayList<>();
    int n = points.size();
    for (int i = 0; i < n - 1; i++) {
      edges.add(new Edge(points.get(i), points.get(i + 1)));
    }
    edges.add(new Edge(points.get(n - 1), points.get(0))); // Closing edge
    return edges;
  }

  public void setA(Polygon.Point pointA) {
    this.pointA = pointA;
  }

  public void setB(Polygon.Point pointB) {
    this.pointB = pointB;
  }

  public Polygon.Point getA() {
    return pointA;
  }

  public Polygon.Point getB() {
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

  /**
   * Checks if this edge intersects with the edge e.
   *
   * @param e the other edge.
   * @return true if this edge and e are intersecting
   */
  public boolean strictlyIntersects(Edge e) {
    return checkIntersectionStrict(this, e);
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
    return "u[ " + pointA.x() + ", " + pointA.y() + "]"
        + " v[" + pointB.x() + ", " + pointB.y() + "]";
  }

  /**
   * Get the normal for this edge.
   *
   * @return the normal
   */
  @SuppressWarnings("all")
  public Polygon.Point getNormal() {
    float dirX = pointB.x() - pointA.x();
    float dirY = pointB.y() - pointA.y();
    return new Polygon.Point(-dirY, dirX);
  }


  /**
   * Compute the distance between this edge and the point p0.
   *
   * @param p0 the point
   * @return the distance to the point p0 from this line.
   */
  public double distanceToPoint(Polygon.Point p0) {
    double num = Math.abs((pointB.x() - pointA.x())
        * (pointB.y() - p0.y()) - ((pointA.x() - p0.x())
        * (pointB.y() - pointA.y())));
    double x1 = (pointB.x() - pointA.x());
    double y1 = (pointB.y() - pointA.y());
    double denom = Math.sqrt(x1 * x1 + y1 * y1);
    return num / denom;
  }

  public double projectPoint(Polygon.Point point) {
    Polygon.Point normalizedAxis = normalize(getDirectionVector());
    return projectPointOnAxis(normalizedAxis, point);
  }

  private Polygon.Point getDirectionVector() {
    float dx = pointB.x() - pointA.x();
    float dy = pointB.y() - pointA.y();
    return new Polygon.Point(dx, dy);
  }

  private double projectPointOnAxis(Polygon.Point axis, Polygon.Point point) {
    return axis.x() * (point.x() - pointA.x()) + axis.y()
        * (point.y() - pointA.y());
  }

  private Polygon.Point normalize(Polygon.Point vector) {
    double length = Math.sqrt(vector.x() * vector.x() + vector.y() * vector.y());
    return new Polygon.Point((float) (vector.x() / length), (float) (vector.y() / length));
  }
}