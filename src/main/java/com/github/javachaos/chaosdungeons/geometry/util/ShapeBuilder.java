package com.github.javachaos.chaosdungeons.geometry.util;

import com.github.javachaos.chaosdungeons.collision.Polygon;

import java.awt.geom.Ellipse2D;
import java.util.LinkedHashSet;
import java.util.Set;

import org.joml.Vector2f;

public class ShapeBuilder {

  private ShapeBuilder() {
    //Unused
  }

  public static class Random {
    private Vector2f pos;

    public Random setPosition(Vector2f pos) {
      this.pos = pos;
      return this;
    }

    public Random setPosition(float x, float y) {
      return setPosition(new Vector2f(x, y));
    }

    public Polygon build() {
      java.util.Random rand = new java.util.Random(System.nanoTime());
      int r = rand.nextInt(10);
      return switch (r) {
          case 1 -> new Rectangle().setPosition(pos).setHeight(rand.nextFloat() * 5.0f).setWidth(rand.nextFloat() * 5.0f).build();
          case 2 -> new Triangle().setPosition(pos).setP0(new Vector2f(rand.nextFloat() * 5f, rand.nextFloat() * 5f))
                  .setP1(new Vector2f(rand.nextFloat() * 5f, rand.nextFloat() * 5f))
                  .setP2(new Vector2f(rand.nextFloat() * 5f, rand.nextFloat() * 5f)).build();
          default -> new Circle().setPosition(pos).setNumPoints(r).setRadius(rand.nextFloat() * 5f).build();
      };
    }
  }
  public static class Rectangle {
    private float width;
    private float height;
    private Vector2f pos;

    public Rectangle() {
      this.width = 1;
      this.height = 1;
      this.pos = new Vector2f();
    }

    public Rectangle setPosition(Vector2f pos) {
      this.pos = new Vector2f(pos);
      return this;
    }

    public Rectangle setPosition(float x, float y) {
      this.pos = new Vector2f(x, y);
      return this;
    }

    public Rectangle setWidth(float w) {
      this.width = w;
      return this;
    }

    public Rectangle setHeight(float h) {
      this.height = h;
      return this;
    }

    public Polygon build() {
      Polygon.Point p0 = new Polygon.Point(pos.x, pos.y);
      Polygon.Point p1 = new Polygon.Point(pos.x + width, pos.y);
      Polygon.Point p2 = new Polygon.Point(pos.x + width, pos.y + height);
      Polygon.Point p3 = new Polygon.Point(pos.x, pos.y + height);
      return new Polygon(Set.of(p0, p1, p2, p3));
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

    public Polygon build() {
      Polygon.Point p00 = new Polygon.Point(pos.x + p0.x, pos.y + p0.y);
      Polygon.Point p01 = new Polygon.Point(pos.x + p1.x, pos.y + p1.y);
      Polygon.Point p02 = new Polygon.Point(pos.x + p2.x, pos.y + p2.y);
      return new Polygon(Set.of(p00, p01, p02));
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

    private static Set<Polygon.Point> calculateEllipsePoints(Ellipse2D ellipse, int segments) {
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
        float x = (float) (centerX + width * Math.cos(radians) / 2f);
        float y = (float) (centerY + height * Math.sin(radians) / 2f);
        points.add(new Polygon.Point(x, y));
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

    public Polygon build() {
      return new Polygon(calculateEllipsePoints(
              new Ellipse2D.Double(pos.x, pos.y, radius * 2, radius * 2), numPoints));
    }

  }
}
