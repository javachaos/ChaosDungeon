package com.github.javachaos.chaosdungeons.geometry.math;

import java.util.Random;

/**
 * Utility class to get a random generator.
 */
public class RandomUtils {
  private static final Random random = new Random(System.nanoTime());

  private RandomUtils() {
    //Unused
  }

  /**
   * Get random number generator.
   *
   * @return random instance
   */
  public static Random getRandom() {
    return random;
  }

  public static int getRandom(int lowerInclusive, int upperExlusive) {
    return getRandom().nextInt(upperExlusive - lowerInclusive) + lowerInclusive;
  }
}
