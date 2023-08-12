package com.github.javachaos.chaosdungeons.ecs.components.render;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.utils.MatrixUtils;

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
	GameEntity ge = ((GameEntity) getEntity());
	Vector3f p = ge.getPosition();
	//Get matrix transform without rotation.
	GameWindow.getWorldShader().setUniform("transformation", MatrixUtils.createTransformationMatrix(p, 0, 0, 0, ge.getScale()));
	
    glBegin(GL_LINES);

    glVertex2f(p.x -shape.w / 2, p.y -shape.h / 2);

    glVertex2f(p.x -shape.w / 2, p.y -shape.h / 2 + shape.h);
    glVertex2f(p.x -shape.w / 2, p.y -shape.h / 2 + shape.h);

    glVertex2f(p.x -shape.w / 2 + shape.w, p.y -shape.h / 2 + shape.h);

    glVertex2f(p.x -shape.w / 2 + shape.w, p.y -shape.h / 2 + shape.h);
    glVertex2f(p.x -shape.w / 2 + shape.w, p.y -shape.h / 2);

    glVertex2f(p.x -shape.w / 2 + shape.w, p.y -shape.h / 2);
    glVertex2f(p.x -shape.w / 2, p.y -shape.h / 2);

    glEnd();

    GameWindow.getWorldShader().setUniform("transformation", ((GameEntity) getEntity()).getModelMatrix());
  }
}
