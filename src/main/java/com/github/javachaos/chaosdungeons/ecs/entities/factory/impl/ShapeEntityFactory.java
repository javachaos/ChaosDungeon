package com.github.javachaos.chaosdungeons.ecs.entities.factory.impl;

import java.awt.Color;
import java.util.Random;

import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.EntityFactory;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.RandomShapeEntity;

public class ShapeEntityFactory implements EntityFactory<RandomShapeEntity> {
    private static final Random rand = new Random(System.nanoTime());

    @Override
    public RandomShapeEntity create(SpawnData data, GameContext gameContext) {
        return new RandomShapeEntity(
                (int)data.getPosition().x,
                (int)data.getPosition().y,
                rand.nextInt(5) + 3,
                rand.nextInt(100) + 3f,
                rand.nextInt(100) + 3f,
                Color.GREEN,
                data, gameContext);
    }
}
