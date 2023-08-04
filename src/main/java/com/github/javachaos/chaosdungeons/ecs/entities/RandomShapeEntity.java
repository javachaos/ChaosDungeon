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
public class RandomShapeEntity extends Entity {

  private static final Logger LOGGER = LogManager.getLogger(RandomShapeEntity.class);

  private final Vector2i pos;
  private final Vector2d size;
  private final int numEdges;

  /**
   * Create a new Random shape entity.
   *
   * @param x x-pos
   * @param y y-pos
   * @param numEdges number of edges
   * @param maxX max x vertex
   * @param maxY max y vertex
   */
  public RandomShapeEntity(int x, int y, int numEdges, double maxX, double maxY) {
    super();
    this.pos = new Vector2i(x, y);
    this.size = new Vector2d(maxX, maxY);
    this.numEdges = numEdges;
    addComponent(new ShapeComponent(new Vertex(generateNonRegularPolygon(
        pos.x, pos.y, numEdges, size.x, size.y)), Color.DARK_GRAY));
  }

  @Override
  public void update(float dt) {
    getComponents().forEach(c -> c.update(dt));
  }

  @Override
  public void destroy() {
  }
}
