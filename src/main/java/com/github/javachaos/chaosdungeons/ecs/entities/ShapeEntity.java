package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.render.ShapeComponent;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import java.awt.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple shape entity class.
 */
public class ShapeEntity extends Entity {
  private static final Logger LOGGER = LogManager.getLogger(ShapeEntity.class);

  /**
   * Create a new Shape Entity.
   *
   * @param shape the shape of this shape entity
   */
  public ShapeEntity(Vertex shape) {
    super();
    addComponent(new ShapeComponent(shape, Color.DARK_GRAY));
  }

  @Override
  public void init() {

  }

  @Override
  public void update(float dt) {
  }

  @Override
  public void destroy() {
    LOGGER.debug("ShapeEntity destroyed.");
  }

  @Override
  public void onAdded(GameEntity e) {

  }

  @Override
  public void onRemoved(GameEntity e) {

  }
}
