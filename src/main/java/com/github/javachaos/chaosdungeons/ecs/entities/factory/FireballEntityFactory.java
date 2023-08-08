package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import com.github.javachaos.chaosdungeons.ecs.entities.impl.Fireball;
import org.joml.Vector3f;

/**
 * Fireball factory for creating fireballs.!
 */
public class FireballEntityFactory implements EntityFactory<Fireball> {

  public FireballEntityFactory() {
  }

  @Override
  public Fireball create() {
    return new Fireball(createRandSpawnData());
  }

  private SpawnData createRandSpawnData() {
    return new SpawnData.Builder()
        .setSpawnRate(10.0f)
        .setPosition(.5f, (float) (1f * Math.random()), 0f)
        .setRotation(new Vector3f(
            (float) (Math.random() * 3.0f),
            (float) (Math.random() * 3.0f),
            (float) (Math.random() * 3.0f)))
        .setInitialVelocity(new Vector3f(
            (float) -(Math.random() * 16.0f),
            (float) (Math.random() * 3.0f),
            0f))
        .build();
  }
}
