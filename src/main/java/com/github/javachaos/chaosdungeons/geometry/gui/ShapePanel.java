package com.github.javachaos.chaosdungeons.geometry.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.geometry.DelaunayTriangulation;
import com.github.javachaos.chaosdungeons.geometry.polygons.Edge;
import com.github.javachaos.chaosdungeons.geometry.polygons.Triangle;

/**
 * A JPanel to draw a shape on.
 */
public class ShapePanel extends JPanel {

  private final Rectangle bounds;
  private boolean drawInfo;
  private boolean drawTriangulation;
  private final transient Polygon polygon;

  /**
   * Create a new ShapePanel.
   *
   * @param background the background color
   */
  public ShapePanel(Color background, Polygon polygon) {
    setBackground(background);
    bounds = new Rectangle((int) polygon.getBounds().x(), (int) polygon.getBounds().y(),
            (int) polygon.getBounds().w(), (int) polygon.getBounds().h());
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
        g2d.draw(new Line2D.Double(new Point2D.Double(e.getA().x, e.getA().y),
                 new Point2D.Double(e.getB().x, e.getB().y)));
        g2d.draw(bounds);
        g2d.setPaint(Color.GREEN);
      }
    } else {
      g2d.setColor(Color.RED);
      drawShape(g2d, polygon.getPoints());
      g2d.setPaint(Color.RED);
    }
  }

  private void drawShape(Graphics2D g2d, Set<Polygon.Point> points) {
    int[] xpts = new int[points.size()];
    int[] ypts = new int[points.size()];
    int i = 0;
    for (Polygon.Point point : points) {
      xpts[i] = (int) point.x();
      ypts[i] = (int) point.y();
      i++;
    }
    g2d.drawPolygon(xpts, ypts, points.size());
    Iterator<Polygon.Point> iter = points.iterator();
    // Print the coordinates of each point on the paint canvas
    for (i = 0; i < points.size(); i++) {
      Polygon.Point point = iter.next();
      String coordinatesText =
          "P" + i + "[x = " + point.x() + ",y = " + point.y() + "]";
      // Adjust the text position for each point to avoid overlapping
      int textX = (int) point.x() + 5;
      int textY = (int) point.y() + 15 * i;
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
