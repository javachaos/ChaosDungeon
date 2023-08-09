package com.github.javachaos.chaosdungeons.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * Camera 2D class.
 */
public class Camera implements GLFWKeyCallbackI {

  private static final Logger LOGGER = LogManager.getLogger(Camera.class);
  private static final float SPEED = 5f;
  private final Vector3f pos = new Vector3f(0);
  private final Vector3f velocity = new Vector3f();
  private final Vector3f movement = new Vector3f();
  private float yaw;
  private float pitch;
  private float roll;

  private boolean[] pressedKeys = new boolean[1024];

  public Camera() {
  }

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
    return pos;
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
    }
    if (isPressed(GLFW_KEY_A)) {
      movement.x -= SPEED;
    }
    if (isPressed(GLFW_KEY_S)) {
      movement.y -= SPEED;
    }
    if (isPressed(GLFW_KEY_D)) {
      movement.x += SPEED;
    }
    movement.normalize(SPEED);
    float acceleration = 0.48f;
    velocity.add(movement.mul(acceleration));

    float friction = .9f;
    velocity.mul(1.0f - friction);
    if (action == GLFW_RELEASE
        && !(isPressed(GLFW_KEY_W)
        || isPressed(GLFW_KEY_A)
        || isPressed(GLFW_KEY_S)
        || isPressed(GLFW_KEY_D))) {
      velocity.set(0);
      movement.set(0);
    }
  }

  public boolean isPressed(int key) {
    return pressedKeys[key];
  }

  public void update() {
    pos.add(velocity);
    //pos.add(velocity); // Update the camera position based on velocity
  }
}
