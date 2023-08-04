package com.github.javachaos.chaosdungeons.gui;

import static com.github.javachaos.chaosdungeons.constants.Constants.FOV;
import static com.github.javachaos.chaosdungeons.constants.Constants.Z_FAR;
import static com.github.javachaos.chaosdungeons.constants.Constants.Z_NEAR;

import org.joml.Matrix4f;

/**
 * Projection class.
 */
public class Projection {

  private final Matrix4f projectionMatrix;

  private int width;
  private int height;

  /**
   * Create a new projection.
   *
   * @param width the viewport width.
   * @param height the viewport height.
   */
  public Projection(int width, int height) {
    this.width = width;
    this.height = height;
    projectionMatrix = new Matrix4f();
    updateProjection(width, height);
  }

  /**
   * Update the projection matrix.
   *
   * @param width viewport width
   * @param height viewport height
   */
  public void updateProjection(int width, int height) {
    this.width = width;
    this.height = height;
    projectionMatrix.setPerspective(FOV, (float) width / height, Z_NEAR, Z_FAR);
  }

  public Matrix4f getProjection() {
    return projectionMatrix;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
