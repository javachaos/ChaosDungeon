package com.github.javachaos.chaosdungeons.ecs;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Simple Entity class.
 *
 * @author Javachaos
 */
@SuppressWarnings("unused")
public abstract class Entity extends Component {

  /**
   * Basic idea here, each entity has a list of components.
   * Store these components via class type, if there are multiples of
   * the same class type, we store them via their id.
   * If two components share the same id, then only one instance is kept.
   */
  private final Map<Class<? extends Component>, SortedMap<Integer, Component>> components;

  public Entity(int id) {
    super(id);
    components = new HashMap<>();
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
    components.get(c.getClass()).remove(c.getId());
  }

  public abstract void update(float dt);

  /**
   * Update order based on id value. (Lower ids are updated first)
   */
  @Override
  public void update(double dt) {
    components.values().forEach(c -> c.values().forEach(cc -> cc.update(dt)));
    update((float) dt);
  }
}
