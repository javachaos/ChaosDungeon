package com.github.javachaos.chaosdungeons.ecs.components;

import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * A simple physics component.
 */
@SuppressWarnings("unused")
public class PhysicsComponent extends Component {

  private final Vector3f velocity;
  private final Vector3f angularVelocity;
  private Vector3f angularAcceleration;
  private final Vector3f prevPos;
  private final double mass;
  private final double restitution; // Coefficient of restitution for bounciness
  private boolean isStatic;   // Flag to indicate if the entity is static (immovable)
  private GameEntity gameEntity;

  private static final Logger LOGGER = LogManager.getLogger(PhysicsComponent.class);
  private final Quaternionf prevRotation;

  /**
   * Create a new physics component.
   *
   * @param mass the mass of this object
   * @param restitution the restitution
   * @param isStatic true if this is a static object
   */
  public PhysicsComponent(double mass, double restitution, Vector3f initialVelocity,
                          boolean isStatic) {
    super();
    this.velocity = initialVelocity;
    this.mass = mass;
    this.restitution = restitution;
    this.isStatic = isStatic;
    this.prevPos = new Vector3f(0, 0, 0);
    this.prevRotation = new Quaternionf();
    this.angularVelocity = new Vector3f();
  }

  /**
   * Create a new physics component.
   *
   * @param mass the mass of this object
   * @param restitution the restitution
   * @param isStatic true if this is a static object
   */
  public PhysicsComponent(double mass, double restitution,
                          Vector3f initialVelocity, Vector3f initialAngularVelocity,
                          boolean isStatic) {
    super();
    this.velocity = initialVelocity;
    this.mass = mass;
    this.restitution = restitution;
    this.isStatic = isStatic;
    this.prevRotation = new Quaternionf();
    this.prevPos = new Vector3f(0, 0, 0);
    this.angularVelocity = initialAngularVelocity;
  }

  // Getter and setter methods
  public double getXpos() {
    return gameEntity.getPosition().x;
  }

  public double getYpos() {
    return gameEntity.getPosition().y;
  }

  public double getVx() {
    return velocity.x;
  }

  public double getVy() {
    return velocity.y;
  }

  public double getMass() {
    return mass;
  }

  public double getRestitution() {
    return restitution;
  }

  public boolean isStatic() {
    return isStatic;
  }


  public Vector3f getPosition() {
    return gameEntity.getPosition();
  }
  
  public void setPosition(Vector3f pos) {
	  gameEntity.setPosition(pos);
  }

  public Vector3f getScale() {
    return gameEntity.getScale();
  }

  public Quaternionf getRotation() {
    return gameEntity.getRotation();
  }

  /**
   * Method to apply forces to the entity.
   *
   * @param forceX force in the x direction
   * @param forceY force in the y direction
   * @param forceZ force in the z direction
   */
  public void applyForce(double forceX, double forceY, double forceZ) {
    if (!isStatic) {
      double ax = forceX / mass;
      double ay = forceY / mass;
      double az = forceZ / mass;
      velocity.x += (float) ax;
      velocity.y += (float) ay;
      velocity.z += (float) az;
    }
  }
  
  public void applyForce(Vector3f f) {
	  applyForce(f.x, f.y, f.z);
  }

  /**
   * Method to apply forces to the entity.
   *
   * @param forceX force in the x direction
   * @param forceY force in the y direction
   */
  public void applyForce(double forceX, double forceY) {
    applyForce(forceX, forceY, 0);
  }
  
  public void applyImpulse(Vector3f impulse) {
	  velocity.add(impulse);
  }
  
  public float getInvMass() {
      if (mass == 0) {
          return 0;  // Infinite mass or immovable object
      } else {
          return (float) (1.0f / mass);
      }
  }

  /**
   * Apply an angular force to this physics object.
   *
   * @param forceX angular force along the x-axis
   * @param forceY angular force along the y-axis
   * @param forceZ angular force along the z-axis
   */
  public void applyAngularForce(double forceX, double forceY, double forceZ) {
    double ax = forceX / mass;
    double ay = forceY / mass;
    double az = forceZ / mass;
    angularVelocity.set(new Vector3f((float) ax, (float) ay, (float) az));
  }
  
  /**
   * Verlet integration method to update position and velocity.
   *
   * @param dt the change in time between updates
   */
  @Override
  public void update(double dt) {
    if (!isStatic) {
      prevPos.x = getPosition().x;
      prevPos.y = getPosition().y;
      prevPos.z = getPosition().z;
      if (angularVelocity.x < 0) {
        angularVelocity.x = 0;
      }
      if (angularVelocity.y < 0) {
        angularVelocity.y = 0;
      }
      if (angularVelocity.z < 0) {
        angularVelocity.z = 0;
      }
      float angularMomentum = 0.995f;
      float drag = 0.998f;
      float momentumX = (float) mass * velocity.x;
      float momentumY = (float) mass * velocity.y;
      float momentumZ = (float) mass * velocity.z;
      angularVelocity.mul(angularMomentum);
      velocity.mul(drag);
      
      getRotation().integrate((float) dt, angularVelocity.x, angularVelocity.y, angularVelocity.z);
      double newVx = velocity.x + (getPosition().x - prevPos.x);
      double newVy = velocity.y + (getPosition().y - prevPos.y);
      double newVz = velocity.z + (getPosition().z - prevPos.z);
      double newX = getPosition().x + newVx * dt + 0.5 * newVx * dt * dt;
      double newY = getPosition().y + newVy * dt + 0.5 * newVy * dt * dt;
      double newZ = getPosition().z + newVz * dt + 0.5 * newVz * dt * dt;
      GameEntity ge = ((GameEntity) getEntity());
      ge.updateModelMatrix(
          new Vector3f(
              (float) newX,
              (float) newY,
              (float) newZ), //position
          getRotation(), //rotation
          getScale()); //scale
    }
  }

  @Override
  public void destroy() {
    isStatic = true;
  }

  @Override
  public void onAdded(Entity e) {
    this.gameEntity = (GameEntity) e;
  }

  @Override
  public void onRemoved(Entity e) {
  }

  public void setAngularVelocity(Vector3f initialAngularVelocity) {
    angularVelocity.set(initialAngularVelocity);
  }

  public Vector3f getVelocity() {
	return velocity;
  }
	
  public void setVelocity(Vector3f v1Prime) {
	  this.velocity.set(v1Prime);
  }
}
