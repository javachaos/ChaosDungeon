package com.github.javachaos.chaosdungeons.ecs.components.render;

import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.geometry.polygons.Mesh;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import java.awt.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Shape Renderer class.
 */
@SuppressWarnings("unused")
public class ShapeComponent extends RenderComponent {

  private static final Logger LOGGER = LogManager.getLogger(ShapeComponent.class);
  private final Mesh mesh;
  private final Vertex shape;
  private Color color;

  /**
   * Create a new component.
   */
  public ShapeComponent(Vertex shape, Color c) {
    super();
    this.shape = shape;
    this.mesh = shape.createMesh();
    setColor(c);
  }

  public void setColor(Color c) {
    this.color = c;
  }

  public Vertex getShape() {
    return shape;
  }

  @Override
  public void render() {
    GL11.glColor3f(color.getRed()   / 255.0f,
                  color.getGreen() / 255.0f,
                  color.getBlue()  / 255.0f); // Red color
    GL30.glBindVertexArray(mesh.getVaoID());
    GL20.glEnableVertexAttribArray(0);
    GL11.glDrawElements(GL11.GL_POLYGON, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
    GL20.glDisableVertexAttribArray(0);
    GL30.glBindVertexArray(0);
  }

  @Override
  public void onAdded(GameEntity e) {

  }

  @Override
  public void onRemoved(GameEntity e) {

  }
}
