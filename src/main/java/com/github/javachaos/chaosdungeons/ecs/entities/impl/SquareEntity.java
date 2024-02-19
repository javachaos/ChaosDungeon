package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.geometry.util.ShapeBuilder;

public class SquareEntity extends GameEntity {
    public SquareEntity(float x, float y, float w, float h, GameContext gameContext) {
        super("", new SpawnData.Builder()
                .setRestitution(100f)
                .setMass(10f)
                .setGravitationFactor(0f)
                .setMaxSpawns(1)
                .setSpawnRate(1)
                .setDynamic(false)
                .setShape(new ShapeBuilder.Rectangle().setPosition(x, y).setWidth(w).setHeight(h).build())
                .build(), gameContext);
    }

    @Override
    protected void update(float dt) {
        //Unused
    }

    @Override
    public void destroy() {
        //Unused
    }
}
