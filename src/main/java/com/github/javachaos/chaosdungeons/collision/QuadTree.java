package com.github.javachaos.chaosdungeons.collision;

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

  public static class Quad {
    public float x;
    public float y;
    public float w;
    public float h;
    
    public Quad(float x, float y, float w, float h) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
    }

    public boolean intersects(Quad other) {
      // Calculate the boundaries of the current Quad
      float x1 = this.x;
      float y1 = this.y;
      float x2 = this.x + w;
      float y2 = this.y + h;

      // Calculate the boundaries of the other Quad
      float x3 = other.x;
      float y3 = other.y;
      float x4 = other.x + other.w;
      float y4 = other.y + other.h;

      // Check for intersection using the separating axis theorem
      return !(x2 < x3 || x1 > x4 || y2 < y3 || y1 > y4);
    }


    public boolean contains(float x, float y) {
        float x1 = this.x;
        float x2 = this.x + w;
        float y1 = this.y;
        float y2 = this.y + h;
        return x >= x1 && x <= x2
            && y >= y1 && y <= y2;
    }


    public void setPosition(Vector3f pos) {
      this.x = pos.x;
      this.y = pos.y;
    }
  }

  public class Node {
    float x;
    float y;
    Node NW, NE, SE, SW;
    T value;
    public T getValue() {
      return value;
    }
    Node(float x, float y, T ge) {
      this.value = ge;
      this.x = x;
      this.y = y;
    }
  }

  private Quad boundary;

  private Node root;

  public QuadTree(Quad boundary) {
    this.boundary = boundary;
  }

  public QuadTree(float w, float h) {
    Vector3f pos = GameWindow.getCamera().getPosition();
    this.boundary = new Quad(pos.x, pos.y, w, h);
  }

  public void insert(float x, float y, T value) {
    if (boundary.contains(x, y)) {
      root = insert(root, x, y, value);
    }
  }

  public void render(float w, float h) {
    render(root, 0, 0, w, h);
  }

  private void render(Node n, float x, float y, float w, float h) {
    if (n == null) {
      return;
    }

    // Draw rectangle representing the quad
    glBegin(GL_LINES);
    glVertex2f(x, y);
    glVertex2f(x + w, y);

    glVertex2f(x + w, y);
    glVertex2f(x + w, y + h);

    glVertex2f(x + w, y + h);
    glVertex2f(x, y + h);

    glVertex2f(x, y + h);
    glVertex2f(x, y);
    glEnd();

    // Recurse for child nodes
    float halfWidth = w / 2f;
    float halfHeight = h / 2f;

    render(n.NW, x, y, halfWidth, halfHeight);
    render(n.NE, x + halfWidth, y, halfWidth, halfHeight);
    render(n.SW, x, y + halfHeight, halfWidth, halfHeight);
    render(n.SE, x + halfWidth, y + halfHeight, halfWidth, halfHeight);
  }

  private Node insert(Node n, float x, float y, T cc) {
    if (n == null) {
      return new Node(x, y, cc);
    }
    if (x < n.x && y < n.y) {
      n.SW = insert(n.SW, x, y, cc);
    } else if (x < n.x && !(y < n.y)) {
      n.NW = insert(n.NW, x, y, cc);
    } else if (!(x < n.x) && y < n.y) {
      n.SE = insert(n.SE, x, y, cc);
    } else if (!(x < n.x) && !(y < n.y)) {
      n.NE = insert(n.NE, x, y, cc);
    }
    return n;
  }

  public List<Node> find(Quad q) {
      return find(new LinkedList<>(), root, q);
  }

  private List<Node> find(List<Node> nodes, Node n, Quad q) {
    if (n == null || !boundary.intersects(q)) {
      return nodes;
    }

    float xmin = q.x;
    float ymin = q.y;
    float xmax = q.w + q.x;
    float ymax = q.h + q.y;

    if (q.contains(n.x, n.y)) {
      nodes.add(n);
    }

    if (xmin < n.x && ymin < n.y) {
      find(nodes, n.SW, q);
    }
    if (xmin < n.x && !(ymax <= n.y)) {
      find(nodes, n.NW, q);
    }
    if (!(xmax <= n.x) && ymax < n.y) {
      find(nodes, n.SE, q);
    }
    if (!(xmax <= n.x) && !(ymax <= n.y)) {
      find(nodes, n.NE, q);
    }

    return nodes; // Handle case when no conditions match
  }

}
