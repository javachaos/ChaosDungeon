package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

  public void update(double dt) { // update all systems.
    getEntities().forEach(e -> e.update(dt));
    update((float) dt);
  }

  public static void addEntity(GameEntity e, boolean front) {
    if (front) {
      entities.add(0, e);
    } else {
      entities.add(e);
    }
  }

  /**
   * Get the entities associated with this system.
   *
   * @return the list of entities for this System.
   */
  public List<Entity> getEntities() {
    return entities;
  }

  public static void shutdown() {
    entities.forEach(Entity::shutdown);
  }

  /**
   * Initialize this system.
   */
  public abstract void initSystem();

  /**
   * Called before shutdown, all entities at this point will have
   * been shutdown. Used when you have extra resources not tied
   * to entities you wish to destroy.
   */
  public abstract void destroy();
}
