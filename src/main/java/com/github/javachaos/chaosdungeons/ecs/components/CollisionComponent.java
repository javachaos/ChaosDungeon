package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Basic collision component class.
 */
public class CollisionComponent extends Component {

  private static final Logger LOGGER = LogManager.getLogger(CollisionComponent.class);

  private QuadTree.Quad shape;
  private final PhysicsComponent physicsComponent;

  /**
   * Create a new component.
   */
  public CollisionComponent(QuadTree.Quad shape, PhysicsComponent physicsComponent) {
    super();
    this.shape = shape;
    this.physicsComponent = physicsComponent;
  }

  /**
   * Check if this collision component is colliding with another collision component.
   *
   * @param other the other entity
   * @param otherCc the other entities collision component
   */
  public void onCollision(GameEntity other, CollisionComponent otherCc) {
    PhysicsComponent otherPhys = otherCc.physicsComponent;
    GameEntity thisGe = (GameEntity) getEntity();
    if (thisGe != other && shape.intersects(otherCc.getShape())) {
      LOGGER.debug("Collision detected between {} and {}", getEntity(), other);
      physicsComponent.applyForce(otherPhys.getVx(), otherPhys.getVy());
    }
  }

  /**
   * Return the shape for this collision component.
   *
   * @return the shape of this collision component
   */
  public QuadTree.Quad getShape() {
    return shape;
  }

  @Override
  public void update(double dt) {
    shape.x = ((GameEntity) getEntity()).getPosition().x;
    shape.y = ((GameEntity) getEntity()).getPosition().y;
    physicsComponent.update(dt);
  }

  @Override
  public void destroy() {
    shape = null;
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {

  }
}
