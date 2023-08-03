package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.Component;
import com.github.javachaos.chaosdungeons.ecs.Entity;
import java.util.List;

/**
 * A simple physics component.
 */
@SuppressWarnings("unused")
public class PhysicsComponent extends Component {

  // Position and velocity properties
  private double xpos;   // Current x position
  private double ypos;   // Current y position
  private double prevX;  // Previous x position
  private double prevY;  // Previous y position
  private double vx;     // Velocity in x direction
  private double vy;     // Velocity in y direction

  // Other physics-related properties
  private final double mass;
  private final double restitution; // Coefficient of restitution for bounciness
  private boolean isStatic;   // Flag to indicate if the entity is static (immovable)

  /**
   * Create a new physics component.
   *
   * @param id the component id
   * @param x the initial x position
   * @param y the initial y position
   * @param mass the mass of this object
   * @param restitution the restitution
   * @param isStatic true if this is a static object
   */
  public PhysicsComponent(int id,
                          double x, double y,
                          double mass, double restitution,
                          boolean isStatic) {
    super(id);
    this.xpos = x;
    this.ypos = y;
    this.prevX = x;
    this.prevY = y;
    this.mass = mass;
    this.restitution = restitution;
    this.isStatic = isStatic;
  }

  // Getter and setter methods
  public double getXpos() {
    return xpos;
  }

  public double getYpos() {
    return ypos;
  }

  public double getVx() {
    return vx;
  }

  public double getVy() {
    return vy;
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
      vx += ax;
      vy += ay;
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
      double newVx = vx + (xpos - prevX);
      double newVy = vy + (ypos - prevY);

      double newX = xpos + newVx * dt + 0.5 * newVx * dt * dt;
      prevX = xpos;
      double newY = ypos + newVy * dt + 0.5 * newVy * dt * dt;
      prevY = ypos;
      xpos = newX;
      ypos = newY;
      getEntity().getComponents(PhysicsComponent.class)
          .forEach(c -> c.handleCollision(getEntity()));
    }
  }

  @Override
  public void destroy() {
    isStatic = true;
  }
}
