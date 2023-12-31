package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.collision.CollisionData;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import org.joml.Vector3f;

/**
 * Basic collision component class.
 */
public class CollisionComponent extends Component {

  private final PhysicsComponent physicsComponent;
  private Vertex shape;

  /**
   * Create a new component.
   */
  public CollisionComponent(Vertex shape, PhysicsComponent physicsComponent) {
    super();
    this.shape = shape;
    this.physicsComponent = physicsComponent;
  }

  /**
   * Check if this collision component is colliding with another collision component.
   *
   * @param other   the other entity
   * @param otherCc the other entities collision component
   */
  public void onCollision(GameEntity other, CollisionComponent otherCc) {
    GameEntity thisGe = (GameEntity) getEntity();
    CollisionData c = shape.checkCollision(otherCc.getShape());
    if (thisGe != other && c.isColliding()) {
      resolveCollision(otherCc.physicsComponent, physicsComponent);
    }
  }

  private void resolveCollision(PhysicsComponent otherPhys, PhysicsComponent thisPhys) {
    //TODO solve for static-static and static-dynamic body collisions
    // currently resolves dynamic-dynamic collisions
    Vector3f thisCenter = physicsComponent.getCenter();
    Vector3f otherCenter = otherPhys.getCenter();
    Vector3f v1minusv2 = thisPhys.getVelocity().sub(otherPhys.getVelocity());
    Vector3f v2minusv1 = otherPhys.getVelocity().sub(thisPhys.getVelocity());
    Vector3f x1minusx2 = thisCenter.sub(otherCenter);
    float numerator = v1minusv2.dot(x1minusx2);
    float len = Vector3f.lengthSquared(x1minusx2.x, x1minusx2.y, x1minusx2.z);
    float scalarMul = (numerator / len)
        * (float) (2 * otherPhys.getMass() / (thisPhys.getMass() + otherPhys.getMass()));
    x1minusx2.mul(scalarMul);
    Vector3f v1 = thisPhys.getVelocity();
    Vector3f v1Prime = v1.sub(x1minusx2);
    thisPhys.applyImpulse(v1Prime);
    Vector3f x2minusx1 = otherCenter.sub(thisCenter);
    numerator = v2minusv1.dot(x2minusx1);
    len = Vector3f.lengthSquared(x2minusx1.x, x2minusx1.y, x2minusx1.z);
    scalarMul = (numerator / len)
        * (float) (2 * thisPhys.getMass() / (thisPhys.getMass() + otherPhys.getMass()));
    x2minusx1.mul(scalarMul);
    Vector3f v2 = otherPhys.getVelocity();
    Vector3f v2Prime = v2.sub(x2minusx1);
    otherPhys.applyImpulse(v2Prime);
  }

  /**
   * Return the shape for this collision component.
   *
   * @return the shape of this collision component
   */
  public Vertex getShape() {
    return shape;
  }

  @Override
  public void update(double dt) {
    physicsComponent.update(dt);
  }

  @Override
  public void destroy() {
    shape = null;
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {
  }
}
