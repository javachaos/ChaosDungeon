package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import java.util.List;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * A simple physics component.
 */
@SuppressWarnings("unused")
public class PhysicsComponent extends Component {

  private final Vector3f velocity;
  private final Vector3f prevPos;
  private final double mass;
  private final double restitution; // Coefficient of restitution for bounciness
  private boolean isStatic;   // Flag to indicate if the entity is static (immovable)
  private GameEntity gameEntity;

  /**
   * Create a new physics component.
   *
   * @param mass the mass of this object
   * @param restitution the restitution
   * @param isStatic true if this is a static object
   */
  public PhysicsComponent(double mass, double restitution, Vector3f initialVelocity,
                          boolean isStatic) {
    super();
    this.velocity = initialVelocity;
    this.mass = mass;
    this.restitution = restitution;
    this.isStatic = isStatic;
    this.prevPos = new Vector3f(0, 0, 0);
  }

  // Getter and setter methods
  public double getXpos() {
    return gameEntity.getTransform().getPosition().x;
  }

  public double getYpos() {
    return gameEntity.getTransform().getPosition().y;
  }

  public double getVx() {
    return velocity.x;
  }

  public double getVy() {
    return velocity.y;
  }

  public double getMass() {
    return mass;
  }

  public double getRestitution() {
    return restitution;
  }

  public boolean isStatic() {
    return isStatic;
  }


  public Vector3f getPosition() {
    return gameEntity.getTransform().getPosition();
  }

  /**
   * Set the position of this physic object.
   *
   * @param x x-pos
   * @param y y-pos
   * @param z z-pos
   */
  public void setPosition(float x, float y, float z) {
    gameEntity.getTransform().setPosition(new Vector3f(x, y, z));
  }

  public Vector3f getScale() {
    return gameEntity.getTransform().getScale();
  }

  public void setScale(Vector3f scale) {
    gameEntity.getTransform().setScale(scale);
  }

  public Quaternionf getRotation() {
    return gameEntity.getTransform().getRotation();
  }

  /**
   * Set the rotation of this Physics object.
   *
   * @param rot rotation angle in degrees along the z-axis.
   */
  public void setRotation(Quaternionf rot) {
    gameEntity.getTransform().setRotation(rot);
  }

  /**
   * Method to apply forces to the entity.
   *
   * @param forceX force in the x direction
   * @param forceY force in the y direction
   */
  public void applyForce(double forceX, double forceY) {
    if (!isStatic) {
      double ax = forceX / mass;
      double ay = forceY / mass;
      velocity.x += (float) ax;
      velocity.y += (float) ay;
    }
  }

  /**
   *  Collision handling method.
   */
  public void handleCollision(Entity otherEntity) {
    // Check if this entity has a CollisionComponent attached
    List<CollisionComponent> collisionComponents =
        getEntity().getComponents(CollisionComponent.class);

    if (collisionComponents != null && !collisionComponents.isEmpty()) {
      // Signal the collision to the CollisionComponent
      collisionComponents.forEach(c -> c.onCollision(otherEntity, this));
    }
  }

  /**
   * Verlet integration method to update position and velocity.
   *
   * @param dt the change in time between updates
   */
  @Override
  public void update(double dt) {
    if (!isStatic) {
      double newVx = velocity.x + (getPosition().x - prevPos.x);
      double newVy = velocity.y + (getPosition().y - prevPos.y);

      double newX = getPosition().x + newVx * dt + 0.5 * newVx * dt * dt;
      prevPos.x = getPosition().x;
      double newY = getPosition().y + newVy * dt + 0.5 * newVy * dt * dt;
      prevPos.y = getPosition().y;
      getPosition().x = (float) newX;
      getPosition().y = (float) newY;

      //TODO add rotation
    }
  }

  @Override
  public void destroy() {
    isStatic = true;
  }

  @Override
  public void onAdded(GameEntity e) {
    this.gameEntity = e;
  }

  @Override
  public void onRemoved(GameEntity e) {
  }

}
