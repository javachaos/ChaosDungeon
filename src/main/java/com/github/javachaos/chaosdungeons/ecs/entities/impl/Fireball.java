package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.ecs.entities.TextEntity;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.ecs.systems.System;
import java.util.Objects;

/**
 * Create a new fireball entity.
 */
@SuppressWarnings("unused")
public class Fireball extends GameEntity {

  /**
   * Create a new fireball.
   */
  public Fireball() {
    super("assets/textures/fireball.png",
        new SpawnData.Builder().build());
  }

  public Fireball(SpawnData data) {
    super("assets/textures/fireball.png",
        data);
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
  }

  @Override
  protected void update(float dt) {
  }

  @Override
  public void destroy() {
  }
}
