package com.github.javachaos.chaosdungeons.constants;

/**
 * Constants file.
 */
@SuppressWarnings("unused")
public class Constants {

  public static final String FILENAME_REGEX = "^([a-z])+([_]){0,1}([a-z])+$";
  public static final String PNG_EXT = ".png";
  public static final double EPSILON = 1e-6;

  /**
   * Timeout in milliseconds.
   */
  public static final long DEFAULT_SHUTDOWN_TIMEOUT = 5000;

  public static final String TITLE = "Chaos Dungeons";
  public static final double ONETHOUSAND = 1000.0;

  /**
   * Unused constructor.
   */
  private Constants() {
    //unused
  }
}
