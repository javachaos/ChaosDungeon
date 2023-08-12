package com.github.javachaos.chaosdungeons.collision;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;

/**
 * CollisionData pojo.
 */
@SuppressWarnings("unused")
public class CollisionData {
  private final Vector2f collisionNormal; // The collision normal vector
  private final double penetrationDepth; // The penetration depth of the collision
  private final List<Vector2f> contactPoints; // List of contact points (optional)
  private final boolean isColliding;

  /**
   * Create a new CollisionData object.
   *
   * @param collisionNormal the collision normal vector
   * @param penetrationDepth the penetration depth
   */
  public CollisionData(Vector2f collisionNormal, double penetrationDepth) {
    this.collisionNormal = collisionNormal;
    this.penetrationDepth = penetrationDepth;
    this.contactPoints = new ArrayList<>();
    this.isColliding = penetrationDepth > 0.0;
  }

  public boolean isColliding() {
    return isColliding;
  }

  public double getPenetrationDepth() {
    return penetrationDepth;
  }

  public List<Vector2f> getContactPoints() {
    return contactPoints;
  }

  public Vector2f getCollisionNormal() {
    return collisionNormal;
  }

}
