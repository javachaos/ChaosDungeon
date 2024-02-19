package com.github.javachaos.chaosdungeons.collision;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;
import com.github.javachaos.chaosdungeons.utils.Pair;
import org.joml.Vector2f;

/**
 * CollisionData pojo.
 */
@SuppressWarnings("unused")
public class Collision implements Comparable<Collision> {

  private Pair<GameEntity, GameEntity> colliders;
  private Vector2f collisionNormal; // The collision normal vector
  private double penetrationDepth; // The penetration depth of the collision
  private List<Vector2f> contactPoints; // List of contact points (optional)
  private final boolean isColliding;
  private final boolean incomplete;
  private final long currentTimeNanos;

  /**
   * Create a new CollisionData object.
   *
   * @param colliders the two objects which are colliding
   * @param collisionNormal  the collision normal vector
   * @param penetrationDepth the penetration depth
   */
  public Collision(Pair<GameEntity, GameEntity> colliders, Vector2f collisionNormal, double penetrationDepth,
                   List<Vector2f> contactPoints, boolean isColliding, boolean incomplete) {
    this.colliders = colliders;
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
  public Collision(Pair<GameEntity, GameEntity> colliders, Vector2f collisionNormal, double penetrationDepth, boolean isColliding,
                   boolean incomplete) {
    this.colliders = colliders;
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

  public Pair<GameEntity, GameEntity> getColliders() {
    return colliders;
  }

  public void setColliders(Pair<GameEntity, GameEntity> gameEntityGameEntityPair) {
    this.colliders = gameEntityGameEntityPair;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Collision collision = (Collision) o;
    return Objects.equals(getColliders().getKey(), collision.getColliders().getKey()) &&
            Objects.equals(getColliders().getValue(), collision.getColliders().getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getColliders());
  }

  @Override
  public int compareTo(Collision o) {
    long minThis = Math.min(getColliders().getKey().getEntityId(), getColliders().getValue().getEntityId());
    long maxThis = Math.max(getColliders().getKey().getEntityId(), getColliders().getValue().getEntityId());
    long minOther = Math.min(o.getColliders().getKey().getEntityId(), o.getColliders().getValue().getEntityId());
    long maxOther = Math.max(o.getColliders().getKey().getEntityId(), o.getColliders().getValue().getEntityId());

    if (minThis == minOther && maxThis == maxOther) {
      return 0;
    } else if (minThis < minOther || (minThis == minOther && maxThis < maxOther)) {
      return -1;
    } else {
      return 1;
    }
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
    private Pair<GameEntity, GameEntity> colliders;

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

    public Builder setColliders(Pair<GameEntity, GameEntity> colliders) {
      this.colliders = colliders;
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

    public Collision build() {
      return new Collision(colliders, collisionNormal, penetrationDepth, contactPoints, isColliding, incomplete);
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
