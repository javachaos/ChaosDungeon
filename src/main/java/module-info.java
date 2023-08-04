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
    exports com.github.javachaos.chaosdungeons;
  exports com.github.javachaos.chaosdungeons.gui;
}