package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.ui.CameraComponent;

/**
 * Player entity class.
 */
@SuppressWarnings("unused")
public class PlayerEntity extends GameEntity {

  /**
   * Create a new player entity.
   */
  public PlayerEntity() {
    super("assets/textures/player.png");
  }

  @Override
  public void init() {
    addComponent(new CameraComponent());
  }

  @Override
  public void update(float dt) {

  }

  @Override
  public void destroy() {
  }

  @Override
  public void onAdded(Entity e) {

  }

  @Override
  public void onRemoved(Entity e) {

  }
}
