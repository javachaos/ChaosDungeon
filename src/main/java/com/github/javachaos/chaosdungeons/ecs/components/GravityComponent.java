package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;

/**
 * A gravity component.
 */
@SuppressWarnings("unused")
public class GravityComponent extends Component {

  /**
   * Create a new component.
   */
  public GravityComponent() {
    super();
  }

  @Override
  public void update(double dt) {
    getEntity().getComponents(PhysicsComponent.class)
        .forEach(c -> c.applyForce(0.0, -9.8));
  }

  @Override
  public void destroy() {
  }

  @Override
  public void onAdded(GameEntity e) {

  }

  @Override
  public void onRemoved(GameEntity e) {

  }
}
