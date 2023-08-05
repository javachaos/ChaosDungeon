package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.geometry.SpriteModel;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.utils.Texture;

public class SpriteEntity extends GameEntity {

  private final String texturePath;

  public SpriteEntity(String texturePath) {
    super();
    this.texturePath = texturePath;
  }

  @Override
  public void onAdded(GameEntity e) {
  }

  @Override
  public void onRemoved(GameEntity e) {
  }

  @Override
  public void init() {
    addComponent(new SpriteComponent(
        new SpriteModel(new Texture(texturePath),
        GameWindow.getShader())));
  }

  @Override
  protected void update(float dt) {

  }

  @Override
  public void destroy() {

  }
}
