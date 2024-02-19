package com.github.javachaos.chaosdungeons.geometry.math;

import static com.github.javachaos.chaosdungeons.constants.Constants.EPSILON;

import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;
import java.awt.geom.Point2D;

/**
 * Linear math helper functions.
 */
public class LinearMath {

  private LinearMath() {
    //Unused
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
  public static boolean checkIntersectionStrict(Polygon.Point p1, Polygon.Point p2, Polygon.Point p3, Polygon.Point p4) {
    double orientation1 = (p2.y() - p1.y()) * (p3.x() - p2.x())
            - (p2.x() - p1.x()) * (p3.y() - p2.y());
    double orientation2 = (p2.y() - p1.y()) * (p4.x() - p2.x())
            - (p2.x() - p1.x()) * (p4.y() - p2.y());
    double orientation3 = (p4.y() - p3.y()) * (p1.x() - p4.x())
            - (p4.x() - p3.x()) * (p1.y() - p4.y());
    double orientation4 = (p4.y() - p3.y()) * (p2.x() - p4.x())
            - (p4.x() - p3.x()) * (p2.y() - p4.y());
    return (orientation1 * orientation2 < 0) && (orientation3 * orientation4 < 0);
  }

  public static boolean checkIntersection(Polygon.Point p1, Polygon.Point p2, Polygon.Point p3, Polygon.Point p4) {
    double orientation1 = (p2.y() - p1.y()) * (p3.x() - p2.x())
            - (p2.x() - p1.x()) * (p3.y() - p2.y());
    double orientation2 = (p2.y() - p1.y()) * (p4.x() - p2.x())
            - (p2.x() - p1.x()) * (p4.y() - p2.y());
    double orientation3 = (p4.y() - p3.y()) * (p1.x() - p4.x())
            - (p4.x() - p3.x()) * (p1.y() - p4.y());
    double orientation4 = (p4.y() - p3.y()) * (p2.x() - p4.x())
            - (p4.x() - p3.x()) * (p2.y() - p4.y());
    return (orientation1 * orientation2 <= 0) && (orientation3 * orientation4 <= 0);
  }

  public static boolean checkIntersection(Edge a, Edge b) {
    return checkIntersection(a.getA(), a.getB(), b.getA(), b.getB());
  }

  public static boolean checkIntersectionStrict(Edge a, Edge b) {
    return checkIntersectionStrict(a.getA(), a.getB(), b.getA(), b.getB());
  }

  public static boolean epsEquals(double d1, double d2) {
    return Math.abs(d1 - d2) < EPSILON;
  }

  public static boolean epsPtEquals(Polygon.Point p1, Polygon.Point p2) {
    return epsEquals(p1.x(), p2.x()) && epsEquals(p1.y(), p2.y());
  }

  /**
   * Orientation of three points (p, q, r).
   *
   * @param p point p
   * @param q point q
   * @param r point r
   * @return the orientation -1 for clockwise, 1 for counterclockwise and 0 for co-linear
   */
  public static int orientation(Polygon.Point p, Polygon.Point q, Polygon.Point r) {
    double val = (q.y() - p.y()) * (r.x() - q.x())
        - (q.x() - p.x()) * (r.y() - q.y());
    if (val == 0) {
      return 0;  // Collinear
    }
    return (val > 0) ? -1 : 1; // Clockwise or Counterclockwise
  }

  /**
   * Helper function to calculate the dot product of two 2D vectors.
   */
  public static double dotProduct(Polygon.Point a, Polygon.Point b) {
    return a.x() * b.x() + a.y() * b.y();
  }
}
