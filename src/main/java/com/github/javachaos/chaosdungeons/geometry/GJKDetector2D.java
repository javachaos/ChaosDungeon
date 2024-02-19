package com.github.javachaos.chaosdungeons.geometry;

import com.github.javachaos.chaosdungeons.collision.Collision;
import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.ecs.components.CollisionComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;
import com.github.javachaos.chaosdungeons.utils.Pair;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GJKDetector2D {

    private static final float ERROR = 0.0001f;

    private record EdgeInfo(float dist, int index, Vector2f a, Vector2f b, Vector2f normal) {}
    private record Simplex(Vector2f a, Vector2f b, Vector2f c) {}

    /**
     * Check for a collision between the two GameEntities a and b.
     *
     * @param a first game entity
     * @param b second game entity
     * @return a Collision object
     */
    public static Collision checkCollision(GameEntity a, GameEntity b) {
        Collision.Builder builder = new Collision.Builder().setColliders(new Pair<>(a, b));
        if (!a.isDynamic() && !b.isDynamic()) {
            return builder.setIsColliding(false).build();
        }
        if (!a.hasComponent(CollisionComponent.class) || !b.hasComponent(CollisionComponent.class)) {
            return builder.setIsColliding(false).build();
        }
        Polygon av = a.getCollisionComponent().getShape();
        Polygon bv = b.getCollisionComponent().getShape();
        Simplex simp = gjk(av, bv);
        if (simp != null) {
            EdgeInfo ei = epa(av, bv, simp);
            assert ei != null;
            return builder.setIncomplete(false)
                    .setIsColliding(true)
                    .setCollisionNormal(ei.normal())
                    .setPenetrationDepth(ei.dist())
                    .build();
        } else {
            return builder.setIsColliding(false).build();
        }
    }

    /**
     * Check for a collision between the two GameEntities a and b.
     *
     * @param a first game entity
     * @param b second game entity
     * @return a Collision object
     */
    public static Collision checkCollision(Polygon a, Polygon b) {
        Collision.Builder builder = new Collision.Builder();
        Simplex simp = gjk(a, b);
        if (simp != null) {
            EdgeInfo ei = epa(a, b, simp);
            if (ei == null) {
                return builder.setIncomplete(false)
                        .setIsColliding(false)
                        .build();
            } else {
                return builder.setIncomplete(false)
                        .setIsColliding(true)
                        .setCollisionNormal(ei.normal())
                        .setPenetrationDepth(ei.dist())
                        .build();
            }
        } else {
            return builder.setIsColliding(false).build();
        }
    }

    private static Simplex gjk(Polygon v1, Polygon v2) {
        Vector2f p1 = v1.getCenter();
        Vector2f p2 = v2.getCenter();
        Vector2f dir = p1.sub(p2);
        Vector2f a = support2(
                v1,
                v2,
                dir);

        Vector2f v = new Vector2f(-a.x, -a.y);
        Vector2f b = support2(v1, v2, v);
        if (b.dot(v) <= 0) {
            return null;
        }

        Vector2f ab = new Vector2f(b).sub(a);
        v = vectorTripleProduct(ab, v, ab);
        for (;;) {
            Vector2f c = support2(v1, v2, v);
            float d = new Vector2f(c).dot(v);
            if (d <= 0) {
                return null;
            }
            Vector2f c0 = new Vector2f(-c.x, -c.y);
            Vector2f cb = new Vector2f(b).sub(c);
            Vector2f ca = new Vector2f(a).sub(c);

            Vector2f cbOrth = vectorTripleProduct(ca, cb, cb);
            Vector2f caOrth = vectorTripleProduct(cb, ca, ca);
            if (new Vector2f(caOrth).dot(c0) > 0) {
                b = c;
                v = caOrth;
            } else if (new Vector2f(cbOrth).dot(c0) > 0) {
                a = c;
                v = cbOrth;
            } else {
                return new Simplex(a, b, c);
            }
        }
    }

    private static EdgeInfo epa(Polygon a, Polygon b, Simplex s) {
        List<Vector2f> polytope = new ArrayList<>(3);
        polytope.add(s.a());
        polytope.add(s.b());
        polytope.add(s.c());
        for (;;) {
            EdgeInfo e = closestEdge(polytope);
            if (e == null) {
                return null;
            }
            Vector2f r = support2(a, b, e.normal());
            if (Math.abs(e.normal().dot(r) - e.dist()) < ERROR) {
                return e;
            }
            polytope.add(e.index() + 1, r);
        }
    }

    private static long support(Polygon pts, Vector2f v) {
        long index = 0;
        float maxDot = v.dot(pts.getPoint(0));
        for (long i = 1; i < pts.getSize(); i++) {
            float dot = v.dot(pts.getPoint(i));
            if (dot > maxDot) {
                maxDot = dot;
                index = i;
            }
        }
        return index;
    }

    private static Vector2f support2(Polygon pts1, Polygon pts2, Vector2f v) {
        Polygon.Point v1 = pts1.getPoint(support(pts1, v));
        Polygon.Point v2 = pts2.getPoint(support(pts2, new Vector2f(-v.x, -v.y)));
        return v1.sub(v2);
    }

    private static EdgeInfo closestEdge(List<Vector2f> polytopes) {
        float dmin = Float.POSITIVE_INFINITY;
        EdgeInfo closest = null;
        int numPts = polytopes.size();

        for (int i = 0; i < numPts; i++) {
            Vector2f p = polytopes.get(i);
            Vector2f q = polytopes.get((i + 1) % polytopes.size());
            Vector2f e = q.sub(p);
            Vector2f n = vectorTripleProduct(e, p, e).normalize();
            float dist = n.dot(p);
            if (dist < dmin) {
                dmin = dist;
                closest = new EdgeInfo(dist, i, p, q, n);
            }
        }

        return closest;
    }

    private static Vector2f vectorTripleProduct(final Vector2f a, final Vector2f b, final Vector2f c) {
        Vector3f a3 = new Vector3f(a, 0f);
        Vector3f b3 = new Vector3f(b, 0f);
        Vector3f c3 = new Vector3f(c, 0f);
        Vector3f lhs = new Vector3f(b3).mul(a3.dot(c3));
        Vector3f rhs = new Vector3f(c3).mul(a3.dot(b3));
        Vector3f res = lhs.sub(rhs);
        return new Vector2f(res.x, res.y);
    }

}
