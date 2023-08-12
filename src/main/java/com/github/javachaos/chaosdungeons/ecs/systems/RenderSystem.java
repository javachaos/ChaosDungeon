package com.github.javachaos.chaosdungeons.ecs.systems;

import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.ecs.components.render.RenderComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.PlayerEntity;
import com.github.javachaos.chaosdungeons.ecs.entities.TextEntity;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.FireballEntityFactory;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.RandomFireballSpawnDataFactory;
import com.github.javachaos.chaosdungeons.ecs.entities.factory.Spawner;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.Fireball;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.graphics.TextModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Rendering system class.
 */
@SuppressWarnings("unused")
public class RenderSystem extends System {

  private static final Logger LOGGER = LogManager.getLogger(RenderSystem.class);

  public RenderSystem(GameWindow window) {
    super(window);
  }

  @Override
  public void update(float dt) { // update all entities with a render component.
    gameEntityList.stream().filter(
        c -> c.hasComponent(RenderComponent.class))
        .forEach(e -> e.render(dt));
  }

  @Override
  public void initSystem() {
    FireballEntityFactory fireballEntityFactory = new FireballEntityFactory();
    Spawner<Fireball> s = new Spawner<>(
        fireballEntityFactory,
        new RandomFireballSpawnDataFactory());
    addEntity(s, false);
    addEntity(new PlayerEntity(), false);
    addEntity(new TextEntity(Constants.EIGHT_BIT_FONT), false);
  }

  @Override
  public void destroy() {
    LOGGER.debug("Rendering system shutdown.");
  }

}
