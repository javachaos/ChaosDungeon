package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * A simple physics component.
 */
@SuppressWarnings("unused")
public class PhysicsComponent extends Component {

  private static final Logger LOGGER = LogManager.getLogger(PhysicsComponent.class);
  private final Vector2f velocity;
  private final Vector3f angularVelocity;
  private final Vector2f prevPos;
  private final float invMass;
  private final double restitution; // Coefficient of restitution for bounciness
  private final Quaternionf prevRotation;
  private boolean isStatic;   // Flag to indicate if the entity is static (immovable)

  /**
   * Create a new physics component.
   *
   * @param invMass        the mass of this object
   * @param restitution the restitution
   * @param isStatic    true if this is a static object
   */
  public PhysicsComponent(float invMass, double restitution, Vector2f initialVelocity,
                          boolean isStatic) {
    super();
    this.velocity = initialVelocity;
    this.invMass = (float) (1.0 / invMass);
    this.restitution = restitution;
    this.isStatic = isStatic;
    this.prevPos = new Vector2f(0, 0);
    this.prevRotation = new Quaternionf();
    this.angularVelocity = new Vector3f();
  }

  /**
   * Create a new physics component.
   *
   * @param invMass        the mass of this object
   * @param restitution the restitution
   * @param isStatic    true if this is a static object
   */
  public PhysicsComponent(float invMass, double restitution,
                          Vector2f initialVelocity, Vector3f initialAngularVelocity,
                          boolean isStatic) {
    super();
    this.velocity = initialVelocity;
    this.invMass = (float) (1.0 / invMass);
    this.restitution = restitution;
    this.isStatic = isStatic;
    this.prevRotation = new Quaternionf();
    this.prevPos = new Vector2f(0, 0);
    this.angularVelocity = initialAngularVelocity;
  }

  public double getVx() {
    return velocity.x;
  }

  public double getVy() {
    return velocity.y;
  }

  public float getInvMass() {
    return invMass;
  }

  public double getRestitution() {
    return restitution;
  }

  public boolean isStatic() {
    return isStatic;
  }

  /**
   * Method to apply forces to the entity.
   *
   * @param forceX force in the x direction
   * @param forceY force in the y direction
   */
  public void applyForce(double forceX, double forceY) {
    if (!isStatic) {
      double ax = forceX * invMass;
      double ay = forceY * invMass;
      velocity.x += (float) ax;
      velocity.y += (float) ay;
    }
  }

  public void applyForce(Vector2f v) {
    this.applyForce(v.x, v.y);
  }

  public synchronized void applyImpulse(Vector2f impulse) {
   velocity.add(impulse).mul(invMass);
  }

  /**
   * Apply an angular force to this physics object.
   *
   * @param forceX angular force along the x-axis
   * @param forceY angular force along the y-axis
   * @param forceZ angular force along the z-axis
   */
  public void applyAngularForce(double forceX, double forceY, double forceZ) {
    double ax = forceX * invMass;
    double ay = forceY * invMass;
    double az = forceZ * invMass;
    angularVelocity.set(new Vector3f((float) ax, (float) ay, (float) az));
  }

  /**
   * Clamp the velocity vector the maxSpeed.
   *
   * @param maxSpeed the max speed.
   */
  public void clampVelocity(float maxSpeed) {
    // Clamp each component of the velocity vector separately
    velocity.x = Math.min(maxSpeed, Math.max(-maxSpeed, velocity.x));
    velocity.y = Math.min(maxSpeed, Math.max(-maxSpeed, velocity.y));
  }


  /**
   * Verlet integration method to update position and velocity.
   *
   * @param dt the change in time between updates
   */
  @Override
  public void update(double dt) {
    GameEntity gameEntity = (GameEntity) getEntity();
      Vector3f pos = gameEntity.getTransformComponent().getPosition();
      Quaternionf rot = gameEntity.getTransformComponent().getRotation();
      prevPos.x = pos.x;
      prevPos.y = pos.y;

      // Perform Verlet integration for position
      float newX = (float) (pos.x + velocity.x * dt);
      float newY = (float) (pos.y + velocity.y * dt);

      // Update velocity using Verlet integration
      velocity.x = (float) ((newX - prevPos.x) / dt);
      velocity.y = (float) ((newY - prevPos.y) / dt);

      // Update rotation
      rot.integrate((float) dt, angularVelocity.x, angularVelocity.y, angularVelocity.z);

      // Update position
      gameEntity.getTransformComponent().setPosition(new Vector3f(newX, newY, 0));
  }

  /**
   * Get the center of the entity this PhysicsComponents is attached to.
   *
   * @return the center point as a Vector 3.
   */
  public Vector2f getCenter() {
    GameEntity gameEntity = (GameEntity) getEntity();
    return gameEntity.getCollisionComponent().getShape().getCenter();
  }

  @Override
  public void destroy() {
    isStatic = true;
  }

  public void setAngularVelocity(Vector3f initialAngularVelocity) {
    angularVelocity.set(initialAngularVelocity);
  }

  public Vector2f getVelocity() {
    return velocity;
  }

  public void setVelocity(Vector2f v1Prime) {
    this.velocity.set(v1Prime);
  }

}
