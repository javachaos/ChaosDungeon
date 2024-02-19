package com.github.javachaos.chaosdungeons.geometry.math;

/**
 * Helper class to hold code related to calculating a 3x3 determinant.
 */
public class Matrix3x3Det {

  private Matrix3x3Det() {
    //Unused
  }

  /**
   * Calculate the determinant of the 3x3 matrix m.
   *
   * @param m the 3x3 matrix (warning: size of matrix is not checked)
   *          It is up to the caller to ensure the size of the input
   *          matrix is 3x3.
   * @return the determinant of the matrix m
   */
  public static double compute(double[][] m) {
    return m[0][0] * (m[1][1] * m[2][2] - m[2][1] * m[1][2])
        - m[0][1] * (m[1][0] * m[2][2] - m[2][0] * m[1][2])
        + m[0][2] * (m[1][0] * m[2][1] - m[2][0] * m[1][1]);
  }

}
