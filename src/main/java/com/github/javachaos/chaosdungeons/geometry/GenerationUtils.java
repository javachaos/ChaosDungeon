package com.github.javachaos.chaosdungeons.geometry;

import static com.github.javachaos.chaosdungeons.geometry.polygons.Edge.checkIntersection;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Helper class to generate things.
 */
public class GenerationUtils {

  private static double calculateAngle(Point2D p) {
    return Math.atan2(p.getY(), p.getX());
  }

  /**
   * Generate a Non-regular polygon as a list of points (clockwise orientation).
   *
   * @param xpos the x starting pos
   * @param ypos the y starting pos
   * @param numVertices the number of verticies in the polygon
   * @param maxX the maximum width of the polygon
   * @param maxY the maximum height of the polygon
   * @return a new random non-regular polygon
   */
  public static List<Point2D> generateNonRegularPolygon(int xpos, int ypos, int numVertices,
                                                        double maxX, double maxY) {
    List<Point2D> vertices = new ArrayList<>();

    for (int i = 0; i < numVertices; i++) {
      vertices.add(getRandomPoint2D(xpos, ypos, maxX, maxY));
    }

    Point center = new Point(0, 0);
    for (Point2D vertex : vertices) {
      center.x += (int) vertex.getX();
      center.y += (int) vertex.getY();
    }
    center.x /= numVertices;
    center.y /= numVertices;

    vertices.sort(Comparator.comparingDouble(p -> calculateAngle(p) - calculateAngle(center)));

    for (int i = 0; i < numVertices; i++) {
      Point2D p1 = vertices.get(i);
      Point2D p2 = vertices.get((i + 1) % numVertices);
      for (int j = i + 2; j < i + numVertices - 1; j++) {
        Point2D p3 = vertices.get(j % numVertices);
        Point2D p4 = vertices.get((j + 1) % numVertices);
        if (checkIntersection(p1, p2, p3, p4)) {
          return generateNonRegularPolygon(xpos, ypos, numVertices, maxX, maxY);
        }
      }
    }

    return vertices;
  }

  /**
   * Generates a random point between (xPos, yPos) and (maxX, maxY).
   *
   * @param xpos the starting position in the x-direction
   * @param ypos the starting position in the y-direction
   * @param maxX the end position in the x-direction
   * @param maxY the end position in the y-direction
   * @return a new random point (x,y) as (xPos+x, yPos+y)
   */
  public static Point2D getRandomPoint2D(double xpos, double ypos,
                                         double maxX, double maxY) {
    Random random = new Random();
    double x = random.nextDouble() * 2 * maxX - maxX + xpos;
    double y = random.nextDouble() * 2 * maxY - maxY + ypos;
    return new Point2D.Double(x, y);
  }
}
