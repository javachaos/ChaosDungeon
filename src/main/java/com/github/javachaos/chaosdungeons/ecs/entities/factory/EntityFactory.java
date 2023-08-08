package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;

public interface EntityFactory<T extends GameEntity> {

  /**
   * Create a new entity of type <T>
   *
   * @return a new instance of this entity.
   */
  T create();
}
