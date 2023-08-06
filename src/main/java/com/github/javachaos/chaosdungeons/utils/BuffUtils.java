package com.github.javachaos.chaosdungeons.utils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

/**
 * Buffer utility class.
 */
public class BuffUtils {

  /**
   * Create a buffer from the data array.
   *
   * @param data the float array to create a buffer for
   * @return a new float buffer
   */
  public static FloatBuffer createBuffer(float[] data) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }

  /**
   * Create a buffer from the 4D vector of floats data.
   *
   * @param data the data vector
   * @return a float buffer containing the vector components
   */
  public static FloatBuffer createBuffer(Vector4f data) {
    FloatBuffer buffer;
    try (MemoryStack stack = MemoryStack.stackPush()) {
      buffer = stack.mallocFloat(4);
      data.get(buffer);
    }
    buffer.flip();
    return buffer;
  }

  /**
   * Create a float buffer of the vector3f data.
   *
   * @param data the 3D vector of floats
   * @return a float buffer of data
   */
  public static FloatBuffer createBuffer(Vector3f data) {
    FloatBuffer buffer;
    try (MemoryStack stack = MemoryStack.stackPush()) {
      buffer = stack.mallocFloat(3);
      data.get(buffer);
    }
    buffer.flip();
    return buffer;
  }

  /**
   * Create an int buffer of the integer array data.
   *
   * @param data the integer data
   * @return a new int buffer containing the data values
   */
  public static IntBuffer createBuffer(int[] data) {
    IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }
}
