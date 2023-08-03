package com.github.javachaos.chaosdungeons.ecs;

import java.util.List;

/**
 * System class for ECS.
 */
@SuppressWarnings("unused")
public abstract class System {
  private List<Entity> entities;

  /**
   * Update method for this system.
   *
   * @param dt the delta time between updates
   */
  public abstract void update(float dt);

  public void update(double dt) {
    getEntities().forEach(e -> e.update(dt));
  }

  /**
   * Get the entities associated with this system.
   *
   * @return the list of entities for this System.
   */
  public List<Entity> getEntities() {
    return entities;
  }
}
