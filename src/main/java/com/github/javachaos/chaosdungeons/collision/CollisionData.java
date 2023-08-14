package com.github.javachaos.chaosdungeons.collision;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;

/**
 * CollisionData pojo.
 */
@SuppressWarnings("unused")
public class CollisionData {
  private Vector2f collisionNormal; // The collision normal vector
  private double penetrationDepth; // The penetration depth of the collision
  private List<Vector2f> contactPoints; // List of contact points (optional)
  private final boolean isColliding;
  private final boolean incomplete;
  private final long currentTimeNanos;

  /**
   * Create a new CollisionData object.
   *
   * @param collisionNormal the collision normal vector
   * @param penetrationDepth the penetration depth
   */
  public CollisionData(Vector2f collisionNormal, double penetrationDepth,
                       List<Vector2f> contactPoints, boolean isColliding, boolean incomplete) {
    this.collisionNormal = collisionNormal;
    this.penetrationDepth = penetrationDepth;
    this.contactPoints = contactPoints;
    this.isColliding = isColliding;
    this.incomplete = incomplete;
    this.currentTimeNanos = System.nanoTime();
  }

  /**
   * Create a new CollisionData object.
   *
   * @param collisionNormal the collision normal vector
   * @param penetrationDepth the penetration depth
   */
  public CollisionData(Vector2f collisionNormal, double penetrationDepth, boolean isColliding,
                       boolean incomplete) {
    this.collisionNormal = collisionNormal;
    this.penetrationDepth = penetrationDepth;
    this.contactPoints = new ArrayList<>();
    this.isColliding = isColliding;
    this.incomplete = incomplete;
    this.currentTimeNanos = System.nanoTime();
  }

  public long getCurrentTimeNanos() {
    return currentTimeNanos;
  }

  public boolean isColliding() {
    return isColliding;
  }

  public boolean isIncomplete() {
    return incomplete;
  }

  public double getPenetrationDepth() {
    return penetrationDepth;
  }

  public void setPenetrationDepth(double depth) {
    this.penetrationDepth = depth;
  }

  public List<Vector2f> getContactPoints() {
    return contactPoints;
  }

  public void setContactPoints(List<Vector2f> points) {
    this.contactPoints = points;
  }

  public Vector2f getCollisionNormal() {
    return collisionNormal;
  }

  public void setCollisionNormal(Vector2f norm) {
    this.collisionNormal = norm;
  }

  /**
   * A builder class.
   */
  public static class Builder {
    private Vector2f collisionNormal; // The collision normal vector
    private double penetrationDepth; // The penetration depth of the collision
    private List<Vector2f> contactPoints; // List of contact points (optional)
    private boolean isColliding;
    private boolean incomplete;

    /**
     * Construct a new builder.
     */
    public Builder() {
      this.collisionNormal = new Vector2f();
      this.penetrationDepth = 0f;
      this.contactPoints = new ArrayList<>();
      this.isColliding = false;
      this.incomplete = true;
    }

    public Builder setCollisionNormal(Vector2f collisionNormal) {
      this.collisionNormal = collisionNormal;
      return this;
    }

    public Builder setIsColliding(boolean isColliding) {
      this.isColliding = isColliding;
      return this;
    }

    public Builder setPenetrationDepth(double penetrationDepth) {
      this.penetrationDepth = penetrationDepth;
      return this;
    }

    public Builder setContactPoints(List<Vector2f> contactPoints) {
      this.contactPoints = contactPoints;
      return this;
    }

    public CollisionData build() {
      return new CollisionData(collisionNormal, penetrationDepth, contactPoints, isColliding, incomplete);
    }

    public Builder setIncomplete(boolean b) {
      this.incomplete = b;
      return this;
    }
  }

  @Override
  public String toString() {
    return "Time: " + currentTimeNanos + ", Normal: " + collisionNormal + ", Depth: (" + penetrationDepth + ")";
  }

}
