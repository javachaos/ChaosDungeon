package com.github.javachaos.chaosdungeons.ecs.components.render;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.shaders.Shaders;
import com.github.javachaos.chaosdungeons.utils.MatrixUtils;
import org.joml.Vector3f;

/**
 * A class to draw simple debugging lines over the attached entity.
 */
public class DebugCollisionRenderer extends RenderComponent {
  private final Vertex shape;

  public DebugCollisionRenderer(Vertex v) {
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
    Shaders.getCurrentShader().setUniform("transformation",
        MatrixUtils.createTransformationMatrix(p, 0, 0, 0, ge.getScale()));

    glBegin(GL_LINES);

    glVertex2f(p.x - shape.getWidth() / 2, p.y - shape.getHeight() / 2);

    glVertex2f(p.x - shape.getWidth() / 2, p.y - shape.getHeight() / 2 + shape.getHeight());
    glVertex2f(p.x - shape.getWidth() / 2, p.y - shape.getHeight() / 2 + shape.getHeight());

    glVertex2f(p.x - shape.getWidth() / 2
            + shape.getWidth(), p.y - shape.getHeight() / 2 + shape.getHeight());

    glVertex2f(p.x - shape.getWidth() / 2
            + shape.getWidth(), p.y - shape.getHeight() / 2 + shape.getHeight());
    glVertex2f(p.x - shape.getWidth() / 2
            + shape.getWidth(), p.y - shape.getHeight() / 2);

    glVertex2f(p.x - shape.getWidth() / 2
            + shape.getWidth(), p.y - shape.getHeight() / 2);
    glVertex2f(p.x - shape.getWidth() / 2, p.y - shape.getHeight() / 2);

    glEnd();

    Shaders.getCurrentShader()
        .setUniform("transformation", ((GameEntity) getEntity()).getModelMatrix());
  }
}
