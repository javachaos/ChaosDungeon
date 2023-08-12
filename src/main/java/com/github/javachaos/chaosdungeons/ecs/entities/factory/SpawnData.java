package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
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
  private float gravitationFactor;
  private QuadTree.Quad shape;
  private float mass;
  private float restitution;
  private float spawnRate = 1.0f;
  private int maxSpawns;

  /**
   * Create a new spawn data instance.
   *
   * @param initialVelocity the initial velocity
   * @param angularVelocity the initial angular velocity
   * @param spawnRate the spawn rate
   * @param maxSpawns the maximum number of entities to spawn from this spawner
   *                  if this number is less than zero there is no maximum.
   * @param position initial position of this spawner
   * @param rotation initial rotation of this spawner
   * @param scale initial scale of this spawner
   */
  private SpawnData(Vector3f rotation,
                    Vector3f position,
                    Vector3f scale,
                    Vector3f angularVelocity,
                    Vector3f initialVelocity,
                    QuadTree.Quad shape,
                    float mass,
                    float gravitationFactor,
                    float restitution,
                    float spawnRate,
                    int maxSpawns) {
    this.angularVelocity = Objects.requireNonNullElseGet(angularVelocity, () -> new Vector3f(0));
    this.initialVelocity = Objects.requireNonNullElseGet(initialVelocity, () -> new Vector3f(0));
    this.rotation = Objects.requireNonNullElseGet(rotation, () -> new Vector3f(0));
    this.position = Objects.requireNonNullElseGet(position, () -> new Vector3f(0));
    this.scale = Objects.requireNonNullElseGet(scale, () -> new Vector3f(1));
    this.shape = Objects.requireNonNullElseGet(shape, () -> new QuadTree.Quad(0, 0, 1, 1));
    if (mass > 0) {
      this.mass = mass;
    }
    if (restitution > 0) {
      this.restitution = restitution;
    }
    if (gravitationFactor > 0) {
      this.gravitationFactor = gravitationFactor;
    }
    if (spawnRate > 0) {
      this.spawnRate = spawnRate;
    }
    this.maxSpawns = maxSpawns;
  }

  public Vector3f getAngularVelocity() {
    return new Vector3f(angularVelocity);
  }

  public Vector3f getInitialVelocity() {
    return new Vector3f(initialVelocity);
  }

  public Vector3f getScale() {
    return new Vector3f(scale);
  }

  // Getter methods for rotation and position
  public Vector3f getRotation() {
    return new Vector3f(rotation);
  }

  public Vector3f getPosition() {
    return new Vector3f(position);
  }

  public QuadTree.Quad getShape() {
    return shape;
  }

  public float getSpawnRate() {
    return spawnRate;
  }

  public int getMaxSpawns() {
    return maxSpawns;
  }

  public void decrementMaxSpawns() {
    maxSpawns--;
  }

  public float getMass() {
    return mass;
  }

  public float getGravitationFactor() {
    return gravitationFactor;
  }

  public float getRestitution() {
    return restitution;
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
    private QuadTree.Quad shape;
    private float gravitationFactor;
    private float mass;
    private float restitution;
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
      shape = new QuadTree.Quad(0, 0, 1, 1);
      gravitationFactor = 1.0f;
      mass = 1.0f;
      restitution = 1.0f;
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

    public Builder setMass(float mass) {
      this.mass = mass;
      return this;
    }

    public Builder setRestitution(float restitution) {
      this.restitution = restitution;
      return this;
    }

    public Builder setGravitationFactor(float gravitationFactor) {
      this.gravitationFactor = gravitationFactor;
      return this;
    }

    public Builder setShape(QuadTree.Quad shape) {
      this.shape = shape;
      return this;
    }

    /**
     * Build this builder.
     *
     * @return the configured spawn data.
     */
    public SpawnData build() {
      // Create and return the SpawnData object
      return new SpawnData(rotation, position, scale,
          angularVelocity, initialVelocity, shape,
          mass, gravitationFactor, restitution,
          spawnRate, maxSpawns);
    }
  }
}