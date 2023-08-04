package com.github.javachaos.chaosdungeons.gui;

import org.joml.Vector3f;

/**
 * Camera 2D class.
 */
public class Camera {

  private final Vector3f position;

  private final Vector3f rotation;

  public Camera() {
    position = new Vector3f(0, 0, 0);
    rotation = new Vector3f(0, 0, 0);
  }

  public Camera(Vector3f position, Vector3f rotation) {
    this.position = position;
    this.rotation = rotation;
  }

  public Vector3f getPosition() {
    return position;
  }

  /**
   * Set position.
   *
   * @param x x-pos
   * @param y y-pos
   * @param z z-pos
   */
  public void setPosition(float x, float y, float z) {
    position.x = x;
    position.y = y;
    position.z = z;
  }

  /**
   * Move this camera.
   *
   * @param offsetX x-pos offset
   * @param offsetY y-pos offset
   * @param offsetZ z-pos offset
   */
  public void movePosition(float offsetX, float offsetY, float offsetZ) {
    if (offsetZ != 0) {
      position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
      position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
    }
    if (offsetX != 0) {
      position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
      position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
    }
    position.y += offsetY;
  }

  public Vector3f getRotation() {
    return rotation;
  }

  /**
   * Set rotation of this camera.
   *
   * @param x x-rot
   * @param y y-rot
   * @param z z-rot
   */
  public void setRotation(float x, float y, float z) {
    rotation.x = x;
    rotation.y = y;
    rotation.z = z;
  }

  /**
   * Translate the rotation vector for this camera.
   *
   * @param offsetX x-pos offset
   * @param offsetY y-pos offset
   * @param offsetZ z-pos offset
   */
  public void moveRotation(float offsetX, float offsetY, float offsetZ) {
    rotation.x += offsetX;
    rotation.y += offsetY;
    rotation.z += offsetZ;
  }
}
