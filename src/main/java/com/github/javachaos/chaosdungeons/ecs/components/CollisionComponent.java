package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.collision.CollisionData;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.geometry.SatCollisionDetector;
import com.github.javachaos.chaosdungeons.geometry.math.LinearMath;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import java.awt.geom.Point2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Basic collision component class.
 */
public class CollisionComponent extends Component {

  private static final Logger LOGGER = LogManager.getLogger(CollisionComponent.class);

  private Vertex shape;

  /**
   * Create a new component.
   */
  public CollisionComponent(Vertex shape) {
    super();
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
    CollisionData cdata = SatCollisionDetector.checkCollisionDelaunay(shape,
        otherCollision.getShape());
    if (cdata.isColliding()) {
      LOGGER.debug("Collision detected between {} and {}", getEntity(), other);
      PhysicsComponent pyThis = getEntity().getComponent(PhysicsComponent.class);
      if (!otherPhys.isStatic()) {
        otherPhys.applyForce(pyThis.getVx(), pyThis.getVy());
        pyThis.applyForce(otherPhys.getVx(), otherPhys.getVy());
      } else {
        double normalX = cdata.getCollisionNormal().getX();
        double normalY = cdata.getCollisionNormal().getY();
        double relativeVelocityX = pyThis.getVx() - otherPhys.getVx();
        double relativeVelocityY = pyThis.getVy() - otherPhys.getVy();
        double dotProduct = LinearMath.dotProduct(cdata.getCollisionNormal(),
            new Point2D.Double(relativeVelocityX, relativeVelocityY));
        double dotProductSum = pyThis.getMass() + otherPhys.getMass();
        // Calculate impulse
        double impulse = -(1 + pyThis.getRestitution()) * dotProduct / dotProductSum;
        // Update velocity based on collision response (bounce off stationary entity)
        double impulseX = impulse * normalX;
        double impulseY = impulse * normalY;
        pyThis.applyForce(impulseX, impulseY);
      }
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

  @Override
  public void onAdded(Entity e) {

  }

  @Override
  public void onRemoved(Entity e) {

  }
}
