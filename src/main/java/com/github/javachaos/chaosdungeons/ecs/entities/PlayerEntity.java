package com.github.javachaos.chaosdungeons.ecs.entities;

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
