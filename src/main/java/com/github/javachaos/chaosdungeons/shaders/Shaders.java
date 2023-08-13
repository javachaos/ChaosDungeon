package com.github.javachaos.chaosdungeons.shaders;

import com.github.javachaos.chaosdungeons.exceptions.ShaderLoadException;

import java.util.HashMap;
import java.util.Map;

public class Shaders {
    private static Map<String, ShaderProgram> shaderProgramMap = new HashMap<>();
    private static ShaderProgram currentShader;
    static {
        try {
            shaderProgramMap.put("world", new WorldShader());
            shaderProgramMap.put("ui", new UiShader());
        } catch (ShaderLoadException e) {
            throw new RuntimeException(e);
        }
        currentShader = shaderProgramMap.get("world");
    }

    public static void init() {
        if (!currentShader.isInitialized()) {
            try {
                currentShader.init();
            } catch (ShaderLoadException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ShaderProgram getCurrentShader() {
        return currentShader;
    }

    public static void setCurrentShader(ShaderProgram program) {
        currentShader = program;
    }

    public static ShaderProgram getShader(String shaderName) {
        if (!shaderProgramMap.containsKey(shaderName)) {
            throw new IllegalArgumentException("Shader not found.");
        }
        return shaderProgramMap.get(shaderName);
    }


}
