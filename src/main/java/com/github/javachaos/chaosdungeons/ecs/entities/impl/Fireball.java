package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.components.GravityComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import org.joml.Vector3f;

/**
 * Create a new fireball entity.
 */
public class Fireball extends GameEntity {
  public Fireball() {
    super("assets/textures/fireball.png");
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {
  }

  @Override
  public void init() {
    super.init();
    addComponent(true, new GravityComponent(100.0, 1.0, new Vector3f(0)));
  }

  @Override
  protected void update(float dt) {
  }

  @Override
  public void destroy() {
  }
}
