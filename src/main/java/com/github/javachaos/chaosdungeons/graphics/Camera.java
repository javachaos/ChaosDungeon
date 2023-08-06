package com.github.javachaos.chaosdungeons.graphics;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Camera 2D class.
 */
@SuppressWarnings("unused")
public class Camera {

  private final Transform cameraTransform;
  private final Matrix4f projection;

  /**
   * Setup a camera with position, rotation and projection set to zero.
   */
  public Camera(int width, int height) {
    cameraTransform = new Transform();
    projection = new Matrix4f().setOrtho2D(-width / 2f, width / 2f,
        -height / 2f, height / 2f);
  }

  /**
   * Create a new camere with position, rotation and projection.
   *
   * @param position the position of the camera
   * @param rotation the rotation of the camera
   * @param projection the camera projection matrix
   */
  public Camera(int width, int height, Vector3f position, Quaternionf rotation,
                Matrix4f projection) {
    this(width, height);
    this.cameraTransform.setPosition(position);
    this.cameraTransform.setRotation(rotation);
    this.projection.set(projection);
  }


  public void setPosition(Vector3f pos) {
    this.cameraTransform.setPosition(pos);
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
      cameraTransform.getPosition().x += (float) Math.sin(Math.toRadians(
          cameraTransform.getRotation().y)) * -1.0f * offsetZ;
      cameraTransform.getPosition().z += (float) Math.cos(Math.toRadians(
          cameraTransform.getRotation().y)) * offsetZ;
    }
    if (offsetX != 0) {
      cameraTransform.getPosition().x += (float) Math.sin(Math.toRadians(
          cameraTransform.getRotation().y - 90)) * -1.0f * offsetX;
      cameraTransform.getPosition().z += (float) Math.cos(Math.toRadians(
          cameraTransform.getRotation().y - 90)) * offsetX;
    }
    cameraTransform.getPosition().y += offsetY;
  }

  public Quaternionf getRotation() {
    return cameraTransform.getRotation();
  }

  /**
   * Set rotation of this camera.
   *
   * @param x x-rot
   * @param y y-rot
   * @param z z-rot
   * @param w w-rot
   */
  public void setRotation(float x, float y, float z, float w) {
    cameraTransform.getRotation().x = x;
    cameraTransform.getRotation().y = y;
    cameraTransform.getRotation().z = z;
    cameraTransform.getRotation().w = w;
  }

  public void setRotation(Quaternionf rot) {
    cameraTransform.getRotation().set(rot);
  }

  /**
   * Translate the rotation vector for this camera.
   *
   * @param offsetX x-pos offset
   * @param offsetY y-pos offset
   * @param offsetZ z-pos offset
   */
  public void moveRotation(float offsetX, float offsetY, float offsetZ) {
    cameraTransform.getRotation().x += offsetX;
    cameraTransform.getRotation().y += offsetY;
    cameraTransform.getRotation().z += offsetZ;
  }

  public Matrix4f getProjection() {
    return projection;
  }
}
