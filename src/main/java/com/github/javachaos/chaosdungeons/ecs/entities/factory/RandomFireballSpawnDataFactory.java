package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
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
        .setSpawnRate(10f)
        .setPosition(.5f, (float) (1f * Math.random()), 0f)
        .setRotation(new Vector3f(
            (rand.nextFloat()),
            (rand.nextFloat()),
            (rand.nextFloat())))
        .setInitialVelocity(new Vector3f(
            -(rand.nextFloat()),
            (rand.nextFloat()),
            0f))
        .setShape(new QuadTree.Quad(-0.25f, -0.25f, .5f, .5f))
        .setMaxSpawns(1000)
        .setMass(10.0f)
        .setGravitationFactor(0.001f)
        .setRestitution(1.0f)
        .build();
  }
}
