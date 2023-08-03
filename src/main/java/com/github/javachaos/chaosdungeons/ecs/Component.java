package com.github.javachaos.chaosdungeons.ecs;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Component class for ECS.
 */
public abstract class Component {

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

  abstract void update(double dt);

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

  protected boolean isRemoved() {
    return markedForRemoval;
  }
}
