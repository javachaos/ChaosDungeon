package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.geometry.SpriteModel;
import com.github.javachaos.chaosdungeons.graphics.Texture;

/**
 * Sprite entity class.
 */
public class SpriteEntity extends GameEntity {

  private final String texturePath;

  public SpriteEntity(String texturePath) {
    super();
    this.texturePath = texturePath;
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {
  }

  @Override
  public void init() {
    addComponent(new SpriteComponent(
        new SpriteModel(new Texture(texturePath))));
  }

  @Override
  protected void update(float dt) {

  }

  @Override
  public void destroy() {

  }
}
