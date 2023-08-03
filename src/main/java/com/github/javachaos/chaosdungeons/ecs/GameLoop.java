package com.github.javachaos.chaosdungeons.ecs;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game loop class.
 */
public class GameLoop {
  private static final long targetFps = 60;
  private static final long targetTime = 1000 / targetFps;

  private static boolean init;

  private static List<System> systems;

  /**
   * Initialize the game loop.
   */
  public static void init() {
    systems = new ArrayList<>();
    systems.add(new PhysicsSystem());
    systems.add(new RenderSystem());
    // add more later
    systems.forEach(System::init);
    init = true;
  }

  @SuppressWarnings("all")
  public static void run() {
    boolean isRunning = true;
    while (isRunning) {
      if (!init) {
        init();
      }
      long startTime = java.lang.System.nanoTime();
      // Update the game logic
      update();
      // Calculate the time taken for update and render
      long elapsedTime = java.lang.System.nanoTime() - startTime;
      // Calculate the time to sleep to achieve the target FPS
      long sleepTime = targetTime - elapsedTime;
      if (sleepTime > 0) {
        try {
          Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new RuntimeException("GameLoop interrupted.");
        }
      }
    }
  }

  private static void update() {
    // Compute delta time here and pass it to the update method of each game object
    // For example, if you have a list of RenderComponents, you can call their update methods
    double dt = (double) targetTime / 1000.0; // Assuming fixed timestep
    for (System s : systems) {
      s.update(dt);
    }
  }

}
