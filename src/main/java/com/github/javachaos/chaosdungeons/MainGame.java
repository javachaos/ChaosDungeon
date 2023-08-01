package com.github.javachaos.chaosdungeons;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.github.javachaos.chaosdungeons.factory.LivingEntityFactory;
import com.github.javachaos.chaosdungeons.factory.SpriteFactory;
import com.github.javachaos.chaosdungeons.factory.TileEntityFactory;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class MainGame extends GameApplication {

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setMenuKey(KeyCode.ESCAPE);
        gameSettings.setTitle("Chaos Dungeons");
        gameSettings.setFileSystemWriteAllowed(false);
        gameSettings.setApplicationMode(ApplicationMode.DEBUG);
    }

    @Override
    protected void initInput() {
    }

    @Override
    protected void initGame() {
        //entityBuilder().buildScreenBoundsAndAttach(50);//Add a screen bounds
        getGameWorld().addEntityFactory(new SpriteFactory());
        getGameWorld().addEntityFactory(new LivingEntityFactory());
        getGameWorld().addEntityFactory(new TileEntityFactory());
        FXGL.setLevelFromMap("level1/level1.tmx");
        spawn("enemy", 12, 12);
        var player = spawn("player", 500, 500);
        //spawn("Platform");
        getGameScene().getViewport().setBounds(30, 30, 4096 - 30, 4096 - 30);
        getGameScene().getViewport().bindToEntity(player, (double) getAppWidth() / 2, (double) getAppHeight() / 2);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0,0);//no gravity
    }

    public static void main(String[] args) {
        launch(args);
    }
}
