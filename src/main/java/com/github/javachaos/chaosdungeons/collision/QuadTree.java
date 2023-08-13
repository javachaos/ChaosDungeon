package com.github.javachaos.chaosdungeons.collision;

import static com.github.javachaos.chaosdungeons.utils.PrecisionUtils.equalTo;
import static com.github.javachaos.chaosdungeons.utils.PrecisionUtils.greaterThan;
import static com.github.javachaos.chaosdungeons.utils.PrecisionUtils.lessThan;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.github.javachaos.chaosdungeons.gui.GameWindow;
import java.util.LinkedList;
import java.util.List;
import org.joml.Vector3f;

/**
 * A simple PR quad tree for collision detection.
 */
public class QuadTree<T> {

  /**
   * A simple quad.
   */
  public static class Quad {
    public double xp;
    public double yp;
    public double wp;
    public double hp;

    /**
     * Create a simple quad.
     *
     * @param xp x
     * @param yp y
     * @param wp width
     * @param hp height
     */
    public Quad(double xp, double yp, double wp, double hp) {
      this.xp = xp;
      this.yp = yp;
      this.wp = wp;
      this.hp = hp;
    }

    /**
     * Return true if this quad intersects with the other quad.
     *
     * @param other the quad to check intersection with
     * @return true if this quad and other intersect
     */
    public boolean intersects(Quad other) {
      // Calculate the boundaries of the current Quad
      double x1 = this.xp;
      double y1 = this.yp;
      double x2 = this.xp + wp;
      double y2 = this.yp + hp;

      // Calculate the boundaries of the other Quad
      double x3 = other.xp;
      double y3 = other.yp;
      double x4 = other.xp + other.wp;
      double y4 = other.yp + other.hp;
      // Check for intersection using the separating axis theorem
      return !(lessThan(x2, x3) || greaterThan(x1, x4) || lessThan(y2, y3) || greaterThan(y1, y4));
    }

    /**
     * Return true if this quad contains the point (x, y).
     *
     * @param x the x pos
     * @param y the y pos
     * @return true if this quad contains (x, y)
     */
    public boolean contains(double x, double y) {
      double x1 = this.xp;
      double x2 = this.xp + wp;
      double y1 = this.yp;
      double y2 = this.yp + hp;
      return (greaterThan(x, x1) || equalTo(x, x1))
          && (lessThan(x, x2)    || equalTo(x, x2))
          && (greaterThan(y, y1) || equalTo(y, y1))
          && (lessThan(y, y2)    || equalTo(y, y2));
    }

  }

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

  private final Quad boundary;

  private Node root;

  public QuadTree(double w, double h) {
    Vector3f pos = GameWindow.getCamera().getPosition();
    this.boundary = new Quad(pos.x, pos.y, w, h);
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

  public List<Node> find(Quad q) {
    return find(new LinkedList<>(), root, q);
  }

  private List<Node> find(List<Node> nodes, Node n, Quad q) {
    if (n == null || !boundary.intersects(q)) {
      return nodes;
    }

    if (q.contains(n.xp, n.yp)) {
      nodes.add(n);
    }

    double xmin = q.xp;
    double ymin = q.yp;
    double xmax = q.wp + q.xp;
    double ymax = q.hp + q.yp;
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
