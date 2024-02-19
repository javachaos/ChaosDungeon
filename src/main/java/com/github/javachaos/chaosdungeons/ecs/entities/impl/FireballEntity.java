package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;

/**
 * Create a new fireball entity.
 */
@SuppressWarnings("unused")
public class FireballEntity extends GameEntity {

  /**
   * Create a new fireball.
   */
  public FireballEntity(GameContext gameContext) {
    super("assets/textures/fireball.png",
        new SpawnData.Builder().build(), gameContext);
  }

  public FireballEntity(SpawnData data, GameContext gameContext) {
    super("assets/textures/fireball.png",
        data, gameContext);
  }

  @Override
  protected void update(float dt) {
    //Unused
  }

  @Override
  public void destroy() {
    //Unused
  }
}
