package com.github.javachaos.chaosdungeons.ecs.entities;

import org.joml.Vector3f;

/**
 * Game entity class.
 */
public abstract class GameEntity extends Entity {

  private final Vector3f position;

  private float scale;

  private final Vector3f rotation;

  /**
   * Create a game entity.
   */
  public GameEntity() {
    super();
    position = new Vector3f(0, 0, 0);
    scale = 1;
    rotation = new Vector3f(0, 0, 0);
  }

  public Vector3f getPosition() {
    return position;
  }

  /**
   * Set the position of this game entity in the game world.
   *
   * @param x the x-pos
   * @param y the y-pos
   * @param z the z-pos
   */
  public void setPosition(float x, float y, float z) {
    this.position.x = x;
    this.position.y = y;
    this.position.z = z;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public Vector3f getRotation() {
    return rotation;
  }

  /**
   * Set the rotation of this object.
   *
   * @param x x-rotation
   * @param y y-rotation
   * @param z z-rotation
   */
  public void setRotation(float x, float y, float z) {
    this.rotation.x = x;
    this.rotation.y = y;
    this.rotation.z = z;
  }

}
