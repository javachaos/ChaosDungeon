package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

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
	    PhysicsComponent thisPhys = physicsComponent;
	    PhysicsComponent otherPhys = otherCc.physicsComponent;
	    GameEntity thisGe = (GameEntity) getEntity();

	    if (thisGe != other && shape.intersects(otherCc.getShape())) {
	        //LOGGER.debug("Collision detected between {} and {}", getEntity(), other);
	        Vector3f thisCenter = new Vector3f(thisPhys.getPosition().x + shape.w / 2f,
                    thisPhys.getPosition().y + shape.h / 2f,
                    0);
			Vector3f otherCenter = new Vector3f(otherPhys.getPosition().x + otherCc.getShape().w / 2f,
			                     otherPhys.getPosition().y + otherCc.getShape().h / 2f,
			                     0);
	        Vector3f v1minusv2 = thisPhys.getVelocity().sub(otherPhys.getVelocity());
	        Vector3f v2minusv1 = otherPhys.getVelocity().sub(thisPhys.getVelocity());
	        
	        
	        Vector3f x1minusx2 = thisCenter.sub(otherCenter);
	        float numerator = v1minusv2.dot(x1minusx2);
	        float len = Vector3f.lengthSquared(x1minusx2.x, x1minusx2.y, x1minusx2.z);
	        float scalarMul = (numerator / len) * (float) (2 * otherPhys.getMass() / (thisPhys.getMass() + otherPhys.getMass()));
	        x1minusx2.mul(scalarMul);	        
	        Vector3f v1 = thisPhys.getVelocity();
	        Vector3f v1Prime = v1.sub(x1minusx2);
	        otherPhys.applyImpulse(v1Prime);
	        
	        Vector3f x2minusx1 = otherCenter.sub(thisCenter);
	        numerator = v2minusv1.dot(x2minusx1);
	        len = Vector3f.lengthSquared(x2minusx1.x, x2minusx1.y, x2minusx1.z);
	        scalarMul = (numerator / len) * (float) (2 * thisPhys.getMass() / (thisPhys.getMass() + otherPhys.getMass()));
	        x2minusx1.mul(scalarMul); 
	        Vector3f v2 = otherPhys.getVelocity();
	        Vector3f v2Prime = v2.sub(x2minusx1);
	        otherPhys.applyImpulse(v1Prime);
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
