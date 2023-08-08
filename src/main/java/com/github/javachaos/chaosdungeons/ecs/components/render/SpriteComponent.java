package com.github.javachaos.chaosdungeons.ecs.components.render;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.graphics.SpriteModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Sprite component class.
 */
public class SpriteComponent extends RenderComponent {

  private static final Logger LOGGER = LogManager.getLogger(SpriteComponent.class);

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
    } else {
      img.delete();
    }
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {
    setVisible(false);
    render(0);
  }

  @SuppressWarnings("unused")
  public void setVisible(boolean visible) {
    this.isVisible = visible;
  }

  @Override
  public void destroy() {
    LOGGER.debug("Sprite component destroyed.");
    img.delete();
    super.destroy();
  }

}
