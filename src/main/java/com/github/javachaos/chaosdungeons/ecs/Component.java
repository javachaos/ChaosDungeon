package com.github.javachaos.chaosdungeons.ecs;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Component class for ECS.
 */
public abstract class Component {

  private Entity entity;

  private static final AtomicInteger removalCount = new AtomicInteger(0);

  private boolean markedForRemoval;
  private final int id;

  /**
   * Create a new component with id.
   *
   * @param id the id of this component
   */
  public Component(int id) {
    this.id = id;
  }

  protected void setEntity(Entity e) {
    this.entity = e;
  }

  public abstract void update(double dt);

  protected int getId() {
    return id;
  }

  public void remove() {
    markedForRemoval = true;
    removalCount.incrementAndGet();
  }

  public static int getRemovalCount() {
    return removalCount.get();
  }

  public Entity getEntity() {
    return entity;
  }

  protected boolean isRemoved() {
    return markedForRemoval;
  }

  public abstract void destroy();
}
