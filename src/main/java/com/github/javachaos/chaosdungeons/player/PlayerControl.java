package com.github.javachaos.chaosdungeons.player;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import com.github.javachaos.chaosdungeons.utils.Direction;
import javafx.scene.input.KeyCode;

public class PlayerControl extends Component {

    /**
     * Set up a movement user action.
     *
     * @param dir the direction of movement (UP,DOWN,LEFT or RIGHT)
     * @param r the action performed
     * @param key the key to initiate action
     */
    private void initMovement(Direction dir, final Runnable r, KeyCode key) {
        String direction = switch (dir) {
            case UP -> "Up";
            case DOWN -> "Down";
            case LEFT -> "Left";
            case RIGHT -> "Right";
        };
        FXGL.getInput().addAction(new UserAction(direction) {
            @Override
            protected void onAction() {
                r.run();
            }
        }, key);
    }

    @Override
    public void onAdded() {
        initMovement(Direction.UP,    () -> entity.getComponent(PlayerAnimationComponent.class).moveUp(),    KeyCode.W);
        initMovement(Direction.LEFT,  () -> entity.getComponent(PlayerAnimationComponent.class).moveLeft(),  KeyCode.A);
        initMovement(Direction.DOWN,  () -> entity.getComponent(PlayerAnimationComponent.class).moveDown(),  KeyCode.S);
        initMovement(Direction.RIGHT, () -> entity.getComponent(PlayerAnimationComponent.class).moveRight(), KeyCode.D);
    }
}
