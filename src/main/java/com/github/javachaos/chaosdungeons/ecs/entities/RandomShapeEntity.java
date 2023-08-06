package com.github.javachaos.chaosdungeons.ecs.entities;

import static com.github.javachaos.chaosdungeons.geometry.GenerationUtils.generateNonRegularPolygon;

import com.github.javachaos.chaosdungeons.ecs.components.render.ShapeComponent;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import java.awt.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2d;
import org.joml.Vector2i;

/**
 * Random shape entity.
 */
@SuppressWarnings("unused")
public class RandomShapeEntity extends Entity {

  private static final Logger LOGGER = LogManager.getLogger(RandomShapeEntity.class);

  private final Vector2i pos;
  private final Vector2d size;
  private final int numEdges;
  private final Color color;

  /**
   * Create a new Random shape entity.
   *
   * @param x x-pos
   * @param y y-pos
   * @param numEdges number of edges
   * @param maxX max x vertex
   * @param maxY max y vertex
   */
  public RandomShapeEntity(int x, int y, int numEdges, double maxX, double maxY, Color c) {
    super();
    this.pos = new Vector2i(x, y);
    this.size = new Vector2d(maxX, maxY);
    this.numEdges = numEdges;
    this.color = c;
  }

  @Override
  public void init() {
    addComponent(new ShapeComponent(new Vertex(generateNonRegularPolygon(
        pos.x, pos.y, numEdges, size.x, size.y)), color));
  }

  @Override
  public void update(float dt) {
  }

  @Override
  public void destroy() {
    LOGGER.debug("Random Shape Destroyed!");
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {

  }
}
