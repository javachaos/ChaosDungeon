package com.github.javachaos.chaosdungeons.geometry.polygons;

import static com.github.javachaos.chaosdungeons.constants.Constants.EPSILON;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * A class to represent a polygon,
 * this class is a doubly-circularly linked list of vertices.
 * Each vertex has a next, and previous reference to another vertex.
 */
@SuppressWarnings("unused")
public class Vertex {

  private static final Logger LOGGER = LogManager.getLogger(Vertex.class);
  private double px;
  private double py;
  private Vertex next;
  private Vertex previous;
  private boolean isAcute;
  private double angle;
  private int index;

  public Vertex() {
  }

  public Vertex(Triangle t) {
    this(t.getPoints());
  }

  public Vertex(double x, double y) {
    this.px = x;
    this.py = y;
  }

  public Vertex(Point2D p) {
    this(p.getX(), p.getY());
  }

  /**
   * Create a new Vertex formed by the list of points p.
   *
   * @param p the list of points which define polygon.
   */
  public Vertex(List<Point2D> p) {
    if (p == null) {
      throw new NullPointerException("Points list was null.");
    }
    if (p.size() < 3) {
      throw new IllegalArgumentException("Not enough points.");
    }
    Point2D first = p.get(0);
    this.px = first.getX();
    this.py = first.getY();
    this.index = 0;
    next = null; // Initialize to null, to be set later
    previous = null; // Initialize to null, to be set later

    Vertex prevVertex = this;
    for (int i = 1; i < p.size(); i++) {
      Point2D point = p.get(i);
      Vertex vertex = new Vertex(point.getX(), point.getY());
      vertex.index = i;
      prevVertex.setNext(vertex);
      vertex.setPrevious(prevVertex);
      prevVertex = vertex;
    }

    // Complete the circular link
    this.setPrevious(prevVertex);
    prevVertex.setNext(this);
    calculateAngles(this);
  }

  private static void calculateAngles(Vertex vertices) {
    Vertex current = vertices.next;
    while (current != vertices) {
      current.angle = current.calculateAngle();
      current.isAcute = current.angle < 180.0;
      current = current.next;
    }
    current.angle = current.calculateAngle();
    current.isAcute = current.angle < 180.0;
  }

  /**
    * Get the bounding box of this polygon.
    *
    * @return the Rectangle2D which defines the bounding box of this polygon.
    */
  public Rectangle getBounds() {
    Point2D maxY = new Point2D.Double(0, Double.MIN_VALUE);
    Point2D minY = new Point2D.Double(0, Double.MAX_VALUE);
    Point2D maxX = new Point2D.Double(Double.MIN_VALUE, 0);
    Point2D minX = new Point2D.Double(Double.MAX_VALUE, 0);
    Vertex current = this.next;
    while (current != this) {
      Point2D curr = current.getPoint();
      if (curr.getY() > maxY.getY()) {
        maxY = curr;
      }
      if (curr.getY() < minY.getY()) {
        minY = curr;
      }
      if (curr.getX() > maxX.getX()) {
        maxX = curr;
      }
      if (curr.getX() < minX.getX()) {
        minX = curr;
      }
      current = current.next;
    }
    double x = minX.getX();
    double y = maxY.getY();
    double dx = maxX.getX();
    double dy = minY.getY();
    double w = dx - x;
    double h = dy - y;
    return new Rectangle((int) x, (int) y, (int) w, (int) h);
  }

  private static boolean epsEquals(double d1, double d2) {
    return Math.abs(d1 - d2) < EPSILON;
  }

  /**
   * Fast determinant of 3 points.
   *
   * @param a point 1 (prev)
   * @param b point 2 (current point)
   * @param c point 3 (next)
   * @return the determinant
   */
  public static double calculateDeterminant(Vertex a, Vertex b, Vertex c) {
    return ((b.px - a.px) * (c.py - a.py)) - ((c.px - a.px) * (b.py - a.py));
  }

  /**
   * Calculate the determinant (shoelace formula) of this polygon.
   * (The determinant of each of the vertices together, computes 2*Area of the polygon)
   * E.g. Vertex({0,0}, {0,1}, {1,1}) det(0*0*1 + 0*1*1)
   *
   * @param v the polygon to compute the area of
   * @return 2*Area of the polygon v
   */
  private static double calculateArea(Vertex v) {
    double determinant = 0.0;
    Vertex current = v.next;
    while (current != v) {
      determinant += current.px * current.next.py - current.next.px * current.py;
      current = current.next;
    }
    determinant += current.px * current.next.py - current.next.px * current.py;
    return determinant;
  }

  private static boolean sameSign(double num1, double num2) {
    if (num1 == 0 || num2 == 0) {
      return num1 == num2; // Zero is considered to have the same sign as zero
    } else {
      return (num1 > 0 && num2 > 0) || (num1 < 0 && num2 < 0);
    }
  }

  /**
   * Helper function to calculate the dot product of two 2D vectors.
   */
  private static double dotProduct(Point2D a, Point2D b) {
    return a.getX() * b.getX() + a.getY() * b.getY();
  }

  /**
   * Get the edges of this polygon. Clockwise orientation is assumed.
   *
   * @return the list of edges for this polygon
   */
  public List<Edge> getEdges() {
    List<Edge> e = new ArrayList<>();
    Vertex current = this.next;
    while (current != this) {
      e.add(new Edge(current.getPoint(), current.next.getPoint()));
      current = current.next;
    }
    e.add(new Edge(current.getPoint(), current.next.getPoint()));
    return e;
  }

  public boolean isAcute() {
    return isAcute;
  }

  public double getAngle() {
    return angle;
  }

  public Point2D getPoint() {
    return new Point2D.Double(px, py);
  }

  /**
   * Function to calculate the angle between this vertex and it's neighbours
   * next and previous.
   *
   * @return the angle in degrees between next, this and previous vertices
   */
  public double calculateAngle() {
    if (this.next == null || this.previous == null) {
      throw new IllegalArgumentException("Invalid vertex or insufficient vertices in the polygon.");
    }
    Vertex c = this.next;
    Vertex a = this.previous;

    double x1 = this.px - a.px;
    double y1 = this.py - a.py;
    double x2 = this.px - c.px;
    double y2 = this.py - c.py;

    Point2D v1 = new Point2D.Double(x1, y1);
    Point2D v2 = new Point2D.Double(x2, y2);

    double dot = dotProduct(v1, v2);

    if (epsEquals(dot, 1.0)) {
      return 90.0;
    }

    double magnitude1 = v1.distance(0, 0);
    double magnitude2 = v2.distance(0, 0);

    double angle = 0.0;

    double acos = Math.acos(dot / (magnitude1 * magnitude2));
    int con = concavity();
    if (con == 1) {
      angle = acos + (Math.PI);
    }
    if (con == 0) {
      return 0.0;
    }
    if (con == -1) {
      angle = acos;
    }
    //System.out.println(this + " Angle: " + Math.toDegrees(angle));
    // Convert angle from radians to degrees
    return Math.toDegrees(angle);
  }

  /**
   * Get the concavity of the vertex b.
   *
   * @return 0 if the point is co-linear with its neighbours
   *         1 if the point is concave
   *        -1 if the point is convex
   */
  public int concavity() {
    Vertex b = this;
    Vertex a = this.previous;
    Vertex c = this.next;
    double o = calculateArea(b);
    double det = calculateDeterminant(a, b, c);
    if (epsEquals(det, 0.0)) {
      return 0;
    }
    if ((det < 0.0 && o < 0.0) || (det > 0.0 && o > 0.0)) {
      return -1;
    }
    if (!sameSign(det, o)) {
      return 1;
    }
    return 0;
  }

  /**
   * Test if the polygon formed by this doubly linked list backed
   * vertex has the edge e.
   *
   * @param e the edge {@link Edge}
   * @return true if this polygon contains the edge
   */
  public boolean has(Edge e) {
    Vertex tempStart = this.next;
    if (e.equals(new Edge(this.getPoint(), this.next.getPoint()))
        || e.equals(new Edge(this.next.getPoint(), this.getPoint()))) {
      return true;
    }
    while (tempStart != this) {
      if (e.equals(new Edge(tempStart.getPoint(), tempStart.next.getPoint()))
          || e.equals(new Edge(tempStart.next.getPoint(), tempStart.getPoint()))) {
        return true;
      }
      tempStart = tempStart.next;
    }
    return false;
  }

  /**
   * Find the point a in this vertex, if it does not exist return null.
   *
   * @param a the Point2D point to find
   * @return the vertex if it exists, null otherwise
   */
  public Vertex find(Point2D a) {
    Vertex current = this.next;
    while (current != this) {
      if (current.getPoint().equals(a)) {
        return current;
      }
      current = current.next;
    }
    if (current.getPoint().equals(a)) {
      return current;
    }
    return null;
  }

  public Point2D get(int i) {
    return getVertex(i).getPoint();
  }

  /**
   * Get the ith vertex in this polygon.
   *
   * @param i the index of the vertex, if i is larger than the number of
   *          vertices in this polygon, get the vertex at i%size
   * @return the vertex at i
   */
  public Vertex getVertex(int i) {
    int j = 0;
    Vertex current = this.next;
    while (j++ < i) {
      current = current.next;
    }
    return current;
  }

  /**
   * Print this vertex to the logger.
   */
  public void print() {
    if (this.next == null) {
      LOGGER.debug(this);
    }
    Vertex current = this.next;
    do {
      LOGGER.debug(current);
      current = current.next;
    } while (current != this.next);
  }

  /**
   * Add a vertex to this polygon.
   *
   * @param newVertex the new vertex to be added to this polygon
   */
  public void add(Vertex newVertex) {
    if (this.next == null) {
      this.next = newVertex;
      this.next.next = this.next;
      this.next.previous = this.next;
    } else {
      Vertex last = this.next;
      while (last.next != this.next) {
        last = last.next;
      }
      last.next = newVertex;
      newVertex.next = this.next;
      newVertex.previous = last;
      this.next.previous = newVertex;
    }
  }

  public void add(Point2D point) {
    Vertex newVertex = new Vertex(point);
    add(newVertex);
  }

  private void updateIndicies() {
    if (this.next == null) {
      return;
    }
    Vertex current = this.next;
    int i = 0;
    do {
      current.index = i++;
      current = current.next;
    } while (current != this.next);
  }

  public Vertex getNext() {
    return next;
  }

  public void setNext(Vertex next) {
    this.next = next;
  }

  public Vertex getPrevious() {
    return previous;
  }

  public void setPrevious(Vertex previous) {
    this.previous = previous;
  }

  ////////////////////////////////////// Helper functions /////////////////////////////////////////

  /**
   * Get all the vertices of this polygon as a list of Point2D.
   *
   * @return the list of points
   */
  public List<Point2D> getPoints() {
    List<Point2D> pts = new ArrayList<>();
    Vertex current = this.next;
    pts.add(getPoint());
    while (current != this) {
      pts.add(current.getPoint());
      current = current.next;
    }
    return pts;
  }

  /**
   * Remove the ith vertex from this polygon.
   *
   * @param i the index of the vertex to remove
   * @throws IllegalArgumentException if the index is less than or equal to 0
   *                                  attempting to remove the 0th vertex is not possible
   */
  public void remove(int i) {
    if (i <= 0) {
      throw new IllegalArgumentException("Index " + i + " out of bounds.");
    }
    //    if (next.index - i >= previous.index - i) {
    //      //Counterclockwise
    //    } else {
    //      //Clockwise
    //    }
    //Vertex curr = next;
    updateIndicies();
  }

  /**
   * Remove a point from this vertex. If this vertex does not contain
   * the point, the vertex remains unchanged.
   *
   * @param point the point to remove
   */
  public void remove(Point2D point) {
    Vertex current = this;
    while (current.next != this) {
      if (current.next.getPoint().equals(point)) {
        current.next = current.next.next;
        current.next.previous = current;
        return;
      }
      current.previous = current;
      current = current.next;
    }
    updateIndicies();
  }

  public void removeAll(List<Point2D> badPoints) {
    badPoints.forEach(this::remove);
  }

  /**
   * Get the size of this vertex, namely the number of vertices
   * contained in this circular doubly linked list of vertices.
   *
   * @return the size of this polygon
   */
  public int size() {
    int count = 0;
    Vertex current = this;
    do {
      count++;
      current = current.next;
    } while (current != this);
    return count;
  }

  @Override
  public String toString() {
    return index + " :[" + px + ", " + py + "]";
  }

}
