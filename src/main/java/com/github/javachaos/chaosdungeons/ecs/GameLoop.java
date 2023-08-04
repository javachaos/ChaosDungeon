package com.github.javachaos.chaosdungeons.ecs;

import com.github.javachaos.chaosdungeons.ecs.systems.PhysicsSystem;
import com.github.javachaos.chaosdungeons.ecs.systems.RenderSystem;
import com.github.javachaos.chaosdungeons.ecs.systems.System;
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
  public void init() {
    renderSystem = new RenderSystem();
    systems = new ArrayList<>();
    systems.add(new PhysicsSystem());
    systems.add(renderSystem);
    // add more later
    systems.forEach(System::init);
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
    systems.stream()
        .filter(s -> !(s instanceof RenderSystem))
        .forEach(t -> t.update((float) dt));
  }

  public void shutdown() {
    systems.forEach(System::shutdown);
  }

  public boolean isInitialized() {
    return init;
  }
}
