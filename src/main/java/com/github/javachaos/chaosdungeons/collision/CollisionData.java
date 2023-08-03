package com.github.javachaos.chaosdungeons.collision;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * CollisionData pojo.
 */
@SuppressWarnings("unused")
public class CollisionData {
  private final Point2D collisionNormal; // The collision normal vector
  private final double penetrationDepth; // The penetration depth of the collision
  private final List<Point2D> contactPoints; // List of contact points (optional)
  private final boolean isColliding;

  /**
   * Create a new CollisionData object.
   *
   * @param collisionNormal the collision normal vector
   * @param penetrationDepth the penetration depth
   */
  public CollisionData(Point2D collisionNormal, double penetrationDepth) {
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

  public List<Point2D> getContactPoints() {
    return contactPoints;
  }

  public Point2D getCollisionNormal() {
    return collisionNormal;
  }

}
