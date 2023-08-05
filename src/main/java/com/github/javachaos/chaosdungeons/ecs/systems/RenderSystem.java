package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.components.render.RenderComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.DefaultCameraEntity;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.FireballEntity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  public void update(float dt) {
    getEntities().stream().filter(n -> n.hasComponent(RenderComponent.class))
        .forEach(e -> e.update(dt));
  }

  @Override
  public void initSystem() {
    addEntity(new DefaultCameraEntity());
    addEntity(new FireballEntity());
  }

  @Override
  public void destroy() {

  }

}
