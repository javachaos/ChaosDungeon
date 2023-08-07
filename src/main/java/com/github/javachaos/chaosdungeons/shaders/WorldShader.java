package com.github.javachaos.chaosdungeons.shaders;

import com.github.javachaos.chaosdungeons.exceptions.ShaderLoadException;
import com.github.javachaos.chaosdungeons.exceptions.UniformLoadException;
import com.github.javachaos.chaosdungeons.utils.MatrixUtils;

/**
 * World shader class.
 */
@SuppressWarnings("unused")
public class WorldShader extends ShaderProgram {

  /**
   * Create a new shader program.
   */
  public WorldShader() throws ShaderLoadException {
    super("vertex.glsl", "fragment.glsl");
  }

  @Override
  public void addUniforms() {
    try {
      createUniform("sample");
      createUniform("transformation");
      createUniform("view");
      createUniform("projection");
    } catch (UniformLoadException e) {
      LOGGER.error("Error loading uniforms: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Set the projection matrix for this shader.
   */
  public void loadProjection(float zoom) {
    setUniform("projection", MatrixUtils.createProjectionMatrix(zoom));
  }
}
