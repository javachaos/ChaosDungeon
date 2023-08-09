package com.github.javachaos.chaosdungeons.geometry.polygons;

/**
 * A quad.
 */
public class Quad extends Vertex {

  /**
   * Create a new quad.
   * x,y  --------------- x+w,y
   *      |              |
   *      |              |
   * x,y+h ---------------x+w,y+h
   *
   * @param x top left x co-ordinate
   * @param y top left y co-ordinate
   * @param w width of quad
   * @param h height of quad
   */
  public Quad(float x, float y, float w, float h) {
    super(x, y);
    add(new Vertex(x + w, y));
    add(new Vertex(x + w, y + h));
    add(new Vertex(x, y + h));
  }

}
