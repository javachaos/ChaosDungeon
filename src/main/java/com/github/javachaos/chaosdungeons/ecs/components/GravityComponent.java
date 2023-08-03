package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.Component;

/**
 * A gravity component.
 */
@SuppressWarnings("unused")
public class GravityComponent extends Component {

  /**
   * Create a new component with id.
   *
   * @param id the id of this component
   */
  public GravityComponent(int id) {
    super(id);
  }

  @Override
  public void update(double dt) {
    getEntity().getComponents(PhysicsComponent.class)
        .forEach(c -> c.applyForce(0.0, -9.8));
  }

  @Override
  public void destroy() {
  }
}
