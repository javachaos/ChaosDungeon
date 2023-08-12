package com.github.javachaos.chaosdungeons.ecs.components.render;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;

public class DebugCollisionRenderer extends RenderComponent {
  private QuadTree.Quad shape;

  public DebugCollisionRenderer(QuadTree.Quad v) {
    this.shape = v;
  }
  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {
  }

  @Override
  public void render(double dt) {
    glBegin(GL_LINES);

    glVertex2f(-shape.w / 2, -shape.h / 2);

    glVertex2f(-shape.w / 2, -shape.h / 2 + shape.h);
    glVertex2f(-shape.w / 2, -shape.h / 2 + shape.h);

    glVertex2f(-shape.w / 2 + shape.w, -shape.h / 2 + shape.h);

    glVertex2f(-shape.w / 2 + shape.w, -shape.h / 2 + shape.h);
    glVertex2f(-shape.w / 2 + shape.w, -shape.h / 2);

    glVertex2f(-shape.w / 2 + shape.w, -shape.h / 2);
    glVertex2f(-shape.w / 2, -shape.h / 2);

    glEnd();
  }
}
