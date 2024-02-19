package com.github.javachaos.chaosdungeons.collision;

import com.github.javachaos.chaosdungeons.geometry.GJKDetector2D;
import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;
import com.github.javachaos.chaosdungeons.geometry.polygons.Mesh;
import com.github.javachaos.chaosdungeons.geometry.polygons.MeshLoader;
import org.joml.Vector2f;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.github.javachaos.chaosdungeons.utils.PrecisionUtils.equalTo;
import static com.github.javachaos.chaosdungeons.utils.PrecisionUtils.greaterThan;
import static com.github.javachaos.chaosdungeons.utils.PrecisionUtils.lessThan;

public class Polygon {

    public record Bounds(float x, float y, float w, float h) {
        public boolean contains(float x, float y) {
            double x1 = this.x;
            double x2 = this.x + w;
            double y1 = this.y;
            double y2 = this.y + h;
            return (greaterThan(x, x1) || equalTo(x, x1))
                    && (lessThan(x, x2)    || equalTo(x, x2))
                    && (greaterThan(y, y1) || equalTo(y, y1))
                    && (lessThan(y, y2)    || equalTo(y, y2));
        }
    }

    public static class Point extends Vector2f {
        private static final float EPSILON = 1e-9f;
        public Point() {
            this(0, 0);
        }
        public Point(Point pos) {
            this(pos.x, pos.y);
        }
        public Point(Vector2f pos) {
            this(pos.x, pos.y);
        }
        public Point(float x, float y) {
            super(x, y);
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (other.getClass() != this.getClass()) return false;
            return this.equals(other, EPSILON);
        }

        public boolean equals(Object other, float eps) {
            if (other == null) return false;
            Point p = (Point) other;
            return equalTo(p.x, x, eps) && equalTo(p.y, y, eps);
        }
    }
    private static final int PAR_THRESHOLD = 1024;
    private final Map<Long, Point> points;
    private Point center;
    private final int capacity;
    private long size;

    public Polygon(int numVertices) {
        this.points = Collections.synchronizedMap(new LinkedHashMap<>(numVertices));
        this.capacity = numVertices;
        this.center = new Point();
    }

    public Polygon(Set<Point> points) {
        this.points = Collections.synchronizedMap(new LinkedHashMap<>(points.size()));
        this.capacity = points.size();
        if (points.size() > PAR_THRESHOLD) {
            synchronized (this.points) {
                getParStream().parallel().forEach(this::addPoint);
            }
        } else {
            for (Point p : points) {
                addPoint(p);
            }
        }
    }

    public void addPoint(Point p) {
        if (size > capacity) {
            throw new IllegalArgumentException("No more space.");
        }
        if (hasVertex(p)) {
            throw new IllegalStateException("Point already exists.");
        }
        if (size == 0) {
            center = p;
        }
        points.put(size++, p);
        this.center = computeCentroid();
    }

    public void addPoint(float x, float y) {
        this.addPoint(new Point(x, y));
    }

    public Point remove(long remove) {
        return points.remove(remove);
    }

    public void remove(Point p) {
        points.values().remove(p);
    }

    public void removeAll(List<Point> pts) {
        pts.forEach(this::remove);
    }

    public Set<Point> getPoints() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(points.values()));
    }

    public Point getPoint(long i) {
        return points.get(i);
    }

    /**
     * Return the center of this polygon as a Point copy.
     * @return the center of this polygon as a Point
     */
    public Point computeCentroid() {
        AtomicReference<Float> x = new AtomicReference<>(0f);
        AtomicReference<Float> y = new AtomicReference<>(0f);
        if (points.size() > PAR_THRESHOLD) {
            synchronized (points) {
                getParStream().parallel().forEach(p -> {
                    x.updateAndGet(v -> v + p.x);
                    y.updateAndGet(v -> v + p.y);
                });
            }
            return new Point(x.getAcquire() / size, y.getAcquire() / size);
        } else {
            float xx = 0f;
            float yy = 0f;
            for (Point p : points.values()) {
                xx += p.x;
                yy += p.y;
            }
            return new Point(xx / size, yy / size);
        }
    }

    /**
     * Get the center position of this polygon as a copy.
     * Any changes to the returned position will have no effect
     * on this polygon's position
     *
     * @return the position of this polygon
     */
    public Point getCenter() {
        return new Point(center);
    }

    public void translate(float x, float y) {
        translate(new Point(x, y));
    }

    public void translate(Point newPos) {
        if (points.size() > PAR_THRESHOLD) {
            synchronized (points) {
                getParStream().parallel().forEach(p -> p.add(newPos));
            }
        } else {
            for (Point point : points.values()) {
                point.add(newPos);
            }
        }
    }

    public long getSize() {
        return size;
    }

    /**
     * True if this polygon has other as a vertex.
     * @param other the point to test
     * @return true if this polygon has the vertex other
     */
    public boolean hasVertex(Point other) {
        return hasVertex(other, Point.EPSILON);
    }

    /**
     * True if this polygon has other as a vertex.
     * @param other the point to test
     * @return true if this polygon has the vertex other
     */
    public boolean hasVertex(Point other, float eps) {
        AtomicBoolean result = new AtomicBoolean(false);
        if (points.size() > PAR_THRESHOLD) {
            synchronized (points) {
                result.set(getParStream().parallel().anyMatch(x -> x.equals(other, eps)));
            }
            return result.get();
        } else {
            return getSeqSteam().anyMatch(x -> x.equals(other, eps));
        }
    }

    /**
     * Calculate the area of this polygon using the shoelace formula.
     * @return the area of this polygon.
     */
    public float computeArea() {
        float determinant = 0.0f;
        Point first = points.get(0L);
        if (size > PAR_THRESHOLD) {
            AtomicReference<Float> det = new AtomicReference<>(0.0f);
                LongStream.range(0, size).parallel().forEach(i -> {
                    Point n1 = points.get(i);
                    Point n2 = points.get(i + 1);
                    if (n2 == null)
                        n2 = first;
                    Point finalN = n2;
                    det.updateAndGet(v -> (v + n1.x * finalN.y - finalN.x * n1.y));
                });
            return det.get() / 2f;
        } else {
            for (long i = 0; i < size; i++) {
                Point n1 = points.get(i);
                Point n2 = points.get(i + 1);
                if (n2 == null)
                    n2 = first;
                determinant += n1.x * n2.y - n2.x * n1.y;
            }
            return determinant / 2f;
        }
    }

    public Bounds getBounds() {
        float maxX = 0.0f;
        float maxY = 0.0f;
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        AtomicReference<Float> armaxY = new AtomicReference<>(maxY);
        AtomicReference<Float> arminY = new AtomicReference<>(minY);
        AtomicReference<Float> armaxX = new AtomicReference<>(maxY);
        AtomicReference<Float> arminX = new AtomicReference<>(minX);

        if (size > PAR_THRESHOLD) {
            synchronized (points) {
                getParStream().forEach(p -> {
                    armaxY.getAndUpdate(lmaxY -> Math.max(lmaxY, p.y));
                    arminY.getAndUpdate(lminY -> Math.min(lminY, p.y));
                    armaxX.getAndUpdate(lmaxX -> Math.max(lmaxX, p.x));
                    arminX.getAndUpdate(lminX -> Math.min(lminX, p.x));
                });
            }
            float x = arminX.get();
            float y = arminY.get();
            float w = armaxX.get() - x;
            float h = armaxY.get() - y;
            return new Bounds(x, y, w, h);
        } else {
            for (Point curr : points.values()) {
                float x = curr.x();
                float y = curr.y();
                maxY = Math.max(maxY, y);
                minY = Math.min(minY, y);
                maxX = Math.max(maxX, x);
                minX = Math.min(minX, x);
            }
        }

        float x = minX;
        float y = minY;
        float w = maxX - x;
        float h = maxY - y;

        return new Bounds(x, y, w, h);
    }

    private Stream<Point> getSeqSteam() {
        return points.values().stream().sequential();
    }

    private Stream<Point> getParStream() {
        return points.values().parallelStream();
    }

    /**
     * Create a mesh for this vertex polygon.
     *
     * @return the mesh
     */
    public Mesh createMesh() {
        float[] pos = new float[(int) (getSize() * 2)];
        for (int i = 0; i < pos.length; i += 2) {
            Point p = getPoint(i / 2);
            pos[i] = p.x();
            pos[i + 1] = p.y();
        }
        int[] indices = IntStream.range(0, (int) getSize()).toArray();
        return MeshLoader.createMesh(pos, indices, 2);
    }

    public boolean contains(float x, float y) {
        return getBounds().contains(x, y);
    }

    public Set<Edge> getEdges() {
        if (size <= 1) {
            return Collections.emptySet();
        }
        Set<Edge> e = new LinkedHashSet<>();
        Point first = points.get(0L);
        if (size > PAR_THRESHOLD) {
                LongStream.range(0, size).parallel().forEach(i -> {
                    synchronized (points) {
                        Point n1 = points.get(i);
                        Point n2 = points.get(i + 1);
                        if (n2 == null) {
                            n2 = first;
                        }
                        e.add(new Edge(n1, n2));
                    }
                });
            return e;
        } else {
            for (long i = 0; i < size; i++) {
                Point n1 = points.get(i);
                Point n2 = points.get(i + 1);
                if (n2 == null)
                    n2 = first;
                e.add(new Edge(n1, n2));
            }
            return e;
        }
    }

    public Collision checkCollision(Polygon square) {
        return GJKDetector2D.checkCollision(this, square);
    }

    public Polygon copy() {
        Polygon copy = new Polygon(capacity);
        copy.size = size;
        copy.points.clear();
        copy.points.putAll(points);
        return copy;
    }

}
