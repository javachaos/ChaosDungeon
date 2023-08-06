package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.ecs.components.PhysicsComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Physics system class.
 */
@SuppressWarnings("unused")
public class PhysicsSystem extends System {

  private static final Logger LOGGER = LogManager.getLogger(PhysicsSystem.class);

  public PhysicsSystem(GameWindow window) {
    super(window);
  }

  /**
   * Add a physics entity to this Physics system.
   *
   * @param e the entity to be added, must have a physics component
   */
  public void addEntity(Entity e) {
    if (e.getComponent(PhysicsComponent.class) != null) {
      getEntities().add(e);
    } else {
      throw new IllegalArgumentException("Entity MUST have a physics component.");
    }
  }

  @Override
  public void update(float dt) {
    List<Entity> entities = getEntities();
    if (entities != null) {
      for (Entity e : entities) {
        for (Entity f : entities) {
          if (e.equals(f)) {
            break;
          }
          if (e.hasComponent(PhysicsComponent.class) && f.hasComponent(PhysicsComponent.class)) {
            PhysicsComponent pc1 = e.getComponent(PhysicsComponent.class);
            PhysicsComponent pc2 = f.getComponent(PhysicsComponent.class);
            if (pc1 != null) {
              pc1.handleCollision(f);
            }
            if (pc2 != null) {
              pc2.handleCollision(e);
            }
          }
        }
        e.update(dt);
      }
    }
  }

  @Override
  public void initSystem() {
  }

  @Override
  public void destroy() {
  }

}
