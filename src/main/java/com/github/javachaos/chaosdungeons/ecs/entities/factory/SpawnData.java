package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import java.util.Objects;
import org.joml.Vector3f;

/**
 * Helper class for keeping track of spawn data.
 */
@SuppressWarnings("unused")
public class SpawnData {
  private final Vector3f rotation;
  private final Vector3f position;
  private final Vector3f scale;
  private final Vector3f angularVelocity;
  private final Vector3f initialVelocity;
  private float spawnRate = 1.0f;
  private final int maxSpawns;

  private SpawnData(Vector3f rotation,
                    Vector3f position,
                    Vector3f scale,
                    Vector3f angularVelocity,
                    Vector3f initialVelocity,
                    float spawnRate,
                    int maxSpawns) {
    this.angularVelocity = Objects.requireNonNullElseGet(angularVelocity, () -> new Vector3f(0));
    this.initialVelocity = Objects.requireNonNullElseGet(initialVelocity, () -> new Vector3f(0));
    this.rotation = Objects.requireNonNullElseGet(rotation, () -> new Vector3f(0));
    this.position = Objects.requireNonNullElseGet(position, () -> new Vector3f(0));
    this.scale = Objects.requireNonNullElseGet(scale, () -> new Vector3f(1));
    if (spawnRate > 0) {
      this.spawnRate = spawnRate;
    }
    this.maxSpawns = maxSpawns;
  }

  public Vector3f getAngularVelocity() {
    return angularVelocity;
  }

  public Vector3f getInitialVelocity() {
    return initialVelocity;
  }

  public Vector3f getScale() {
    return scale;
  }

  // Getter methods for rotation and position
  public Vector3f getRotation() {
    return rotation;
  }

  public Vector3f getPosition() {
    return position;
  }

  public float getSpawnRate() {
    return spawnRate;
  }

  public int getMaxSpawns() {
    return maxSpawns;
  }

  /**
   * Builder class.
   */
  public static class Builder {
    private Vector3f rotation;
    private Vector3f position;
    private Vector3f scale;
    private Vector3f angularVelocity;
    private Vector3f initialVelocity;
    private float spawnRate = 1.0f;
    private int maxSpawns = 1;

    /**
     * Create a new builder.
     */
    public Builder() {
      // Set default values if needed
      scale = new Vector3f(1);
      rotation = new Vector3f();
      position = new Vector3f();
      angularVelocity = new Vector3f();
      initialVelocity = new Vector3f();
    }

    public Builder setAngularVelocity(Vector3f angularVelocity) {
      this.angularVelocity = angularVelocity;
      return this;
    }

    public Builder setInitialVelocity(Vector3f initialVelocity) {
      this.initialVelocity = initialVelocity;
      return this;
    }

    public Builder setRotation(Vector3f rotation) {
      this.rotation = rotation;
      return this;
    }

    public Builder setScale(Vector3f scale) {
      this.scale = scale;
      return this;
    }

    public Builder setPosition(Vector3f position) {
      this.position = position;
      return this;
    }

    public Builder setPosition(float x, float y, float z) {
      this.position = new Vector3f(x, y, z);
      return this;
    }

    public Builder setSpawnRate(float rate) {
      this.spawnRate = rate;
      return this;
    }

    public Builder setMaxSpawns(int maxSpawns) {
      this.maxSpawns = maxSpawns;
      return this;
    }

    /**
     * Build this builder.
     *
     * @return the configured spawn data.
     */
    public SpawnData build() {
      // Create and return the SpawnData object
      return new SpawnData(position, rotation, scale,
          angularVelocity, initialVelocity,
          spawnRate, maxSpawns);
    }
  }
}