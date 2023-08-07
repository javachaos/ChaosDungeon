package com.github.javachaos.chaosdungeons.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
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

  private float speed = .01f;
  private Vector3f pos = new Vector3f(0);

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
    if (key == GLFW_KEY_W) {
      pos.y -= speed;
      LOGGER.debug("W");
    }
    if (key == GLFW_KEY_A) {
      pos.x -= speed;

      LOGGER.debug("A");
    }
    if (key == GLFW_KEY_S) {
      pos.y += speed;
      LOGGER.debug("S");

    }
    if (key == GLFW_KEY_D) {
      pos.x += speed;
      LOGGER.debug("D");
    }
  }
}
