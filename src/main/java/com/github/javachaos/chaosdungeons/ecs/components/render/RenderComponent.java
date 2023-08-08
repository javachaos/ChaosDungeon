package com.github.javachaos.chaosdungeons.ecs.components.render;

import com.github.javachaos.chaosdungeons.ecs.components.Component;

/**
 * Render component class.
 */
public abstract class RenderComponent extends Component {

  /**
   * Create a new component.
   */
  public RenderComponent() {
    super();
  }

  @Override
  public void update(double dt) {
    render(dt);
  }

  public abstract void render(double dt);

  @Override
  public void destroy() {
  }
}
