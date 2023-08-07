package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.components.GravityComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import org.joml.Vector3f;

/**
 * Create a new fireball entity.
 */
@SuppressWarnings("unused")
public class Fireball extends GameEntity {
  private final Vector3f initialV;
  private final Vector3f initialAngularVelocity;

  private static final float scale = 1.0f;

  /**
   * Create a new fireball.
   */
  public Fireball() {
    super("assets/textures/fireball.png");
    initialV = new Vector3f(0);
    this.initialAngularVelocity = new Vector3f();
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
        new Vector3f(x, y, 0), new Vector3f(0), new Vector3f(scale));
    this.initialV = initialV;
    this.initialAngularVelocity = new Vector3f();
  }

  /**
   * Create a new fireball at (x, y) which initial velocity initialV and initial angular velocity
   * initialAngularVelocity.
   *
   * @param x the x-position
   * @param y the y-position
   * @param initialV the initial velocity
   * @param initialAngularVelocity the initial angular velocity
   */
  public Fireball(float x, float y, Vector3f initialV, Vector3f initialAngularVelocity) {
    super("assets/textures/fireball.png",
        new Vector3f(x, y, 0),
        new Vector3f(0),
        new Vector3f(scale));
    this.initialV = initialV;
    this.initialAngularVelocity = initialAngularVelocity;
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
    addComponent(new GravityComponent(100.0, 1.0, initialV,
        initialAngularVelocity));
  }

  @Override
  protected void update(float dt) {
  }

  @Override
  public void destroy() {
  }
}
