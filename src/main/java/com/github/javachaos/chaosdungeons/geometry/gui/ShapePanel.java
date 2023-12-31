package com.github.javachaos.chaosdungeons.geometry.gui;

import com.github.javachaos.chaosdungeons.geometry.DelaunayTriangulation;
import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;
import com.github.javachaos.chaosdungeons.geometry.polygons.Triangle;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A JPanel to draw a shape on.
 */
public class ShapePanel extends JPanel {

  private final Rectangle bounds;
  private boolean drawInfo;
  private boolean drawTriangulation;
  private final Vertex polygon;

  /**
   * Create a new ShapePanel.
   *
   * @param background the background color
   */
  public ShapePanel(Color background, Vertex polygon) {
    setBackground(background);
    bounds = new Rectangle((int) polygon.getBounds().px, (int) polygon.getBounds().py,
            (int) polygon.getBounds().w, (int) polygon.getBounds().h);
    setBounds(bounds);
    setPreferredSize(bounds.getSize());
    setBorder(BorderFactory.createLineBorder(Color.RED));
    this.polygon = polygon;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setPaintMode();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    if (drawTriangulation) {
      List<Triangle> triangles1 = DelaunayTriangulation.delaunayTriangulation(
          polygon.getPoints());
      List<Edge> edges = DelaunayTriangulation.constrainDelaunayTriangulation(
          triangles1, polygon);
      for (Edge e : edges) {
        g2d.setPaint(Color.GREEN);
        g2d.draw(new Line2D.Double(e.getA(), e.getB()));
        g2d.draw(bounds);
        g2d.setPaint(Color.GREEN);
      }
    } else {
      g2d.setColor(Color.RED);
      drawShape(g2d, polygon.getPoints());
      g2d.setPaint(Color.RED);
    }
  }

  private void drawShape(Graphics2D g2d, List<Point2D> points) {
    int[] xpts = new int[points.size()];
    int[] ypts = new int[points.size()];
    int i = 0;
    for (Point2D point : points) {
      xpts[i] = (int) point.getX();
      ypts[i] = (int) point.getY();
      i++;
    }
    g2d.drawPolygon(xpts, ypts, points.size());
    // Print the coordinates of each point on the paint canvas
    for (i = 0; i < points.size(); i++) {
      Point2D point = points.get(i);
      String coordinatesText =
          "P" + i + "[x = " + point.getX() + ",y = " + point.getY() + "]";
      // Adjust the text position for each point to avoid overlapping
      int textX = (int) point.getX() + 5;
      int textY = (int) point.getY() + 15 * i;
      if (drawInfo) {
        g2d.drawString(coordinatesText, textX, textY);
      }
    }
  }

  /**
   * Toggle whether to draw the info string.
   */
  public void toggleDrawInfo() {
    drawInfo = !drawInfo;
    repaint();
  }

  /**
   * Toggle whether to draw the triangulation.
   */
  public void toggleShowTriangulation() {
    drawTriangulation = !drawTriangulation;
    repaint();
  }
}
