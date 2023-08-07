package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.graphics.SpriteModel;
import com.github.javachaos.chaosdungeons.graphics.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Game entity class.
 */
@SuppressWarnings("unused")
public abstract class GameEntity extends Entity {

  /**
   * Transform.
   */
  private final Matrix4f modelTransform;
  private final String texturePath;

  /**
   * Create a game entity.
   */
  public GameEntity(String texturePath) {
    super();
    this.texturePath = texturePath;
    this.modelTransform = new Matrix4f();
  }

  @Override
  public void init() {
    addComponent(new SpriteComponent(new SpriteModel(new Texture(texturePath), this)));
  }

  /**
   * Update the model matrix for this entity.
   *
   * @param position the position
   * @param rotation the rotation
   * @param scale the scale
   */
  public void updateModelMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
    modelTransform.identity();
    modelTransform.translate(position)
        .rotateX(rotation.x)
        .rotateY(rotation.y)
        .rotateZ(rotation.z)
        .scale(scale);
  }

  /**
   * Get the position of this game entity.
   *
   * @return the position of this game entity.
   */
  public Vector3f getPosition() {
    return new Vector3f(modelTransform.m30(), modelTransform.m31(), modelTransform.m32());
  }

  /**
   * Get the scale of this game entity.
   *
   * @return the scale as a vector 3
   */
  public Vector3f getScale() {
    return new Vector3f(
        modelTransform.getColumn(0, new Vector4f()).length(),
        modelTransform.getColumn(1, new Vector4f()).length(),
        modelTransform.getColumn(2, new Vector4f()).length()
    );
  }

  /**
   * Get rotation.
   *
   * @return return the rotation of this game entity.
   */
  public Vector3f getRotation() {
    float pitch = (float) Math.atan2(modelTransform.m12(), modelTransform.m22());
    float yaw = (float) Math.atan2(-modelTransform.m02(),
        Math.sqrt(modelTransform.m12() * modelTransform.m12()
            + modelTransform.m22() * modelTransform.m22()));
    float roll = (float) Math.atan2(modelTransform.m01(), modelTransform.m00());

    return new Vector3f(pitch, yaw, roll);
  }

  public Matrix4f getModelMatrix() {
    return modelTransform;
  }

}
