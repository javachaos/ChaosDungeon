package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.graphics.SpriteModel;
import com.github.javachaos.chaosdungeons.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
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
  private final Quaternionf rotationQuaternion;
  private final String texturePath;
  private static final Map<String, Texture> textureMap = new HashMap<>();
  private SpriteComponent sprite;

  /**
   * Create a game entity.
   */
  public GameEntity(String texturePath) {
    super();
    this.rotationQuaternion = new Quaternionf();
    this.texturePath = texturePath;
    this.modelTransform = new Matrix4f();
  }

  public SpriteComponent getSprite() {
    return sprite;
  }

  /**
   * Create a new game entity with initial pos, rotation, and scale.
   *
   * @param texturePath texture for this game entity
   * @param pos initial position of this game entity
   * @param rot initial rotation of this game entity
   * @param scale initial scale of this game entity
   */
  public GameEntity(String texturePath, Vector3f pos, Vector3f rot, Vector3f scale) {
    this(texturePath);
    updateModelMatrix(pos, rot, scale);
  }

  @Override
  public void init() {
    if (!textureMap.containsKey(texturePath)) {
      textureMap.put(texturePath, new Texture(texturePath));
    }
    sprite = new SpriteComponent(new SpriteModel(textureMap.get(texturePath), this));
    addComponent(sprite);
  }

  /**
   * Update the model matrix for this entity.
   *
   * @param position the position
   * @param rotation the rotation
   * @param scale the scale
   */
  public void updateModelMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
    rotationQuaternion.rotateX(rotation.x);
    rotationQuaternion.rotateY(rotation.y);
    rotationQuaternion.rotateZ(rotation.z);
    modelTransform.identity();
    modelTransform.translate(position).rotate(rotationQuaternion)
        .scale(scale);
  }

  /**
   * Update the model matrix for this entity.
   *
   * @param position the position
   * @param rotation the rotation
   * @param scale the scale
   */
  public void updateModelMatrix(Vector3f position, Quaternionf rotation, Vector3f scale) {
    modelTransform.identity();
    modelTransform.translate(position).rotate(rotation)
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
  public Quaternionf getRotation() {
    return rotationQuaternion;
  }

  /**
   * Set rotation.
   *
   * @param pitch The pitch angle in radians.
   * @param yaw The yaw angle in radians.
   * @param roll The roll angle in radians.
   */
  public void setRotation(float pitch, float yaw, float roll) {
    Matrix4f newRotation = new Matrix4f(); // Create a new rotation matrix
    newRotation.rotationXYZ(pitch, yaw, roll); // Set the rotation angles

    // Update the modelTransform matrix with the new rotation
    modelTransform.setTranslation(getPosition());
    modelTransform.setRotationXYZ(getRotation().x, getRotation().y, getRotation().z);
    modelTransform.scale(getScale());
  }

  public Matrix4f getModelMatrix() {
    return modelTransform;
  }

  /**
   * Render this entity.
   *
   * @param dt the delta time.
   */
  public void render(float dt) {
    //TODO restructure and clean this up better. A better implementation could be used here.
    // or possibly refactor the whole project as the complexity is high.
    // Would prefer a simpler project structure.
    sprite.render(dt);
  }
}
