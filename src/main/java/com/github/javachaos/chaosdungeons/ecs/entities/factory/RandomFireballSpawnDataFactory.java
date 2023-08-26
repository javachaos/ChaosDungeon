package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import java.util.Random;

import com.github.javachaos.chaosdungeons.geometry.util.ShapeBuilder;
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
            0f))//-0.25f, -0.25f, .5f, .5f
        .setShape(new ShapeBuilder.Circle().setNumPoints(5)
                .setPosition(-.25f, -.25f)
                .setRadius(0.5f).build())
        .setMaxSpawns(1000)
        .setMass(10.0f)
        .setGravitationFactor(0.001f)
        .setRestitution(1.0f)
        .build();
  }
}
