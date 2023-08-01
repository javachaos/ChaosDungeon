package com.github.javachaos.chaosdungeons.factory;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.shape.Polygon;


import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

public class TileEntityFactory implements EntityFactory {

//    @Spawns("Platform")
//    public Entity newFloor(SpawnData data) {
//        var phys = new PhysicsComponent();
//        phys.setBodyType(BodyType.STATIC);
//        System.out.println(data.getData());
//        return entityBuilder(data)
//                //.bbox(new HitBox(BoundingShape.polygon((List<? extends Point2D>) data.getData().get("polygon"))))
//                .with(phys)
//                .collidable()
//                .build();
//    }
    @Spawns("")
    public Entity newEmpty(SpawnData data) {
        if (data.hasKey("polygon")) {
            return entityBuilder(data)
                    .viewWithBBox((Polygon) data.getData().get("polygon"))
                    .with(new PhysicsComponent())
                    .build();
        }
        return entityBuilder(data)
                .build();
    }

}
