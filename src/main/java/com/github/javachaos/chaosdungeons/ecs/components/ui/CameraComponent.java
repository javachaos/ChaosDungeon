package com.github.javachaos.chaosdungeons.ecs.components.ui;

import com.github.javachaos.chaosdungeons.ecs.components.Component;
import com.github.javachaos.chaosdungeons.ecs.entities.Entity;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import com.github.javachaos.chaosdungeons.utils.Camera;
import com.github.javachaos.chaosdungeons.utils.Transform;

/**
 * A camera component.
 */
@SuppressWarnings("unused")
public class CameraComponent extends Component {
  private final Camera camera = new Camera();
  private final Transform transform = new Transform();
  private float zoom = 512f;
  private float newZoom = 128f;
  private float easeFactor = 0.3f;

  @Override
  public void update(double dt) {
    GameEntity ge = ((GameEntity) getEntity());
    Transform t = ge.getTransform();
    transform.setPosition(t.getPosition());
    transform.setRotation(t.getRotation());
    transform.setScale(t.getScale());
    if (zoom < newZoom) {
      zoom += (float) (newZoom * dt * easeFactor);
    }
    if (zoom > newZoom) {
      zoom -= (float) (newZoom * dt * easeFactor);
    }
    camera.setOrthographic(zoom);
    GameWindow.getShader().setCamera(camera);
    GameWindow.getShader().setTransform(transform);
  }

  public float getZoom() {
    return zoom;
  }

  public void setZoomEase(float zoom, float speed) {
    this.newZoom = zoom;
    this.easeFactor = speed;
  }

  public void setZoom(float zoom) {
    this.zoom = zoom;
  }

  @Override
  public void destroy() {
  }

  @Override
  public void onAdded(Entity e) {
  }

  @Override
  public void onRemoved(Entity e) {
  }
}
