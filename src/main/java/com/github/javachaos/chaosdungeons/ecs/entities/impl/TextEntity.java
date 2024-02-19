package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.components.render.UiSpriteComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.graphics.text.TextModel;
import com.github.javachaos.chaosdungeons.graphics.text.UiTextModel;

/**
 * Text entity.
 */
public class TextEntity extends GameEntity {

  private final String fontPath;
  private TextModel tm;

  public TextEntity(String fontPath, GameContext gameContext) {
    super(null, gameContext);
    this.fontPath = fontPath;
  }

  @Override
  public void init() {
    super.init();
    tm = new UiTextModel(fontPath, this);
    sprite = new UiSpriteComponent(tm);
    addComponent(sprite);
  }

  public void setText(String text) {
    tm.setText(text);
  }

  @Override
  protected void update(float dt) {
    //Unused
  }

  @Override
  public void destroy() {
    //Unused
  }
}
