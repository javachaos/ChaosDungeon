package com.github.javachaos.chaosdungeons.shaders;

import com.github.javachaos.chaosdungeons.exceptions.GeneralGameException;
import com.github.javachaos.chaosdungeons.exceptions.ShaderLoadException;
import com.github.javachaos.chaosdungeons.exceptions.UniformLoadException;

/**
 * UI shader class.
 */
public class UiShader extends ShaderProgram {

  /**
   * Create a new shader program.
   */
  public UiShader() throws ShaderLoadException {
    super("ui_vertex.glsl", "fragment.glsl");
  }

  @Override
  public void addUniforms() {
    try {
      createUniform("pos");
    } catch (UniformLoadException e) {
      LOGGER.error("Error loading uniforms: {}", e.getMessage());
      throw new GeneralGameException(e);
    }
  }

  @Override
  public void loadProjection() {
    //no projection uniform (yet)
  }
}
