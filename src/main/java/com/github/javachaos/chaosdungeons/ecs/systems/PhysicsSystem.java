package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.ecs.components.CollisionComponent;
import com.github.javachaos.chaosdungeons.ecs.components.PhysicsComponent;
import com.github.javachaos.chaosdungeons.ecs.components.render.RenderComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.gui.WindowSize;
import java.util.Deque;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Physics system class.
 */
@SuppressWarnings("unused")
public class PhysicsSystem extends System {

  private static final Logger LOGGER = LogManager.getLogger(PhysicsSystem.class);
  float prevX;
  float prevY;
  float maxX = Float.MIN_VALUE;
  float maxY = Float.MIN_VALUE;
  private QuadTree<GameEntity> collisionQuadtree;

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
    buildQuadTree();
    for (GameEntity e : gameEntityList) {
      CollisionComponent cc = e.getCollisionComponent();
      QuadTree.Quad v;
      if (cc != null) {
        v = cc.getShape();
        if (v != null) {
          for (QuadTree.Node n : collisionQuadtree.find(v)) {
            GameEntity ge = (GameEntity) n.getValue();
            CollisionComponent occ = ge.getCollisionComponent();
            occ.onCollision(e, e.getCollisionComponent());
          }
        }
      }
    }
    if (Constants.DEBUG) {
      WindowSize ws = GameWindow.getWindowSize();
      collisionQuadtree.render(512, 512);
    }
  }

  private void buildQuadTree() {
    WindowSize ws = GameWindow.getWindowSize();
    collisionQuadtree = new QuadTree<>(ws.getWidth(), ws.getHeight());
    for (GameEntity e : gameEntityList) {
      if (e.hasComponent(CollisionComponent.class)) {
        collisionQuadtree.insert(
            e.getPosition().x,
            e.getPosition().y,
            e);
      }
    }
  }

  @Override
  public void initSystem() {
  }

  @Override
  public void destroy() {
    LOGGER.debug("Physics system shutdown.");
  }

}
