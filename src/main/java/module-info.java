open module com.github.javachaos.chaosdungeons {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.desktop;

    requires transitive com.almasb.fxgl.all;

    exports com.github.javachaos.chaosdungeons;
    exports com.github.javachaos.chaosdungeons.factory;
}