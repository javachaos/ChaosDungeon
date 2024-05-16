package com.github.javachaos.chaosdungeons.geometry.gui;

import static com.github.javachaos.chaosdungeons.geometry.GenerationUtils.generateNonRegularPolygon;

import com.github.javachaos.chaosdungeons.geometry.polygons.Triangle;
import com.github.javachaos.chaosdungeons.collision.Polygon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A utility class to draw shapes.
 * Left-click - Toggle wireframe
 * Left-AltKey - Generate new polygon
 * Left-ControlKey - Toggle vertex info
 */
public class ShapeDrawer extends JFrame {

  private static final int WIDTH = 1200;
  private static final int HEIGHT = 800;
  private final List<ShapePanel> shapes;

  private static final Logger LOGGER = LogManager.getLogger(ShapeDrawer.class);

  /**
   * Create an empty shape drawer JFrame.
   */
  protected ShapeDrawer() {
    LOGGER.debug("ShapeDrawer created.");
    Border b = BorderFactory.createLineBorder(Color.BLACK);
    getRootPane().setBorder(BorderFactory.createTitledBorder(b, "ShapeDrawer"));
    this.shapes = new ArrayList<>();
    setSize(WIDTH, HEIGHT);
    setLayout(new GridLayout(4, 4, 10, 10));
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setResizable(false);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    InputListener inputListener = new InputListener(this);
    addMouseListener(inputListener);
    addKeyListener(inputListener);
  }

  /**
   * Create a ShapeDrawer JFrame with a single triangle t.
   *
   * @param t the triangle to draw
   */
  public ShapeDrawer(Triangle t) {
    this();
    Polygon v = new Polygon(t.getPoints());
    this.shapes.add(new ShapePanel(Color.WHITE, v));
    addShapesAndShow();
  }

  /**
   * Create a ShapeDrawer JFrame which takes two polygons represented by lists of points.
   *
   * @param points1 the first polygon point list
   */
  public ShapeDrawer(Set<Polygon.Point> points1) {
    this();
    Polygon v = new Polygon(points1);
    this.shapes.add(new ShapePanel(Color.WHITE, v));
    addShapesAndShow();
  }

  /**
   * Create a ShapeDrawer JFrame with a polygon represented by a Vertex.
   *
   * @param polygon the polygon to draw
   */
  public ShapeDrawer(Polygon polygon) {
    this(polygon.getPoints());
  }

  /**
   * Create a ShapeDrawer JFrame with two triangles t1 and t2.
   *
   * @param t1 first triangle
   * @param t2 second triangle
   */
  public ShapeDrawer(Triangle t1, Triangle t2) {
    this();
    this.shapes.add(new ShapePanel(Color.WHITE, new Polygon(t1.getPoints())));
    this.shapes.add(new ShapePanel(Color.WHITE, new Polygon(t2.getPoints())));
    addShapesAndShow();
  }

  /**
   * Add all ShapePanels to this JFrame, and show the JFrame.
   */
  public void addShapesAndShow() {
    shapes.forEach(this::add);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Create two new shapes.
   */
  public void createNewShapes() {
    shapes.forEach(this::remove);
    shapes.clear();
    for (int i = 3; i < 19; i++) {
      addShape(i);
    }
    shapes.forEach(this::add);
    pack();
  }

  private void addShape(int i) {
    shapes.add(new ShapePanel(Color.WHITE,
        new Polygon(generateNonRegularPolygon(
            96, 96, i, 64, 64))));
  }

  public void toggleDrawInfo() {
    LOGGER.debug("toggleDrawInfo requested.");
    shapes.forEach(ShapePanel::toggleDrawInfo);
  }

  public void toggleShowTriangulation() {
    LOGGER.debug("toggleShowTriangulation requested.");
    shapes.forEach(ShapePanel::toggleShowTriangulation);
  }
}
