package com.github.javachaos.chaosdungeons.geometry;

import static com.github.javachaos.chaosdungeons.utils.BuffUtils.createBuffer;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.utils.ShaderProgram;
import com.github.javachaos.chaosdungeons.utils.Texture;

/**
 * Sprite model.
 */
@SuppressWarnings("unused")
public class SpriteModel {
  private final int drawCount;
  private final int vertexId;
  private final int textureId;
  private int colorId;
  private final int indicesId;
  private final Texture texture;
  private final ShaderProgram shaderProgram;

  float[] vertices = new float[] {
      -0.5f,  0.5f, 0,
      0.5f,  0.5f, 0,
      0.5f,  -0.5f, 0,
      -0.5f, -0.5f, 0
  };
  float[] texCoords = new float[] {
      0, 0,
      1, 0,
      1, 1,
      0, 1,
  };

  int[] indices = new int[] {
    0, 1, 2,
    2, 3, 0
  };

  float[] color = new float[] {1.0f, 1.0f, 1.0f};

  /**
   * Create a new sprite model for rendering on the GPU.
   *
   * @param texture the texture for this sprite model.
   */
  public SpriteModel(Texture texture) {
    this.shaderProgram = GameWindow.getShader();
    this.texture = texture;
    this.drawCount = indices.length;

    this.vertexId = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, vertexId);
    glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

    this.textureId = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, textureId);
    glBufferData(GL_ARRAY_BUFFER, createBuffer(texCoords), GL_STATIC_DRAW);

    setColor(color);

    this.indicesId = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesId);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, createBuffer(indices), GL_STATIC_DRAW);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
  }

  /**
   * Render this sprite model on the GPU.
   */
  public void render() {
    shaderProgram.setSampleTexture(0);
    texture.bind(0);
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);
    glEnableVertexAttribArray(2);
    glBindBuffer(GL_ARRAY_BUFFER, vertexId);
    int dimensions = 3;
    glVertexAttribPointer(0, dimensions, GL_FLOAT, false, 0, 0);
    glBindBuffer(GL_ARRAY_BUFFER, textureId);
    glVertexAttribPointer(1, dimensions - 1, GL_FLOAT, false, 0, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesId);
    glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ARRAY_BUFFER, colorId);
    glVertexAttribPointer(2, 3, GL_FLOAT, true, 0, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(vertexId, 0);
    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
    glDisableVertexAttribArray(2);
  }

  /**
   * Optionally set the color for this sprite model, which is multiplied by the textures color in
   * the fragment shader.
   *
   * @param c the new color to be multiplied by the current texture color.
   */
  public void setColor(float[] c) {
    this.colorId = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, colorId);
    glBufferData(GL_ARRAY_BUFFER, createBuffer(c), GL_STATIC_DRAW);
  }

  /**
   * Delete the texture from the GPU.
   */
  public void delete() {
    texture.unbind();
    texture.delete();
  }

  /**
   * Get the red color value [0.0-1.0]
   *
   * @return the red color value
   */
  public float getRed() {
    return color[0];
  }

  /**
   * Get the green color value [0.0-1.0]
   *
   * @return the green color value
   */
  public float getGreen() {
    return color[1];
  }

  /**
   * Get the blue color value [0.0-1.0]
   *
   * @return the blue color value
   */
  public float getBlue() {
    return color[2];
  }

}
