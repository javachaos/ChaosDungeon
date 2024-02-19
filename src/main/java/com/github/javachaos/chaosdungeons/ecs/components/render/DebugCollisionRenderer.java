package com.github.javachaos.chaosdungeons.ecs.components.render;

import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 * A class to draw simple debugging lines over the attached entity.
 */
public class DebugCollisionRenderer extends RenderComponent {
  private final Polygon shape;

  public DebugCollisionRenderer(Polygon v) {
    this.shape = v;
  }

  @Override
  public void update(double dt) {
    super.update(dt);
    GameEntity ge = ((GameEntity) getEntity());
    Vector3f pos = ge.getTransformComponent().getPosition();
    shape.translate(new Polygon.Point(pos.x, pos.y));
  }

  @Override
  public void render(double dt) {
    GameEntity ge = ((GameEntity) getEntity());
    Vector3f pos = ge.getTransformComponent().getPosition();
      glBegin(GL_LINE_LOOP);
        for (long i = 0; i < shape.getSize(); i++) {
          Polygon.Point pp = shape.getPoint(i);
          glVertex2f(pp.x() + pos.x - shape.getBounds().w() / 2f,
                  pp.y() + pos.y - shape.getBounds().h() / 2f);
        }
      glEnd();
  }
}
