package com.github.javachaos.chaosdungeons.graphics;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * A simple transformation matrix class.
 */
public class Transform {
  private Vector3f position;
  private Quaternionf rotation;
  private Vector3f scale;

  /**
   * Create a transform with position and rotation initialized to zero and scale to 1.
   */
  public Transform() {
    position = new Vector3f();
    rotation = new Quaternionf();
    scale = new Vector3f(1);
  }

  /**
   * Get the transformation matrix (projection).
   *
   * @return the projection
   */
  public Matrix4f getTransformation() {
    Matrix4f returnValue = new Matrix4f();

    returnValue.translate(position);
    returnValue.rotate(rotation);
    returnValue.scale(scale);

    return returnValue;
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }

  public Quaternionf getRotation() {
    return rotation;
  }

  public void setRotation(Quaternionf rotation) {
    this.rotation = rotation;
  }

  public Vector3f getScale() {
    return scale;
  }

  public void setScale(Vector3f scale) {
    this.scale = scale;
  }
}
