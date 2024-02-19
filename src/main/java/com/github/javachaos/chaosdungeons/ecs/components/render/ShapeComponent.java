package com.github.javachaos.chaosdungeons.ecs.components.render;

import com.github.javachaos.chaosdungeons.geometry.polygons.Mesh;
import com.github.javachaos.chaosdungeons.collision.Polygon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.*;

/**
 * Shape Renderer class.
 */
@SuppressWarnings("unused")
public class ShapeComponent extends RenderComponent {

  private static final Logger LOGGER = LogManager.getLogger(ShapeComponent.class);
  private final Mesh mesh;
  private final Polygon shape;
  private Color color;
  private int colorId;

  /**
   * Create a new component.
   */
  public ShapeComponent(Polygon shape, Color c) {
    super();
    this.shape = shape;
    this.mesh = shape.createMesh();
    setColor(c);
  }

  public void setColor(Color c) {
    this.color = c;
  }

  public Color getColor() {
    return color;
  }

  public Polygon getShape() {
    return shape;
  }

  @Override
  public void render(double dt) {
      GL30.glBindVertexArray(mesh.getVaoID());
      GL11.glDrawElements(GL11.GL_POLYGON, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
      GL30.glBindVertexArray(0);
  }

}
