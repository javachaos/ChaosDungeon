package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.graphics.text.TextModel;
import com.github.javachaos.chaosdungeons.graphics.text.UiTextModel;

/**
 * Text entity.
 */
public class TextEntity extends GameEntity {

  private final String fontPath;
  private TextModel tm;

  public TextEntity(String fontPath) {
    super(null);
    this.fontPath = fontPath;
  }

  @Override
  public void init() {
    tm = new UiTextModel(fontPath, this);
    sprite = new SpriteComponent(tm);
    addComponent(sprite);
  }

  public void setText(String text) {
    tm.setText(text);
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {
  }

  @Override
  protected void update(float dt) {
  }

  @Override
  public void destroy() {
  }
}
