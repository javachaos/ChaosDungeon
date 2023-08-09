package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.ecs.components.Component;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.utils.AutoDiscardingDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * System class for ECS.
 */
@SuppressWarnings("unused")
public abstract class System {

  protected static QuadTree collisionQuadtree = new QuadTree();
  private static final Map<Class<? extends GameEntity>, AutoDiscardingDeque<GameEntity>> entityMap =
      new ConcurrentHashMap<>();

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

  /**
   * Add an entity to this System.
   *
   * @param e     the entity to be added.
   * @param front true if the entity should be inserted at the front
   *              of this list of entities
   */
  public static <T extends GameEntity> void addEntity(T e, boolean front) {
    collisionQuadtree.insert(e.getPosition().x, e.getPosition().y, e.getCollisionComponent());
    if (!entityMap.containsKey(e.getClass())) {
      entityMap.put(e.getClass(), new AutoDiscardingDeque<>(Constants.MAX_ENTITIES));
    }
    if (front) {
      entityMap.get(e.getClass()).offerFirst(e);
    } else {
      entityMap.get(e.getClass()).offerLast(e);
    }
  }

  public static <T extends GameEntity> T getEntity(Class<T> entityClass) {
    Deque<GameEntity> entities = entityMap.get(entityClass);

    if (entities != null && !entities.isEmpty()) {
      return entityClass.cast(entities.getFirst());
    }
    return null; // No entity of the specified class found
  }

  public <T extends GameEntity> Deque<GameEntity> getEntities(T clazz) {
    return entityMap.get(clazz.getClass());
  }

  /**
   * Get all entities associated with this system.
   *
   * @return the list of entities for this System.
   */
  public static Deque<GameEntity> getEntities() {
    return coalesceDeques(new ArrayList<>(entityMap.values()));
  }

  /**
   * Coalesce a list of deques into one list.
   *
   * @param dequeList input list
   * @param <T> the type of elements in each deque
   * @return a deque of each smaller deque merged together
   */
  public static <T> Deque<T> coalesceDeques(List<AutoDiscardingDeque<T>> dequeList) {
    AutoDiscardingDeque<T> coalescedDeque = new AutoDiscardingDeque<>();

    for (Deque<T> deque : dequeList) {
      coalescedDeque.addAll(deque);
    }

    return coalescedDeque;
  }

  public static void shutdown() {
    getEntities().forEach(Entity::shutdown);
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
