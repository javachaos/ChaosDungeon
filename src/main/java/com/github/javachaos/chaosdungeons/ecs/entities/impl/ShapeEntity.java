package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple shape entity class.
 */
@SuppressWarnings("unused")
public class ShapeEntity extends GameEntity {
  private static final Logger LOGGER = LogManager.getLogger(ShapeEntity.class);

  /**
   * Create a new Shape Entity.
   */
  public ShapeEntity(SpawnData data, GameContext gameContext) {
    super("", data, gameContext);
  }

  @Override
  public void update(float dt) {
    //Unused
  }

  @Override
  public void destroy() {
    LOGGER.debug("ShapeEntity destroyed.");
  }

}
