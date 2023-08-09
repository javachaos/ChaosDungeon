package com.github.javachaos.chaosdungeons.gui.fonts;
/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.GL_ALPHA;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_PackBegin;
import static org.lwjgl.stb.STBTruetype.stbtt_PackEnd;
import static org.lwjgl.stb.STBTruetype.stbtt_PackFontRange;
import static org.lwjgl.stb.STBTruetype.stbtt_PackSetOversampling;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;

public final class RenderedTextEntity extends GameEntity {

  private static final int BITMAP_W = 512;
  private static final int BITMAP_H = 512;

  private static final float[] scale = {
      24.0f,
      14.0f
  };

  private static final int[] sf = {
      0, 1, 2,
      0, 1, 2
  };

  // ----

  private final STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
  private final FloatBuffer      xb = memAllocFloat(1);
  private final FloatBuffer      yb = memAllocFloat(1);

  private int ww = GameWindow.getWindowSize().getWidth();
  private int wh = GameWindow.getWindowSize().getHeight();

  private int fbw = ww;
  private int fbh = wh;

  private int font_tex;

  private STBTTPackedchar.Buffer chardata;

  private int font = 3;

  private String text = "";

  public RenderedTextEntity() {
    super("assets/textures/fireball.png");
    load_fonts();
  }

  public void setText(String text) {
    this.text = text;
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


  private void load_fonts() {
    font_tex = glGenTextures();
    chardata = STBTTPackedchar.malloc(6 * 128);

    try (STBTTPackContext pc = STBTTPackContext.malloc()) {
      ByteBuffer ttf = loadFontFile("/assets/fonts/8bit.ttf");

      ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);

      stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
      for (int i = 0; i < 2; i++) {
        int p = (i * 3) * 128 + 32;
        chardata.limit(p + 95);
        chardata.position(p);
        stbtt_PackSetOversampling(pc, 1, 1);
        stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, chardata);

        p = (i * 3 + 1) * 128 + 32;
        chardata.limit(p + 95);
        chardata.position(p);
        stbtt_PackSetOversampling(pc, 2, 2);
        stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, chardata);

        p = (i * 3 + 2) * 128 + 32;
        chardata.limit(p + 95);
        chardata.position(p);
        stbtt_PackSetOversampling(pc, 3, 1);
        stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, chardata);
      }
      chardata.clear();
      stbtt_PackEnd(pc);

      glBindTexture(GL_TEXTURE_2D, font_tex);
      glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    }
  }

  private void draw_init() {
    glDisable(GL_CULL_FACE);
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_LIGHTING);
    glDisable(GL_DEPTH_TEST);

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0.0, ww, wh, 0.0, -1.0, 1.0);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
  }

  private static void drawBoxTC(float x0, float y0, float x1, float y1,
                                float s0, float t0, float s1, float t1) {
    glTexCoord2f(s0, t0);
    glVertex2f(x0, y0);
    glTexCoord2f(s1, t0);
    glVertex2f(x1, y0);
    glTexCoord2f(s1, t1);
    glVertex2f(x1, y1);
    glTexCoord2f(s0, t1);
    glVertex2f(x0, y1);
  }

  public void print(float x, float y, int font, String text) {
    xb.put(0, x);
    yb.put(0, y);

    chardata.position(font * 128);

    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, font_tex);

    glBegin(GL_QUADS);
    for (int i = 0; i < text.length(); i++) {
      stbtt_GetPackedQuad(chardata, BITMAP_W, BITMAP_H,
          text.charAt(i), xb, yb, q, font == 0);
      drawBoxTC(
          q.x0(), q.y0(), q.x1(), q.y1(),
          q.s0(), q.t0(), q.s1(), q.t1()
      );
    }
    glEnd();
  }

  private void draw_world() {
    int sfont = sf[font];

    float x = 20;

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    print(getPosition().x + (ww / 2f),
          getPosition().y + (wh / 2f),
        sfont, text);
    print(ww - 256, wh - 64, sfont, "FPS: " + GameWindow.getFPS());
  }

  private void draw() {
    if (!getSprite().isRemoved()) {
      getSprite().remove();
    }
    draw_init();
    draw_world();
  }

  @Override
  protected void update(float dt) {
    draw();
  }

  @Override
  public void destroy() {
    chardata.free();
    memFree(yb);
    memFree(xb);
    q.free();
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {
  }
}