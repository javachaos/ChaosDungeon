package com.github.javachaos.chaosdungeons.shaders;

import com.github.javachaos.chaosdungeons.exceptions.ShaderLoadException;
import com.github.javachaos.chaosdungeons.exceptions.UniformLoadException;
import com.github.javachaos.chaosdungeons.graphics.Camera;
import com.github.javachaos.chaosdungeons.graphics.Transform;

/**
 * World shader class.
 */
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
      createUniform("model");
      createUniform("sample");
      createUniform("view");
      createUniform("projection");
    } catch (UniformLoadException e) {
      LOGGER.error("Error loading uniforms: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Set the camera for this shader program.
   *
   * @param camera the camera instance.
   */
  public void setCamera(Camera camera) {
    setUniform("projection", camera.getProjection());
    setUniform("view", camera.getTransformation());
  }

  /**
   * Set the transform for this shader.
   *
   * @param transform the transform to be set.
   */
  public void setTransform(Transform transform) {
    setUniform("model", transform.getTransformation());
  }
}
