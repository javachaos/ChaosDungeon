package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.graphics.Transform;

/**
 * Game entity class.
 */
@SuppressWarnings("unused")
public abstract class GameEntity extends Entity {

  /**
   * Transform.
   */
  private Transform transform;

  /**
   * Create a game entity.
   */
  public GameEntity() {
    super();
    transform = new Transform();
  }

  /**
   * Set the transform for this entity.
   *
   * @param transform the transform
   */
  public void setTransform(Transform transform) {
    this.transform = transform;
  }

  public Transform getTransform() {
    return transform;
  }
}
