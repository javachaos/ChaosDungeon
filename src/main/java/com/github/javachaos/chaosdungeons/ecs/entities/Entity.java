package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.Component;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple Entity class.
 *
 * @author Javachaos
 */
@SuppressWarnings("unused")
public abstract class Entity extends Component {

  private static final Logger LOGGER = LogManager.getLogger(Entity.class);

  private static final int REMOVAL_THRESHOLD = 256;

  /**
   * Basic idea here, each entity has a list of components.
   * Store these components via class type, if there are multiples of
   * the same class type, we store them via their id.
   * If two components share the same id, then only one instance is kept.
   */
  private final Map<Class<? extends Entity>, Deque<Component>> components =
      Collections.synchronizedMap(new HashMap<>());

  public Entity() {
    super();
  }

  public abstract void init();

  /**
   * Add a component to this entity. (same as addComponent(false, c))
   *
   * @param c the component to add.
   */
  public void addComponent(Component c) {
    if (!components.containsKey(getClass())) {
      components.put(getClass(), new ConcurrentLinkedDeque<>());
    }
    c.setEntity(this);
    c.onAdded(this);
    components.get(getClass()).offerLast(c);
  }

  /**
   * Add a component to this entity.
   *
   * @param front true if the component c should be added to the front
   *              of the list of components.
   * @param c the component to add.
   */
  public void addComponent(boolean front, Component c) {
    if (!components.containsKey(getClass())) {
      components.put(getClass(), new ConcurrentLinkedDeque<>());
    }
    if (front) {
      c.setEntity(this);
      c.onAdded(this);
      components.get(getClass()).offerFirst(c);
    } else {
      c.setEntity(this);
      c.onAdded(this);
      components.get(getClass()).offerLast(c);
    }
  }

  /**
   * Get a component for this entity based on it's class type.
   *
   * @param clazz the type of component.
   * @return the component of type clazz
   *
   */
  public <T extends Component> T getComponent(Class<T> clazz) {
    return components.get(getClass())
           .stream()
           .filter(x -> x.getClass().equals(clazz))
           .findFirst()
           .map(clazz::cast)
           .orElse(null);
  }

  /**
   * Get all components of type clazz for this entity.
   *
   * @param clazz the type of components
   * @return the list of components of type clazz
   */
  public <T extends Component> List<T> getComponents(Class<T> clazz) {
    return components.get(getClass())
           .stream()
           .filter(x -> x.getClass().equals(clazz))
           .map(clazz::cast) // Cast the component to the desired type
           .collect(Collectors.toList());
  }

  /**
   * Return the list of components associated with this Entity.
   *
   * @return the components of this entity.
   */
  public List<Component> getComponents() {
    return components.get(getClass())
        .stream()
        .map(Component.class::cast)
        .collect(Collectors.toList());
  }

  /**
   * Remove the component c from this entity if it exists.
   *
   * @param c the component to be removed.
   */
  public void removeComponent(Component c) {
    getComponents().remove(c);
  }

  protected abstract void update(float dt);

  /**
   * Update order based on id value. (Lower ids are updated first)
   */
  @Override
  public void update(double dt) {
    for (Component c : getComponents()) {
      if (!c.isRemoved()) {
        c.update(dt);
      }
    }
    update((float) dt);
    checkRemoval();
  }

  /**
   * Check if there are enough removed components to warrant a
   * large removal operation.
   */
  private void checkRemoval() {
    if (Component.getRemovalCount() > REMOVAL_THRESHOLD) {
      Set<Component> remove = new HashSet<>();
      components.values().forEach(c -> c.stream().filter(Component::isRemoved)
          .forEach(remove::add));
      if (Entity.this instanceof GameEntity) {
        remove.forEach(c -> onRemoved(Entity.this));
      }
      remove.forEach(Component::destroy);
      remove.forEach(c -> components.get(c.getClass()).remove(c));
    }
  }

  /**
   * Destroy this entity.
   */
  public abstract void destroy();

  public void shutdown() {
    components.values().forEach(v -> v.forEach(Component::destroy));
    destroy();
  }

  /**
   * Returns true if this entity contains at least 1 instance of component.
   *
   * @param component the component to look for.
   * @return true if this entity contains at least 1 of component
   */
  public boolean hasComponent(Component component) {
    return getComponents().contains(component);
  }

  /**
   * Returns true if this entity contains at least 1 instance of component.
   *
   * @param component the component to look for.
   * @return true if this entity contains at least 1 of component
   */
  public <T extends Component> boolean hasComponent(Class<T> component) {
    Deque<Component> comps = components.get(getClass());
    if (comps == null) {
      return false;
    }
    return comps.stream().anyMatch(c -> component.isAssignableFrom(c.getClass()));
  }
}
