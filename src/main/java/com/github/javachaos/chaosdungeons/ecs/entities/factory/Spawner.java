package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.ecs.systems.System;
import java.util.ArrayDeque;
import java.util.Deque;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

/**
 * A spawner class to spawn other entities.
 * Game entities created using this spawner must have a default
 * constructor with no arguments.
 */
@SuppressWarnings("unused")
public class Spawner<T extends GameEntity> extends GameEntity {

  private static final Logger LOGGER = LogManager.getLogger(Spawner.class);
  private final EntityFactory<T> factory;
  private final Deque<T> spawnQueue;
  private final float spawnRate;
  private int maxSpawns;

  private float timeSinceLastSpawn;

  /**
   * Create a new spawner which creates new entities from the provided factory
   * at a rate of (1.0 / spawnRate) and a maximum of max spawns. However, if
   * maxSpawns is negative, this spawner will spawn entities indefinitely.
   *
   * @param factory the factory instance to create new entities from
   * @param spawnRate the spawn rate
   * @param maxSpawns the maximum number of entities to spawn from this spawner
   *                  if this number is less than zero there is no maximum.
   */
  public Spawner(EntityFactory<T> factory, float spawnRate, int maxSpawns) {
    this(factory, spawnRate, maxSpawns,
        new Vector3f(),     // position
        new Vector3f(),     // rotation
        new Vector3f()); // scale
  }

  /**
   * Create a spawner with initial rotation and scale set to zero.
   *
   * @param factory entity creation factory
   * @param spawnRate the spawn rate
   * @param maxSpawns the maximum number of entities to spawn from this spawner
   *                  if this number is less than zero there is no maximum.
   * @param pos the initial spawn location
   */
  public Spawner(EntityFactory<T> factory, float spawnRate, int maxSpawns, Vector3f pos) {
    this(factory, spawnRate, maxSpawns, pos, new Vector3f(), new Vector3f());
  }

  /**
   * Create a new spawner which creates new entities from the provided factory
   * at a rate of (1.0 / spawnRate) and a maximum of max spawns. However, if
   * maxSpawns is negative, this spawner will spawn entities indefinitely.
   *
   * @param factory the factory instance to create new entities from
   * @param spawnRate the spawn rate
   * @param maxSpawns the maximum number of entities to spawn from this spawner
   *                  if this number is less than zero there is no maximum.
   * @param pos initial position of this spawner
   * @param rot initial rotation of this spawner
   * @param scale initial scale of this spawner
   */
  public Spawner(EntityFactory<T> factory, float spawnRate, int maxSpawns,
                 Vector3f pos, Vector3f rot, Vector3f scale) {
    super("assets/textures/fireball.png", pos, rot, scale);
    this.factory = factory;
    this.spawnRate = spawnRate;
    this.maxSpawns = maxSpawns;
    this.spawnQueue = new ArrayDeque<>();
  }

  @Override
  public void onAdded(Entity e) {
    LOGGER.debug("New spawner added.");
    //getComponent(SpriteComponent.class).remove();
  }

  @Override
  public void onRemoved(Entity e) {
  }

  @Override
  protected void update(float dt) {
    if (maxSpawns != 0) {
      timeSinceLastSpawn += dt;

      if (timeSinceLastSpawn >= (1.0f / spawnRate)) {
        spawnQueue.offer(newInstance());
        if (maxSpawns > 0) {
          maxSpawns--;
        }
        timeSinceLastSpawn = 0.0f; // Reset the timer
      }
    }
    if (!spawnQueue.isEmpty()) {
      T e = spawnQueue.poll();
      spawn(e);
    }
  }

  private T newInstance() {
    return factory.create();
  }

  private void spawn(T entity) {
    System.addEntity(entity, false);
    entity.init();
    entity.onAdded(null);
  }

  @Override
  public void destroy() {
    //clean up any potential remaining un-spawned items.
    spawnQueue.forEach(Entity::shutdown);
    spawnQueue.clear();
  }
}
