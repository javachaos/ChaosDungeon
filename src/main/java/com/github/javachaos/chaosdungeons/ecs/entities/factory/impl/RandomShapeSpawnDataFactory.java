package com.github.javachaos.chaosdungeons.ecs.entities.factory.impl;

import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnDataFactory;
import com.github.javachaos.chaosdungeons.geometry.util.ShapeBuilder;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Random;

public class RandomShapeSpawnDataFactory implements SpawnDataFactory {
    private static final Random rand = new Random();
    @Override
    public SpawnData create() {
        Vector2f pos = new Vector2f(50f * rand.nextFloat(), 100f * rand.nextFloat());
        return new SpawnData.Builder()
                .setSpawnRate(100f)
                .setPosition(pos)
                .setRotation(new Vector3f(
                        (rand.nextFloat()),
                        (rand.nextFloat()),
                        (rand.nextFloat())))
                .setInitialVelocity(new Vector2f(
                        -1f * (rand.nextFloat()),
                        5f * (rand.nextFloat())))
                .setShape(new ShapeBuilder.Random()
                        .setPosition(pos)
                        .build())
                .setMaxSpawns(1000)
                .setMass(.10f)
                .setGravitationFactor(1f)
                .setAngularVelocity(new Vector3f(
                        (rand.nextFloat()),
                        (rand.nextFloat()),
                        (rand.nextFloat())))
                .setRestitution(.02f)
                .build();
    }
}
