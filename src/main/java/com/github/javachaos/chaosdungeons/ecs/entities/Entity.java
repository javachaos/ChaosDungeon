package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.Component;

import java.util.*;

import com.github.javachaos.chaosdungeons.ecs.components.render.RenderComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

/**
 * Simple Entity class.
 *
 * @author Javachaos
 */
@SuppressWarnings("unused")
public abstract class Entity {

  private static final Logger LOGGER = LogManager.getLogger(Entity.class);

  private static final int REMOVAL_THRESHOLD = 256;
  private long id;
  private long eid;
  protected Vector2f pos;
  private final Map<Long, Component> components = new LinkedHashMap<>();

  protected Entity() {
    super();
    id = 0;
  }

  public abstract void init();

  /**
   * Add a component to this entity. (same as addComponent(false, c))
   *
   * @param c the component to add.
   */
  public void addComponent(Component c) {
    if (c != null) {
      c.setIndex(id++);
      components.put(id, c);
      c.setEntity(this);
    }
  }

  public <T extends Component> T getComponent(Class<T> clazz) {
    return components.values().stream()
            .filter(x -> clazz.isAssignableFrom(x.getClass()))
            .findFirst().map(clazz::cast).orElse(null);
  }

  public <T extends Component> List<T> getComponents(Class<T> clazz) {
    return components.values().stream()
            .filter(x -> clazz.isAssignableFrom(x.getClass()))
            .map(clazz::cast)
            .toList();
  }

  public List<RenderComponent> getRenderComponents() {
    return components.values().stream().filter(RenderComponent.class::isInstance)
            .map(RenderComponent.class::cast)
            .toList();
  }

  public List<Component> getComponents() {
    return List.copyOf(components.values());
  }

  /**
   * Remove the component c from this entity if it exists.
   *
   * @param c the component to be removed.
   */
  public void removeComponent(Component c) {
    components.remove(c.getIndex());
  }

  protected abstract void update(float dt);

  /**
   * Update order based on id value. (Lower ids are updated first)
   */
  public void update(double dt) {
    for (Component c : getComponents()) {
        c.update(dt);
    }
    update((float) dt);
  }

  /**
   * Destroy this entity.
   */
  public abstract void destroy();

  public void shutdown() {
    components.values().forEach(Component::destroy);
    destroy();
  }

  public boolean hasComponent(Component component) {
    return components.containsValue(component);
  }

  public <T extends Component> boolean hasComponent(Class<T> component) {
    return components.values().stream()
            .anyMatch(component::isInstance);
  }

  public void setEntityId(long i) {
    this.eid = i;
  }

  public long getEntityId() {
    return eid;
  }

}
