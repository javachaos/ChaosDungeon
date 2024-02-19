package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;

/**
 * Component class for ECS.
 */
public abstract class Component {

  private long id;

  private Entity entity;

  public void setEntity(Entity e) {
    this.entity = e;
  }

  public abstract void update(double dt);

  public Entity getEntity() {
    return entity;
  }

  public abstract void destroy();

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  public void setIndex(long l) {
    this.id = l;
  }

  public long getIndex() {
    return id;
  }
}
