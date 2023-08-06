package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.components.ui.CameraComponent;
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
    getTransform().setScale(new Vector3f(2, 2, 1));
    addComponent(true, new CameraComponent());
  }

  @Override
  protected void update(float dt) {

  }

  @Override
  public void destroy() {

  }
}
