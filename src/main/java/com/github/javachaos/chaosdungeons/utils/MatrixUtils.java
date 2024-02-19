package com.github.javachaos.chaosdungeons.utils;

import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.graphics.Camera;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Matrix utilities.
 */
@SuppressWarnings("unused")
public class MatrixUtils {

  private MatrixUtils() {
    //Unused
  }

  /**
   * Create a view matrix for camera c.
   *
   * @param c the camera
   * @return a new view matrix for the camera c
   */
  public static Matrix4f createViewMatrix(Camera c) {
    Matrix4f viewMatrix = new Matrix4f();
    viewMatrix.rotation((float) java.lang.Math.toRadians(c.getPitch()), new Vector3f(1, 0, 0));
    viewMatrix.rotation((float) java.lang.Math.toRadians(c.getYaw()), new Vector3f(0, 1, 0));
    viewMatrix.rotation((float) java.lang.Math.toRadians(c.getRoll()), new Vector3f(0, 0, 1));
    Vector3f cameraPos = c.getPosition();
    Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    viewMatrix.translation(negativeCameraPos);
    return viewMatrix;
  }

  /**
   * Create a projection matrix with the desired zoom level.
   *
   * @return the projection matrix.
   */
  public static Matrix4f createProjectionMatrix() {
    float halfHeight = Constants.Z_FAR * (float) Math.tan(Math.toRadians(Constants.FOV / 2f));
    float halfWidth = halfHeight * GameWindow.getWindowSize().getAspectRatio();
    return new Matrix4f().identity().ortho2D(
        -halfWidth,
        halfWidth,
        -halfHeight,
        halfHeight);
  }

}
