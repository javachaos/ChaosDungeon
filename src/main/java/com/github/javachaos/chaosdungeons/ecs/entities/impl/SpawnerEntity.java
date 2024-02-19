package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.components.CollisionComponent;
import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.EntityFactory;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnDataFactory;

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
public class SpawnerEntity<T extends GameEntity> extends GameEntity {

  private static final Logger LOGGER = LogManager.getLogger(SpawnerEntity.class);
  private final EntityFactory<T> factory;
  private final SpawnData spawnData;
  private final Deque<T> spawnQueue;
  private SpawnDataFactory spawnDataFactory;
  private float timeSinceLastSpawn;
  private final GameContext gameContext;

  /**
   * Create a new spawner which creates new entities from the provided factory
   * at a rate of (1.0 / spawnRate) and a maximum of max spawns. However, if
   * maxSpawns is negative, this spawner will spawn entities indefinitely.
   *
   * @param gameContext object used to manage shared state between systems
   * @param factory the factory instance to create new entities from
   * @param spawnRate the spawn rate
   * @param maxSpawns the maximum number of entities to spawn from this spawner
   *                  if this number is less than zero there is no maximum.
   */
  public SpawnerEntity(GameContext gameContext, EntityFactory<T> factory, float spawnRate, int maxSpawns) {
    this(gameContext, factory, new SpawnData.Builder()
        .setMaxSpawns(maxSpawns)
        .setSpawnRate(spawnRate)
        .build());
  }

  /**
   * Create a new spawner which creates new entities from the provided factory
   * at a rate of (1.0 / spawnRate) and a maximum of max spawns. However, if
   * maxSpawns is negative, this spawner will spawn entities indefinitely.
   *
   * @param gameContext object used to manage shared state between systems
   * @param factory the factory instance to create new entities from
   * @param data the spawn data factory used to create each entity provided by the
   *             factory along with information such as spawnRate and
   *             maxSpawns.
   */
  public SpawnerEntity(GameContext gameContext, EntityFactory<T> factory, SpawnDataFactory data) {
    this(gameContext, factory, data.create());
    this.spawnDataFactory = data;
  }

  /**
   * Create a new spawner which creates new entities from the provided factory
   * at a rate of (1.0 / spawnRate) and a maximum of max spawns. However, if
   * maxSpawns is negative, this spawner will spawn entities indefinitely.
   *
   * @param gameContext object used to manage shared state between systems
   * @param factory the factory instance to create new entities from
   * @param data the spawn data used to create each entity provided by the
   *             factory along with information such as spawnRate and
   *             maxSpawns.
   */
  public SpawnerEntity(GameContext gameContext, EntityFactory<T> factory, SpawnData data) {
    super("", data, gameContext);
    this.factory = factory;
    this.spawnData = data;
    this.spawnQueue = new ArrayDeque<>();
    this.gameContext = gameContext;
  }

  @Override
  protected void update(float dt) {
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
      return factory.create(spawnDataFactory.create(), gameContext);
    }
    return factory.create(spawnData, gameContext);
  }

  private void spawn(T entity) {
    gameContext.addEntity(entity);
    entity.init();
  }

  @Override
  public void destroy() {
    //clean up any potential remaining un-spawned items.
    spawnQueue.forEach(Entity::shutdown);
    spawnQueue.clear();
  }
}
