package com.github.javachaos.chaosdungeons.ecs;

import com.github.javachaos.chaosdungeons.ecs.components.RenderComponent;

/**
 * Rendering system class.
 */
@SuppressWarnings("unused")
public class RenderSystem extends System {

  /**
   * Add the entity e to the render system.
   *
   * @param e the entity e to be added.
   */
  public void addEntity(Entity e) {
    if (e.getComponent(RenderComponent.class) != null) {
      getEntities().add(e);
    }
  }

  @Override
  public void update(float dt) {
    getEntities().forEach(e -> e.update(dt));
  }

  @Override
  public void init() {
    //Init code.
  }
}
