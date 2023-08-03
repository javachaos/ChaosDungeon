package com.github.javachaos.chaosdungeons;

import com.github.javachaos.chaosdungeons.ecs.GameLoop;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main entry point.
 */
public class MainGame {
  private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

  /**
   * Main.
   *
   * @param args the args
   */
  public static void main(String[] args) {
    executorService.execute(GameLoop::run);
  }
}
