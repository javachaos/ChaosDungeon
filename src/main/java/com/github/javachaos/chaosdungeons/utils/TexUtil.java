package com.github.javachaos.chaosdungeons.utils;

import com.almasb.fxgl.texture.Texture;
import com.github.javachaos.chaosdungeons.constants.Constants;

import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;

public class TexUtil {
    public static Texture loadPng(String name) {
        if (!name.matches(Constants.FILENAME_REGEX)) {
            throw new IllegalArgumentException("Invalid filename.");
        }
        return getAssetLoader().loadTexture(
                Objects.requireNonNull(TexUtil.class.getResource("/"+name+".png")));
    }
}
