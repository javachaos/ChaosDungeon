package com.github.javachaos.chaosdungeons;

import com.github.javachaos.chaosdungeons.ecs.GameLoop;
import com.github.javachaos.chaosdungeons.exceptions.ShaderLoadException;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.utils.PropertyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Application main entry.
 */
public class Main {
  private static final PropertyManager propertyManager = new PropertyManager();
  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  static {
    propertyManager.init();
  }

  /**
   * Property manager instance.
   *
   * @return the property manager.
   */
  public static PropertyManager getPropertyManager() {
    return propertyManager;
  }

  /**
   * Main method.
   *
   * @param args application arguments.
   */
  public static void main(String[] args) {
    GameLoop gl = new GameLoop();
    try {
      new GameWindow().run(gl);
    } catch (ShaderLoadException | InterruptedException e) {
      LOGGER.error("Error: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
