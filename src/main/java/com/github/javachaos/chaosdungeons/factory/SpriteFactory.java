package com.github.javachaos.chaosdungeons.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.github.javachaos.chaosdungeons.constants.Sprites;
import javafx.geometry.Point2D;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

public class SpriteFactory implements EntityFactory {

    @Spawns(Sprites.FIREBALL)
    public Entity newFireball(SpawnData data) {
        return FXGL.entityBuilder(data)
                //loadPng(Sprites.FIREBALL)
                .viewWithBBox(texture("fireball.png"))
                .with(new ProjectileComponent(new Point2D(1, 0), 225))
                .build();
    }

}
