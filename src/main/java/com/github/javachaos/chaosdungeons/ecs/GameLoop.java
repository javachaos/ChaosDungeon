package com.github.javachaos.chaosdungeons.ecs;

import com.github.javachaos.chaosdungeons.ecs.systems.LoadSystem;
import com.github.javachaos.chaosdungeons.ecs.systems.PhysicsSystem;
import com.github.javachaos.chaosdungeons.ecs.systems.RenderSystem;
import com.github.javachaos.chaosdungeons.ecs.systems.System;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import java.util.ArrayList;
import java.util.List;

import com.github.javachaos.chaosdungeons.shaders.Shaders;
import com.github.javachaos.chaosdungeons.utils.MatrixUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main game loop class.
 */
public class GameLoop {

  private boolean init;
  private List<System> systems;
  private RenderSystem renderSystem;

  private static final double FIXED_TIME_STEP = .016; // 16 milliseconds

  private double accumulatedTime = 0.0;
  private PhysicsSystem physicsSystem;

  /**
   * Initialize the game loop.
   */
  public void init(GameWindow window) {
    systems = new ArrayList<>();
    renderSystem = new RenderSystem(window);
    physicsSystem = new PhysicsSystem(window);
    systems.add(physicsSystem);
    systems.add(renderSystem);
    systems.add(new LoadSystem(window));
    // add more later
    for (System system : systems) {
      system.initSystem();
    }
    init = true;
  }

  /**
   * Update render system.
   */
  public void render(float dt) {
    Shaders.getCurrentShader().bind();
    Shaders.getCurrentShader().loadProjection();
    Shaders.getCurrentShader().setUniform("view",
            MatrixUtils.createViewMatrix(GameWindow.getCamera()));
    renderSystem.update(dt);
    Shaders.getCurrentShader().unbind();
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
    systems.stream().filter(s -> !(s instanceof RenderSystem) && !(s instanceof PhysicsSystem))
        .forEach(t -> t.update(dt));

    // Perform physics updates for each fixed time step
    while (accumulatedTime >= FIXED_TIME_STEP) {
      physicsSystem.update(FIXED_TIME_STEP);
      accumulatedTime = 0;
    }
  }

  public void shutdown() {
    System.shutdown();
  }

  public boolean isInitialized() {
    return init;
  }
}
