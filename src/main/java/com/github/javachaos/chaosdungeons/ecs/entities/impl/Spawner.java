package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.EntityFactory;
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
public class Spawner<T extends GameEntity> extends GameEntity {

  private static final Logger LOGGER = LogManager.getLogger(Spawner.class);
  private final EntityFactory<T> factory;
  private final Deque<T> spawnQueue;
  private float spawnRate = 1.0f;
  private int maxSpawns = 1;

  private float timeSinceLastSpawn;

  public Spawner(EntityFactory<T> factory, float spawnRate, int maxSpawns) {
    super("assets/textures/images.png");
    this.factory = factory;
    this.spawnRate = spawnRate;
    this.maxSpawns = maxSpawns;
    this.spawnQueue = new ArrayDeque<>();
  }

  @Override
  public void onAdded(Entity e) {
    getComponent(SpriteComponent.class).setVisible(false);
  }

  @Override
  public void onRemoved(Entity e) {
  }

  @Override
  protected void update(float dt) {
    if (maxSpawns > 0) {
      timeSinceLastSpawn += dt;

      if (timeSinceLastSpawn >= (1.0f / spawnRate)) {
        spawnQueue.offer(newInstance());
        maxSpawns--;
        timeSinceLastSpawn = 0.0f; // Reset the timer
      }
    }
    if (!spawnQueue.isEmpty()) {
      T e = spawnQueue.poll();
      e.init();
      spawn(e);
    }
  }

  private T newInstance() {
    return factory.create();
  }

  private void spawn(T entity) {
    //TODO figure out how best to spawn an entity into the world.
    System.addEntity(entity, false);
  }

  @Override
  public void destroy() {
    //clean up any potential remaining un-spawned items.
    spawnQueue.forEach(Entity::shutdown);
    spawnQueue.clear();
  }
}
