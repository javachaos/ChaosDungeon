package com.github.javachaos.chaosdungeons.ecs;

import com.github.javachaos.chaosdungeons.ecs.systems.LoadSystem;
import com.github.javachaos.chaosdungeons.ecs.systems.PhysicsSystem;
import com.github.javachaos.chaosdungeons.ecs.systems.RenderSystem;
import com.github.javachaos.chaosdungeons.ecs.systems.System;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game loop class.
 */
public class GameLoop {

  private boolean init;
  private List<System> systems;
  private RenderSystem renderSystem;

  /**
   * Initialize the game loop.
   */
  public void init(GameWindow window) {
    systems = new ArrayList<>();
    renderSystem = new RenderSystem(window);
    systems.add(renderSystem);
    systems.add(new PhysicsSystem(window));
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
  public void render() {
    renderSystem.update((float) 0.0);
  }

  /**
   * Update systems.
   *
   * @param dt the time between render and update calls.
   */
  public void update(double dt) {
    systems.stream().filter(s -> !(s instanceof RenderSystem))
          .forEach(t -> t.update(dt));
  }

  public void shutdown() {
    System.shutdown();
  }

  public boolean isInitialized() {
    return init;
  }
}
