package com.github.javachaos.chaosdungeons.utils;

import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.graphics.Camera;
import com.github.javachaos.chaosdungeons.gui.GameWindow;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Matrix utilities.
 */
public class MatrixUtils {

  /**
   * Create a transformation matrix.
   *
   * @param translation the translation vector
   * @param rx rotation in the x direction
   * @param ry rotation in the y direction
   * @param rz rotation in the z direction
   * @param scale the scale as a 3D vector
   * @return a transformation matrix
   */
  public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
                                                    float rz, Vector3f scale) {
    Matrix4f matrix = new Matrix4f().identity();
    matrix.translation(translation);
    matrix.rotation((float) java.lang.Math.toRadians(rx), new Vector3f(1, 0, 0));
    matrix.rotation((float) java.lang.Math.toRadians(ry), new Vector3f(0, 1, 0));
    matrix.rotation((float) java.lang.Math.toRadians(rz), new Vector3f(0, 0, 1));
    matrix.scaling(scale);
    return matrix;
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
    Matrix4f projMatrix = new Matrix4f().identity().ortho2D(
        halfWidth,
        -halfWidth,
        -halfHeight,
        halfHeight);
    return projMatrix;
  }

}
