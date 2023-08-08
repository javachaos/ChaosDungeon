package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;

/**
 * EntityFactory interface.
 *
 * @param <T> the type of game entity this factory will create.
 */
public interface EntityFactory<T extends GameEntity> {

  /**
   * Create a new entity of type {@link T}.
   *
   * @return a new instance of this entity.
   */
  T create();
}
