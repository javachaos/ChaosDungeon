package com.github.javachaos.chaosdungeons.player;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;


import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class PlayerAnimationComponent extends Component {
    private int speedX = 0;
    private int speedY = 0;

    private AnimatedTexture tex;
    private AnimationChannel animIdle;
    private AnimationChannel animWalk;

    public PlayerAnimationComponent() {
    }

    @Override
    public void onAdded() {
        animIdle = new AnimationChannel(
                image("player.png"), 4, 64, 64,
                Duration.seconds(1), 1, 1);
        animWalk = new AnimationChannel(
                image("player.png"), 4, 64, 64,
                Duration.seconds(1), 1, 3);
        tex = new AnimatedTexture(animIdle);
        entity.getTransformComponent().setScaleOrigin(new Point2D(16,24));
        entity.getViewComponent().addChild(tex);
    }

    @Override
    public void onUpdate(double tpf) {
        PhysicsComponent py = entity.getComponent(PhysicsComponent.class);
        py.setLinearVelocity(speedX, speedY);
        py.setAngularVelocity(0.0);
        py.overwriteAngle(0);
        entity.translateX(speedX * tpf);
        entity.translateY(speedY * tpf);
        if (speedX != 0) {
            if (tex.getAnimationChannel() == animIdle) {
                tex.loopAnimationChannel(animWalk);
            }
            speedX = (int) (speedX * 0.9);
            if (FXGLMath.abs(speedX) < 1) {
                speedX = 0;
                tex.loopAnimationChannel(animIdle);
            }
        }
        if (speedY != 0) {
            if (tex.getAnimationChannel() == animIdle) {
                tex.loopAnimationChannel(animWalk);
            }
            speedY = (int) (speedY * 0.9);
            if (FXGLMath.abs(speedY) < 1) {
                speedY = 0;
                tex.loopAnimationChannel(animIdle);
            }
        }
    }

    public void moveUp() {
        speedY = -150;
    }

    public void moveDown() {
        speedY = 150;
    }

    public void moveRight() {
        speedX = 150;
        getEntity().setScaleX(1);
    }

    public void moveLeft() {
        speedX = -150;
        getEntity().setScaleX(-1);
    }

}
