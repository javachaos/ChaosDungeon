package com.github.javachaos.chaosdungeons.geometry;

import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.exceptions.GeneralGameException;
import com.github.javachaos.chaosdungeons.geometry.math.LinearMath;

import java.util.*;

/**
 * Helper class to generate things.
 */
public class GenerationUtils {
  private static final Random random = new Random();

  private GenerationUtils() {
    //Unused
  }

  private static double calculateAngle(Polygon.Point p) {
    return Math.atan2(p.x(), p.y());
  }

//  /**
//   * Generate a Non-regular polygon as a list of points (clockwise orientation).
//   *
//   * @param xpos the x starting pos
//   * @param ypos the y starting pos
//   * @param numVertices the number of verticies in the polygon
//   * @param maxX the maximum width of the polygon
//   * @param maxY the maximum height of the polygon
//   * @return a new random non-regular polygon
//   */
//  public static Set<Polygon.Point> generateNonRegularPolygon(int xpos, int ypos, int numVertices,
//                                                             double maxX, double maxY) {
//    List<Polygon.Point> vertices = new ArrayList<>();
//
//    for (int i = 0; i < numVertices; i++) {
//      vertices.add(getRandomPoint2D(xpos, ypos, maxX, maxY));
//    }
//
//    Polygon.Point center = new Polygon.Point(0, 0);
//    for (Polygon.Point vertex : vertices) {
//      center.x += (int) vertex.x();
//      center.y += (int) vertex.y();
//    }
//    center.x /= numVertices;
//    center.y /= numVertices;
//
//    vertices.sort(Comparator.comparingDouble(p -> calculateAngle(p) - calculateAngle(center)));
//
//    for (int i = 0; i < numVertices; i++) {
//      Polygon.Point p1 = vertices.get(i);
//      Polygon.Point p2 = vertices.get((i + 1) % numVertices);
//      for (int j = i + 2; j < i + numVertices - 1; j++) {
//        Polygon.Point p3 = vertices.get(j % numVertices);
//        Polygon.Point p4 = vertices.get((j + 1) % numVertices);
//        if (LinearMath.checkIntersection(p1, p2, p3, p4)) {
//          return generateNonRegularPolygon(xpos, ypos, numVertices, maxX, maxY);
//        }
//      }
//    }
//
//    return new LinkedHashSet<>(vertices);
//  }


  public static Set<Polygon.Point> generateNonRegularPolygon(int xpos, int ypos, int numVertices,
                                                                      double maxX, double maxY) {
    final int MAX_ATTEMPTS = 100;
    int attemptCount = 0;
    while (attemptCount < MAX_ATTEMPTS) {
      List<Polygon.Point> vertices = new ArrayList<>();

      for (int i = 0; i < numVertices; i++) {
        vertices.add(getRandomPoint2D(xpos, ypos, maxX, maxY));
      }

      Polygon.Point center = new Polygon.Point(0, 0);
      for (Polygon.Point vertex : vertices) {
        center.x += (int) vertex.x();
        center.y += (int) vertex.y();
      }
      center.x /= numVertices;
      center.y /= numVertices;

      vertices.sort(Comparator.comparingDouble(p -> calculateAngle(p) - calculateAngle(center)));

      boolean intersectionFound = false;
      for (int i = 0; i < numVertices; i++) {
        Polygon.Point p1 = vertices.get(i);
        Polygon.Point p2 = vertices.get((i + 1) % numVertices);
        for (int j = i + 2; j < i + numVertices - 1; j++) {
          Polygon.Point p3 = vertices.get(j % numVertices);
          Polygon.Point p4 = vertices.get((j + 1) % numVertices);
          if (LinearMath.checkIntersection(p1, p2, p3, p4)) {
            intersectionFound = true;
            break;
          }
        }
        if (intersectionFound) {
          break;
        }
      }

      if (!intersectionFound) {
        return new LinkedHashSet<>(vertices);
      }
      attemptCount++;
    }
    throw new GeneralGameException("Count not generate polygon in under 100 attempts with given input.");
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
  public static Polygon.Point getRandomPoint2D(double xpos, double ypos,
                                         double maxX, double maxY) {
    double x = random.nextDouble() * 2 * maxX - maxX + xpos;
    double y = random.nextDouble() * 2 * maxY - maxY + ypos;
    return new Polygon.Point((float) x, (float) y);
  }
}
