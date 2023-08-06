package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.components.ui.CameraComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;

/**
 * Create a new Default game camera.
 */
public class DefaultCamera extends GameEntity {
  @Override
  public void onAdded(GameEntity e) {

  }

  @Override
  public void onRemoved(GameEntity e) {

  }

  @Override
  public void init() {
    addComponent(new CameraComponent());
  }

  @Override
  protected void update(float dt) {

  }

  @Override
  public void destroy() {

  }
}
