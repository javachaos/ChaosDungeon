package com.github.javachaos.chaosdungeons.ecs.components.render;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.graphics.SpriteModel;

/**
 * Sprite component class.
 */
public class SpriteComponent extends RenderComponent {

  private final SpriteModel img;
  private boolean isVisible = true;

  /**
   * Create a new component.
   */
  public SpriteComponent(SpriteModel img) {
    super();
    this.img = img;
  }

  @Override
  public void render(double dt) {
    if (isVisible) {
      img.render();
    }
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {
    img.delete();
  }

  @SuppressWarnings("unused")
  public void setVisible(boolean visible) {
    this.isVisible = visible;
  }

}
