package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Component class for ECS.
 */
public abstract class Component {

  private Entity entity;

  private static final AtomicInteger removalCount = new AtomicInteger(0);

  private boolean markedForRemoval;
  private int id;

  /**
   * Create a new component with id.
   *
   */
  public Component() {
  }

  public void setEntity(Entity e) {
    this.entity = e;
  }

  public abstract void update(double dt);

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Mark this component for removal. It will no longer be updated
   * but will remain in memory until a set number of components
   * are ready to be removed, then they are removed in one batch
   * removal operation.
   */
  public void remove() {
    markedForRemoval = true;
    removalCount.incrementAndGet();
    onRemoved(getEntity());
  }

  public static int getRemovalCount() {
    return removalCount.get();
  }

  public Entity getEntity() {
    return entity;
  }

  public boolean isRemoved() {
    return markedForRemoval;
  }

  public abstract void destroy();

  public abstract void onAdded(Entity e);

  public abstract void onRemoved(Entity e);

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
