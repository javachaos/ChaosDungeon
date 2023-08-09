package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.ecs.systems.System;
import java.util.ArrayDeque;
import java.util.Deque;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A spawner class to spawn other entities.
 * Game entities created using this spawner must have a default
 * constructor with no arguments.
 */
@SuppressWarnings("unused")
public class Spawner<T extends GameEntity> extends GameEntity {

  private static final Logger LOGGER = LogManager.getLogger(Spawner.class);
  private final EntityFactory<T> factory;
  private final SpawnData spawnData;
  private final Deque<T> spawnQueue;
  private SpawnDataFactory spawnDataFactory;
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
    this(factory, new SpawnData.Builder()
        .setMaxSpawns(maxSpawns)
        .setSpawnRate(spawnRate)
        .build());
  }

  /**
   * Create a new spawner which creates new entities from the provided factory
   * at a rate of (1.0 / spawnRate) and a maximum of max spawns. However, if
   * maxSpawns is negative, this spawner will spawn entities indefinitely.
   *
   * @param factory the factory instance to create new entities from
   * @param data the spawn data used to create each entity provided by the
   *             factory along with information such as spawnRate and
   *             maxSpawns.
   */
  public Spawner(EntityFactory<T> factory, SpawnData data) {
    super("assets/textures/fireball.png",
        data.getPosition(), data.getRotation(), data.getScale());
    this.factory = factory;
    this.spawnData = data;
    this.spawnQueue = new ArrayDeque<>();
  }

  /**
   * Create a new spawner which creates new entities from the provided factory
   * at a rate of (1.0 / spawnRate) and a maximum of max spawns. However, if
   * maxSpawns is negative, this spawner will spawn entities indefinitely.
   *
   * @param factory the factory instance to create new entities from
   * @param data the spawn data factory used to create each entity provided by the
   *             factory along with information such as spawnRate and
   *             maxSpawns.
   */
  public Spawner(EntityFactory<T> factory, SpawnDataFactory data) {
    this(factory, data.create());
    this.spawnDataFactory = data;
  }

  @Override
  public void onAdded(Entity e) {
    LOGGER.debug("New spawner added.");
    getComponent(SpriteComponent.class).remove();
  }

  @Override
  public void onRemoved(Entity e) {
  }

  @Override
  protected void update(float dt) {
    if (!getSprite().isRemoved()) {
      getSprite().remove();
    }
    if (spawnData.getMaxSpawns() != 0) {
      timeSinceLastSpawn += dt;

      if (timeSinceLastSpawn >= (1.0f / spawnData.getSpawnRate())) {
        spawnQueue.offer(newInstance());
        if (spawnData.getMaxSpawns() > 0) {
          spawnData.decrementMaxSpawns();
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
    if (spawnDataFactory != null) {
      return factory.create(spawnDataFactory.create());
    }
    return factory.create(spawnData);
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
