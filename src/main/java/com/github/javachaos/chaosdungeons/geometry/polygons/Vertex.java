package com.github.javachaos.chaosdungeons.geometry.polygons;

import static com.github.javachaos.chaosdungeons.constants.Constants.EPSILON;
import static com.github.javachaos.chaosdungeons.geometry.math.LinearMath.dotProduct;
import static com.github.javachaos.chaosdungeons.utils.PrecisionUtils.*;
import static com.github.javachaos.chaosdungeons.utils.PrecisionUtils.equalTo;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import com.github.javachaos.chaosdungeons.collision.CollisionData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;


/**
 * A class to represent a polygon,
 * this class is a doubly-circularly linked list of vertices.
 * Each vertex has a next, and previous reference to another vertex.
 */
@SuppressWarnings("unused")
public class Vertex implements Iterable<Vertex> {

  public static class Bounds {
    public double px;
    public double py;
    public double w;
    public double h;

    public Bounds(double x, double y, double w, double h) {
      this.px = x;
      this.py = y;
      this.w = w;
      this.h = h;
    }

    /**
     * Return true if this quad contains the point (x, y).
     *
     * @param x the x pos
     * @param y the y pos
     * @return true if this quad contains (x, y)
     */
    public boolean contains(double x, double y) {
      double x1 = this.px;
      double x2 = this.px + w;
      double y1 = this.py;
      double y2 = this.py + h;
      return (greaterThan(x, x1) || equalTo(x, x1))
              && (lessThan(x, x2)    || equalTo(x, x2))
              && (greaterThan(y, y1) || equalTo(y, y1))
              && (lessThan(y, y2)    || equalTo(y, y2));
    }

    public boolean contains(Point2D p) {
      return contains(p.getX(), p.getY());
    }
  }
  private static final Logger LOGGER = LogManager.getLogger(Vertex.class);

  private Vertex next;
  private Vertex previous;
  private boolean isAcute;
  private double angle;
  private int index;
  private List<Edge> edges;
  private List<Point2D> points;
  
  private Bounds bounds;
  

  public Vertex() {
    bounds = new Bounds(0,0,0,0);
  }

  public Vertex(Triangle t) {
    this(new ArrayList<>(t.getPoints()));
  }

  public Vertex(double x, double y) {
    this();
    this.bounds.px = x;
    this.bounds.py = y;
    edges = new ArrayList<>();
    points = new ArrayList<>();
    points.add(new Point2D.Double(x, y));
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
    this();
    if (p == null) {
      throw new NullPointerException("Points list was null.");
    }
    if (p.size() < 3) {
      throw new IllegalArgumentException("Not enough points.");
    }
    Point2D first = p.get(0);
    this.bounds.px = first.getX();
    this.bounds.py = first.getY();
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
    this.edges = cacheEdges();
    this.bounds = cacheBounds();
    this.points = p;
  }

  private static void calculateAngles(Vertex vertices) {
    Vertex current = vertices;
    do {
      current.angle = current.calculateAngle();
      current.isAcute = current.angle < 180.0;
      current = current.next;
    } while (current != vertices);
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
    return ((b.bounds.px - a.bounds.px) * (c.bounds.py - a.bounds.py)) - ((c.bounds.px - a.bounds.px) * (b.bounds.py - a.bounds.py));
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
    Vertex current = v;
    do {
      determinant += current.bounds.px * current.next.bounds.py - current.next.bounds.px * current.bounds.py;
      current = current.next;
    } while (current != v);
    return determinant;
  }

  private static boolean sameSign(double num1, double num2) {
    if (num1 == 0 || num2 == 0) {
      return num1 == num2; // Zero is considered to have the same sign as zero
    } else {
      return (num1 > 0 && num2 > 0) || (num1 < 0 && num2 < 0);
    }
  }

  public int getNumVertices() {
    return points.size();
  }

  public Bounds getBounds() {
    return bounds;
  }

  public float getX() {
    return (float) bounds.px;
  }

  public float getY() {
    return (float) bounds.py;
  }

  public float getWidth() {
    return (float) bounds.w;
  }

  public float getHeight() {
    return (float) bounds.h;
  }

  /**
   * Get the bounding box of this polygon.
   *
   * @return the Rectangle2D which defines the bounding box of this polygon.
   */
  public Bounds cacheBounds() {
    double maxY = Double.MIN_VALUE;
    double minY = Double.MAX_VALUE;
    double maxX = Double.MIN_VALUE;
    double minX = Double.MAX_VALUE;
    Vertex current = this;

    do {
      Point2D curr = current.getPoint();
      double x = curr.getX();
      double y = curr.getY();
      maxY = Math.max(maxY, y);
      minY = Math.min(minY, y);
      maxX = Math.max(maxX, x);
      minX = Math.min(minX, x);
      current = current.next;
    } while (current != this);

    double x = minX;
    double y = minY;
    double w = maxX - x;
    double h = maxY - y;

    return new Bounds(x, y, w, h);
  }

  /**
   * Get the edges of this polygon. Clockwise orientation is assumed.
   *
   * @return the list of edges for this polygon
   */
  private List<Edge> cacheEdges() {
    List<Edge> e = new ArrayList<>();
    Vertex current = this;
    do {
      e.add(new Edge(current.getPoint(), current.next.getPoint()));
      current = current.next;
    } while (current != this);
    return e;
  }

  public List<Edge> getEdges() {
    return edges;
  }

  public boolean isAcute() {
    return isAcute;
  }

  public double getAngle() {
    return angle;
  }

  public Point2D getPoint() {
    return new Point2D.Double(bounds.px, bounds.py);
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

    double x1 = this.bounds.px - a.bounds.px;
    double y1 = this.bounds.py - a.bounds.py;
    double x2 = this.bounds.px - c.bounds.px;
    double y2 = this.bounds.py - c.bounds.py;

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
   *         -1 if the point is convex
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
    Vertex tempStart = this;
    do {
      if (e.equals(new Edge(tempStart.getPoint(), tempStart.next.getPoint()))
          || e.equals(new Edge(tempStart.next.getPoint(), tempStart.getPoint()))) {
        return true;
      }
      tempStart = tempStart.next;
    } while (tempStart != this);
    return false;
  }

  /**
   * Find the point a in this vertex, if it does not exist return null.
   *
   * @param a the Point2D point to find
   * @return the vertex if it exists, null otherwise
   */
  public Vertex find(Point2D a) {
    Vertex current = this;
    do {
      if (current.getPoint().equals(a)) {
        return current;
      }
      current = current.next;
    } while (current != this);
    return null;
  }

  public Point2D get(int i) {
    return getVertex(i).getPoint();
  }

  /**
   * Translate this shape by x and y.
   *
   * @param x the x pos
   * @param y the y pos
   */
  public void translate(double x, double y) {
    Vertex current = this;
    do {
      current.bounds.px += x;
      current.bounds.py += y;
      current = current.next;
    } while (current != this);
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
    LOGGER.debug("--------------------------------------------");
    if (this.next == null) {
      LOGGER.debug(this);
    }
    Vertex current = this;
    do {
      LOGGER.debug(current);
      current = current.next;
    } while (current != this);
    LOGGER.debug("Size: " + size());
    LOGGER.debug("--------------------------------------------");
  }

  /**
   * Print this vertex to the logger.
   */
  public String printHelper() {
    StringBuilder sb = new StringBuilder();
    if (this.next == null) {
      sb.append(this);
      sb.append(System.lineSeparator());
    }
    Vertex current = this;
    do {
      sb.append(current);
      sb.append(System.lineSeparator());
      if (current == null) {
        break;
      }
      current = current.next;
    } while (current != this);
    return sb.toString();
  }

  /**
   * Add a vertex to this polygon.
   *
   * @param newVertex the new vertex to be added to this polygon
   */
  public void add(Vertex newVertex) {
    points.add(new Point2D.Double(newVertex.bounds.px,
            newVertex.bounds.py));
    edges.add(new Edge(bounds.px, bounds.py,
            newVertex.bounds.px, newVertex.bounds.py));
    if (this.next == null) { // Next is null, we have a zero sized polygon
      this.next = newVertex;
      newVertex.next = this;
      newVertex.previous = this;
    } else { // Next not null, more than one vertex
      Vertex curr = this.next;
      while (curr.next != this) {
        curr = curr.next;
      }
      Vertex tmp = curr.next;
      curr.next = newVertex;
      newVertex.next = tmp;
      newVertex.previous = curr;
      newVertex.next.previous = newVertex;
    }
    updateIndicies();
  }

  public void add(Point2D point) {
    Vertex newVertex = new Vertex(point);
    add(newVertex);
  }

  private void updateIndicies() {
    if (this.next == null) {
      return;
    }
    Vertex current = this;
    int i = 0;
    do {
      current.index = i++;
      current = current.next;
    } while (current != this);
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
    return points;
  }

  /**
   * Remove the ith vertex from this polygon.
   *
   * @param i the index of the vertex to remove
   * @throws IllegalArgumentException if the index is less than or equal to 0
   *                                  attempting to remove the 0th vertex is not possible
   */
  public void remove(int i) {
    if (i <= 0 || i >= size()) {
      throw new IllegalArgumentException(
          "Index " + i + " out of bounds.");
    }
    Vertex curr = this;
    int j = 0;
    while (j++ != (i - 1)) {
      curr = curr.next;
    }
    curr.next = curr.next.next;
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
    do {
      if (current.next.getPoint().equals(point)) {
        current.next = current.next.next;
        current.next.previous = current;
        return;
      }
      current.previous = current;
      current = current.next;
    } while (current.next != this);
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
    return getPoints().size();
  }

  /**
   * Create a mesh for this vertex polygon.
   *
   * @return the mesh
   */
  public Mesh createMesh() {
    float[] pos = new float[size() * 2];
    for (int i = 0; i < pos.length; i += 2) {
      Point2D p = get(i);
      pos[i] = (float) p.getX();
      pos[i + 1] = (float) p.getY();
    }
    int[] indices = IntStream.range(0, size()).toArray();
    return MeshLoader.createMesh(pos, indices, 2);
  }

  @Override
  public String toString() {
    return index + " :[" + bounds.px + ", " + bounds.py + "]";
  }

  @Override
  public Iterator<Vertex> iterator() { //Test this
    return new Iterator<>() {
      final Vertex curr = Vertex.this;

      @Override
      public boolean hasNext() {
        return Vertex.this.next != curr;
      }

      @Override
      public Vertex next() {
        return Vertex.this.next;
      }
    };
  }

  /**
   * Return true if this vertex contains the point p within its bounds.
   * Does not work with degenerate polygons, works with holes or concave shapes.
   *
   * @param p the point to check
   * @return the distance to the nearest edge
   *         if p is inside the bounds of the polygon represented by this vertex
   *         0 if the point is not contained in this vertex.
   */
  public boolean contains(Point2D p) {
    if (!bounds.contains(p)) {
      return false;
    }
    Point2D horizontalPoint = new Point2D.Double(bounds.px,
            bounds.h - (bounds.h - p.getY()));
    Edge e = new Edge(horizontalPoint, p);
    int intersectionCount = 0;
    for (Edge f : edges) {
      if (f.intersects(e)) {
        intersectionCount++;
        if (intersectionCount >= 2) {
          break;
        }
      }
    }
    // odd number of intersecting points, means this vertex
    // contains the point p
    return intersectionCount % 2 != 0;
  }

  /**
   * Check collision between two polygons defined by two sets of points.
   * //** unit test this.**
   *
   * @param otherPolygon set of points defining the other polygon
   * @return collision data
   */
  public CollisionData checkCollision(Vertex otherPolygon) {
    boolean isColliding = false;
    List<Point2D> otherPts = otherPolygon.getPoints();
    List<Vector2f> contactPoints1 = new ArrayList<>();
    List<Vector2f> contactPoints2 = new ArrayList<>();

    Vertex current = otherPolygon;
    do {
      if (contains(current.getPoint())) {
        isColliding = true;
        contactPoints1.add(new Vector2f((float) current.bounds.px, (float) current.bounds.py));
      }
      current = current.next;
    } while (current != otherPolygon);

    current = this;
    do {
      if (otherPolygon.contains(current.getPoint())) {
        isColliding = true;
        contactPoints2.add(new Vector2f((float) current.bounds.px, (float) current.bounds.py));
      }
      current = current.next;
    } while (current != this);

    double totalDistance = 0;
    double maxDist = 0;
    int maxDistV1Idx = 0;
    int maxDistV2Idx = 0;

    if (!contactPoints1.isEmpty() && !contactPoints2.isEmpty()) {

      for (int i = 0; i < contactPoints1.size(); i++) {
        Vector2f v1 = contactPoints1.get(i);
        for (int j = 0; j < contactPoints2.size(); j++) {
          Vector2f v2 = contactPoints2.get(j);
          double currentDist = v1.distance(v2);
          if (currentDist > maxDist) {
            maxDist = currentDist;
            maxDistV1Idx = i;
            maxDistV2Idx = j;
          }
          totalDistance += currentDist;
        }
      }

      Vector2f collisionNormal = new Vector2f(contactPoints2.get(maxDistV2Idx)
              .sub(contactPoints1.get(maxDistV1Idx)));
      List<Vector2f> allPts = new ArrayList<>(contactPoints1);
      allPts.addAll(contactPoints2);
      return new CollisionData.Builder()
              .setPenetrationDepth(collisionNormal.length())
              .setContactPoints(allPts)
              .setIsColliding(true)
              .setCollisionNormal(collisionNormal).build();
    }
    //In this case if it is colliding it will return true, but the rest of the
    // collision information must be filled in by collision component
    // using continuous collision detection. (keep a fixed stack of position data)
    return new CollisionData.Builder().setPenetrationDepth(totalDistance)
            .setContactPoints(Collections.emptyList())
            .setIncomplete(true)
            .setIsColliding(isColliding)
            .setCollisionNormal(new Vector2f()).build();
  }

  private boolean findCollidingContactPoints(Vertex polygon, List<Vector2f> contactPoints) {
    boolean isColliding = false;
    Vertex current = polygon;
    do {
      if (contains(current.getPoint())) {
        isColliding = true;
        contactPoints.add(new Vector2f((float) current.bounds.px, (float) current.bounds.py));
      }
      current = current.next;
    } while (current != polygon);
    return isColliding;
  }

  public boolean contains(double x, double y) {
    return contains(new Point2D.Double(x, y));
  }

  public Vector2f getPosition() {
    return new Vector2f((float) bounds.px, (float) bounds.py);
  }
}
