package com.github.javachaos.chaosdungeons.gui;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Camera 2D class.
 */
public class Camera {

  private final Vector3f position;
  private final Quaternionf rotation;
  private final Matrix4f projection;

  public Camera() {
    position = new Vector3f(0, 0, 0);
    rotation = new Quaternionf();
    projection = new Matrix4f();
  }

  public Camera(Vector3f position, Quaternionf rotation, Matrix4f projection) {
    this.position = position;
    this.rotation = rotation;
    this.projection = projection;
  }

  public Matrix4f getTransformation() {
    Matrix4f r = new Matrix4f();
    r.rotate(rotation.conjugate(new Quaternionf()));
    r.translate(position.mul(-1, new Vector3f()));
    return r;
  }

  public void setOrtho2D(float l, float r, float t, float b) {
    projection.setOrtho2D(l, r, t, b);
  }

  public void setPerspective(float fov, float aspectRatio, float near, float far) {
    projection.setPerspective(fov, aspectRatio, near, far);
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

  public void setPosition(Vector3f pos) {
    this.position.set(pos);
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

  public Quaternionf getRotation() {
    return rotation;
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
    rotation.x = x;
    rotation.y = y;
    rotation.z = z;
    rotation.w = w;
  }
  public void setRotation(Quaternionf rot) {
    rotation.set(rot);
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

  public Matrix4f getProjection() {
    return projection;
  }
}
