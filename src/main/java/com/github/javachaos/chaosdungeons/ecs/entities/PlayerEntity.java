package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.gui.GameWindow;

/**
 * Player entity class.
 */
@SuppressWarnings("unused")
public class PlayerEntity extends GameEntity {

  /**
   * Create a new player entity.
   */
  public PlayerEntity() {
    super("assets/textures/player.png",
        new SpawnData.Builder()
            .setRestitution(1000f)
            .setMass(40f)
            .setGravitationFactor(0f)
            .setMaxSpawns(1)
            .setSpawnRate(1)
            .setShape(new QuadTree.Quad(0, 0, 2, 2))
            .build());
  }

  @Override
  public void init() {
    super.init();
  }

  @Override
  public void update(float dt) {
    updateModelMatrix(GameWindow.getCamera().getPosition(), getRotation(), getScale());
  }

  @Override
  public void destroy() {
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {
  }
}
