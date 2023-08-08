package com.github.javachaos.chaosdungeons.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * Camera 2D class.
 */
@SuppressWarnings("unused")
public class Camera implements GLFWKeyCallbackI {

  private float yaw;
  private float pitch;
  private float roll;
  private final Vector3f pos = new Vector3f(0);

  private static final Logger LOGGER = LogManager.getLogger(Camera.class);

  public Camera() {
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
      glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
    }
    if (action == GLFW_PRESS || action == GLFW_REPEAT) {
      // Adjust camera position based on the key pressed
      Vector3f movement = new Vector3f();

      if (key == GLFW_KEY_W) {
        movement.y += 1.0f;
      }
      if (key == GLFW_KEY_A) {
        movement.x -= 1.0f;
      }
      if (key == GLFW_KEY_S) {
        movement.y -= 1.0f;
      }
      if (key == GLFW_KEY_D) {
        movement.x += 1.0f;
      }

      // Normalize movement vector and apply speed
      movement.normalize();
      float speed = 10.0f;
      movement.mul(speed);

      // Update camera position
      pos.add(movement);
    }
  }
}
