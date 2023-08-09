package com.github.javachaos.chaosdungeons.ecs.components.render;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;

public class TextRenderer {
  private int fontTexture;
  private final int fontHeight = 24;
  private final int fontWidth = 24;
  private final IntBuffer ix0 = BufferUtils.createIntBuffer(1);
  private final IntBuffer iy0 = BufferUtils.createIntBuffer(1);
  private final IntBuffer ix1 = BufferUtils.createIntBuffer(1);
  private final IntBuffer iy1 = BufferUtils.createIntBuffer(1);
  private final IntBuffer advanceBuffer = BufferUtils.createIntBuffer(1);
  private final IntBuffer bearingBuffer = BufferUtils.createIntBuffer(1);

  private STBTTFontinfo fontInfo;

  public TextRenderer() {
    // Load the font and create a font texture
    ByteBuffer fontBuffer = loadFontFile("assets/fonts/font.ttf");
    int bitmapWidth = 1024; // Width of your texture atlas
    int bitmapHeight = 1024; // Height of your texture atlas

    fontInfo = STBTTFontinfo.create();
    assert fontBuffer != null;
    STBTruetype.stbtt_InitFont(fontInfo, fontBuffer);

    // Create a buffer to store the baked glyph data
    STBTTBakedChar.Buffer bakedCharBuffer = STBTTBakedChar.malloc(256);
    // Adjust size as needed        // Create a buffer to store the baked glyph data
    int bakedCharBufferSize = 256 * STBTTBakedChar.SIZEOF;
    ByteBuffer pixData = BufferUtils.createByteBuffer(bakedCharBufferSize);

    // Bake the font glyphs into the buffer
    STBTruetype.stbtt_BakeFontBitmap(fontBuffer, fontHeight, pixData,
        bitmapWidth, bitmapHeight, 0, bakedCharBuffer); // Adjust the range if needed

    fontTexture = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, fontTexture);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, bitmapWidth, bitmapHeight, 0, GL_RED,
        GL_UNSIGNED_BYTE, pixData);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glBindTexture(GL_TEXTURE_2D, 0);

    // Clean up the bakedCharBuffer
    bakedCharBuffer.free();
  }

  private ByteBuffer loadFontFile(String filePath) {
    try (InputStream inputStream = getClass().getResourceAsStream(filePath)) {
      if (inputStream == null) {
        throw new IOException("Font file not found: " + filePath);
      }

      byte[] fontData = inputStream.readAllBytes();

      ByteBuffer buffer = BufferUtils.createByteBuffer(fontData.length);
      buffer.put(fontData);
      buffer.flip();

      return buffer;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }


  public void renderText(String text, float x, float y) {
    glBegin(GL_QUADS);

    float posX = x;
    float posY = y;

    for (int i = 0; i < text.length(); i++) {
      int charCode = text.charAt(i);

      STBTruetype.stbtt_GetCodepointHMetrics(fontInfo, charCode, advanceBuffer, bearingBuffer);
      float advance = advanceBuffer.get(0) *
          (fontHeight / STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, fontHeight));
      float bearingX = bearingBuffer.get(0) *
          (fontHeight / STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, fontHeight));
      // Calculate texture coordinates for the character
     STBTruetype.stbtt_GetCodepointBitmapBoxSubpixel(
              fontInfo, charCode,
              fontHeight,
              fontWidth,
              0, 0,
              ix0, iy0, ix1, iy1);
      float[] texCoords = new float[] {
          ix0.get(0),
          iy0.get(0),
          ix1.get(0),
          iy1.get(0)
      };
      glTexCoord2f(texCoords[0] / fontHeight, texCoords[1] / fontHeight);
      glVertex2f(posX + bearingX, posY);

      glTexCoord2f(texCoords[0] / fontHeight, texCoords[3] / fontHeight);
      glVertex2f(posX + bearingX, posY + fontHeight);

      glTexCoord2f(texCoords[2] / fontHeight, texCoords[3] / fontHeight);
      glVertex2f(posX + bearingX + texCoords[2], posY + fontHeight);

      glTexCoord2f(texCoords[2] / fontHeight, texCoords[1] / fontHeight);
      glVertex2f(posX + bearingX + texCoords[2], posY);

      posX += advance;
    }

    glEnd();
  }
}
