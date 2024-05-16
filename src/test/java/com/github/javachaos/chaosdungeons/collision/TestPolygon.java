package com.github.javachaos.chaosdungeons.collision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;

class TestPolygon {

    private static final Polygon twoThousandPts = new Polygon(2048);
    private static final Polygon oneHunredPts = new Polygon(100);

    @BeforeAll
    static void initPoints() {
        float angleIncrement = (float) (2f * Math.PI / 2048f);
        float angleIncrement2 = (float) (2f * Math.PI / 100f);
        float r = 500.0f;
        float r2 = 45.0f;
        IntStream.range(0, 2048).forEach(i -> {
            float theta = i * angleIncrement;
            float x = (float) (300.0f + r * Math.cos(theta));
            float y = (float) (500.0f + r * Math.sin(theta));
            twoThousandPts.addPoint(new Polygon.Point(x, y));
            });
        IntStream.range(0, 100).forEach(i -> {
            float theta = i * angleIncrement2;
            float x = (float) (30.0f + r2 * Math.cos(theta));
            float y = (float) (50.0f + r2 * Math.sin(theta));
            oneHunredPts.addPoint(new Polygon.Point(x, y));
        });
    }

    @Tag("fast")
    @Test
    void testPolygon() {
        Polygon p = new Polygon(0);
        assertEquals(0, p.getSize());
    }

    @Tag("fast")
    @Test
    void testAddPoints() {
        Polygon p = new Polygon(4);

        assertEquals(new HashSet<>(), p.getPoints());
        assertEquals(0, p.getSize());
        p.addPoint(new Polygon.Point(1, 2));
        p.addPoint(new Polygon.Point(3, 4));
        p.addPoint(new Polygon.Point(4, 5));
        p.addPoint(new Polygon.Point(6, 7));
        LinkedHashSet<Polygon.Point> pts = new LinkedHashSet<>();
        pts.add(new Polygon.Point(1, 2));
        pts.add(new Polygon.Point(3, 4));
        pts.add(new Polygon.Point(4, 5));
        pts.add(new Polygon.Point(6, 7));
        Iterator<Polygon.Point> piter = pts.iterator();
        Iterator<Polygon.Point> oiter = p.getPoints().iterator();
        while(piter.hasNext() && oiter.hasNext()) {
            assertEquals(piter.next(), oiter.next());
        }
        assertEquals(4, p.getSize());

        p.remove(0);
        assertThrows(IllegalStateException.class, () -> p.addPoint(new Polygon.Point(3, 4)));
        p.addPoint(new Polygon.Point(1, 7));
        assertThrows(IllegalArgumentException.class, () -> p.addPoint(new Polygon.Point(4, 9)));

    }

    @Tag("fast")
    @Test
    void testGetCentroid() {
        Polygon.Point c0 = twoThousandPts.computeCentroid();
        assertTrue(new Polygon.Point(300.0f, 500.0f).equals(c0, 0.01f));
        Polygon.Point c1 = oneHunredPts.computeCentroid();
        assertTrue(new Polygon.Point(30f, 50f).equals(c1, 0.0001f));
    }


    @Tag("fast")
    @Test
    void testRemovePoint() {
        Polygon p = new Polygon(4);
        assertEquals(0, p.getSize());//[1][3][4][6]
        p.addPoint(new Polygon.Point(1, 2));  //[0][1][2][3]
        p.addPoint(new Polygon.Point(3, 4));
        p.addPoint(new Polygon.Point(4, 5));
        p.addPoint(new Polygon.Point(6, 7));

        Polygon.Point p0 = p.remove(0);
        Polygon.Point p1 = p.remove(1);
        Polygon.Point p2 = p.remove(2);
        Polygon.Point p3 = p.remove(3);
        assertEquals(new Polygon.Point(1, 2), p0);
        assertEquals(new Polygon.Point(3, 4), p1);
        assertEquals(new Polygon.Point(4, 5), p2);
        assertEquals(new Polygon.Point(6, 7), p3);
    }

    @Tag("fast")
    @Test
    void testTranslate() {
        Polygon poly = new Polygon(4);
        poly.addPoint(new Polygon.Point(5, 2));
        poly.addPoint(new Polygon.Point(7, 2));
        poly.addPoint(new Polygon.Point(7, 4));
        poly.addPoint(new Polygon.Point(5, 4));
        poly.translate(new Polygon.Point(4, 4));
        LinkedHashSet<Polygon.Point> pts = new LinkedHashSet<>();
        pts.add(new Polygon.Point(9, 6));
        pts.add(new Polygon.Point(11, 6));
        pts.add(new Polygon.Point(11, 8));
        pts.add(new Polygon.Point(9, 8));
        Iterator<Polygon.Point> piter = pts.iterator();
        Iterator<Polygon.Point> oiter = poly.getPoints().iterator();
        while(piter.hasNext() && oiter.hasNext()) {
            assertEquals(piter.next(), oiter.next());
        }

        Polygon polycopy = twoThousandPts.copy();
        polycopy.translate(4, 4);
        Iterator<Polygon.Point> pointIterator = twoThousandPts.getPoints().iterator();
        Iterator<Polygon.Point> otherIterator = polycopy.getPoints().iterator();
        while(piter.hasNext() && oiter.hasNext()) {
            assertEquals(pointIterator.next(), otherIterator.next().add(new Polygon.Point(4, 4)));
        }
    }

    @Test
    void testComputeArea() {
        Polygon poly = new Polygon(4);
        poly.addPoint(7, 2);
        poly.addPoint(7, 4);
        poly.addPoint(5, 4);
        poly.addPoint(5, 2);
        assertEquals(4, poly.computeArea());
        Polygon poly2 = new Polygon(4);
        poly2.addPoint(5, 4);
        poly2.addPoint(5, 2);
        poly2.addPoint(9, 2);
        poly2.addPoint(9, 4);
        assertEquals(8, poly2.computeArea());

        Polygon poly3 = new Polygon(8);
        poly3.addPoint(10, 2);
        poly3.addPoint(11, 3);
        poly3.addPoint(11, 4);
        poly3.addPoint(10, 5);
        poly3.addPoint(9, 5);
        poly3.addPoint(8, 4);
        poly3.addPoint(8, 3);
        poly3.addPoint(9, 2);

        assertEquals(7, poly3.computeArea());
        assertEquals(785397f, twoThousandPts.computeArea(), 2.0);
    }

    @Test
    void testGetCenter() {
        assertTrue(new Polygon.Point(300f, 500f).equals(twoThousandPts.getCenter(), 0.001f));
    }

    @Test
    void testHasVertex() {
        Polygon poly3 = new Polygon(8);
        poly3.addPoint(10, 2);
        poly3.addPoint(11, 3);
        poly3.addPoint(11, 4);
        poly3.addPoint(10, 5);
        poly3.addPoint(9, 5);
        poly3.addPoint(8, 4);
        poly3.addPoint(8, 3);
        poly3.addPoint(9, 2);
        assertTrue(poly3.hasVertex(new Polygon.Point(10, 2)));
        assertTrue(poly3.hasVertex(new Polygon.Point(11, 3)));
        assertTrue(poly3.hasVertex(new Polygon.Point(11, 4)));
        assertTrue(poly3.hasVertex(new Polygon.Point(10, 5)));
        assertTrue(poly3.hasVertex(new Polygon.Point(9, 5)));
        assertTrue(poly3.hasVertex(new Polygon.Point(8, 4)));
        assertTrue(poly3.hasVertex(new Polygon.Point(8, 3)));
        assertTrue(poly3.hasVertex(new Polygon.Point(9, 2)));
        assertTrue(twoThousandPts.hasVertex(new Polygon.Point(800, 500)));
        assertTrue(twoThousandPts.hasVertex(new Polygon.Point(169.6f, 982.7f), 0.01f));
    }

    @Test
    void testEquals() {
        Polygon.Point p = new Polygon.Point(0, 0);
        Polygon.Point p0 = new Polygon.Point(0, 0);
        assertTrue(p.equals(p0, 1e-6f));
        assertTrue(p.equals(p0, 1e-7f));
        assertTrue(p.equals(p0, 1e-8f));
        assertTrue(p.equals(p0, 1e-9f));
        Polygon.Point p1 = null;
        Polygon.Point p3 = new Polygon.Point(p);
        assertEquals(p, p0);
        assertNotEquals(p0, p1);
        assertFalse(p0.equals(p1, 1e-6f));
        assertNotEquals(p3, new ArrayList<>());
        assertEquals(p, p3);
    }

    @Test
    void testGetBounds() {
        Polygon poly = new Polygon(1);
        Polygon.Point p = new Polygon.Point(0f, 0f);
        poly.addPoint(p);
        assertEquals(new Polygon.Bounds(0f, 0f, 0f, 0f), poly.getBounds());
    }

    @Test
    void testGetEdges() {
        Polygon poly = new Polygon(1);
        assertEquals(Collections.emptySet(), poly.getEdges());

        Polygon poly8 = new Polygon(8);
        poly8.addPoint(10, 2);
        poly8.addPoint(11, 3);
        poly8.addPoint(11, 4);
        poly8.addPoint(10, 5);
        poly8.addPoint(9, 5);
        poly8.addPoint(8, 4);
        poly8.addPoint(8, 3);
        poly8.addPoint(9, 2);

        Edge edge1 = new Edge(10, 2, 11, 3);
        Edge edge2 = new Edge(11, 3, 11, 4);
        Edge edge3 = new Edge(11, 4, 10, 5);
        Edge edge4 = new Edge(10, 5, 9, 5);
        Edge edge5 = new Edge(9, 5, 8, 4);
        Edge edge6 = new Edge(8, 4, 8, 3);
        Edge edge7 = new Edge(8, 3, 9, 2);
        Edge edge8 = new Edge(9, 2, 10, 2);

        Set<Edge> edges = poly8.getEdges();

        assertEquals(8, edges.size());

        Iterator<Edge> iter = edges.iterator();

        assertEquals(edge1, iter.next());
        assertEquals(edge2, iter.next());
        assertEquals(edge3, iter.next());
        assertEquals(edge4, iter.next());
        assertEquals(edge5, iter.next());
        assertEquals(edge6, iter.next());
        assertEquals(edge7, iter.next());
        assertEquals(edge8, iter.next());
        assertFalse(iter.hasNext());

        Polygon poly5 = new Polygon(8);
        poly5.addPoint(10, 2);
        poly5.addPoint(11, 3);
        poly5.addPoint(11, 4);
        poly5.addPoint(10, 5);
        poly5.addPoint(9, 5);
        Edge tedge1 = new Edge(10, 2, 11, 3);
        Edge tedge2 = new Edge(11, 3, 11, 4);
        Edge tedge3 = new Edge(11, 4, 10, 5);
        Edge tedge4 = new Edge(10, 5, 9, 5);
        Edge tedge5 = new Edge(9, 5, 10, 2);
        Set<Edge> tedges = poly5.getEdges();

        assertEquals(5, tedges.size());

        Iterator<Edge> iter2 = tedges.iterator();

        assertEquals(tedge1, iter2.next());
        assertEquals(tedge2, iter2.next());
        assertEquals(tedge3, iter2.next());
        assertEquals(tedge4, iter2.next());
        assertEquals(tedge5, iter2.next());
        assertFalse(iter.hasNext());
        Set<Edge> twoThousandEdges = twoThousandPts.getEdges();
        assertNotNull(twoThousandEdges);
        assertEquals(2048, twoThousandEdges.size());

    }
}
