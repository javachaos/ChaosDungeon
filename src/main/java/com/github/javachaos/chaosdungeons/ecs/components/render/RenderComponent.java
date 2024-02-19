package com.github.javachaos.chaosdungeons.ecs.components.render;

import com.github.javachaos.chaosdungeons.ecs.components.Component;
import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.utils.MatrixUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Render component class.
 */
public abstract class RenderComponent extends Component {

  /**
   * Create a new component.
   */
  protected RenderComponent() {
    super();
  }

  @Override
  public void update(double dt) {
    GameEntity ge = (GameEntity) getEntity();
    GameContext gameContext = ge.getContext();

    if (this instanceof UiRenderComponent) {
      Vector3f pos = ge.getTransformComponent().getPosition();
      gameContext.setCurrentShader("ui");
      gameContext.getCurrentShader().bind();
      gameContext.getCurrentShader().setUniform("pos", new Vector2f(pos.x, pos.y));
    } else {
      gameContext.setCurrentShader("world");
      gameContext.getCurrentShader().bind();
      gameContext.getCurrentShader().setSampleTexture(0);
      gameContext.getCurrentShader().setUniform("transformation", ge.getTransformComponent().getTransform());
      gameContext.getCurrentShader().setUniform("view", MatrixUtils.createViewMatrix(GameWindow.getCamera()));
      gameContext.getCurrentShader().loadProjection();
    }

    render(dt);
    gameContext.getCurrentShader().unbind();
  }

  public abstract void render(double dt);

  @Override
  public void destroy() {
  }
}
