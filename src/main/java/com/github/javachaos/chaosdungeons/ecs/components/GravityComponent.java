package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

/**
 * A gravity component.
 */
@SuppressWarnings("unused")
public class GravityComponent extends PhysicsComponent {

  private static final Logger LOGGER = LogManager.getLogger(GravityComponent.class);

  /**
   * Create a new component.
   */
  public GravityComponent(double mass, double restitution, Vector3f initialVelocity) {
    super(mass, restitution, initialVelocity, false);
  }

  @Override
  public void update(double dt) {
    super.update(dt);
    super.applyForce(0.0, -9.8);
  }

  @Override
  public void destroy() {
    super.destroy();
  }

  @Override
  public void onAdded(Entity e) {
    super.onAdded(e);
    LOGGER.debug("Added Gravity Component to entity: " + e);
  }

  @Override
  public void onRemoved(Entity e) {
    super.onRemoved(e);
  }
}
