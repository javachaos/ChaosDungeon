package com.github.javachaos.chaosdungeons.ecs.entities.factory.impl;

import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.EntityFactory;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.FireballEntity;

/**
 * Fireball factory for creating fireballs.!
 */
public class FireballEntityFactory implements EntityFactory<FireballEntity> {
  @Override
  public FireballEntity create(SpawnData data, GameContext gameContext) {
    return new FireballEntity(data, gameContext);
  }
}
