package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.Component;
import com.github.javachaos.chaosdungeons.ecs.Entity;
import com.github.javachaos.chaosdungeons.geometry.SatCollisionDetector;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Basic collision component class.
 */
public class CollisionComponent extends Component {

  private static final Logger LOGGER = LogManager.getLogger(CollisionComponent.class);

  private Vertex shape;

  /**
   * Create a new component with id.
   *
   * @param id the id of this component
   */
  public CollisionComponent(int id, Vertex shape) {
    super(id);
    this.shape = shape;
  }

  /**
   * Check if this collision component is colliding with another collision component.
   *
   * @param other the other entity
   * @param otherPhys the other entities physics component
   */
  public void onCollision(Entity other, PhysicsComponent otherPhys) {
    CollisionComponent otherCollision = other.getComponent(CollisionComponent.class);
    if (SatCollisionDetector.checkCollisionDelaunay(shape, otherCollision.getShape())) {
      LOGGER.debug("Collision detected between {} and {}", getEntity(), other);
      PhysicsComponent pyThis = getEntity().getComponent(PhysicsComponent.class);
      otherPhys.applyForce(pyThis.getVx(), pyThis.getVy());
      pyThis.applyForce(otherPhys.getVx(), otherPhys.getVy());
    }
  }

  /**
   * Return the shape for this collision component.
   *
   * @return the shape of this collision component
   */
  private Vertex getShape() {
    return shape;
  }

  @Override
  public void update(double dt) { //Unused
  }

  @Override
  public void destroy() {
    shape = null;
  }
}
