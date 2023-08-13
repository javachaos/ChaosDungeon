package com.github.javachaos.chaosdungeons.graphics.text;

import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.shaders.Shaders;

public class WorldTextModel extends TextModel {

    /**
     * Create a new RenderedTextEntity given the font path.
     *
     * @param fontPath the path to the ttf font file used to construct this text entity.
     * @param ge game entity
     */
    public WorldTextModel(String fontPath, GameEntity ge) {
        super(Shaders.getShader("world"), fontPath, ge);
    }
}
