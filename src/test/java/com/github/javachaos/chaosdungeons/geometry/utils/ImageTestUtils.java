package com.github.javachaos.chaosdungeons.geometry.utils;

import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Image Test utility class for drawing a polygon to a png image.
 */
@SuppressWarnings("unused")
public class ImageTestUtils {

  /**
   * Draw a vararg list of polygons to a png file at filename.
   *
   * @param filename the file to save the PNG to
   * @param polygons a list of polygon
   */
  public static void drawPolygon(String filename, Vertex... polygons) {
    int w = 512;
    int h = 512;
    final BufferedImage img = new BufferedImage(w, h,
            BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) img.getGraphics();
    g2d.setBackground(Color.WHITE);
    g2d.setColor(Color.WHITE);
    for (Vertex poly : polygons) {
      g2d.setPaint(getRandomColor());
      g2d.setStroke(new BasicStroke(2.0f));
      drawShape(g2d, poly.getPoints());
    }
    JPanel jp = new JPanel();
    jp.printAll(g2d);
    jp.setVisible(true);
    writeBufferedImageToFile(img,
            new File(filename));
  }


  /**
   * Draw a polygon to a png file at filename.
   *
   * @param poly     the polygon as a vertex
   * @param filename the file to save the PNG to
   */
  public static void drawPolygon(Vertex poly, String filename) {
    int w = 512;
    int h = 512;
    final BufferedImage img = new BufferedImage(w, h,
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) img.getGraphics();
    g2d.setBackground(Color.WHITE);
    g2d.setColor(Color.WHITE);
    g2d.setPaint(Color.RED);
    g2d.setStroke(new BasicStroke(2.0f));
    drawShape(g2d, poly.getPoints());
    JPanel jp = new JPanel();
    jp.printAll(g2d);
    jp.setVisible(true);
    writeBufferedImageToFile(img,
        new File(filename));
  }

  private static void writeBufferedImageToFile(BufferedImage img, File f) {
    try {
      ImageIO.write(img, "PNG", f);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void drawShape(Graphics2D g2d, List<Point2D> points) {
    int[] x = new int[points.size()];
    int[] y = new int[points.size()];
    int i = 0;
    for (Point2D point : points) {
      x[i] = (int) point.getX();
      y[i] = (int) point.getY();
      i++;
    }
    g2d.drawPolygon(x, y, points.size());
  }

  private static Color getRandomColor() {
    int red = (int) (Math.random() * 256);   // Random value between 0 and 255
    int green = (int) (Math.random() * 256); // Random value between 0 and 255
    int blue = (int) (Math.random() * 256);  // Random value between 0 and 255

    return new Color(red, green, blue);
  }
}
