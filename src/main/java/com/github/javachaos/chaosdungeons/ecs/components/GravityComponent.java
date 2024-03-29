package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * A gravity component.
 */
@SuppressWarnings("unused")
public class GravityComponent extends PhysicsComponent {

  private static final Logger LOGGER = LogManager.getLogger(GravityComponent.class);
  private static final double GRAVITATIONAL_FACTOR = -9.8;

  private double fg = 1.0;

  /**
   * Create a new component.
   */
  public GravityComponent(float mass,
                          double restitution,
                          Vector2f initialVelocity) {
    super(mass, restitution, initialVelocity, false);
  }

  /**
   * Create a new component.
   */
  public GravityComponent(float mass,
                          double restitution,
                          Vector2f initialVelocity,
                          Vector3f initialAngularVelocity) {
    super(mass, restitution, initialVelocity, initialAngularVelocity, false);
  }

  /**
   * Create a new component.
   */
  public GravityComponent(float mass,
                          double restitution,
                          Vector2f initialVelocity,
                          double gravitationalMultiplier) {
    super(mass, restitution, initialVelocity, false);
    this.fg = gravitationalMultiplier;
  }

  /**
   * Create a new component.
   */
  public GravityComponent(float mass,
                          double restitution,
                          Vector2f initialVelocity,
                          Vector3f initialAngularVelocity,
                          double gravitationalMultiplier) {
    super(mass, restitution, initialVelocity, initialAngularVelocity, false);
    this.fg = gravitationalMultiplier;
  }

  @Override
  public void update(double dt) {
    super.update(dt);
    applyForce(0.0, fg * GRAVITATIONAL_FACTOR);
  }

}
