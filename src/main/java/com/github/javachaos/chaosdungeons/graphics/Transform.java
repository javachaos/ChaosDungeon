package com.github.javachaos.chaosdungeons.graphics;

import org.joml.Vector3f;

/**
 * A simple transformation matrix class.
 */
public class Transform {
  private Vector3f position;
  private Vector3f rotation;
  private Vector3f scale;

  /**
   * Create a transform with position and rotation initialized to zero and scale to 1.
   */
  public Transform() {
    position = new Vector3f();
    rotation = new Vector3f();
    scale = new Vector3f(1);
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }

  public Vector3f getRotation() {
    return rotation;
  }

  public void setRotation(Vector3f rotation) {
    this.rotation = rotation;
  }

  public Vector3f getScale() {
    return scale;
  }

  public void setScale(Vector3f scale) {
    this.scale = scale;
  }
}
