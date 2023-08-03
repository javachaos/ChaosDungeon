package com.github.javachaos.chaosdungeons.ecs.components;

import java.awt.image.BufferedImage;

/**
 * Sprite component class.
 */
@SuppressWarnings("unused")
public class SpriteComponent extends RenderComponent {

  private BufferedImage png;

  /**
   * Create a new component with id.
   *
   * @param id the id of this component
   */
  public SpriteComponent(int id) {
    super(id);
  }

  @Override
  public void render(double dt) {

  }
}
