package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.graphics.SpriteModel;
import com.github.javachaos.chaosdungeons.graphics.Texture;
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
  private final String texturePath;

  /**
   * Create a game entity.
   */
  public GameEntity(String texturePath) {
    super();
    this.texturePath = texturePath;
    this.transform = new Transform();
  }

  @Override
  public void init() {
    addComponent(new SpriteComponent(new SpriteModel(new Texture(texturePath))));
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
