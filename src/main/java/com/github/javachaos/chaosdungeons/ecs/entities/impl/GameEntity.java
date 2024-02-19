package com.github.javachaos.chaosdungeons.ecs.entities.impl;

import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.ecs.components.CollisionComponent;
import com.github.javachaos.chaosdungeons.ecs.components.GravityComponent;
import com.github.javachaos.chaosdungeons.ecs.components.PhysicsComponent;
import com.github.javachaos.chaosdungeons.ecs.components.TransformComponent;
import com.github.javachaos.chaosdungeons.ecs.components.render.DebugCollisionRenderer;
import com.github.javachaos.chaosdungeons.ecs.components.render.RenderComponent;
import com.github.javachaos.chaosdungeons.ecs.components.render.SpriteComponent;
import com.github.javachaos.chaosdungeons.ecs.components.render.UiRenderComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.SpawnData;
import com.github.javachaos.chaosdungeons.graphics.SpriteModel;
import com.github.javachaos.chaosdungeons.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

import org.joml.*;

/**
 * Game entity class.
 */
@SuppressWarnings("unused")
public abstract class GameEntity extends Entity {

  protected static final Map<String, Texture> textureMap = new HashMap<>();
  protected final String texturePath;
  private final SpawnData spawnData;
  protected RenderComponent sprite;
  protected CollisionComponent collisionComponent;
  protected TransformComponent transformComponent;
  private GravityComponent grav;
  private boolean init;

  private final GameContext gameContext;

  protected GameEntity(String texturePath, GameContext gameContext) {
    this(texturePath, new SpawnData.Builder().build(), gameContext);
  }

  /**
   * Create a game entity.
   */
  protected GameEntity(String texturePath, SpawnData data, GameContext gameContext) {
    super();
    this.gameContext = gameContext;
    this.spawnData = data;
    this.texturePath = texturePath;
  }

  /**
   * Create a new game entity with initial pos, rotation, and scale.
   *
   * @param texturePath texture for this game entity
   * @param pos         initial position of this game entity
   * @param rot         initial rotation of this game entity
   * @param scale       initial scale of this game entity
   */
  protected GameEntity(String texturePath,
                    Vector2f pos, Vector3f rot, Vector2f scale,
                    Vector2f initialV, Vector3f initialAngularVelocity,
                    GameContext gameContext) {
    this(texturePath, new SpawnData.Builder()
        .setPosition(pos)
        .setRotation(rot)
        .setScale(scale)
        .setInitialVelocity(initialV)
        .setAngularVelocity(initialAngularVelocity)
        .build(), gameContext);
  }

  /**
   * Create a new game entity with initial pos, rotation, and scale.
   *
   * @param texturePath texture for this game entity
   * @param pos         initial position of this game entity
   * @param rot         initial rotation of this game entity
   * @param scale       initial scale of this game entity
   */
  protected GameEntity(String texturePath, Vector2f pos, Vector3f rot, Vector2f scale, GameContext gameContext) {
    this(texturePath, new SpawnData.Builder()
        .setPosition(pos)
        .setRotation(rot)
        .setScale(scale)
        .build(), gameContext);
  }

  @Override
  public void init() {
    transformComponent = new TransformComponent();
    Quaternionf rotationQuaternion = new Quaternionf().rotationXYZ(
            spawnData.getRotation().x,
            spawnData.getRotation().y,
            spawnData.getRotation().z);
    rotationQuaternion.rotateX(spawnData.getRotation().x);
    rotationQuaternion.rotateY(spawnData.getRotation().y);
    rotationQuaternion.rotateZ(spawnData.getRotation().z);
    transformComponent.setRotation(rotationQuaternion);
    transformComponent.setPosition(new Vector3f(spawnData.getPosition(), 0f));
    transformComponent.setScale(new Vector3f(spawnData.getScale(), 0f));
    if (texturePath != null && !texturePath.isEmpty()) {
      textureMap.computeIfAbsent(texturePath, k ->  new Texture(texturePath));
    }
    if (texturePath != null && !texturePath.isEmpty()) {
      sprite = new SpriteComponent(new SpriteModel(textureMap.get(texturePath), this));
    }
    RenderComponent dcr = new DebugCollisionRenderer(spawnData.getShape());
    grav = new GravityComponent(
        spawnData.getMass(),
        spawnData.getRestitution(),
        spawnData.getInitialVelocity(),
        spawnData.getAngularVelocity(),
        spawnData.getGravitationFactor());
    collisionComponent = new CollisionComponent(spawnData.getShape());
    addComponent(dcr);
    if (sprite != null) {
      addComponent(sprite);
    }
    addComponent(grav);
    addComponent(collisionComponent);
    addComponent(transformComponent);
    init = true;
  }

  public RenderComponent getSprite() {
    return sprite;
  }

  public CollisionComponent getCollisionComponent() {
    return collisionComponent;
  }

  /**
   * Set the position of this game entity.
   *
   * @param pos the new position
   */
  public void setPosition(Vector2f pos) {
    if (!init) {
      throw new IllegalStateException("Game entity not yet initialized.");
    }
    transformComponent.setPosition(new Vector3f(pos.x, pos.y, 0));
  }

  public TransformComponent getTransformComponent() {
    return transformComponent;
  }

  /**
   * Render this entity.
   *
   * @param dt the delta time.
   */
  public void render(double dt) {
//    for(RenderComponent r : getRenderComponents()) {
//      r.render(dt);
//    }
  }

  public PhysicsComponent getPhysicsComponent() {
    return grav;
  }

  public boolean isDynamic() {
    return spawnData.isDynamic();
  }

  public GameContext getContext() {
    return gameContext;
  }
}
