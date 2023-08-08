package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import com.github.javachaos.chaosdungeons.ecs.entities.impl.Fireball;

/**
 * Fireball factory for creating fireballs.!
 */
public class FireballEntityFactory implements EntityFactory<Fireball> {

  public FireballEntityFactory() {
  }

  @Override
  public Fireball create(SpawnData data) {
    return new Fireball(data);
  }
}
