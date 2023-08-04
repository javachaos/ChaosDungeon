package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.components.render.RenderComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.ShapeEntity;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import java.awt.geom.Point2D;
import java.util.List;
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
    if (e.hasComponent(RenderComponent.class)) {
      getEntities().add(e);
    } else {
      throw new IllegalArgumentException("Entity must have a render component.");
    }
  }

  @Override
  public void update(float dt) {
    getEntities().forEach(e -> e.update(dt));
    LOGGER.debug("Render Delta: {}", dt);
  }

  @Override
  public void init() {
    Vertex v = new Vertex(List.of(new Point2D.Double(-0.5, -0.5),
                                  new Point2D.Double(0.5, -0.5),
                                  new Point2D.Double(0, 0.5)));
    addEntity(new ShapeEntity(v));
    //Init code.
  }
}
