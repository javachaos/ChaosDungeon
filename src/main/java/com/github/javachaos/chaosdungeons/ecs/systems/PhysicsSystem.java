package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.components.PhysicsComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Physics system class.
 */
@SuppressWarnings("unused")
public class PhysicsSystem extends System {

  private static final Logger LOGGER = LogManager.getLogger(PhysicsSystem.class);

  public PhysicsSystem() {
    super();
  }

  /**
   * Add a physics entity to this Physics system.
   *
   * @param e the entity to be added, must have a physics component
   */
  public void addEntity(Entity e) {
    if (e.getComponent(PhysicsComponent.class) != null) {
      getEntities().add(e);
    } else {
      throw new IllegalArgumentException("Entity MUST have a physics component.");
    }
  }

  @Override
  public void update(float dt) {
    getEntities().forEach(e -> {
      getEntities()
          .stream()
          .filter(f -> f.hasComponent(PhysicsComponent.class))
          .forEach(entity -> entity.getComponent(PhysicsComponent.class)
          .handleCollision(e));
      e.update(dt);
    });
    LOGGER.debug("Physics Delta: {}", dt);
  }

  @Override
  public void init() {

    //Init code.
  }
}
