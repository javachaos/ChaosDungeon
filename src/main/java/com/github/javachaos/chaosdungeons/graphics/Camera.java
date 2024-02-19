package com.github.javachaos.chaosdungeons.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import com.github.javachaos.chaosdungeons.constants.Constants;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * Camera 2D class.
 */
@SuppressWarnings("unused")
public class Camera implements GLFWKeyCallbackI {
  private static final float SPEED = 5f;
  private final Vector3f pos = new Vector3f(0);
  private final Vector3f velocity = new Vector3f();
  private final Vector3f movement = new Vector3f();
  private float yaw;
  private float pitch;
  private float roll;

  private final boolean[] pressedKeys = new boolean[2048];

  /**
   * Set the rotation of this camera.
   *
   * @param rot rotation (yaw, pitch, roll)
   */
  public void setRotation(Vector3f rot) {
    this.yaw = rot.x;
    this.pitch = rot.y;
    this.roll = rot.z;
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public float getRoll() {
    return roll;
  }

  public Vector3f getPosition() {
    return new Vector3f(pos);
  }

  @Override
  public void invoke(long window, int key, int scancode, int action, int mods) {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
      glfwSetWindowShouldClose(window, true);
    }
    // Update the pressedKeys flags based on the key event
    if (action == GLFW_PRESS) {
      pressedKeys[key] = true;
    } else if (action == GLFW_RELEASE) {
      pressedKeys[key] = false;
    }

    if (isPressed(GLFW_KEY_W)) {
      movement.y += SPEED;
      updateVelocity();
    }
    if (isPressed(GLFW_KEY_A)) {
      movement.x -= SPEED;
      updateVelocity();
    }
    if (isPressed(GLFW_KEY_S)) {
      movement.y -= SPEED;
      updateVelocity();
    }
    if (isPressed(GLFW_KEY_D)) {
      movement.x += SPEED;
      updateVelocity();
    }
    if (action == GLFW_RELEASE
        && !(isPressed(GLFW_KEY_W)
        || isPressed(GLFW_KEY_A)
        || isPressed(GLFW_KEY_S)
        || isPressed(GLFW_KEY_D))) {
      velocity.set(0);
      movement.set(0);
    }
  }

  private void updateVelocity() {
    movement.normalize(SPEED);
    float acceleration = 0.78f;
    velocity.add(movement.mul(acceleration));

    float friction = .9f;
    velocity.mul(1.0f - friction);
  }

  public boolean isPressed(int key) {
    return pressedKeys[key];
  }

  public void update() {
    pos.add(velocity);
  }
}
