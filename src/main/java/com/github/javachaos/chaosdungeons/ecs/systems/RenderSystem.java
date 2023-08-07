package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.components.render.RenderComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.Fireball;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

/**
 * Rendering system class.
 */
@SuppressWarnings("unused")
public class RenderSystem extends System {

  private static final Logger LOGGER = LogManager.getLogger(RenderSystem.class);

  public RenderSystem(GameWindow window) {
    super(window);
  }

  /**
   * Add the entity e to the render system.
   *
   * @param e the entity e to be added.
   */
  public void addEntity(Entity e) {
    getEntities().add(e);
  }

  @Override
  public void update(float dt) { // update all entities with a render component.
    getEntities().stream().filter(
        n -> n.hasComponent(RenderComponent.class))
        .forEach(e -> e.update(dt));
  }

  @Override
  public void initSystem() {
    for (int i = 0; i < 100; i++) {
      addEntity(new Fireball(0, (float) (-2f * Math.random()),
          new Vector3f(
              (float) (Math.random() * 5.1f),
              (float) (Math.random() * 12.1f),
              (float) (Math.random() * 5.1f)),
          new Vector3f(
              (float) (4f * Math.random()),
              (float) (4f * Math.random()),
              (float) (4f * Math.random()))));
    }
  }

  @Override
  public void destroy() {
  }

}
