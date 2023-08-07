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
    getEntities().stream().filter(n -> n.hasComponent(RenderComponent.class))
        .forEach(e -> e.update(dt));
  }

  @Override
  public void initSystem() {
    addEntity(new Fireball());
    for (int i = 0; i < 20; i++) {
      addEntity(new Fireball(0, -3f, new Vector3f(((float) Math.random()),
                   ((float) Math.random() * 3),
                   0)));
    }
  }

  @Override
  public void destroy() {
  }

}
