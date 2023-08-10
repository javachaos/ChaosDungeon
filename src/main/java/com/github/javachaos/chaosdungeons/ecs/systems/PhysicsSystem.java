package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.ecs.components.CollisionComponent;
import com.github.javachaos.chaosdungeons.ecs.components.PhysicsComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import java.util.Deque;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Physics system class.
 */
@SuppressWarnings("unused")
public class PhysicsSystem extends System {

  float prevX;
  float prevY;

  private static final Logger LOGGER = LogManager.getLogger(PhysicsSystem.class);

  public PhysicsSystem(GameWindow window) {
    super(window);
  }

  /**
   * Add a physics entity to this Physics system.
   *
   * @param e the entity to be added, must have a physics component
   */
  public void addEntity(GameEntity e) {
    if (e.getComponent(PhysicsComponent.class) != null) {
      getEntities().add(e);
    } else {
      throw new IllegalArgumentException("Entity MUST have a physics component.");
    }
  }

  @Override
  public void update(float dt) {
    getEntities().forEach(e -> {
      prevX = e.getPosition().x;
      prevY = e.getPosition().y;
      e.update(dt);
      collisionQuadtree.updateNode(
          prevX,
          prevY,
          e.getPosition().x,
          e.getPosition().y, e.getCollisionComponent());
    });

    collisionQuadtree.render(1024, 1024);
    // update all entities with a PhysicsComponent and handle collisions
    //Deque<GameEntity> entities = getEntities();
//    for (GameEntity e : entities) {
//      e.update(dt);
//      for (GameEntity f : entities) {
//        if (e.equals(f)) {
//          break;
//        }
//          CollisionComponent pc1 = e.getCollisionComponent();
//          CollisionComponent pc2 = f.getCollisionComponent();
//          if (pc1 != null) {
//            pc1.handleCollision(f);
//          }
//          if (pc2 != null) {
//            pc2.handleCollision(e);
//          }
//        }
//      }
//    }
  }

  @Override
  public void initSystem() {
  }

  @Override
  public void destroy() {
    LOGGER.debug("Physics system shutdown.");
  }

}
