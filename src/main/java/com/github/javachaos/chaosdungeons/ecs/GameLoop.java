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
    renderSystem = new RenderSystem(window);
    systems = new ArrayList<>();
    systems.add(new PhysicsSystem(window));
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
  public void render() {
    renderSystem.update((float) 0.0);
  }

  /**
   * Update systems.
   *
   * @param dt the time between render and update calls.
   */
  public void update(double dt) {
    systems.stream() // Loop over all system except the render system.
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
