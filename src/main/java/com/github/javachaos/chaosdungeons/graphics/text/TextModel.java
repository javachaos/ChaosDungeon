package com.github.javachaos.chaosdungeons.graphics.text;

import static com.github.javachaos.chaosdungeons.utils.FileUtils.loadFontFile;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_PackBegin;
import static org.lwjgl.stb.STBTruetype.stbtt_PackEnd;
import static org.lwjgl.stb.STBTruetype.stbtt_PackFontRange;
import static org.lwjgl.stb.STBTruetype.stbtt_PackSetOversampling;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

import com.github.javachaos.chaosdungeons.ecs.components.TransformComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;
import com.github.javachaos.chaosdungeons.graphics.Model;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.gui.WindowSize;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;

/**
 * RenderedTextEntity class.
 * Heavily influenced by: <a href="https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/stb/TruetypeOversample.java">lwjgl 3 text demo</a>
 * Credit where credit is due.
 */
public class TextModel implements Model {

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

  private final STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
  private final FloatBuffer xb = memAllocFloat(1);
  private final FloatBuffer yb = memAllocFloat(1);
  private final String fontPath;
  private final GameEntity pos;

  private int fontText;

  private STBTTPackedchar.Buffer chardata;

  private String text = "";


  /**
   * Create a new RenderedTextEntity given the font path.
   *
   * @param fontPath the path to the ttf font file used to construct this text entity.
   */
  protected TextModel(String fontPath, GameEntity ge) {
    this.fontPath = fontPath;
    this.pos = ge;
    init();
  }

  public void setText(String text) {
    this.text = text;
  }



  private static void drawSquareTexCoords(final float[] texCoords) {
    float x0 = texCoords[0];
    float y0 = texCoords[1];
    float x1 = texCoords[2];
    float y1 = texCoords[3];
    float s0 = texCoords[4];
    float t0 = texCoords[5];
    float s1 = texCoords[6];
    float t1 = texCoords[7];
    glTexCoord2f(s0, t0);
    glVertex2f(x0, y0);
    glTexCoord2f(s1, t0);
    glVertex2f(x1, y0);
    glTexCoord2f(s1, t1);
    glVertex2f(x1, y1);
    glTexCoord2f(s0, t1);
    glVertex2f(x0, y1);
  }

  /**
   * Print text to the screen, at x, y in screen co-ordinates.
   *
   * @param x the x-pos
   * @param y the y-pos
   * @param font the font
   * @param text the string to print
   */
  private void print(float x, float y, int font, String text) {
    xb.put(0, x);
    yb.put(0, y);

    chardata.position(font * 128);

    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, fontText);
    glBegin(GL_QUADS);
    for (int i = 0; i < text.length(); i++) {
      stbtt_GetPackedQuad(chardata, BITMAP_W, BITMAP_H,
          text.charAt(i), xb, yb, quad, font == 0);
      drawSquareTexCoords(new float[] {
          quad.x0(), quad.y0(), quad.x1(), quad.y1(),
          quad.s0(), quad.t0(), quad.s1(), quad.t1() }
      );
    }
    glEnd();
  }

  @Override
  public void render() {
    glDisable(GL_CULL_FACE);
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_LIGHTING);
    glDisable(GL_DEPTH_TEST);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    WindowSize ws = GameWindow.getWindowSize();
    glOrtho(0.0, ws.getWidth(), ws.getHeight(), 0.0, -1.0, 1.0);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    int font = 3;
    int sfont = sf[font];
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    TransformComponent transform = pos.getComponent(TransformComponent.class);
    if (transform != null) {
      print(transform.getPosition().x + (ws.getWidth() / 2f),
              transform.getPosition().y + (ws.getHeight() / 2f),
              sfont, text);
    }
    print(ws.getWidth() - 256f, ws.getHeight() - 64f, sfont, "FPS: " + GameWindow.getFps());
  }

  @Override
  public void delete() {
    chardata.free();
    memFree(yb);
    memFree(xb);
    quad.free();
  }

  @Override
  public void init() {
    fontText = glGenTextures();
    chardata = STBTTPackedchar.malloc(6 * 128);
    try (STBTTPackContext pc = STBTTPackContext.malloc()) {
      ByteBuffer ttf = loadFontFile(fontPath);
      ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
      stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
      for (int i = 0; i < 2; i++) {
        int p = (i * 3) * 128 + 32;
        chardata.limit(p + 95);
        chardata.position(p);
        stbtt_PackSetOversampling(pc, 1, 1);
        assert ttf != null;
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
      glBindTexture(GL_TEXTURE_2D, fontText);
      glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H,
          0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    }
  }

}