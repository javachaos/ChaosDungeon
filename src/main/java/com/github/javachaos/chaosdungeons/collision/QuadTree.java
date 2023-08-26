package com.github.javachaos.chaosdungeons.collision;

import static com.github.javachaos.chaosdungeons.utils.PrecisionUtils.equalTo;
import static com.github.javachaos.chaosdungeons.utils.PrecisionUtils.lessThan;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import com.github.javachaos.chaosdungeons.geometry.util.ShapeBuilder;
import com.github.javachaos.chaosdungeons.gui.GameWindow;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import org.joml.Vector3f;

/**
 * A simple PR quad tree for collision detection.
 */
public class QuadTree<T> {

  /**
   * A quadtree node.
   */
  public class Node {
    double xp;
    double yp;
    private Node nw;
    private Node ne;
    private Node se;
    private Node sw;
    private final T value;

    public T getValue() {
      return value;
    }

    /**
     * Create a new Quadtree node.
     *
     * @param xp x
     * @param yp y
     * @param ge value
     */
    public Node(double xp, double yp, T ge) {
      this.value = ge;
      this.xp = xp;
      this.yp = yp;
    }
  }

  private final Vertex boundary;

  private Node root;

  public QuadTree(double w, double h) {
    Vector3f pos = GameWindow.getCamera().getPosition();
    this.boundary = new ShapeBuilder.Rectangle()
            .setPosition(pos.x, pos.y)
            .setWidth(w).setHeight(h)
            .build();
  }

  /**
   * Insert a new value into this quad tree at (x, y).
   *
   * @param x x pos
   * @param y y pos
   * @param value the value
   */
  public void insert(double x, double y, T value) {
    if (boundary.contains(x, y)) {
      root = insert(root, x, y, value);
    }
  }

  private Node insert(Node n, double x, double y, T cc) {
    if (n == null) {
      return new Node(x, y, cc);
    }
    if (lessThan(x, n.xp) && lessThan(y, n.yp)) {
      n.sw = insert(n.sw, x, y, cc);
    } else if (lessThan(x, n.xp) && !lessThan(y, n.yp)) {
      n.nw = insert(n.nw, x, y, cc);
    } else if (!lessThan(x, n.xp) && lessThan(y, n.yp)) {
      n.se = insert(n.se, x, y, cc);
    } else if (!lessThan(x, n.xp) && !lessThan(y, n.yp)) {
      n.ne = insert(n.ne, x, y, cc);
    }
    return n;
  }

  public void render(double w, double h) {
    render(root, 0, 0, w, h);
  }

  private void render(Node n, double x, double y, double w, double h) {
    if (n == null) {
      return;
    }

    // Draw rectangle representing the quad
    glBegin(GL_LINES);
    glVertex2f((float) x, (float) y);
    glVertex2f((float) (x + w), (float) y);

    glVertex2f((float) (x + w), (float) y);
    glVertex2f((float) (x + w), (float) (y + h));

    glVertex2f((float) (x + w), (float) (y + h));
    glVertex2f((float) x, (float) (y + h));

    glVertex2f((float) x, (float) (y + h));
    glVertex2f((float) x, (float) y);
    glEnd();

    // Recurse for child nodes
    double halfWidth = w / 2f;
    double halfHeight = h / 2f;

    render(n.nw, x, y, halfWidth, halfHeight);
    render(n.ne, x + halfWidth, y, halfWidth, halfHeight);
    render(n.sw, x, y + halfHeight, halfWidth, halfHeight);
    render(n.se, x + halfWidth, y + halfHeight, halfWidth, halfHeight);
  }

  public List<Node> find(Vertex.Bounds q) {
    return find(new LinkedList<>(), root, q);
  }

  private List<Node> find(List<Node> nodes, Node n, Vertex.Bounds q) {
    if (n == null || !boundary.contains(new Point2D.Double(q.px, q.py))) {
      return nodes;
    }

    if (q.contains(n.xp, n.yp)) {
      nodes.add(n);
    }

    double xmin = q.px;
    double ymin = q.py;
    double xmax = q.w + q.px;
    double ymax = q.h + q.py;
    if (lessThan(xmin, n.xp) && lessThan(ymin, n.yp)) {
      find(nodes, n.sw, q);
    }
    if (lessThan(xmin, n.xp) && !(lessThan(ymax, n.yp) || equalTo(ymax, n.yp))) {
      find(nodes, n.nw, q);
    }
    if (!(lessThan(xmax, n.xp) || equalTo(xmax, n.xp)) && lessThan(ymax, n.yp)) {
      find(nodes, n.se, q);
    }
    if (!(lessThan(xmax, n.xp) || equalTo(xmax, n.xp))
        && !(lessThan(ymax, n.yp) || equalTo(ymax, n.yp))) {
      find(nodes, n.ne, q);
    }

    return nodes; // Handle case when no conditions match
  }

}
