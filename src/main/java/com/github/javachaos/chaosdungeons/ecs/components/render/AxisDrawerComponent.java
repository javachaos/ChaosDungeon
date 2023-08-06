package com.github.javachaos.chaosdungeons.ecs.components.render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;

/**
 * Axis Drawer component for drawing x, y and z axes.
 */
public class AxisDrawerComponent extends RenderComponent {
  @Override
  public void onAdded(Entity e) {

  }

  @Override
  public void onRemoved(Entity e) {

  }

  @Override
  public void render(double dt) {
    glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_TEST);  // <---- add

    // set matrix mode
    glMatrixMode(GL_MODELVIEW);
    // clear model view matrix
    glLoadIdentity();

    // multiply view matrix to current matrix
    //gluLookAt(3.0, 3.0, 3.0-4.5, 0.0, 0.0,-4.5,0,1,0);

    // ******
    glPushMatrix();

    // glLoadIdentity(); <---- delete

    glTranslatef(0.0f, 0.0f, 1.5f);

    glBegin(GL_LINES);

    glColor3f(1.0f, 1.0f, 0.0f);
    glVertex3f(0.0f, 0.0f, 0.0f);
    glVertex3f(2.0f, 0.0f, 0.0f);

    glColor3f(1.0f, 1.0f, 0.0f);
    glVertex3f(0.0f, 0.0f, 0.0f);
    glVertex3f(0.0f, 2.0f, 0.0f);

    glColor3f(1.0f, 1.0f, 0.0f);
    glVertex3f(0.0f, 0.0f, 0.0f);
    glVertex3f(0.0f, 0.0f, 2.0f);
    glEnd();

    glPopMatrix();

    // clear the drawing buffer.
    // glClear(GL_COLOR_BUFFER_BIT);  // <---- delete

    // traslate the draw by z = -4.0
    // Note this when you decrease z like -8.0 the drawing will looks far , or smaller.
    glTranslatef(0.0f, 0.0f, -4.5f);
    // Red color used to draw.
    glColor3f(0.8f, 0.2f, 0.1f);
    glFlush();
  }
}
