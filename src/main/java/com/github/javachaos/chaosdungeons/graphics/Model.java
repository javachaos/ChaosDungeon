package com.github.javachaos.chaosdungeons.graphics;

/**
 * Model interface.
 */
public interface Model {

  /**
   * Render the model.
   */
  void render();

  /**
   * Free any GPU resources used.
   */
  void delete();

  /**
   * Initialize the rendering context, setup/initialize GPU buffers ect.
   */
  void init();
}
