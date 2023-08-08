package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Initialize all entities.
 */
public class LoadSystem extends System {

  private static final Logger LOGGER = LogManager.getLogger(LoadSystem.class);

  public LoadSystem(GameWindow window) {
    super(window);
  }

  @Override
  protected void update(float dt) {
    getEntities().forEach(e -> e.update(dt));
  }

  /**
   * Call the init function for all entities in all systems.
   */
  @Override
  public void initSystem() {
    getEntities().forEach(Entity::init);
  }

  @Override
  public void destroy() {
    getEntities().forEach(Entity::destroy);
    LOGGER.debug("Loading system shutdown.");
  }
}
