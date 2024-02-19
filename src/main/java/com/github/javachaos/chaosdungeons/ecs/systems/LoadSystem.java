package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Initialize all entities.
 */
public class LoadSystem extends System {

  private static final Logger LOGGER = LogManager.getLogger(LoadSystem.class);

  public LoadSystem(GameContext gameContext) {
    super(gameContext);
  }

  @Override
  public void update(double dt) {
    //Unused
  }

  /**
   * Call the init function for all entities in all systems.
   */
  @Override
  public void initSystem() {
    gameContext.getEntities().forEach(Entity::init);
  }

  @Override
  public void destroy() {
    LOGGER.debug("Loading system shutdown.");
  }
}
