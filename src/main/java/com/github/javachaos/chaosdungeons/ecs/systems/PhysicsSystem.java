package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.collision.Collision;
import com.github.javachaos.chaosdungeons.collision.QuadTree;
import com.github.javachaos.chaosdungeons.collision.Solver;
import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.ecs.components.PhysicsComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;
import com.github.javachaos.chaosdungeons.geometry.GJKDetector2D;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.gui.WindowSize;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
  private final Solver solver;

  public PhysicsSystem(GameContext gameContext) {
    super(gameContext);
    this.solver = new Solver();
  }

  /**
   * Add a physics entity to this Physics system.
   *
   * @param e the entity to be added, must have a physics component
   */
  public void addEntity(GameEntity e) {
    if (e.getComponent(PhysicsComponent.class) != null) {
      gameContext.getEntities().add(e);
    } else {
      throw new IllegalArgumentException("Entity MUST have a physics component.");
    }
  }

  @Override
  public void update(double dt) {
    buildQuadTree();
    List<GameEntity> entityList = gameContext.getEntitiesWithComponent(PhysicsComponent.class);
    for (GameEntity e : entityList) {
     for (QuadTree<GameEntity>.Node j : collisionQuadtree.find(e.getCollisionComponent().getShape().getBounds())) {
        if (e.getEntityId() != j.getValue().getEntityId()) {
          Collision c = GJKDetector2D.checkCollision(e, j.getValue());
          if (c.isColliding()) {
            e.getCollisionComponent().onCollision(e, j.getValue());
            solver.addCollision(c);
          }
        }
      }
    }
    solver.solve();
    for (GameEntity e1 : entityList) {
      e1.update(dt);
    }
    if (Constants.DEBUG) {
      WindowSize ws = GameWindow.getWindowSize();
      collisionQuadtree.render(256, 256, gameContext);
    }
  }

  private void buildQuadTree() {
    WindowSize ws = GameWindow.getWindowSize();
    collisionQuadtree = new QuadTree<>(ws.getWidth(), ws.getHeight());
    Set<GameEntity> entitySet = gameContext.getEntityStream().collect(Collectors.toSet());
      for (GameEntity e : entitySet) {
          if (e.getTransformComponent() != null) {
              collisionQuadtree.insert(
                      e.getTransformComponent().getPosition().x,
                      e.getTransformComponent().getPosition().y,
                      e);
          }
      }
  }

  @Override
  public void initSystem() {
    //Unused
  }

  @Override
  public void destroy() {
    LOGGER.debug("Physics system shutdown.");
  }

}
