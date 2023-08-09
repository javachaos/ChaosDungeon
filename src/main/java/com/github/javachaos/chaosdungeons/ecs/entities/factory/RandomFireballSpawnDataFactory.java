package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import java.util.Random;
import org.joml.Vector3f;

/**
 * Fireball spawn data factory for creating random fireballs.
 */
public class RandomFireballSpawnDataFactory implements SpawnDataFactory {

  private static final Random rand = new Random();

  @Override
  public SpawnData create() {
    return new SpawnData.Builder()
        .setSpawnRate(100f)
        .setPosition(.5f, (float) (1f * Math.random()), 0f)
        .setRotation(new Vector3f(
            (rand.nextFloat()),
            (rand.nextFloat()),
            (rand.nextFloat())))
        .setInitialVelocity(new Vector3f(
            (rand.nextFloat() * 16.0f),
            (rand.nextFloat() * 3.0f),
            0f))
        .setMaxSpawns(1000)
        .build();
  }
}
