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
  private final Vertex shape;

  /**
   * Create a new Shape Entity.
   *
   * @param shape the shape of this shape entity
   */
  public ShapeEntity(Vertex shape) {
    super();
    this.shape = shape;
    addComponent(new ShapeComponent(shape, Color.DARK_GRAY));
  }

  @Override
  public void update(float dt) {
    LOGGER.debug("Updating shape {}.", shape);
  }

  @Override
  public void destroy() {

  }
}
