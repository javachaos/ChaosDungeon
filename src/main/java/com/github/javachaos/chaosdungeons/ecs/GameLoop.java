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
  private final long targetFps = 60;
  private long targetTime = 1000 / targetFps;

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
  }

  public long getTargetTime() {
    return targetTime;
  }

  /**
   * Update render system.
   *
   * @param dt the time between calls to update and render
   */
  public void render(double dt) {
    renderSystem.update((float) dt);
  }

  /**
   * Update systems.
   *
   * @param dt the time between render and update calls.
   */
  public void update(double dt) {
    systems.stream()
        .filter(s -> !(s instanceof RenderSystem))
        .forEach(t -> t.update(dt));
  }

  public void setTargetTime(long elapsedTime) {
    this.targetTime = elapsedTime;
  }

  public void shutdown() {
    systems.forEach(System::shutdown);
  }
}
