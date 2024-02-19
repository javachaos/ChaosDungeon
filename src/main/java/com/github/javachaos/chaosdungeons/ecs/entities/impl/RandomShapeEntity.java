package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.ecs.components.render.ShapeComponent;

import java.awt.Color;

import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.geometry.GenerationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * Random shape entity.
 */
@SuppressWarnings("unused")
public class RandomShapeEntity extends GameEntity {

  private static final Logger LOGGER = LogManager.getLogger(RandomShapeEntity.class);
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
  public RandomShapeEntity(int x, int y,
                           int numEdges,
                           double maxX, double maxY,
                           Color c,
                           SpawnData spawnData,
                           GameContext gameContext) {
    super("", spawnData, gameContext);
    this.pos = new Vector2f(x, y);
    this.size = new Vector2d(maxX, maxY);
    this.numEdges = numEdges;
    this.color = c;
  }

  @Override
  public void init() {
    super.init();
    addComponent(new ShapeComponent(new Polygon(GenerationUtils.generateNonRegularPolygon(
            (int) pos.x, (int) pos.y, numEdges, size.x, size.y)), color));
  }

  @Override
  public void update(float dt) {
    //Unused
  }

  @Override
  public void destroy() {
    LOGGER.debug("Random Shape Destroyed!");
  }

}
