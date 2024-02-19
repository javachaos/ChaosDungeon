package com.github.javachaos.chaosdungeons.geometry.math;

import static com.github.javachaos.chaosdungeons.geometry.math.LinearMath.orientation;

import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Convex hull helper functions.
 */
public class Hull {

  private Hull() {
    //Unused
  }

  public static List<Edge> convexHull(Polygon polygon) {
    return convexHull(polygon.getPoints());
  }

  /**
   * Function to find the convex hull using Graham Scan algorithm.
   *
   * @param points the points to find the convex hull of.
   * @return the edges of the convex hull
   */
  public static List<Edge> convexHull(Set<Polygon.Point> points) {
    // Find the point with the lowest y-coordinate (and leftmost if tie)
    Iterator<Polygon.Point> iter = points.iterator();
    Polygon.Point lowestPoint = iter.next();
    for (int i = 1; i < points.size(); i++) {
      Polygon.Point current = iter.next();
      if (current.y() < lowestPoint.y()
          || (current.y() == lowestPoint.y() && current.x() < lowestPoint.x())) {
        lowestPoint = current;
      }
    }
    Deque<Polygon.Point> stack = new ArrayDeque<>(convexHullPoints(points));

    // Convert stack to list of edges and return the convex hull
    List<Edge> convexHullEdges = new ArrayList<>();
    Polygon.Point lastPoint = stack.pop();
    while (!stack.isEmpty()) {
      Polygon.Point currentPoint = stack.pop();
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
  public static Set<Polygon.Point> convexHullPoints(Set<Polygon.Point> points) {
    int n = points.size();
    if (n <= 3) {
      return points; // Convex hull is the same as the original points
    }

    Iterator<Polygon.Point> iter = points.iterator();
    // Find the point with the lowest y-coordinate (and leftmost if tie)
    Polygon.Point lowestPoint = iter.next();
    for (int i = 1; i < n; i++) {
      Polygon.Point current = iter.next();
      if (current.y() < lowestPoint.y()
          || (current.y() == lowestPoint.y() && current.x() < lowestPoint.x())) {
        lowestPoint = current;
      }
    }

    // Sort points by polar angle with respect to the lowest point
    Polygon.Point finalLowestPoint = lowestPoint;
    Set<Polygon.Point> sorted = points.stream().sorted(Comparator.comparingDouble(p -> {
      double angle =
              Math.atan2(p.y() - finalLowestPoint.y(), p.x() - finalLowestPoint.x());
      return angle < 0 ? angle + 2 * Math.PI : angle;
    })).collect(Collectors.toCollection(LinkedHashSet::new));

    // Build the convex hull using a stack
    Deque<Polygon.Point> stack = new ArrayDeque<>();
    Iterator<Polygon.Point> sortedIter = sorted.iterator();
    stack.push(sortedIter.next());
    stack.push(sortedIter.next());

    for (int i = 2; i < n; i++) {
      Polygon.Point curr = sortedIter.next();
      while (!stack.isEmpty()
          && orientation(
          stack.getLast(), //last
          stack.peek(), curr) <= 0) {
        stack.pop();
      }
      stack.push(curr);
    }

    // Convert stack to set and return the convex hull
    return new LinkedHashSet<>(stack);
  }
}
