package com.github.javachaos.chaosdungeons.geometry.math;

import static com.github.javachaos.chaosdungeons.geometry.math.LinearMath.orientation;

import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * Convex hull helper functions.
 */
public class Hull {

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
          stack.get(stack.size() - 1), //last
          stack.peek(),
          points.get(i)) <= 0) {
        stack.pop();
      }
      stack.push(points.get(i));
    }

    // Convert stack to list and return the convex hull
    return new ArrayList<>(stack);
  }
}
