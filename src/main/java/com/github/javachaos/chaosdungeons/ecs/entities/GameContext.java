package com.github.javachaos.chaosdungeons.ecs.entities;

import com.github.javachaos.chaosdungeons.ecs.components.Component;
import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;
import com.github.javachaos.chaosdungeons.ecs.systems.LoadSystem;
import com.github.javachaos.chaosdungeons.ecs.systems.PhysicsSystem;
import com.github.javachaos.chaosdungeons.ecs.systems.RenderSystem;
import com.github.javachaos.chaosdungeons.exceptions.GeneralGameException;
import com.github.javachaos.chaosdungeons.exceptions.ShaderLoadException;
import com.github.javachaos.chaosdungeons.shaders.ShaderProgram;
import com.github.javachaos.chaosdungeons.shaders.UiShader;
import com.github.javachaos.chaosdungeons.shaders.WorldShader;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * Helper class to store shared state of game entities between
 * systems.
 */
public class GameContext {
    private static final String WORLD = "world";
    private final Map<String, ShaderProgram> shaderProgramMap = new HashMap<>();
    private ShaderProgram currentShader;
    private final Map<Long, GameEntity> entityMap;
    private final RenderSystem renderSystem;
    private final PhysicsSystem physicsSystem;
    private final LoadSystem loadSystem;

    private final AtomicLong entityCount = new AtomicLong(0);

    public GameContext() {
        entityMap = new LinkedHashMap<>();
        renderSystem = new RenderSystem(this);
        physicsSystem = new PhysicsSystem(this);
        loadSystem = new LoadSystem(this);
        try {
            shaderProgramMap.put(WORLD, new WorldShader());
            shaderProgramMap.put("ui", new UiShader());
        } catch (ShaderLoadException e) {
            throw new GeneralGameException(e);
        }
        currentShader = shaderProgramMap.get(WORLD);
    }

    public final RenderSystem getRenderSystem() {
        return this.renderSystem;
    }

    public final PhysicsSystem getPhysicsSystem() {
        return this.physicsSystem;
    }

    public final LoadSystem getLoadSystem() {
        return this.loadSystem;
    }

    /**
     * Return the entity map.
     * @return the entity map
     */
    public Map<Long, GameEntity> getEntityMap() {
        return Collections.unmodifiableMap(entityMap);
    }

    /**
     * Return all entities which have the component c
     * @param c the component class
     * @return a list of entities which all have the component c attached.
     */
    public <T extends Component> List<GameEntity> getEntitiesWithComponent(Class<T> c) {
        return getEntities().stream().filter(
                        x -> x.hasComponent(c))
                .toList();
    }

    /**
     * Get a list of entities which are of type T
     * @param clazz the class
     * @return a list of entities which are of type T
     * @param <T> the type of entity
     */
    public <T extends GameEntity> List<GameEntity> getEntitiesOfType(Class<T> clazz) {
        return entityMap.values().stream()
                .filter(x -> clazz.isAssignableFrom(x.getClass()))
                .toList();
    }

    /**
     * Add an entity to this System.
     *
     * @param e     the entity to be added.
     */
    public <T extends GameEntity> void addEntity(T e) {
        e.setEntityId(entityCount.getAndIncrement());
        entityMap.put(e.getEntityId(), e);
    }

    /**
     * Get all entities.
     * @return the list of entities
     */
    public List<GameEntity> getEntities() {
        return entityMap.values().stream().toList();
    }

    public Stream<GameEntity> getEntityStream() {
        return entityMap.values().stream();
    }

    public void shutdown() {
        getEntities().forEach(Entity::shutdown);
    }

    public void init() {
        if (!currentShader.isInitialized()) {
            try {
                currentShader.init();
            } catch (ShaderLoadException e) {
                throw new GeneralGameException(e);
            }
        }
    }

    public ShaderProgram getCurrentShader() {
        return currentShader;
    }

    public void setCurrentShader(ShaderProgram program) {
        currentShader = program;
    }

    public void setCurrentShader(String name) {
        setCurrentShader(shaderProgramMap.get(name));
    }

    public ShaderProgram getShader(String shaderName) {
        if (!shaderProgramMap.containsKey(shaderName)) {
            throw new IllegalArgumentException("Shader not found.");
        }
        return shaderProgramMap.get(shaderName);
    }

    public ShaderProgram getUiShader() {
        return getShader("ui");
    }

    public ShaderProgram getWorldShader() {
        return getShader(WORLD);
    }
}

