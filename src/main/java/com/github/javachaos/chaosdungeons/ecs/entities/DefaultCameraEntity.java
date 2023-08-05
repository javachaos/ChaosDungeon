package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.ui.CameraComponent;

public class DefaultCameraEntity extends GameEntity {
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
