package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.components.GravityComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.SpriteEntity;
import org.joml.Vector3f;

/**
 * Create a new fireball entity.
 */
public class Fireball extends SpriteEntity {
  public Fireball() {
    super("assets/textures/fireball.png");
  }

  @Override
  public void init() {
    super.init();
    addComponent(new GravityComponent(10.0, 2.0,
        new Vector3f(0.0f, 0.0f, 0.0f)));
  }
}
