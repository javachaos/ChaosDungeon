package com.github.javachaos.chaosdungeons.geometry.math;

import static com.github.javachaos.chaosdungeons.constants.Constants.EPSILON;

import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;
import java.awt.geom.Point2D;

/**
 * Linear math helper functions.
 */
public class LinearMath {

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
    return checkIntersection(a.getA(), a.getB(), b.getA(), b.getB());
  }

  public static boolean epsEquals(double d1, double d2) {
    return Math.abs(d1 - d2) < EPSILON;
  }

  public static boolean epsPtEquals(Point2D p1, Point2D p2) {
    return epsEquals(p1.getX(), p2.getX()) && epsEquals(p1.getY(), p2.getY());
  }

  /**
   * Orientation of three points (p, q, r).
   *
   * @param p point p
   * @param q point q
   * @param r point r
   * @return the orientation -1 for clockwise, 1 for counterclockwise and 0 for co-linear
   */
  public static int orientation(Point2D p, Point2D q, Point2D r) {
    double val = (q.getY() - p.getY()) * (r.getX() - q.getX())
        - (q.getX() - p.getX()) * (r.getY() - q.getY());
    if (val == 0) {
      return 0;  // Collinear
    }
    return (val > 0) ? -1 : 1; // Clockwise or Counterclockwise
  }
}
