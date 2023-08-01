package com.github.javachaos.chaosdungeons.geometry;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

import static com.github.javachaos.chaosdungeons.geometry.Edge.checkIntersection;

public class GenerationUtils {

    private static double calculateAngle(Point2D p) {
        return Math.atan2(p.getY(), p.getX());
    }

    public static List<Point2D> generateNonRegularPolygon(int xPos, int yPos, int numVertices, double maxX, double maxY) {
        List<Point2D> vertices = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {
            vertices.add(getRandomPoint2D(xPos, yPos, maxX, maxY));
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
                    return generateNonRegularPolygon(xPos, yPos, numVertices, maxX, maxY);
                }
            }
        }

        return vertices;
    }

    public static Point2D getRandomPoint2D(double xPos, double yPos,
                                            double maxX, double maxY) {
        Random random = new Random();
        double x = random.nextDouble() * 2 * maxX - maxX + xPos;
        double y = random.nextDouble() * 2 * maxY - maxY + yPos;
        return new Point2D.Double(x, y);
    }
}
