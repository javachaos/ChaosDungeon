package com.github.javachaos.chaosdungeons.utils;

/**
 * Floating point tools.
 */
public class PrecisionUtils {
  private static final double EPSILON_D = 1e-9d;
  private static final float EPSILON_F = 1e-6f;

  public static boolean equalTo(float f1, float f2) {
    return Math.abs(f1 - f2) < EPSILON_F;
  }

  public static boolean equalTo(double f1, double f2) {
    return Math.abs(f1 - f2) < EPSILON_D;
  }

  public static boolean lessThan(float f1, float f2) {
    return f1 + EPSILON_F < f2;
  }

  public static boolean lessThan(double f1, double f2) {
    return f1 + EPSILON_D < f2;
  }

  public static boolean greaterThan(float f1, float f2) {
    return f1 - EPSILON_F > f2;
  }

  public static boolean greaterThan(double f1, double f2) {
    return f1 - EPSILON_D > f2;
  }

}
