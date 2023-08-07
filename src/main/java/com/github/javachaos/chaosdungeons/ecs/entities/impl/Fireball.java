package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.components.GravityComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import org.joml.Vector3f;

/**
 * Create a new fireball entity.
 */
public class Fireball extends GameEntity {
  private final Vector3f initialV;

  public Fireball() {
    super("assets/textures/fireball.png");
    initialV = new Vector3f(0);
  }

  /**
   * Create a new fireball!.
   *
   * @param x initial x position
   * @param y initial y position
   * @param initialV initial velocity
   */
  public Fireball(float x, float y, Vector3f initialV) {
    super("assets/textures/fireball.png",
        new Vector3f(x, y, 0), new Vector3f(0), new Vector3f(1));
    this.initialV = initialV;
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
    addComponent(new GravityComponent(100.0, 1.0, initialV));
  }

  @Override
  protected void update(float dt) {
  }

  @Override
  public void destroy() {
  }
}
