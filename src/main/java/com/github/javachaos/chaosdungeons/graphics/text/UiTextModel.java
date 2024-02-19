package com.github.javachaos.chaosdungeons.graphics.text;

import com.github.javachaos.chaosdungeons.ecs.entities.impl.GameEntity;

public class UiTextModel extends TextModel {

    /**
     * Create a new RenderedTextEntity given the font path.
     *
     * @param fontPath the path to the ttf font file used to construct this text entity.
     * @param ge
     */
    public UiTextModel(String fontPath, GameEntity ge) {
        super(fontPath, ge);
    }
}
