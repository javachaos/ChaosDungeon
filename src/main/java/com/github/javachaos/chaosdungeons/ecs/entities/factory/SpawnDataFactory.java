package com.github.javachaos.chaosdungeons.ecs.entities.factory;

/**
 * Spawn data factory interface.
 */
public interface SpawnDataFactory {

  /**
   * Create a new entity of type {@link SpawnData}.
   *
   * @return a new instance of this entity.
   */
  SpawnData create();
}
