package com.github.javachaos.chaosdungeons.geometry.polygons;

/**
 * Simple mesh class.
 */
@SuppressWarnings("all")
public class Mesh {

  private int vao;
  private int vertices;

  public Mesh(int vao, int vertex) {
    this.vao = vao;
    this.vertices = vertex;
  }

  public int getVaoID() {
    return vao;
  }

  public int getVertexCount() {
    return vertices;
  }
}
