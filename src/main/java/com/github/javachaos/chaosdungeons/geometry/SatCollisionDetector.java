package com.github.javachaos.chaosdungeons.geometry;

import static com.github.javachaos.chaosdungeons.geometry.GenerationUtils.generateNonRegularPolygon;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import com.github.javachaos.chaosdungeons.collision.Collision;
import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.geometry.gui.ShapeDrawer;
import com.github.javachaos.chaosdungeons.geometry.math.LinearMath;
import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;
import com.github.javachaos.chaosdungeons.geometry.polygons.Triangle;

/**
 * Separating Axis Theorem collision detector.
 */
@SuppressWarnings("unused")
public class SatCollisionDetector {

  private static final Logger LOGGER = LogManager.getLogger(SatCollisionDetector.class);

  /**
   * Main entry point.
   *
   * @param args the args
   */
  public static void main(String[] args) {

    int[] xpoints = {0, 315, 115, 299, 47, 55, 26, 0};
    int[] ypoints = {0, 53, 115, 234, 176, 271, 383, 0};
    Set<Polygon.Point> pts = generateNonRegularPolygon(100, 100, 120, 100, 100);

    // Example usage
    Rectangle redRect = new Rectangle(0, 0, 150, 200);
    Rectangle blueRect = new Rectangle(466, 88, 80, 65);
    Ellipse2D ellipse2D = new Ellipse2D.Double();
    ellipse2D.setFrame(redRect);

    List<Polygon.Point> blue = calculateRectanglePoints(blueRect);
    List<Polygon.Point> red = calculateRectanglePoints(redRect);

    SwingUtilities.invokeLater(() -> new ShapeDrawer(new LinkedHashSet<>(red)));
    long start = System.nanoTime();
    long end = System.nanoTime();
    LOGGER.debug("Runtime: {} ms", (end - start) / 1000000.0);
  }

  /**
   * Check collision between two polygons defined by two sets of points.
   *
   * @param polygon1 set of points defining the first polygon
   * @param polygon2 set of points defining the second polygon
   * @return true if the two polygons collide
   */
  public static boolean checkCollision(Set<Polygon.Point> polygon1, Set<Polygon.Point> polygon2) {
    List<Polygon.Point> axes = getAxes(polygon1, polygon2);
    Optional<Polygon.Point> p;
    if (axes.size() > 100) {
      p = axes.parallelStream().filter(x -> isSeparatingAxis(x, polygon1, polygon2)).findAny();
    } else {
      for (Polygon.Point point : axes) {
        if (isSeparatingAxis(point, polygon1, polygon2)) {
          return false;
        }
      }
      return true;
    }
    return p.isEmpty();
  }

  public static Collision checkCollision(Polygon a, Polygon b) {
    Vector2f normal = checkCollisionDelaunay(a.getPoints(), b.getPoints());
    if (normal != null) {
      //colliding
      return new Collision.Builder().setCollisionNormal(normal).setIsColliding(true).build();
    }
    return new Collision.Builder().setIsColliding(false).build();
  }

  /**
   * Check collision between two triangle.
   *
   * @param t1 set of points defining the first triangle
   * @param t2 set of points defining the second triangle
   * @return true if the two polygons collide
   */
  public static boolean checkCollision(Triangle t1, Triangle t2) {
    List<Polygon.Point> axes = getAxes(t1.getPoints(), t2.getPoints());
    Optional<Polygon.Point> p;
    if (axes.size() > 100) {
      p = axes.parallelStream()
          .filter(x -> isSeparatingAxis(x, t1.getPoints(), t2.getPoints())).findAny();
    } else {
      for (Polygon.Point point : axes) {
        if (isSeparatingAxis(point, t1.getPoints(), t2.getPoints())) {
          return false;
        }
      }
      return true;
    }
    return p.isEmpty();
  }

  /**
   * Check collision between two polygons defined by two sets of points.
   *
   * @param polygon1 set of points defining the first polygon
   * @param polygon2 set of points defining the second polygon
   * @return true if the two polygons collide
   */
  public static Vector2f checkCollisionDelaunay(Set<Polygon.Point> polygon1, Set<Polygon.Point> polygon2) {
    List<Triangle> triangles1 = DelaunayTriangulation.delaunayTriangulation(polygon1);
    List<Triangle> triangles2 = DelaunayTriangulation.delaunayTriangulation(polygon2);

    for (Triangle t : triangles1) {
      for (Triangle t2 : triangles2) {
        if (checkCollision(t, t2)) {
          return calculateCollisionNormal(t, t2);
        }
      }
    }
    return null;
  }

  private static List<Polygon.Point> getAxes(Set<Polygon.Point> polygon1, Set<Polygon.Point> polygon2) {
    List<Polygon.Point> axes = new CopyOnWriteArrayList<>();
    axes.addAll(getEdgesAsAxes(polygon1));
    axes.addAll(getEdgesAsAxes(polygon2));
    return axes;
  }

  @SuppressWarnings("all")
  private static List<Polygon.Point> getEdgesAsAxes(Set<Polygon.Point> polygon) {
    List<Polygon.Point> axes = new CopyOnWriteArrayList<>();
    List<Polygon.Point> points = new CopyOnWriteArrayList<>(polygon);
    int numPoints = points.size();
    if (numPoints > 10000) {
      IntStream.range(0, numPoints).parallel().forEach(i -> {
        Polygon.Point p1 = points.get(i);
        Polygon.Point p2 = points.get((i + 1) % numPoints);
        double edgeX = p2.x() - p1.x();
        double edgeY = p2.y() - p1.y();
        axes.add(new Polygon.Point((float) edgeY, (float) -edgeX));
      });
    } else {
      for (int i = 0; i < numPoints; i++) {
        Polygon.Point p1 = points.get(i);
        Polygon.Point p2 = points.get((i + 1) % numPoints);
        double edgeX = p2.x() - p1.x();
        double edgeY = p2.y() - p1.y();
        axes.add(new Polygon.Point((float) edgeY, (float) -edgeX));
      }
    }
    return axes;
  }

  private static boolean isSeparatingAxis(Polygon.Point axis, Set<Polygon.Point> polygon1,
                                          Set<Polygon.Point> polygon2) {
    Projection p1 = projectPolygonOntoAxis(axis, polygon1);
    Projection p2 = projectPolygonOntoAxis(axis, polygon2);
    return p1.max < p2.min || p2.max < p1.min;
  }

  private static Projection projectPolygonOntoAxis(Polygon.Point axis, Set<Polygon.Point> polygon) {
    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;
    for (Polygon.Point point : polygon) {
      double projection = (axis.x() * point.x()
          + axis.y() * point.y()) / (axis.x() * axis.x() + axis.y() * axis.y());
      min = Math.min(min, projection);
      max = Math.max(max, projection);
    }
    return new Projection(min, max);
  }

  /**
   * Convert a rectangle into it's four points.
   *
   * @param rect the rectangle to be converted
   * @return a list of all four corner points
   */
  public static List<Polygon.Point> calculateRectanglePoints(Rectangle rect) {
    List<Polygon.Point> points = new ArrayList<>();
    int x = rect.x;
    int y = rect.y;
    int width = rect.width;
    int height = rect.height;
    points.add(new Polygon.Point(x, y));
    points.add(new Polygon.Point((float) x + width, y));
    points.add(new Polygon.Point((float) x + width,(float) y + height));
    points.add(new Polygon.Point(x, (float) y + height));
    return points;
  }

  /**
   * Calculates the ellipse points given an ellipse and the number of desired segments.
   * So given any ellipse, we construct a n sided polygon where n is the desired number
   * of line segments.
   * E.g. n = 8 would result in an octagon. Whereas n = 5 a pentagon
   *
   * @param ellipse  the original ellipse
   * @param segments the desired number of line segments
   * @return the set of points representing the final polygon
   */
  public static Set<Polygon.Point> calculateEllipsePoints(Ellipse2D ellipse, int segments) {
    double centerX = ellipse.getCenterX();
    double centerY = ellipse.getCenterY();
    double width = ellipse.getWidth();
    double height = ellipse.getHeight();
    if (segments <= 5) {
      segments = 5;
    }
    Set<Polygon.Point> points = new LinkedHashSet<>();
    for (double angle = 0.0; angle < 360.0; angle += (double) 360 / segments) {
      double radians = Math.toRadians(angle);
      double x = centerX + width * Math.cos(radians) / 2;
      double y = centerY + height * Math.sin(radians) / 2;
      points.add(new Polygon.Point((float) x, (float) y));
    }
    return points;
  }

  private static Vector2f calculateCollisionNormal(Triangle t1, Triangle t2) {
    // Find the edge of t1 that is intersecting with t2
    Edge intersectionEdge = findIntersectionEdge(t1, t2);
    if (intersectionEdge != null) {
      // Calculate the collision normal as the perpendicular vector to the edge
      double nx = -(intersectionEdge.getB().y() - intersectionEdge.getA().y());
      double ny = intersectionEdge.getB().x() - intersectionEdge.getA().x();

      // Normalize the collision normal
      double length = Math.sqrt(nx * nx + ny * ny);
      if (length > 0) {
        nx /= length;
        ny /= length;
      }

      return new Vector2f((float) nx, (float) ny);
    } else {
      return null;
    }
  }

  private static double calculatePenetrationDepth(Triangle t1, Triangle t2,
                                                  Vector2f collisionNormal) {
    // Find the edge of t1 that is intersecting with t2
    Edge intersectionEdge = findIntersectionEdge(t1, t2);

    if (intersectionEdge != null) {

      // Project the vertices of t1 onto the collision normal to find the minimum overlap
      double minOverlap = Double.POSITIVE_INFINITY;
      for (Polygon.Point point : t1.getPoints()) {
        double projection = collisionNormal.x * point.x() + collisionNormal.y
            * point.y();
        double overlap = projection - intersectionEdge.projectPoint(point);
        minOverlap = Math.min(minOverlap, overlap);
      }

      // Project the vertices of t2 onto the collision normal to find the minimum overlap
      for (Polygon.Point point : t2.getPoints()) {
        double projection = collisionNormal.x * point.x() + collisionNormal.y
            * point.y();
        double overlap = intersectionEdge.projectPoint(point) - projection;
        minOverlap = Math.min(minOverlap, overlap);
      }

      return minOverlap;
    } else {
      return 0.0;
    }
  }

  private static Edge findIntersectionEdge(Triangle t1, Triangle t2) {
    for (Edge edge1 : t1.getEdges()) {
      for (Edge edge2 : t2.getEdges()) {
        if (edge1.intersects(edge2)) {
          // If the edges intersect, return the edge with the smallest overlap
          double overlap1 = calculateEdgeOverlap(edge1, t1, t2);
          double overlap2 = calculateEdgeOverlap(edge2, t1, t2);

          if (Math.abs(overlap1) < Math.abs(overlap2)) {
            return edge1;
          } else {
            return edge2;
          }
        }
      }
    }
    // If no intersection edge is found.
    // For example, return an arbitrary edge, throw an exception, or return null.
    // In the context of your physics simulation, you may want to handle this differently.
    return null;
  }

  private static double calculateEdgeOverlap(Edge edge, Triangle t1, Triangle t2) {
    Polygon.Point normal = edge.getNormal(); // Get the normal vector of the edge
    double minProjection1 = Double.POSITIVE_INFINITY;
    double maxProjection1 = Double.NEGATIVE_INFINITY;
    double minProjection2 = Double.POSITIVE_INFINITY;
    double maxProjection2 = Double.NEGATIVE_INFINITY;

    // Project the vertices of triangle 1 onto the edge's normal vector
    for (Polygon.Point vertex : t1.getPoints()) {
      double projection = LinearMath.dotProduct(normal,
          new Polygon.Point(vertex.x() - edge.getA().x(),
              vertex.y() - edge.getA().y()));
      minProjection1 = Math.min(minProjection1, projection);
      maxProjection1 = Math.max(maxProjection1, projection);
    }

    // Project the vertices of triangle 2 onto the edge's normal vector
    for (Polygon.Point vertex : t2.getPoints()) {
      double projection = LinearMath.dotProduct(normal,
          new Polygon.Point(vertex.x() - edge.getA().x(),
              vertex.y() - edge.getA().y()));
      minProjection2 = Math.min(minProjection2, projection);
      maxProjection2 = Math.max(maxProjection2, projection);
    }

    // Calculate the overlap as the difference between the maximum and minimum projections
    return Math.min(maxProjection1, maxProjection2) - Math.max(minProjection1, minProjection2);
  }

  static class Projection {

    double min;
    double max;

    Projection(double min, double max) {
      this.min = min;
      this.max = max;
    }
  }
}