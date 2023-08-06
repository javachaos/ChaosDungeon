package com.github.javachaos.chaosdungeons.shaders;

import com.github.javachaos.chaosdungeons.exceptions.ShaderLoadException;

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
    //no uniforms for UI shader. (yet)
  }
}
