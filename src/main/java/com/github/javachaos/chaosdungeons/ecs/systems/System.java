package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * System class for ECS.
 */
@SuppressWarnings("unused")
public abstract class System {

  private static final List<Entity> entities = new CopyOnWriteArrayList<>();

  public System(GameWindow window) {
  }

  /**
   * Update method for this system.
   *
   * @param dt the delta time between updates
   */
  protected abstract void update(float dt);

  public void update(double dt) {
    getEntities().forEach(e -> e.update(dt));
    update((float) dt);
  }

  /**
   * Get the entities associated with this system.
   *
   * @return the list of entities for this System.
   */
  public List<Entity> getEntities() {
    return entities;
  }

  public void shutdown() {
    entities.forEach(Entity::destroy);
  }

  public abstract void initSystem();

  public abstract void destroy();
}
