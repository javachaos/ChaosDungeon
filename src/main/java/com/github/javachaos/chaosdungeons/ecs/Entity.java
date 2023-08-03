package com.github.javachaos.chaosdungeons.ecs;

import com.github.javachaos.chaosdungeons.constants.Constants;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
   * Executor for removing items from entities.
   */
  private static final ExecutorService executor = Executors.newSingleThreadExecutor();

  /**
   * Basic idea here, each entity has a list of components.
   * Store these components via class type, if there are multiples of
   * the same class type, we store them via their id.
   * If two components share the same id, then only one instance is kept.
   */
  private static final Map<Class<? extends Entity>, SortedMap<Integer, Component>> components =
      Collections.synchronizedMap(new HashMap<>());

  public Entity(int id) {
    super(id);
  }

  /**
   * Add a component to this entity.
   *
   * @param c the component to add.
   */
  public void addComponent(Component c) {
    if (components.containsKey(getClass())) {
      c.setEntity(this);
      components.get(getClass()).put(c.getId(), c);
    } else {
      components.put(getClass(), Collections.synchronizedSortedMap(new TreeMap<>()));
    }
  }

  /**
   * Get a component from this entity with the id, id.
   *
   * @param id the id of the desired component
   * @return the component with id, id
   */
  public Component getComponent(int id) {
    return components.get(getClass()).get(id);
  }

  /**
   * Get a component for this entity based on it's class type.
   *
   * @param clazz the type of component.
   * @return the component of type clazz
   *
   */
  public <T extends Component> T getComponent(Class<T> clazz) {
    return components.get(getClass()).values()
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
    return components.get(getClass()).values()
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
    return (List<Component>) components.get(getClass()).values();
  }

  /**
   * Remove the component c from this entity if it exists.
   *
   * @param c the component to be removed.
   */
  public void removeComponent(Component c) {
    List<Component> comps = (List<Component>) components.get(getClass()).values();
    if (comps.contains(c)) {
      components.get(c.getClass()).get(c.getId()).remove();
    }
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
        remove.forEach(Component::destroy);
        remove.forEach(c -> components.get(c.getClass()).remove(c.getId()));
      });
    }
  }

  public abstract void destroy();

  /**
   * Release all components in the ECS framework.
   */
  public void shutdownComponentRegistry() {
    executor.execute(
        () -> components.values().forEach(v -> v.values().forEach(Component::destroy)));
    try {
      int i = 0;
      // Wait for the executor to terminate, specifying a reasonable timeout
      while (!executor.awaitTermination(Constants.DEFAULT_SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS)
             && i < 10) {
        LOGGER.debug("Component registry shutdown attempt {}.", i);
        i++;
      }
      executor.shutdownNow();
      if (executor.awaitTermination(Constants.DEFAULT_SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS)) {
        LOGGER.debug("Component registry shutdown forcibly.");
      } else {
        LOGGER.error("Component registry failed to shutdown.");
        throw new RuntimeException("Component registry failed to shutdown.");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

}
