package com.github.javachaos.chaosdungeons.utils;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.exceptions.ShaderLoadException;
import com.github.javachaos.chaosdungeons.exceptions.UniformException;
import com.github.javachaos.chaosdungeons.exceptions.UniformLoadException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

/**
 * Class which represents a shader program.
 */
@SuppressWarnings("unused")
public class ShaderProgram {
  private static final Logger LOGGER = LogManager.getLogger(ShaderProgram.class);
  private int programId;
  private final Map<String, Integer> uniforms;
  private final String vertexSrc;
  private final String fragSrc;
  private int uniMatProjection;
  private int uniMatView;
  private int uniMatModel;
  private int uniSample;


  /**
   * Create a new shader program.
   *
   * @param vertexShaderPath path to the vertex shader source, relative to src/main/resources
   * @param fragmentShaderPath path to the fragment shader source, relative to src/main/resources
   * @throws ShaderLoadException if the file path does not match
   *     ^([a-z])+([_]){0,1}([a-z])+([.]){1}([a-z])+$
   */
  public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) throws
      ShaderLoadException {
    if (!vertexShaderPath.matches(Constants.FILENAME_REGEX)
        || !fragmentShaderPath.matches(Constants.FILENAME_REGEX)) {
      throw new ShaderLoadException("Failed to load shader,"
          + " invalid file path string. Must be "
          + Constants.FILENAME_REGEX);
    }
    this.vertexSrc = readShaderSource(File.separator + vertexShaderPath);
    this.fragSrc = readShaderSource(File.separator + fragmentShaderPath);
    this.uniforms = new HashMap<>();
  }

  /**
   * Create a uniform named uniformName.
   *
   * @param uniformName the name of the uniform.
   * @throws UniformLoadException if the uniform does not exist in the shader program.
   */
  public void createUniform(String uniformName) throws UniformLoadException {
    int uni = glGetUniformLocation(programId, uniformName);
    if (uni < 0) {
      throw new UniformLoadException("Could not find uniform: " + uniformName);
    }
    uniforms.put(uniformName, uni);
  }

  /**
   * Set the uniform.
   *
   * @param name name of the uniform to set
   * @param value the value to set
   */
  public void setUniform(String name, Matrix4f value) throws UniformLoadException {
    if (!uniforms.containsKey(name)) {
      createUniform(name);
    }
    try (MemoryStack stack = MemoryStack.stackPush()) {
      FloatBuffer fb = stack.mallocFloat(16);
      value.get(fb);
      glUniformMatrix4fv(uniforms.get(name), false, fb);
    }
  }

  /**
   * Set the uniform for name.
   *
   * @param name the name of the uniform
   * @param value the 4D vector value
   */
  public void setUniform(String name, Vector4f value) {
    int loc = glGetUniformLocation(programId, name);
    if (loc != -1) {
      try (MemoryStack stack = MemoryStack.stackPush()) {
        FloatBuffer fb = stack.mallocFloat(4);
        value.get(fb);
        glUniform4fv(loc, fb);
      }
    }
  }

  /**
   * Set the uniform for name.
   *
   * @param name the name of the uniform
   * @param value the integer value
   */
  public void setUniform(String name, int value) {
    int loc = glGetUniformLocation(programId, name);
    if (loc != -1) {
      glUniform1i(loc, value);
    }
  }

  /**
   * Set sample value.
   *
   * @param sample the sample value to set.
   */
  public void setSampleTexture(int sample) {
    if (uniSample != -1) {
      glUniform1i(uniSample, sample);
    }
  }

  /**
   * Set the camera for this shader program.
   *
   * @param camera the camera instance.
   */
  public void setCamera(Camera camera) {
    if (uniMatProjection != -1) {
      float[] matrix = new float[16];
      camera.getProjection().get(matrix);
      glUniformMatrix4fv(uniMatProjection, false, matrix);
    }
    if (uniMatView != -1) {
      float[] matrix = new float[16];
      camera.getTransformation().get(matrix);
      glUniformMatrix4fv(uniMatView, false, matrix);
    }
  }

  /**
   * Set the transform for this shader.
   *
   * @param transform the transform to be set.
   */
  public void setTransform(Transform transform) {
    if (uniMatModel != -1) {
      float[] matrix = new float[16];
      transform.getTransformation().get(matrix);
      glUniformMatrix4fv(uniMatModel, false, matrix);
    }
  }


  /**
   * Get the location in memory as an integer for the uniform with name n.
   *
   * @param n the name of the uniform
   * @return the memory location as an integer
   */
  public int getUniformId(String n) {
    if (!uniforms.containsKey(n)) {
      throw new UniformException("Uniform not found: " + n);
    }
    return uniforms.get(n);
  }

  /**
   * Initialize this shader program.
   */
  public void init() throws ShaderLoadException {
    // Create the vertex shader
    int vertexShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShader,
        Objects.requireNonNull(vertexSrc));
    glCompileShader(vertexShader);

    // Create the fragment shader
    int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentShader,
        Objects.requireNonNull(fragSrc));
    glCompileShader(fragmentShader);

    // Create the shader program and attach the shaders
    programId = glCreateProgram();
    glAttachShader(programId, vertexShader);
    glAttachShader(programId, fragmentShader);

    // Link the shader program
    glLinkProgram(programId);

    if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
      throw new ShaderLoadException("Failed to link shader: "
          + glGetProgramInfoLog(programId, 1024));
    } else {
      LOGGER.debug("Shaders compiled successfully.");
    }

    // Validate the shader program (optional but recommended)
    glValidateProgram(programId);
    if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
      throw new ShaderLoadException("Error validating shader: "
          + glGetProgramInfoLog(programId, 1024));
    } else {
      LOGGER.debug("Shaders linked successfully.");
      LOGGER.debug("Vertex Source: " + System.lineSeparator() + vertexSrc);
      LOGGER.debug("Fragment Source: " + System.lineSeparator() + fragSrc);
    }

    uniMatProjection = glGetUniformLocation(programId, "projection");
    uniMatView = glGetUniformLocation(programId, "view");
    uniMatModel = glGetUniformLocation(programId, "model");
    uniSample = glGetUniformLocation(programId, "sample");

    glDetachShader(programId, vertexShader);
    glDetachShader(programId, fragmentShader);

    // Delete the individual shaders (they are already linked to the program)
    glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);
  }

  public String getVertexSrc() {
    return vertexSrc;
  }

  public String getFragSrc() {
    return fragSrc;
  }

  /**
   * Read shader source file.
   *
   * @param filePath the path to the shader source file
   * @return the file as a String
   */
  private String readShaderSource(String filePath) {
    InputStream inputStream = getClass().getResourceAsStream(filePath);
    if (inputStream == null) {
      LOGGER.error("Shader file not found: {}", filePath);
      return null;
    }
    StringBuilder sb = new StringBuilder();
    try (BufferedReader br =
             new BufferedReader(new InputStreamReader(inputStream))) {
      String ln;
      while ((ln = br.readLine()) != null) {
        sb.append(ln).append(System.lineSeparator());
      }
    } catch (IOException e) {
      LOGGER.error(e);
    }
    return sb.toString();
  }

  public int getProgramId() {
    return programId;
  }

  public void bind() {
    glUseProgram(getProgramId());
  }

  public void unbind() {
    glUseProgram(0);
  }
}
