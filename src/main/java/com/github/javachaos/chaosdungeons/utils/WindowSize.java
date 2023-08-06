package com.github.javachaos.chaosdungeons.utils;

/**
 * Window size pojo class.
 */
public class WindowSize {
  private int width;
  private int height;

  public WindowSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public float getAspectRatio() {
    return (1f * width) / (1f * height);
  }

}
