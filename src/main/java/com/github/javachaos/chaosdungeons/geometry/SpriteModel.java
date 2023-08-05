package com.github.javachaos.chaosdungeons.geometry;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import com.github.javachaos.chaosdungeons.utils.ShaderProgram;
import com.github.javachaos.chaosdungeons.utils.Texture;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class SpriteModel {
  private int drawCount;
  private int vertexId;
  private int textureId;
  private int indicesId;
  private int dimensions = 3;
  private Texture texture;
  private ShaderProgram shaderProgram;

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

  public SpriteModel(Texture texture, ShaderProgram shaderProgram) {
    this.shaderProgram = shaderProgram;
    this.texture = texture;
    this.drawCount = indices.length;

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

  public void render() {
    shaderProgram.setSampleTexture(0);
    texture.bind(0);
//    glEnableClientState(GL_VERTEX_ARRAY);
//    glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);

      glBindBuffer(GL_ARRAY_BUFFER, vertexId);
//      glVertexPointer(dimensions, GL_FLOAT, 0, 0);
      glVertexAttribPointer(0, dimensions, GL_FLOAT, false, 0, 0);

      glBindBuffer(GL_ARRAY_BUFFER, textureId);
//      glTexCoordPointer(dimensions - 1, GL_FLOAT, 0, 0);
      glVertexAttribPointer(1, dimensions - 1, GL_FLOAT, false, 0, 0);

      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesId);
      glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

      glBindBuffer(vertexId, 0);

//    glDisableClientState(GL_VERTEX_ARRAY);
//    glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
  }

  private FloatBuffer createBuffer(float[] data) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }

  private IntBuffer createBuffer(int[] data) {
    IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }

  public void delete() {
    texture.unbind();
    texture.delete();
  }
}
