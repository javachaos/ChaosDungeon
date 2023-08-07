package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

/**
 * A simple physics component.
 */
@SuppressWarnings("unused")
public class PhysicsComponent extends Component {

  private final Vector3f velocity;
  private final Vector3f angularVelocity;
  private Vector3f angularAcceleration;
  private final Vector3f prevPos;
  private final double mass;
  private final double restitution; // Coefficient of restitution for bounciness
  private boolean isStatic;   // Flag to indicate if the entity is static (immovable)
  private GameEntity gameEntity;

  /**
   * Accumulator used for debugging.
   */
  private float acc;

  private static final Logger LOGGER = LogManager.getLogger(PhysicsComponent.class);

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
    this.angularVelocity = new Vector3f();
  }

  // Getter and setter methods
  public double getXpos() {
    return gameEntity.getPosition().x;
  }

  public double getYpos() {
    return gameEntity.getPosition().y;
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
    return gameEntity.getPosition();
  }

  public Vector3f getScale() {
    return gameEntity.getScale();
  }

  public Vector3f getRotation() {
    return gameEntity.getRotation();
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

  private double normalizeAngle(double angle) {
    while (angle > Math.PI) {
      angle -= 2 * Math.PI;
    }
    while (angle < -Math.PI) {
      angle += 2 * Math.PI;
    }
    return angle;
  }

  /**
   * Verlet integration method to update position and velocity.
   *
   * @param dt the change in time between updates
   */
  @Override
  public void update(double dt) {
    if (acc > 1.0) {
      LOGGER.debug("{} is at [{}, {}, {}]", getEntity(),
          getPosition().x, getPosition().y, getPosition().z);
      acc = 0;
    }
    if (!isStatic) {
      double newVx = velocity.x + (getPosition().x - prevPos.x);
      double newVy = velocity.y + (getPosition().y - prevPos.y);

      prevPos.x = getPosition().x;
      double newX = getPosition().x + newVx * dt + 0.5 * newVx * dt * dt;
      double newY = getPosition().y + newVy * dt + 0.5 * newVy * dt * dt;
      prevPos.y = getPosition().y;
      acc += (float) dt;
      Vector3f prevRotation = getRotation();

      double newRotationX = prevRotation.x + angularVelocity.x * dt;
      double newRotationY = prevRotation.y + angularVelocity.y * dt;
      double newRotationZ = prevRotation.z + angularVelocity.z * dt;

      // Apply angular momentum damping factor
      double angularMomentumDamping = 0.95; // Adjust this damping factor as needed
      newRotationX *= angularMomentumDamping;
      newRotationY *= angularMomentumDamping;
      newRotationZ *= angularMomentumDamping;

      newRotationX = normalizeAngle(newRotationX);
      newRotationY = normalizeAngle(newRotationY);
      newRotationZ = normalizeAngle(newRotationZ);

      GameEntity ge = ((GameEntity) getEntity());
      ge.updateModelMatrix(
          new Vector3f((float) newX,  (float) newY, getPosition().z), //position
          new Vector3f((float) newRotationX, (float) newRotationY, (float) newRotationZ), //rotation
          getScale()); //scale
    }
  }

  @Override
  public void destroy() {
    isStatic = true;
  }

  @Override
  public void onAdded(Entity e) {
    LOGGER.debug("Physics Component added to: " + e);
    this.gameEntity = (GameEntity) e;
  }

  @Override
  public void onRemoved(Entity e) {
  }

}
