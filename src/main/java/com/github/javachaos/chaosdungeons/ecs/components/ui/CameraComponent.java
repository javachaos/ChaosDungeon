package com.github.javachaos.chaosdungeons.ecs.components.ui;

import com.github.javachaos.chaosdungeons.ecs.components.render.RenderComponent;
import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.graphics.Camera;
import com.github.javachaos.chaosdungeons.graphics.Transform;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import java.util.List;

/**
 * A camera component.
 */
@SuppressWarnings("unused")
public class CameraComponent extends RenderComponent {
  private final Camera camera = new Camera(GameWindow.getWindowSize().getWidth(),
      GameWindow.getWindowSize().getHeight());
  private List<SpriteComponent> sprites;

  @Override
  public void update(double dt) {
  }

  @Override
  public void render(double dt) {
    GameEntity ge = ((GameEntity) getEntity());
    Transform t = ge.getTransform();
    GameWindow.getWorldShader().bind();
    GameWindow.getWorldShader().setSampleTexture(0);
    GameWindow.getWorldShader().setUniform("projection",
        t.getProjection(camera.getProjection()));
    sprites.forEach(s -> s.render(dt));
    GameWindow.getWorldShader().unbind();
  }

  @Override
  public void onAdded(Entity e) {
    if (!e.hasComponent(SpriteComponent.class)) {
      e.removeComponent(this);
      throw new RuntimeException("Entity does not have a sprite component,"
          + " cannot add CameraComponent.");
    }
    sprites = e.getComponents(SpriteComponent.class);
  }

  @Override
  public void onRemoved(Entity e) {
  }
}
