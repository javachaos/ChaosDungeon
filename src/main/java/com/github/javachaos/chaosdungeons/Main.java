package com.github.javachaos.chaosdungeons;

import com.github.javachaos.chaosdungeons.ecs.GameLoop;
import com.github.javachaos.chaosdungeons.exceptions.ShaderLoadException;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.utils.PropertyManager;

/**
 * Main class.
 */
public class Main {
  private static final PropertyManager propertyManager = new PropertyManager();
  static {
    propertyManager.init();
  }

  public static PropertyManager getPropertyManager() {
    return propertyManager;
  }

  public static void main(String[] args) throws ShaderLoadException {
    GameLoop gl = new GameLoop();
    new GameWindow().run(gl);
  }
}
