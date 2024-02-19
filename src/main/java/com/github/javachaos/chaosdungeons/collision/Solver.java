package com.github.javachaos.chaosdungeons.collision;

import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import java.util.Collections;
import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * A simple collision solver.
 */
public class Solver {
    private static final Logger LOGGER = LogManager.getLogger(Solver.class);
    private final Set<Collision> collisions = Collections.synchronizedSet(new ConcurrentSkipListSet<>());
    private final Deque<Collision> resolved = new ConcurrentLinkedDeque<>();

    private static final int THRESHOLD = 1000;

    public void solve() {
      if (collisions.size() >= THRESHOLD) {
          synchronized (collisions) {
              collisions.stream().parallel().forEach(c -> {
                  resolve(c);
                  resolved.push(c);
              });
          }
          resolved.stream().parallel().forEach(collisions::remove);
      } else {
          for (Collision c : collisions) {
            resolve(c);
            resolved.push(c);
        }
        resolved.forEach(collisions::remove);
    }
    }

    public void addCollision(Collision c) {
        if (c.getColliders().getKey().getEntityId() != c.getColliders().getValue().getEntityId()) {
            collisions.add(c);
        }
    }

    /**
     * Resolve this collision.
     */
    private void resolve(Collision c) {
        // This should work, need to test and getCollisionNormal() and getVelocity()
        GameEntity e1 = c.getColliders().getKey();
        GameEntity e2 = c.getColliders().getValue();
        Vector2f impulseDirection = c.getCollisionNormal();
        Vector2f v1 = new Vector2f(e2.getPhysicsComponent().getVelocity());
        Vector2f v2 = new Vector2f((e1.getPhysicsComponent().getVelocity()));

        float relativeVdotn = v1.sub(v2).dot(impulseDirection);
        double m1 = e1.getPhysicsComponent().getInvMass();
        double m2 = e2.getPhysicsComponent().getInvMass();
        double re1 = e1.getPhysicsComponent().getRestitution();
        double re2 = e2.getPhysicsComponent().getRestitution();
        double finalR = Math.min(re2, re1);

        double impulseMagnitude = (-(1 + finalR) * relativeVdotn) / (m1+m2);//calculate impulse magnitude

        //Compute new velocities
        Vector2f j = impulseDirection.mul((float) impulseMagnitude);

        if (e1.isDynamic()) {
            e1.getPhysicsComponent().applyImpulse(j);
        }
        if (e2.isDynamic()) {
            e2.getPhysicsComponent().applyImpulse(j.mul(-1f));
        }
        LOGGER.debug("Resolved: {} and {}. ", e1.getEntityId(), e2.getEntityId());
    }
}
