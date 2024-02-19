package com.github.javachaos.chaosdungeons.ecs.entities.factory.impl;

import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnDataFactory;
import com.github.javachaos.chaosdungeons.geometry.util.ShapeBuilder;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Fireball spawn data factory for creating random fireballs.
 */
public class RandomFireballSpawnDataFactory implements SpawnDataFactory {

  private static final ThreadLocalRandom rand = ThreadLocalRandom.current();

  @Override
  public SpawnData create() {
    Vector2f pos = new Vector2f(
            (float) rand.nextDouble(105.0),
            (float) rand.nextDouble(105.0));
    return new SpawnData.Builder()
        .setSpawnRate(100f)
        .setPosition(pos)
        .setInitialVelocity(new Vector2f())
        .setShape(new ShapeBuilder.Circle().setNumPoints(5)
                .setPosition(pos)
                .setRadius(rand.nextDouble(1f)).build())
        .setMaxSpawns(5000)
        .setMass((float) (0.1510f * Math.random()))
        .setGravitationFactor(1.0f)
            .setAngularVelocity(new Vector3f(
                    (float) (rand.nextDouble(2f)),
                    (float) (rand.nextDouble(2f)),
                    (float) (rand.nextDouble(2f))))
        .setRestitution((float) (0.5f * Math.random()))
        .build();
  }
}
