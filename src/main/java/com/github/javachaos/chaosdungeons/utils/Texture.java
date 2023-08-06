package com.github.javachaos.chaosdungeons.utils;

import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;
import org.lwjgl.system.MemoryStack;

/**
 * Texture class.
 */
@SuppressWarnings("unused")
public class Texture {

  private final int width;
  private final int height;
  private final int id;

  /**
   * Create a new texture.
   *
   * @param imagePath the path to the texture. e.g. if the file
   *                  were stored in src/main/resources/images/img.png
   *                  the string would be "images/img.png"
   */
  @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
  public Texture(String imagePath) {
    String path;
    URL resource = Texture.class.getResource(File.separator + imagePath);
    try {
      assert resource != null;
      path = Paths.get(resource.toURI()).toFile().getAbsolutePath();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

    try (MemoryStack stack = stackPush()) {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer components = stack.mallocInt(1);
      ByteBuffer decodedImage = stbi_load(path, w, h, components, 4);
      this.width = w.get();
      this.height = h.get();


      // Create a new OpenGL texture
      this.id = glGenTextures();
      glBindTexture(GL_TEXTURE_2D, this.id);

      // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
      glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_REPEAT);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_REPEAT);

      // Upload the texture data
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                   this.width, this.height, 0,
                   GL_RGBA, GL_UNSIGNED_BYTE, decodedImage);

      // Generate Mip Map
      glGenerateMipmap(GL_TEXTURE_2D);
    }
  }

  /**
   * Bind this texture to the texture sampler s.
   *
   * @param s the sampler index
   */
  public void bind(int s) {
    if (s > 0 && s <= 31) {
      // Bind the texture
      glActiveTexture(GL_TEXTURE0 + s);
      glBindTexture(GL_TEXTURE_2D, this.id);
    }
  }

  public void unbind() {
    glBindTexture(GL_TEXTURE_2D, 0);
  }

  public void delete() {
    glDeleteTextures(this.id);
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