package com.github.javachaos.chaosdungeons.geometry.polygons;

import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.geometry.math.Matrix3x3Det;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Set;

/**
 * Class to represent a Triangle defined by three Point2D points.
 */
@SuppressWarnings("unused")
public class Triangle {

  private Polygon.Point pointA;
  private Polygon.Point pointB;
  private Polygon.Point pointC;

  boolean badTriangle;

  /**
   * Create a triangle from three points a, b and c.
   *
   * @param a the first point
   * @param b the second point
   * @param c the third point
   */
  public Triangle(Polygon.Point a, Polygon.Point b, Polygon.Point c) {
    this.pointA = a;
    this.pointB = b;
    this.pointC = c;
  }

  public Polygon.Point getA() {
    return pointA;
  }

  public Polygon.Point getB() {
    return pointB;
  }

  public Polygon.Point getC() {
    return pointC;
  }

  public void setA(Polygon.Point p) {
    this.pointA = p;
  }

  public void setB(Polygon.Point p) {
    this.pointB = p;
  }

  public void setC(Polygon.Point p) {
    this.pointC = p;
  }

  /**
   * Check if this triangle contains the point p.
   *
   * @param p the Point2D point to check.
   * @return true if the point p is contained in this triangle
   */
  public boolean contains(Polygon.Point p) {
    Polygon.Point a = pointA;
    Polygon.Point b = pointB;
    Polygon.Point c = pointC;

    double delta = a.x() - p.x();
    double gamma = a.y() - p.y();
    double epsilon = b.x() - p.x();
    double iota = b.y() - p.y();
    double phi = c.x() - p.x();
    double theta = c.y() - p.y();
    double[][] temp = {
        {a.x() - p.x(), a.y() - p.y(), delta * delta + gamma * gamma},
        {b.x() - p.x(), b.y() - p.y(), epsilon * epsilon + iota * iota},
        {c.x() - p.x(), c.y() - p.y(), phi * phi + theta * theta}
    };

    return Matrix3x3Det.compute(temp) > 1e-6;
  }

  /**
   * Check if this triangle contains the edge e.
   *
   * @param e the edge to check.
   * @return true if this triangle contains the edge e
   */
  @SuppressWarnings("unused")
  public boolean contains(Edge e) {
    return e.equals(new Edge(pointA, pointB)) || e.equals(new Edge(pointB, pointC))
        || e.equals(new Edge(pointA,
        pointC));
  }

  /**
   * Check if this triangle's vertices contain p.
   *
   * @param p the point to check.
   * @return true if any of the vertices of this triangle equal p
   */
  public boolean containsVertex(Polygon.Point p) {
    return pointA.equals(p) || pointB.equals(p) || pointC.equals(p);
  }

  public Set<Polygon.Point> getPoints() {
    return Set.of(pointA, pointB, pointC);
  }

  public boolean isBad() {
    return badTriangle;
  }

  public void setBad(boolean isBad) {
    this.badTriangle = isBad;
  }

  public List<Edge> getEdges() {
    return List.of(new Edge(pointA, pointB), new Edge(pointB, pointC), new Edge(pointA, pointC));
  }

}
