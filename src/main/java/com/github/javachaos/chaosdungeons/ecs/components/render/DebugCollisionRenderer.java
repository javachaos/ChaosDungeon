package com.github.javachaos.chaosdungeons.ecs.components.render;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.utils.MatrixUtils;
import org.joml.Vector3f;

/**
 * A class to draw simple debugging lines over the attached entity.
 */
public class DebugCollisionRenderer extends RenderComponent {
  private final QuadTree.Quad shape;

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
    GameWindow.getWorldShader().setUniform("transformation",
        MatrixUtils.createTransformationMatrix(p, 0, 0, 0, ge.getScale()));

    glBegin(GL_LINES);

    glVertex2f((float) (p.x - shape.wp / 2), (float) (p.y - shape.hp / 2));

    glVertex2f((float) (p.x - shape.wp / 2), (float) (p.y - shape.hp / 2 + shape.hp));
    glVertex2f((float) (p.x - shape.wp / 2), (float) (p.y - shape.hp / 2 + shape.hp));

    glVertex2f((float) (p.x - shape.wp / 2 + shape.wp), (float) (p.y - shape.hp / 2 + shape.hp));

    glVertex2f((float) (p.x - shape.wp / 2 + shape.wp), (float) (p.y - shape.hp / 2 + shape.hp));
    glVertex2f((float) (p.x - shape.wp / 2 + shape.wp), (float) (p.y - shape.hp / 2));

    glVertex2f((float) (p.x - shape.wp / 2 + shape.wp), (float) (p.y - shape.hp / 2));
    glVertex2f((float) (p.x - shape.wp / 2), (float) (p.y - shape.hp / 2));

    glEnd();

    GameWindow.getWorldShader()
        .setUniform("transformation", ((GameEntity) getEntity()).getModelMatrix());
  }
}
