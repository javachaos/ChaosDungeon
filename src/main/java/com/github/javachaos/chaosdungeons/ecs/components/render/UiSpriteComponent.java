package com.github.javachaos.chaosdungeons.ecs.components.render;

import com.github.javachaos.chaosdungeons.graphics.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UiSpriteComponent extends UiRenderComponent {

    private static final Logger LOGGER = LogManager.getLogger(UiSpriteComponent.class);

    private final Model img;

    /**
     * Create a new component.
     *
     * @param img
     */
    public UiSpriteComponent(Model img) {
        super();
        this.img = img;
    }

    @Override
    public void render(double dt) {
        img.render();
    }

    @Override
    public void destroy() {
        LOGGER.debug("Sprite component destroyed.");
        img.delete();
        super.destroy();
    }
}
