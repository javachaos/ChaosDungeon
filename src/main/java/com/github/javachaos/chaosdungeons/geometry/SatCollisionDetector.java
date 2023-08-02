package com.github.javachaos.chaosdungeons.geometry;

import static com.github.javachaos.chaosdungeons.geometry.GenerationUtils.generateNonRegularPolygon;

import com.github.javachaos.chaosdungeons.geometry.polygons.Triangle;
import com.github.javachaos.chaosdungeons.gui.ShapeDrawer;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;
import javax.swing.SwingUtilities;

/**
 * Separating Axis Theorem collision detector.
 */
public class SatCollisionDetector {

  //TODO finish cleaning up the style issues.

  /**
   * Main entry point.
   *
   * @param args the args
   */
  public static void main(String[] args) {

    int[] xpoints = {0, 315, 115, 299, 47, 55, 26, 0};
    int[] ypoints = {0, 53, 115, 234, 176, 271, 383, 0};
    //List<Point2D> pentapoints = new ArrayList<>();
    List<Point2D> pts = generateNonRegularPolygon(100, 100, 120, 100, 100);
    Polygon concavePentagon = new Polygon(xpoints, ypoints, 5);
    //    for (int i = 0; i < concavePentagon.npoints; i++) {
    //      pentapoints.add(new Point2D.Double(xpoints[i], ypoints[i]));
    //    }

    // Example usage
    Rectangle redRect = new Rectangle(22, 200, 150, 200);
    Rectangle blueRect = new Rectangle(466, 88, 80, 65);
    Ellipse2D ellipse2D = new Ellipse2D.Double();
    ellipse2D.setFrame(redRect);

    // you can change 10 here to draw more segments and see how the runtime scales.
    //Set<Point2D> red = calculateEllipsePoints(ellipse2D, 6);
    List<Point2D> blue = calculateRectanglePoints(blueRect);
    List<Point2D> red = calculateRectanglePoints(redRect);

    SwingUtilities.invokeLater(() -> new ShapeDrawer(red));
    long start = System.nanoTime();
    //boolean colliding = checkCollisionDelaunay(pentapoints, blue);
    boolean colliding = checkCollisionDelaunay(pts, blue);
    long end = System.nanoTime();
    System.out.println("Colliding: " + colliding);
    System.out.println("Runtime: " + (end - start) / 1000000.0 + " ms");
  }

  public static boolean checkCollision(Set<Point2D> polygon1, Set<Point2D> polygon2) {
    List<Point2D> axes = getAxes(polygon1, polygon2);
    Optional<Point2D> p;
    if (axes.size() > 100) {
      p = axes.parallelStream().filter(x -> isSeparatingAxis(x, polygon1, polygon2)).findAny();
    } else {
      for (Point2D point : axes) {
        if (isSeparatingAxis(point, polygon1, polygon2)) {
          return false;
        }
      }
      return true;
    }
    return p.isEmpty();
  }

  public static boolean checkCollisionDelaunay(List<Point2D> polygon1, List<Point2D> polygon2) {
    List<Triangle> triangles1 = DelaunayTriangulation.delaunayTriangulation(polygon1);
    List<Triangle> triangles2 = DelaunayTriangulation.delaunayTriangulation(polygon2);

    for (Triangle t : triangles1) {
      for (Triangle t2 : triangles2) {
        if (checkCollision(t, t2)) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean checkCollision(Triangle t1, Triangle t2) {
    return (t2.contains(t1.getA()) || t2.contains(t1.getB()) || t2.contains(t1.getC())) ||
        (t1.contains(t2.getA()) || t1.contains(t2.getB()) || t1.contains(t2.getC()));
  }

  private static List<Point2D> getAxes(Set<Point2D> polygon1, Set<Point2D> polygon2) {
    List<Point2D> axes = new CopyOnWriteArrayList<>();
    axes.addAll(getEdgesAsAxes(polygon1));
    axes.addAll(getEdgesAsAxes(polygon2));
    return axes;
  }

  private static List<Point2D> getEdgesAsAxes(Set<Point2D> polygon) {
    List<Point2D> axes = new CopyOnWriteArrayList<>();
    List<Point2D> points = new CopyOnWriteArrayList<>(polygon);
    int numPoints = points.size();
    if (numPoints > 10000) {
      IntStream.range(0, numPoints).parallel().forEach(i -> {
        Point2D p1 = points.get(i);
        Point2D p2 = points.get((i + 1) % numPoints);
        double edgeX = p2.getX() - p1.getX();
        double edgeY = p2.getY() - p1.getY();
        axes.add(new Point2D.Double(edgeY, -edgeX));
      });
    } else {
      for (int i = 0; i < numPoints; i++) {
        Point2D p1 = points.get(i);
        Point2D p2 = points.get((i + 1) % numPoints);
        double edgeX = p2.getX() - p1.getX();
        double edgeY = p2.getY() - p1.getY();
        axes.add(new Point2D.Double(edgeY, -edgeX));
      }
    }
    return axes;
  }

  private static boolean isSeparatingAxis(Point2D axis, Set<Point2D> polygon1,
                                          Set<Point2D> polygon2) {
    Projection p1 = projectPolygonOntoAxis(axis, polygon1);
    Projection p2 = projectPolygonOntoAxis(axis, polygon2);
    return p1.max < p2.min || p2.max < p1.min;
  }

  private static Projection projectPolygonOntoAxis(Point2D axis, Set<Point2D> polygon) {
    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;
    for (Point2D point : polygon) {
      double projection = (axis.getX() * point.getX()
          + axis.getY() * point.getY()) / (axis.getX() * axis.getX() + axis.getY() * axis.getY());
      min = Math.min(min, projection);
      max = Math.max(max, projection);
    }
    return new Projection(min, max);
  }

  public static List<Point2D> calculateRectanglePoints(Rectangle rect) {
    List<Point2D> points = new ArrayList<>();
    int x = rect.x;
    int y = rect.y;
    int width = rect.width;
    int height = rect.height;
    points.add(new Point2D.Double(x, y));
    points.add(new Point2D.Double((double) x + width, y));
    points.add(new Point2D.Double((double) x + width, (double) y + height));
    points.add(new Point2D.Double(x, (double) y + height));
    return points;
  }

  public static Set<Point2D> calculateEllipsePoints(Ellipse2D ellipse, int segments) {
    double centerX = ellipse.getCenterX();
    double centerY = ellipse.getCenterY();
    double width = ellipse.getWidth();
    double height = ellipse.getHeight();
    if (segments <= 5) {
      segments = 5;
    }
    Set<Point2D> points = new LinkedHashSet<>();
    for (double angle = 0.0; angle < 360.0; angle += (double) 360 / segments) {
      double radians = Math.toRadians(angle);
      double x = centerX + width * Math.cos(radians) / 2;
      double y = centerY + height * Math.sin(radians) / 2;
      points.add(new Point2D.Double(x, y));
    }
    return points;
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