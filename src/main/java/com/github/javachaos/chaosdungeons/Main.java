package com.github.javachaos.chaosdungeons;

import com.github.javachaos.chaosdungeons.ecs.GameLoop;
import com.github.javachaos.chaosdungeons.exceptions.GeneralGameException;
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
  @SuppressWarnings("exports")
  public static PropertyManager getPropertyManager() {
    return propertyManager;
  }

  /**
   * Main method.
   *
   * @param args application arguments.
   */
  public static void main(String[] args) {
    GameWindow gw = new GameWindow();
    GameLoop gl = new GameLoop();
    try {
      gw.run(gl);
    } catch (ShaderLoadException e) {
      LOGGER.error("Error: {}", e.getMessage());
      throw new GeneralGameException(e);
    } catch (InterruptedException e) {
      LOGGER.error("Thread interrupted: {}", e.getMessage());
      Thread.currentThread().interrupt();
      throw new GeneralGameException(e);
    }
  }
}
