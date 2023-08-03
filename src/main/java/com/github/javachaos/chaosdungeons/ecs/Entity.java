package com.github.javachaos.chaosdungeons.ecs;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Simple Entity class.
 *
 * @author Javachaos
 */
@SuppressWarnings("unused")
public abstract class Entity extends Component {

  private static final int REMOVAL_THRESHOLD = 256;

  /**
   * Executor for removing items from entities.
   */
  private static final Executor executor = Executors.newSingleThreadExecutor();

  /**
   * Basic idea here, each entity has a list of components.
   * Store these components via class type, if there are multiples of
   * the same class type, we store them via their id.
   * If two components share the same id, then only one instance is kept.
   */
  private static final Map<Class<? extends Component>, SortedMap<Integer, Component>> components =
      Collections.synchronizedMap(new HashMap<>());

  public Entity(int id) {
    super(id);
  }

  /**
   * Add a component to this entity.
   *
   * @param c the component to add.
   * @param <T> the component type
   */
  public <T extends Component> void addComponent(T c) {
    if (components.containsKey(c.getClass())) {
      components.get(c.getClass()).put(c.getId(), c);
    } else {
      components.put(c.getClass(), Collections.synchronizedSortedMap(new TreeMap<>()));
    }
  }

  public <T extends Component> Component getComponent(Class<T> clazz, int id) {
    return components.get(clazz).get(id);
  }

  public <T extends Component> List<Component> getComponents(Class<T> clazz) {
    return (List<Component>) components.get(clazz).values();
  }

  public <T extends Component> void removeComponent(T c) {
    components.get(c.getClass()).get(c.getId()).remove();
  }

  public abstract void update(float dt);

  /**
   * Update order based on id value. (Lower ids are updated first)
   */
  @Override
  public void update(double dt) {
    components.values().forEach(
        c -> c.values().stream()
              .filter(v -> !v.isRemoved())
              .forEach(cc -> cc.update(dt)));
    update((float) dt);
    checkRemoval();
  }

  private void checkRemoval() {
    if (Component.getRemovalCount() > REMOVAL_THRESHOLD) {
      executor.execute(() -> { // execute potentially long-running operation on a background thread.
        Set<Component> remove = new HashSet<>();
        components.values().forEach(c -> c.values().stream().filter(Component::isRemoved)
            .forEach(remove::add));
        remove.forEach(c -> components.get(c.getClass()).remove(c.getId()));
      });
    }
  }
}
