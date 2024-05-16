package com.github.javachaos.chaosdungeons.graphics;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL42.glTexStorage2D;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import com.github.javachaos.chaosdungeons.exceptions.GeneralGameException;

/**
 * Texture class.
 */
@SuppressWarnings("unused")
public class Texture {

  private static final Logger LOGGER = LogManager.getLogger(Texture.class);

  private final int width;
  private final int height;
  private final int id;
  private boolean deleted;

  /**
   * Create a new texture.
   *
   * @param imagePath the path to the texture. e.g. if the file
   *                  were stored in src/main/resources/images/img.png
   *                  the string would be "images/img.png"
   */
  @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
  public Texture(String imagePath) {
    int initialSize = 1024;
    LOGGER.debug("Loading texture: {}", imagePath);
    String path;
    InputStream resource = Texture.class.getResourceAsStream("/" + imagePath);
    ByteBuffer buffer = BufferUtils.createByteBuffer(initialSize);
    if (resource == null) {
      throw new GeneralGameException("Texture not found: " + imagePath);
    }

    int pixel;
    try {
      while ((pixel = resource.read()) != -1) {
        buffer.put((byte) pixel);
        if (buffer.remaining() == 0) {
          ByteBuffer buff = BufferUtils.createByteBuffer(buffer.capacity() + initialSize);
          buffer.flip();
          buff.put(buffer);
          buffer = buff;
        }
      }
      resource.close();
    } catch (IOException e) {
      LOGGER.error(e);
    }
    buffer.flip();
    try (MemoryStack stack = stackPush()) {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer components = stack.mallocInt(1);

      ByteBuffer decodedImage = stbi_load_from_memory(buffer, w, h, components, 4);
      this.width = w.get();
      this.height = h.get();

      // Create a new OpenGL texture
      this.id = glGenTextures();
      glBindTexture(GL_TEXTURE_2D, this.id);
      glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, width, height);
      assert decodedImage != null;
      glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE,
                decodedImage);
      glGenerateMipmap(GL_TEXTURE_2D);  //Generate num_mipmaps number of mipmaps here.
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);

    }
  }

  /**
   * Bind this texture to the texture sampler s.
   *
   * @param s the sampler index
   */
  public void bind(int s) {
      // Bind the texture
      glActiveTexture(GL_TEXTURE0 + s);
      glBindTexture(GL_TEXTURE_2D, this.id);
  }

  public void unbind() {
    glBindTexture(GL_TEXTURE_2D, 0);
  }

  /**
   * Delete this texture.
   */
  public void delete() {
    if (!deleted) {
      LOGGER.debug("Texture deleted: {}", id);
      glDeleteTextures(this.id);
      deleted = true;
    }
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public int getId() {
    return id;
  }
}