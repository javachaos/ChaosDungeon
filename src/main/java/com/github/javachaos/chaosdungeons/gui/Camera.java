package com.github.javachaos.chaosdungeons.gui;

import com.github.javachaos.chaosdungeons.constants.Constants;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Camera 2D class.
 */
@SuppressWarnings("unused")
public class Camera {

  private final Vector3f position;
  private final Quaternionf rotation;
  private final Matrix4f projection;

  /**
   * Setup a camera with position, rotation and projection set to zero.
   */
  public Camera() {
    position = new Vector3f(0, 0, 0);
    rotation = new Quaternionf();
    projection = new Matrix4f();
  }

  /**
   * Create a new camere with position, rotation and projection.
   *
   * @param position the position of the camera
   * @param rotation the rotation of the camera
   * @param projection the camera projection matrix
   */
  public Camera(Vector3f position, Quaternionf rotation, Matrix4f projection) {
    this.position = position;
    this.rotation = rotation;
    this.projection = projection;
  }

  /**
   * Get transformation matrix.
   *
   * @return the transformation matrix of this camera.
   */
  public Matrix4f getTransformation() {
    Matrix4f r = new Matrix4f();
    r.rotate(rotation.conjugate(new Quaternionf()));
    r.translate(position.mul(-1, new Vector3f()));
    return r;
  }

  public void setOrtho2D(float left, float right, float top, float bottom) {
    projection.setOrtho2D(left, right, top, bottom);
  }

  public void setPerspective(float fov, float aspectRatio, float near, float far) {
    projection.setPerspective(fov, aspectRatio, near, far);
  }

  /**
   * Set the camera to orthographic projection, with the zoom level zoom.
   *
   * @param zoom the zoom level
   */
  public void setOrthographic(float zoom) {
    float tanOfFov = (float) Math.tan(Math.toRadians(Constants.FOV / 2.0));
    float halfHeight = zoom * tanOfFov;
    float halfWidth = halfHeight * ((float) GameWindow.getProjection().getWidth()
        / GameWindow.getProjection().getHeight());
    setOrtho2D(halfWidth, -halfWidth, halfHeight, -halfHeight);
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
