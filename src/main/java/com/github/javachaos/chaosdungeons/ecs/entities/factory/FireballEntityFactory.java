package com.github.javachaos.chaosdungeons.ecs.entities.factory;

import com.github.javachaos.chaosdungeons.ecs.entities.impl.Fireball;
import org.joml.Vector3f;

/**
 * Fireball factory for creating fireballs.!
 */
public class FireballEntityFactory implements EntityFactory<Fireball> {
  @Override
  public Fireball create() {
    return new Fireball(-.5f,
        (float) (1f * Math.random()),
        new Vector3f(
            (float) -(Math.random() * 3.0f),
            (float) (Math.random() * 3.0f),
            0), new Vector3f(
        (float) (Math.random() * 3.0f),
        (float) (Math.random() * 3.0f),
        (float) (Math.random() * 3.0f)));
  }
}
