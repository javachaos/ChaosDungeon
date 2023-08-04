package com.github.javachaos.chaosdungeons.geometry.math;

import com.github.javachaos.chaosdungeons.gui.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Simple transform class.
 */
public class Transformation {

  private final Matrix4f projectionMatrix;

  private final Matrix4f worldMatrix;

  private final Matrix4f viewMatrix;

  public Transformation() {
    worldMatrix = new Matrix4f();
    viewMatrix = new Matrix4f();
    projectionMatrix = new Matrix4f();
  }

  /**
   * Get projection matrix.
   *
   * @param fov field of view
   * @param width width
   * @param height height
   * @param znear z frustum near
   * @param zfar z frustum far
   * @return a new projection matrix
   */
  public final Matrix4f getProjectionMatrix(float fov, float width, float height, float znear,
                                            float zfar) {
    float aspectRatio = width / height;
    projectionMatrix.identity();
    projectionMatrix.perspective(fov, aspectRatio, znear, zfar);
    return projectionMatrix;
  }

  /**
   * Get the view matrix.
   *
   * @param camera the camera.
   * @return the view matrix updated with the camera translation and rotation.
   */
  public Matrix4f getViewMatrix(Camera camera) {
    Vector3f cameraPos = camera.getPosition();
    Vector3f rotation = camera.getRotation();

    viewMatrix.identity();
    // First do the rotation so camera rotates over its position
    viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
        .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
    // Then do the translation
    viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    return viewMatrix;
  }

  /**
   * Get world matrix.
   *
   * @param offset the offset (x, y, z)
   * @param rotation the rotation vector
   * @param scale the scale
   * @return a new world matrix
   */
  public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
    worldMatrix.identity().translate(offset)
        .rotateX((float) Math.toRadians(rotation.x))
        .rotateY((float) Math.toRadians(rotation.y))
        .rotateZ((float) Math.toRadians(rotation.z))
        .scale(scale);
    return worldMatrix;
  }
}


