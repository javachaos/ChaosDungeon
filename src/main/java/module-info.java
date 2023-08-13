module com.github.javachaos.chaosdungeons {
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.iostreams;
    requires java.desktop;
    requires org.lwjgl;
    requires org.lwjgl.natives;
    requires org.lwjgl.glfw;
    requires org.lwjgl.glfw.natives;
    requires org.lwjgl.opengl;
    requires org.lwjgl.opengl.natives;
    requires org.joml;
    requires org.lwjgl.stb;
  exports com.github.javachaos.chaosdungeons;
  exports com.github.javachaos.chaosdungeons.gui;
  exports com.github.javachaos.chaosdungeons.utils;
  exports com.github.javachaos.chaosdungeons.exceptions;
  exports com.github.javachaos.chaosdungeons.graphics;
  exports com.github.javachaos.chaosdungeons.shaders;
  exports com.github.javachaos.chaosdungeons.ecs.components;
  exports com.github.javachaos.chaosdungeons.ecs.entities;
  exports com.github.javachaos.chaosdungeons.geometry;
  exports com.github.javachaos.chaosdungeons.geometry.gui;
  exports com.github.javachaos.chaosdungeons.geometry.polygons;
  exports com.github.javachaos.chaosdungeons.collision;
  exports com.github.javachaos.chaosdungeons.ecs.entities.factory;
  exports com.github.javachaos.chaosdungeons.ecs.components.render;
    exports com.github.javachaos.chaosdungeons.graphics.text;
}