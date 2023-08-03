package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.components.RenderComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Rendering system class.
 */
@SuppressWarnings("unused")
public class RenderSystem extends System {

  private static final Logger LOGGER = LogManager.getLogger(RenderSystem.class);

  public RenderSystem() {
    super();
  }

  /**
   * Add the entity e to the render system.
   *
   * @param e the entity e to be added.
   */
  public void addEntity(Entity e) {
    if (e.getComponent(RenderComponent.class) != null) {
      getEntities().add(e);
    }
  }

  @Override
  public void update(float dt) {
    super.update((double) dt);
    LOGGER.debug("Current delta time: {}", dt);
    getEntities().forEach(e -> e.update(dt));
  }

  @Override
  public void init() {
    //Init code.
  }
}
