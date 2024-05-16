package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;

/**
 * System class for ECS.
 */
public abstract class System {
  protected final GameContext gameContext;

  protected System(GameContext gameContext) {
    this.gameContext = gameContext;
  }

  /**
   * Update method for this system.
   *
   * @param dt the delta time between updates
   */
  public abstract void update(double dt);

  /**
   * Initialize this system.
   */
  public abstract void initSystem();

  /**
   * Called before shutdown, all entities at this point will have
   * been shutdown. Used when you have extra resources not tied
   * to entities you wish to destroy.
   */
  public abstract void destroy();
}
