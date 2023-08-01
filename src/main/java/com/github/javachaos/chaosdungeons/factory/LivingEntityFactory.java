package com.github.javachaos.chaosdungeons.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.javachaos.chaosdungeons.player.PlayerAnimationComponent;
import com.github.javachaos.chaosdungeons.player.PlayerControl;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LivingEntityFactory implements EntityFactory {

    private enum Type {
        PLAYER,
        ENEMY
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(1.7f).restitution(0.1f));
        return FXGL.entityBuilder(data)
                .type(Type.ENEMY)
                .viewWithBBox(new Rectangle(40, 40, Color.RED))
                .collidable()
                .with(physics)
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.7f).restitution(0.03f));
        var player = FXGL.entityBuilder(data)
                .type(Type.PLAYER)
                .at(100,100)
                .bbox(new HitBox(BoundingShape.box(64,64)))
                .with(new PlayerAnimationComponent())
                .collidable()
                .with(physics)
                .with(new PlayerControl())
                .build();
        return player;
    }
}
