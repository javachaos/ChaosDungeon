package com.github.javachaos.chaosdungeons.geometry.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.javachaos.chaosdungeons.geometry.math.RandomUtils;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Test RandomUtil.java class.
 */
class RandomUtilsTest {

  @RepeatedTest(10000)
  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  void testRandomUtils() {
    int i = RandomUtils.getRandom(1, 1000);
    assertTrue(i < 1000 && i >= 1);
  }

}
