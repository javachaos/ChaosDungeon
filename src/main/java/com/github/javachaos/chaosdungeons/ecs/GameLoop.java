package com.github.javachaos.chaosdungeons.ecs;

import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.gui.GameWindow;

/**
 * Main game loop class.
 */
public class GameLoop {

  private boolean init;
  private final GameContext gameContext;
  private static final float FIXED_TIME_STEP = .0016F; // 16 milliseconds
  private double accumulatedTime = 0.0;

  public GameLoop() {
    gameContext = new GameContext();
  }

  /**
   * Initialize the game loop.
   */
  public void init() {
    //note: init order matters, load system should always be last
    gameContext.getRenderSystem().initSystem();
    gameContext.getPhysicsSystem().initSystem();
    gameContext.getLoadSystem().initSystem();
    init = true;
  }

  /**
   * Update render system.
   */
  public void render(double dt) {
    gameContext.getRenderSystem().update(dt);
  }

  /**
   * Update systems.
   *
   * @param dt the time between render and update calls.
   */
  public void update(double dt) {
    GameWindow.getCamera().update();
    // Accumulate time
    accumulatedTime += dt;
    gameContext.getLoadSystem().update(dt);
    // Perform physics updates for each fixed time step
    while (accumulatedTime >= FIXED_TIME_STEP) {
      gameContext.getPhysicsSystem().update(FIXED_TIME_STEP);
      accumulatedTime = 0;
    }
  }

  public void shutdown() {
    gameContext.shutdown();
  }

  public boolean isInitialized() {
    return init;
  }

  public GameContext getGameContext() {
    return gameContext;
  }
}
