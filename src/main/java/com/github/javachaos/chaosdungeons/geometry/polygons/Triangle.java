package com.github.javachaos.chaosdungeons.geometry.polygons;

import com.github.javachaos.chaosdungeons.geometry.Matrix3x3Det;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Class to represent a Triangle defined by three Point2D points.
 */
public class Triangle {

  private Point2D pointA;
  private Point2D pointB;
  private Point2D pointC;

  boolean badTriangle;

  /**
   * Create a triangle from three points a, b and c.
   *
   * @param a the first point
   * @param b the second point
   * @param c the third point
   */
  public Triangle(Point2D a, Point2D b, Point2D c) {
    this.pointA = a;
    this.pointB = b;
    this.pointC = c;
  }

  public Point2D getA() {
    return pointA;
  }

  public Point2D getB() {
    return pointB;
  }

  public Point2D getC() {
    return pointC;
  }

  public void setA(Point2D p) {
    this.pointA = p;
  }

  public void setB(Point2D p) {
    this.pointB = p;
  }

  public void setC(Point2D p) {
    this.pointC = p;
  }

  /**
   * Check if this triangle contains the point p.
   *
   * @param p the Point2D point to check.
   * @return true if the point p is contained in this triangle
   */
  public boolean contains(Point2D p) {
    Point2D a = pointA;
    Point2D b = pointB;
    Point2D c = pointC;

    double delta = a.getX() - p.getX();
    double gamma = a.getY() - p.getY();
    double epsilon = b.getX() - p.getX();
    double iota = b.getY() - p.getY();
    double phi = c.getX() - p.getX();
    double theta = c.getY() - p.getY();
    double[][] temp = {
        {a.getX() - p.getX(), a.getY() - p.getY(), delta * delta + gamma * gamma},
        {b.getX() - p.getX(), b.getY() - p.getY(), epsilon * epsilon + iota * iota},
        {c.getX() - p.getX(), c.getY() - p.getY(), phi * phi + theta * theta}
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
  public boolean containsVertex(Point2D p) {
    return pointA.equals(p) || pointB.equals(p) || pointC.equals(p);
  }

  public List<Point2D> getPoints() {
    return List.of(pointA, pointB, pointC);
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
