package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.ecs.components.render.RenderComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameContext;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.impl.FireballEntityFactory;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.impl.RandomFireballSpawnDataFactory;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.impl.RandomShapeSpawnDataFactory;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.impl.ShapeEntityFactory;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.*;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

/**
 * Rendering system class.
 */
@SuppressWarnings("unused")
public class RenderSystem extends System {

  private static final Logger LOGGER = LogManager.getLogger(RenderSystem.class);

  public RenderSystem(GameContext gameContext) {
    super(gameContext);
  }

  @Override
  public void update(double dt) { // update all entities with a render component.
    List<GameEntity> entities = gameContext.getEntitiesWithComponent(RenderComponent.class);
    for (GameEntity ge : entities) {
      ge.render(dt);
    }
  }

  @Override
  public void initSystem() {
    FireballEntityFactory fireballEntityFactory = new FireballEntityFactory();
    SpawnerEntity<FireballEntity> s = new SpawnerEntity<>(
            gameContext,
        fireballEntityFactory,
        new RandomFireballSpawnDataFactory());

    ShapeEntityFactory rssdf = new ShapeEntityFactory();
    SpawnerEntity<RandomShapeEntity> ss = new SpawnerEntity<>(gameContext, rssdf, new RandomShapeSpawnDataFactory());
    gameContext.addEntity(s);
    gameContext.addEntity(ss);
    gameContext.addEntity(new SquareEntity(10f, -15f, 20f, 10f, gameContext));
    gameContext.addEntity(new PlayerEntity(gameContext));
    gameContext.addEntity(new TextEntity(Constants.EIGHT_BIT_FONT, gameContext));
  }

  @Override
  public void destroy() {
    LOGGER.debug("Rendering system shutdown.");
  }

}
