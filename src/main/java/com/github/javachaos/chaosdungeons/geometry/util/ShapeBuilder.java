package com.github.javachaos.chaosdungeons.geometry.util;

import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.joml.Vector2f;

public class ShapeBuilder {
  public static class Rectangle {
    private double width;
    private double height;
    private Vector2f pos;

    public Rectangle() {
      this.width = 1;
      this.height = 1;
      this.pos = new Vector2f();
    }

    public Rectangle setPosition(Vector2f pos) {
      this.pos = pos;
      return this;
    }

    public Rectangle setPosition(float x, float y) {
      this.pos = new Vector2f(x, y);
      return this;
    }

    public Rectangle setWidth(double w) {
      this.width = w;
      return this;
    }

    public Rectangle setHeight(double h) {
      this.height = h;
      return this;
    }

    public Vertex build() {
      Point2D.Double p0 = new Point2D.Double(pos.x, pos.y);
      Point2D.Double p1 = new Point2D.Double(pos.x + width, pos.y);
      Point2D.Double p2 = new Point2D.Double(pos.x + width, pos.y + height);
      Point2D.Double p3 = new Point2D.Double(pos.x, pos.y + height);
      return new Vertex(List.of(p0, p1, p2, p3));
    }
  }

  public static class Triangle {
    private Vector2f pos;
    private Vector2f p0;
    private Vector2f p1;
    private Vector2f p2;

    public Triangle() {
      this.pos = new Vector2f();
      this.p0 = new Vector2f(.5f, 1f);
      this.p1 = new Vector2f(1, 0);
      this.p0 = new Vector2f();
    }

    public Triangle setPosition(Vector2f pos) {
      this.pos = pos;
      return this;
    }

    public Triangle setPosition(float x, float y) {
      this.pos = new Vector2f(x, y);
      return this;
    }

    public Triangle setP0(Vector2f p0) {
      this.p0 = p0;
      return this;
    }

    public Triangle setP1(Vector2f p1) {
      this.p1 = p1;
      return this;
    }

    public Triangle setP2(Vector2f p2) {
      this.p2 = p2;
      return this;
    }

    public Vertex build() {
      Point2D.Double p00 = new Point2D.Double(pos.x + p0.x, pos.y + p0.y);
      Point2D.Double p01 = new Point2D.Double(pos.x + p1.x, pos.y + p1.y);
      Point2D.Double p02 = new Point2D.Double(pos.x + p2.x, pos.y + p2.y);
      return new Vertex(List.of(p00, p01, p02));
    }
  }

  public static class Circle {
    private Vector2f pos;
    private int numPoints;
    private double radius;

    public Circle() {
      this.numPoints = 50;
      this.radius = 1;
      this.pos = new Vector2f();
    }

    private static Set<Point2D> calculateEllipsePoints(Ellipse2D ellipse, int segments) {
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

    public Circle setPosition(Vector2f pos) {
      this.pos = pos;
      return this;
    }

    public Circle setPosition(float x, float y) {
      this.pos = new Vector2f(x, y);
      return this;
    }

    public Circle setNumPoints(int numPoints) {
      this.numPoints = numPoints;
      return this;
    }

    public Circle setRadius(double r) {
      this.radius = r;
      return this;
    }

    public Vertex build() {
      return new Vertex(new ArrayList<>(
          calculateEllipsePoints(
              new Ellipse2D.Double(pos.x, pos.y, radius * 2, radius * 2), numPoints)));
    }

  }
}
