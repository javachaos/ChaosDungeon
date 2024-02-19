//package com.github.javachaos.chaosdungeons.geometry;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import com.github.javachaos.chaosdungeons.geometry.math.Hull;
//import com.github.javachaos.chaosdungeons.geometry.math.LinearMath;
//import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;
//import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
//import java.awt.geom.Point2D;
//import java.security.SecureRandom;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.RepetitionInfo;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInfo;
//import org.junit.jupiter.api.Timeout;
//
///**
// * Test class for Vertex.java
// */
//public class TestVertex {
//
//  private static final Logger LOGGER = LogManager.getLogger(TestVertex.class);
//
//  private static Random r;
//
//  private static List<Point2D> points;
//
//  private final Stream<Point2D> pointStream = Stream.generate(
//      () -> GenerationUtils.getRandomPoint2D(
//          0, 0,
//          100, 100));
//
//  /**
//   * Setup random, and point list, to run before each test.
//   */
//  @BeforeEach
//  public void setupUtils(TestInfo ti) {
//    r = new SecureRandom();
//    points = pointStream.limit(r.nextInt(15) + 3).collect(Collectors.toList());
//    LOGGER.debug("------------------ {} Begin --------------------", ti.getDisplayName());
//  }
//
//  @AfterEach
//  public void tearDownUtils(TestInfo ti) {
//    LOGGER.debug("------------------ {} End --------------------", ti.getDisplayName());
//  }
//
//  @Test
//  void testHasEdge() {
//    Edge e = new Edge(new Point2D.Double(0, 0), new Point2D.Double(0, 1));
//    Edge e1 = new Edge(new Point2D.Double(0, 0), new Point2D.Double(1, 0));
//    Edge e2 = new Edge(new Point2D.Double(1, 1), new Point2D.Double(0, 0));
//    List<Point2D> pts = List.of(
//        new Point2D.Double(0, 0),
//        new Point2D.Double(1, 0),
//        new Point2D.Double(0, 1),
//        new Point2D.Double(1, 1));
//    Vertex v = new Vertex(pts);
//    assertFalse(v.has(e));
//    assertTrue(v.has(e1));
//    assertTrue(v.has(e2));
//  }
//
//  @Test
//  void testColinearPoints() {
//    // p4 = 90.0  .____. p3 = 206.56
//    //           |      \
//    // p0 = 90.0 |.__.__.\ p2 = 26.56
//    //           p1 = 180.0
//    List<Point2D> pts = List.of(
//        new Point2D.Double(0, 0),
//        new Point2D.Double(1, 0),
//        new Point2D.Double(2, 0),
//        new Point2D.Double(0, 1),
//        new Point2D.Double(1, 1));
//    Vertex quad = new Vertex(pts);
//    for (Point2D pt : pts) {
//      double angle = quad.find(pt).getAngle();
//      System.out.println(pt + " Angle: " + angle);
//    }
//    assertEquals(pts, quad.getPoints());
//  }
//
//  @Test
//  void testGetPoints() {
//    List<Point2D> pts = List.of(
//        new Point2D.Double(0, 0),
//        new Point2D.Double(1, 0),
//        new Point2D.Double(0, 1),
//        new Point2D.Double(1, 1));
//    Vertex square = new Vertex(List.of(
//        new Point2D.Double(0, 0),
//        new Point2D.Double(1, 0),
//        new Point2D.Double(0, 1),
//        new Point2D.Double(1, 1)));
//    assertEquals(pts, square.getPoints());
//  }
//
//  @Test
//  @SuppressWarnings("all")
//  void testEdgeCases() {
//    Point2D nullPoint = null;
//    assertThrows(NullPointerException.class, () -> new Vertex(nullPoint));
//    List<Point2D> nullList = null;
//    assertThrows(NullPointerException.class, () -> new Vertex(nullList));
//    assertThrows(IllegalArgumentException.class, () -> new Vertex(List.of()));
//    Vertex v = new Vertex(0, 0);
//    v.setNext(null);
//    v.setPrevious(new Vertex(1, 1));
//    Vertex finalV = v;
//    assertThrows(IllegalArgumentException.class, finalV::calculateAngle);
//    v = new Vertex(0, 0);
//    v.setNext(new Vertex(1, 1));
//    v.setPrevious(null);
//    Vertex finalV1 = v;
//    assertThrows(IllegalArgumentException.class, finalV1::calculateAngle);
//  }
//
//  @Test
//  void testGetEdges() {
//    for (int i = 3; i < 256; i++) {
//      List<Point2D> p = GenerationUtils.generateNonRegularPolygon(0, 0, i, 10000, 10000);
//      List<Point2D> hull = Hull.convexHullPoints(p);
//      if (hull.size() > 2) {
//        Vertex hullPoly = new Vertex(hull);
//        double totalAngle = 0.0;
//        Vertex current = hullPoly.getNext();
//        while (current != hullPoly) {
//          totalAngle += current.getAngle();
//          current = current.getNext();
//        }
//        totalAngle += current.getAngle();
//
//        Vertex poly = new Vertex(p);
//        double angle = poly.calculateAngle();
//        if (angle < 180.0) {
//          assertTrue(poly.isAcute());
//        }
//        List<Edge> edges = poly.getEdges();
//        assertEquals(i, edges.size());
//        System.out.println(
//            "Total interior angles for polygon with " + hull.size() + " sides = " + totalAngle);
//        assertTrue(LinearMath.epsEquals((hull.size() - 2.0) * 180.0, totalAngle));
//      }
//    }
//  }
//
//
//  @RepeatedTest(10)
//  void testCalculateAngleTimes() {
//    List<Point2D> p = GenerationUtils.generateNonRegularPolygon(0, 0, 4, 10000, 10000);
//    List<Point2D> hull = Hull.convexHullPoints(p);
//    if (hull.size() > 2) {
//      Vertex hullPoly = new Vertex(hull);
//      double totalAngle = 0.0;
//      Vertex current = hullPoly.getNext();
//      while (current != hullPoly) {
//        totalAngle += current.getAngle();
//        current = current.getNext();
//      }
//      totalAngle += current.getAngle();
//
//      Vertex poly = new Vertex(p);
//      List<Edge> edges = poly.getEdges();
//      assertEquals(4, edges.size());
//      assertEquals((hull.size() - 2.0) * 180.0, totalAngle, 1.0e-6);
//    }
//  }
//
//  @RepeatedTest(10)
//  void testRegressCalculateAngleTimes(RepetitionInfo repetitionInfo) {
//    int i = repetitionInfo.getCurrentRepetition() + 2;
//    ArrayList<Point2D> p = (ArrayList<Point2D>) GenerationUtils.generateNonRegularPolygon(
//        128, 128, i, 128, 128);
//
//    Vertex poly = new Vertex(p);
//    double totalAngle = 0.0;
//    Vertex current = poly.getNext();
//    while (current != poly) {
//      totalAngle += current.getAngle();
//      current = current.getNext();
//    }
//    totalAngle += current.getAngle();
//    List<Edge> edges = poly.getEdges();
//    assertEquals(i, edges.size());
//    System.out.println("Total interior angles for polygon with "
//        + p.size() + " sides = " + totalAngle);
//  }
//
//  @Test
//  void testCalculateAngle() {
//    List<Point2D> t = List.of(
//        new Point2D.Double(0, 0),
//        new Point2D.Double(1.0, 0),
//        new Point2D.Double(0.5, 1.0));
//
//    Vertex poly = new Vertex(t);
//    Vertex b = poly.getNext();
//    Vertex c = poly.getPrevious();
//
//    double theta1 = 63.434948;
//    double theta2 = 63.434948;
//    double theta3 = 53.130102;
//
//    assertEquals(poly.getAngle(), theta1, 1.0e-6);
//    assertEquals(b.getAngle(), theta2, 1.0e-6);
//    assertEquals(c.getAngle(), theta3, 1.0e-6);
//  }
//
//  @Test
//  void testSquareAngles() {
//    List<Point2D> s = List.of(
//        new Point2D.Double(0.0000000000001, 0.000000000001),
//        new Point2D.Double(0.000000000001, 1.0),
//        new Point2D.Double(1.0, 0.000000000001),
//        new Point2D.Double(1.0, 1.0));
//
//    Vertex poly = new Vertex(s);
//    Vertex b = poly.getNext();
//    Vertex c = poly.getNext().getNext();
//    Vertex d = poly.getNext().getNext().getNext();
//
//    double theta1 = poly.calculateAngle();
//    double theta2 = b.calculateAngle();
//    double theta3 = c.calculateAngle();
//    double theta4 = d.calculateAngle();
//
//    assertEquals(360.000, theta1 + theta2 + theta3 + theta4, 1.0e-6);
//  }
//
//  @Test
//  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
//  public void testSize() {
//    Vertex v = new Vertex(points);
//    assertEquals(points.size(), v.size());
//    v.print();
//  }
//
//  @Test
//  public void testRemoveEdgeCases() {
//    // Get a random set of points between [3, 99]
//    Vertex v = new Vertex(points);
//    assertThrows(IllegalArgumentException.class, () -> v.remove(-1));
//  }
//
//  /**
//   * Test vertex removal.
//   */
//  @RepeatedTest(4)
//  @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
//  public void testRemove() {
//    Vertex v = new Vertex(List.of(
//        new Point2D.Double(0, 0),
//        new Point2D.Double(0, 1),
//        new Point2D.Double(1, 1),
//        new Point2D.Double(2, 0),
//        new Point2D.Double(2, 1),
//        new Point2D.Double(3, 1)));
//    v.print();
//    v.remove(r.nextInt(v.size() - 1) + 1);
//    v.print();
//  }
//
//  @Test
//  public void testAdd() {
//    Vertex vert = new Vertex(0, 0);
//    for (int i = 2; i < 10; i++) {
//      vert.add(new Vertex(Math.random() * i, Math.random() * i));
//      assertEquals(i, vert.size());
//      vert.print();
//    }
//  }
//}
