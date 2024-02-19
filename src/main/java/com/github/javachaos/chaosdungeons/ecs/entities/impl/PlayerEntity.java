package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.geometry.util.ShapeBuilder;
import com.github.javachaos.chaosdungeons.gui.GameWindow;

/**
 * Player entity class.
 */
@SuppressWarnings("unused")
public class PlayerEntity extends GameEntity {

  /**
   * Create a new player entity.
   */
  public PlayerEntity(GameContext gameContext) {
    super("assets/textures/player.png",
        new SpawnData.Builder()
            .setRestitution(1f)
            .setMass(1f)
            .setGravitationFactor(0f)
            .setMaxSpawns(1)
            .setSpawnRate(1)
            .setShape(new ShapeBuilder.Rectangle().build())
            .build(), gameContext);
  }

  @Override
  public void update(float dt) {
    //Unused
  }

  @Override
  public void destroy() {
    //Unused
  }
}
