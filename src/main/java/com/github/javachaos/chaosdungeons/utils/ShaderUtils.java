package com.github.javachaos.chaosdungeons.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for loading shader source files.
 * UTF-8 encoding assumed.
 */
public class ShaderUtils {

  private static final Logger LOGGER = LogManager.getLogger(ShaderUtils.class);

  /**
   * Read shader source file.
   *
   * @param filePath the path to the shader source file
   * @return the file as a String
   */
  public static String readShaderSource(String filePath) {
    InputStream inputStream = ShaderUtils.class.getResourceAsStream(filePath);
    if (inputStream == null) {
      LOGGER.error("Shader file not found: {}", filePath);
      return null;
    }

    try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
      scanner.useDelimiter("\\A"); // Read the entire file in one go
      return scanner.hasNext() ? scanner.next() : "";
    }
  }
}
