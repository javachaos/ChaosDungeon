package com.github.javachaos.chaosdungeons.ecs;

/**
 * Component class for ECS.
 */
public abstract class Component {

  private final int id;

  /**
   * Create a new component with id.
   *
   * @param id the id of this component
   */
  public Component(int id) {
    this.id = id;
  }

  abstract void update(double dt);

  protected int getId() {
    return id;
  }
}
