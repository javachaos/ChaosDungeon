package com.github.javachaos.chaosdungeons.graphics;

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
import static org.lwjgl.opengl.GL20.*;

import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;

/**
 * Sprite model.
 */
@SuppressWarnings("unused")
public class SpriteModel implements Model {
  private final int drawCount;
  private int vertexId;
  private int textureId;
  private int indicesId;
  private final Texture texture;
  float[] vertices = new float[] {
      -0.5f, 0.5f, 0,
      0.5f, 0.5f, 0,
      0.5f, -0.5f, 0,
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
  private int colorId;
  private final GameEntity ge;
  private boolean deleted;

  /**
   * Create a new sprite model for rendering on the GPU.
   *
   * @param texture the texture for this sprite model.
   */
  public SpriteModel(Texture texture, GameEntity ge) {
    this.texture = texture;
    this.drawCount = indices.length;
    this.ge = ge;
    init();
  }

  /**
   * Render this sprite model on the GPU.
   */
  @Override
  public void render() {
      texture.bind(0);
      glEnableVertexAttribArray(0);
      glEnableVertexAttribArray(1);
      glBindBuffer(GL_ARRAY_BUFFER, vertexId);
      glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
      glBindBuffer(GL_ARRAY_BUFFER, textureId);
      glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesId);
      glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glDisableVertexAttribArray(0);
      glDisableVertexAttribArray(1);
      texture.unbind();
  }

  /**
   * Delete the texture from the GPU.
   */
  @Override
  public void delete() {
    if (!deleted && texture != null) {
      texture.delete();
      deleted = true;
    }
  }

  @Override
  public void init() {
    this.vertexId = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, vertexId);
    glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

    this.textureId = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, textureId);
    glBufferData(GL_ARRAY_BUFFER, createBuffer(texCoords), GL_STATIC_DRAW);

    this.indicesId = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesId);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, createBuffer(indices), GL_STATIC_DRAW);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
  }

}
