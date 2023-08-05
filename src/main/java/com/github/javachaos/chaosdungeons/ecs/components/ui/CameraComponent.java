package com.github.javachaos.chaosdungeons.ecs.components.ui;

import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.ecs.components.Component;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.gui.Camera;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.gui.Transform;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CameraComponent extends Component {
  private final Camera camera = new Camera();
  private final Transform transform = new Transform();

  @Override
  public void update(double dt) {
    transform.setPosition(new Vector3f((float) Math.sin(Math.toRadians(dt)), 0, 0));
    transform.getRotation().rotateAxis((float) Math.toRadians(0.1), 0, 1, 0);
    GameWindow.getShader().setCamera(camera);
    GameWindow.getShader().setTransform(transform);
  }

  @Override
  public void destroy() {
  }

  @Override
  public void onAdded(GameEntity e) {
    camera.setPerspective(Constants.FOV,
        (float) GameWindow.getProjection().getWidth() / GameWindow.getProjection().getHeight(),
        Constants.Z_NEAR, Constants.Z_FAR);
    camera.setPosition(new Vector3f(0f, 1f, 3f));
    camera.setRotation(new Quaternionf(
        new AxisAngle4f((float)
            Math.toRadians(-30), new Vector3f(1, 0, 0))));
  }

  @Override
  public void onRemoved(GameEntity e) {
  }
}
