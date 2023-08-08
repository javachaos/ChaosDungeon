package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import java.util.Random;
import org.joml.Vector3f;

/**
 * Create a random fireball spawn data object.
 */
public class RandomFireballSpawnData {
  private static final Random rand = new Random();

  /**
   * Get the spawn data for a random fireball.
   *
   * @return a random fireball spawn data object.
   */
  public static SpawnData getData() {
    return new SpawnData.Builder()
        .setSpawnRate(5.0f)
        .setPosition(.5f, (float) (1f * Math.random()), 0f)
        .setRotation(new Vector3f(
            (rand.nextFloat() * 3.0f),
            (rand.nextFloat() * 3.0f),
            (rand.nextFloat() * 3.0f)))
        .setInitialVelocity(new Vector3f(
            -(rand.nextFloat() * 16.0f),
            (rand.nextFloat() * 3.0f),
            0f))
        .setMaxSpawns(2500)
        .build();
  }
}
