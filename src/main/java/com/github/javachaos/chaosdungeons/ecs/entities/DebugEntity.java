package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.render.AxisDrawerComponent;

/**
 * Debug entity for displaying debug information in screen space.
 */
public class DebugEntity extends GameEntity {
  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {

  }

  @Override
  public void init() {
    addComponent(new AxisDrawerComponent());
  }

  @Override
  protected void update(float dt) {

  }

  @Override
  public void destroy() {

  }
}
