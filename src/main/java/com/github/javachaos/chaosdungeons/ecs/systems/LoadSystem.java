package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;

/**
 * Initialize all entities.
 */
public class LoadSystem extends System {
  public LoadSystem(GameWindow window) {
    super(window);
  }

  @Override
  protected void update(float dt) {
  }

  @Override
  public void initSystem() {
    getEntities().forEach(Entity::init);
  }

  @Override
  public void destroy() {

  }
}
