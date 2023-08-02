package com.github.javachaos.chaosdungeons.geometry.polygons;

import static com.github.javachaos.chaosdungeons.constants.Constants.EPSILON;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * Edge class, defined by two points (a, b).
 */
public class Edge {
  
  private Point2D pointA;
  private Point2D pointB;
  boolean badEdge;

  public Edge(Point2D a, Point2D b) {
    this.pointA = a;
    this.pointB = b;
  }

  /**
   * Check if two lines represented by (p1, p2) and (p3, p4) intersect.
   *
   * @param p1 the first point of the first line
   * @param p2 the second point of the first line
   * @param p3 the first point of the second line
   * @param p4 the second point of the second line
   * @return true if the two lines intersect
   */
  public static boolean checkIntersection(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
    double orientation1 = (p2.getY() - p1.getY()) * (p3.getX() - p2.getX())
        - (p2.getX() - p1.getX()) * (p3.getY() - p2.getY());
    double orientation2 = (p2.getY() - p1.getY()) * (p4.getX() - p2.getX())
        - (p2.getX() - p1.getX()) * (p4.getY() - p2.getY());
    double orientation3 = (p4.getY() - p3.getY()) * (p1.getX() - p4.getX())
        - (p4.getX() - p3.getX()) * (p1.getY() - p4.getY());
    double orientation4 = (p4.getY() - p3.getY()) * (p2.getX() - p4.getX())
        - (p4.getX() - p3.getX()) * (p2.getY() - p4.getY());
    return (orientation1 * orientation2 < 0) && (orientation3 * orientation4 < 0);
  }

  public static boolean checkIntersection(Edge a, Edge b) {
    return checkIntersection(a.pointA, a.pointB, b.pointA, b.pointB);
  }

  public static boolean epsEquals(double d1, double d2) {
    return Math.abs(d1 - d2) < EPSILON;
  }

  public static boolean epsPtEquals(Point2D p1, Point2D p2) {
    return epsEquals(p1.getX(), p2.getX()) && epsEquals(p1.getY(), p2.getY());
  }

  // Orientation of three points (p, q, r)
  private static int orientation(Point2D p, Point2D q, Point2D r) {
    double val = (q.getY() - p.getY()) * (r.getX() - q.getX())
        - (q.getX() - p.getX()) * (r.getY() - q.getY());
    if (val == 0) {
      return 0;  // Collinear
    }
    return (val > 0) ? -1 : 1; // Clockwise or Counterclockwise
  }

  /**
   * Function to find the convex hull using Graham Scan algorithm O(n log n).
   *
   * @param points point cloud
   * @return points of the convex hull
   */
  public static List<Point2D> convexHullPoints(List<Point2D> points) {
    int n = points.size();
    if (n <= 3) {
      return points; // Convex hull is the same as the original points
    }

    // Find the point with the lowest y-coordinate (and leftmost if tie)
    Point2D lowestPoint = points.get(0);
    for (int i = 1; i < n; i++) {
      Point2D current = points.get(i);
      if (current.getY() < lowestPoint.getY()
          || (current.getY() == lowestPoint.getY() && current.getX() < lowestPoint.getX())) {
        lowestPoint = current;
      }
    }

    // Sort points by polar angle with respect to the lowest point
    Point2D finalLowestPoint = lowestPoint;
    points.sort(Comparator.comparingDouble(p -> {
      double angle =
          Math.atan2(p.getY() - finalLowestPoint.getY(), p.getX() - finalLowestPoint.getX());
      return angle < 0 ? angle + 2 * Math.PI : angle;
    }));

    // Build the convex hull using a stack
    Stack<Point2D> stack = new Stack<>();
    stack.push(points.get(0));
    stack.push(points.get(1));

    for (int i = 2; i < n; i++) {
      while (!stack.isEmpty()
          && orientation(
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

  /**
   * Function to find the convex hull using Graham Scan algorithm.
   *
   * @param points the points to find the convex hull of.
   * @return the edges of the convex hull
   */
  public static List<Edge> convexHull(List<Point2D> points) {
    // Find the point with the lowest y-coordinate (and leftmost if tie)
    Point2D lowestPoint = points.get(0);
    for (int i = 1; i < points.size(); i++) {
      Point2D current = points.get(i);
      if (current.getY() < lowestPoint.getY()
          || (current.getY() == lowestPoint.getY() && current.getX() < lowestPoint.getX())) {
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