package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.Component;

/**
 * Render component class.
 */
public abstract class RenderComponent extends Component {

  /**
   * Create a new component with id.
   *
   * @param id the id of this component
   */
  public RenderComponent(int id) {
    super(id);
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
