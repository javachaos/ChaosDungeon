package com.github.javachaos.chaosdungeons.constants;

import com.github.javachaos.chaosdungeons.Main;

/**
 * Constants file.
 */
@SuppressWarnings("unused")
public class Constants {

  /**
   * Match "normal" filenames, no special characters other than [a-z] or _ and .
   */
  public static final String FILENAME_REGEX = "^([a-z])+([_]){0,1}([a-z])+([.]){1}([a-z])+$";
  public static final String PNG_EXT = ".png";
  public static final double EPSILON = 1e-6;
  public static final String PROPERTY_FILE_NAME = "/engine.properties";
  public static final String EIGHT_BIT_FONT = "/assets/fonts/8bit.ttf";

  /**
   * The maximum number of entities of any given type.
   */
  public static final int MAX_ENTITIES = Main.getPropertyManager()
      .getIntegerProperty("game.default.max_entities", 1000);

  /**
   * Timeout in milliseconds.
   */
  public static final long DEFAULT_SHUTDOWN_TIMEOUT = Main.getPropertyManager()
      .getIntegerProperty("game.default_shutdown.timeout.millis", 5000);

  public static final String TITLE = "Chaos Dungeons";
  public static final double ONETHOUSAND = 1000.0;
  public static final int WINDOW_WIDTH = Main.getPropertyManager()
      .getIntegerProperty("game.default_window.width", 800);
  public static final int WINDOW_HEIGHT = Main.getPropertyManager()
      .getIntegerProperty("game.default_window.height", 600);
  public static final float Z_FAR = Main.getPropertyManager()
      .getFloatProperty("game.z_far", 1000.0f);
  public static final float Z_NEAR = Main.getPropertyManager()
      .getFloatProperty("game.z_near", 0.1f);
  public static final float FOV = (float) Math.toRadians(
      Main.getPropertyManager().getFloatProperty("game.fov", 70f));
    public static final String JAVA_PATH_SEPARATOR = "/";
    public static final boolean DEBUG = Main.getPropertyManager().getBooleanProperty("game.debug",
      false);
  /**
   * Unused constructor.
   */
  private Constants() {
    //unused
  }

  public static void init() {
    //kludge
  }
}
