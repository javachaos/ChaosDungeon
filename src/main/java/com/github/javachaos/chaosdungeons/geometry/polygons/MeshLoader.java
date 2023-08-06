package com.github.javachaos.chaosdungeons.geometry.polygons;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Mesh Loader class.
 */
@SuppressWarnings("all")
public class MeshLoader {

  private static final Logger LOGGER = LogManager.getLogger(MeshLoader.class);

  private static List<Integer> vaos = new ArrayList<>();
  private static List<Integer> vbos = new ArrayList<>();

  private static FloatBuffer createFloatBuffer(float[] data) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }

  private static IntBuffer createIntBuffer(int[] data) {
    IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }
  private static void storeData(int attribute, int dimensions, float[] data) {
    int vbo = GL15.glGenBuffers(); //Creates a VBO ID
    vbos.add(vbo);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo); //Loads the current VBO to store the data
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createFloatBuffer(data), GL15.GL_STATIC_DRAW);
    GL20.glVertexAttribPointer(attribute, dimensions, GL11.GL_FLOAT, false, 0, 0);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //Unloads the current VBO when done.
  }

  private static void bindIndices(int[] data) {
    int vbo = GL15.glGenBuffers();
    vbos.add(vbo);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(data), GL15.GL_STATIC_DRAW);
  }

  public static Mesh createMesh(float[] positions, int[] indices, int dimensions) {
    LOGGER.debug(Arrays.toString(positions));
    LOGGER.debug(Arrays.toString(indices));
    int vao = genVAO();
    storeData(0, dimensions, positions);
    bindIndices(indices);
    GL30.glBindVertexArray(0);
    return new Mesh(vao,indices.length);
  }

  private static int genVAO() {
    int vao = GL30.glGenVertexArrays();
    vaos.add(vao);
    GL30.glBindVertexArray(vao);
    return vao;
  }

}
