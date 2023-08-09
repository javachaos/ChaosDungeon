package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.components.GravityComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.ecs.systems.System;
import com.github.javachaos.chaosdungeons.gui.fonts.RenderedTextEntity;
import java.util.Objects;
import org.joml.Vector3f;

/**
 * Create a new fireball entity.
 */
@SuppressWarnings("unused")
public class Fireball extends GameEntity {
  private final Vector3f initialV;
  private final Vector3f initialAngularVelocity;

  private static final float scale = 1f;

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
    this.initialV = new Vector3f(initialV);
    this.initialAngularVelocity = new Vector3f(initialAngularVelocity);
  }

  public Fireball(SpawnData data) {
    this(data.getPosition().x, data.getPosition().y,
        data.getInitialVelocity(), data.getInitialVelocity());
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
        initialAngularVelocity, 0.1));
  }

  @Override
  protected void update(float dt) {
    RenderedTextEntity rte = Objects.requireNonNull(System.getEntity(RenderedTextEntity.class));
    rte.getModelMatrix().rotation(45, new Vector3f(1,0,0));
    rte.setText("Test");
  }

  @Override
  public void destroy() {
  }
}
