package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.joml.Vector3f;

/**
 * Basic collision component class.
 */
public class CollisionComponent extends Component {

  private static final Logger LOGGER = LogManager.getLogger(CollisionComponent.class);
  private Polygon shape;

  /**
   * Create a new component.
   */
  public CollisionComponent(Polygon shape) {
    super();
    this.shape = shape;
  }

  /**
   * Called when a collision occurs
   */
  public void onCollision(GameEntity thiz, GameEntity other) {
    LOGGER.debug("Collision: {} and {}.", thiz.getEntityId(), other.getEntityId());
  }

  /**
   * Return the shape for this collision component.
   *
   * @return the shape of this collision component
   */
  public Polygon getShape() {
    return shape;
  }

  @Override
  public void update(double dt) {
    GameEntity gameEntity = (GameEntity)getEntity();
    Vector3f pos = gameEntity.getComponent(TransformComponent.class).getPosition();
    shape.translate(new Polygon.Point(pos.x, pos.y));
  }

  @Override
  public void destroy() {
    shape = null;
  }

}
