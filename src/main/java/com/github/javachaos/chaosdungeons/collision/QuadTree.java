package com.github.javachaos.chaosdungeons.collision;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.github.javachaos.chaosdungeons.ecs.components.CollisionComponent;
import com.github.javachaos.chaosdungeons.geometry.polygons.Quad;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuadTree {

  private static final Logger LOGGER = LogManager.getLogger(QuadTree.class);

  private static Map<CollisionComponent, Node> collisionMap = new HashMap<>();

  private Node root;

  private class Node {
    float x;
    float y;
    Node NW, NE, SE, SW;
    boolean isLeaf = true;
    CollisionComponent value;
    Node(float x, float y, CollisionComponent ge) {
      this.value = ge;
      this.x = x;
      this.y = y;
    }
  }

  public void insert(float x, float y, CollisionComponent value) {
    root = insert(root, x, y, value);
  }

  public void updateNode(CollisionComponent cc, float x, float y) {
    Node node = collisionMap.get(cc);
    if (node != null) {
      List<Node> children = getChildren(new LinkedList<>(), node);
      remove(node.x, node.y);
      children.forEach(c -> insert(c.x, c.y, c.value));
      insert(x, y, cc);
    }
  }

  public void render(float w, float h) {
    render(root, root.x, root.y, w, h);
  }

  private void render(Node n, float x, float y, float w, float h) {
    if (n == null) {
      return;
    }

    if (n.isLeaf) {
      glColor3f(1f, 0f, 0f);
    } else {
      glColor3f(0f, 0f, 1f);
    }

    // Draw horizontal lines
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


  public void remove(float x, float y) {
    root = remove(root, x, y);
  }

  private Node remove(Node n, float x, float y) {
    if (n == null) {
      return null;
    }

    // Check if the current node's coordinates match the target coordinates
    if (eq(n.x, x) && eq(n.y, y)) {
      return null;  // Node found, return null to remove it
    }

    // Recurse into appropriate sub-quadrant based on coordinates
    if (x < n.x && y < n.y) {
      n.SW = remove(n.SW, x, y);
    } else if (x < n.x && !(y < n.y)) {
      n.NW = remove(n.NW, x, y);
    } else if (!(x < n.x) && y < n.y) {
      n.SE = remove(n.SE, x, y);
    } else if (!(x < n.x) && !(y < n.y)) {
      n.NE = remove(n.NE, x, y);
    }

    return n;  // Return the updated node after removing or not
  }

  private static final float EPSILON = 1e-6f;

  private boolean eq(float a, float b) {
    return Math.abs(a - b) < EPSILON;
  }

  private List<Node> getChildren(List<Node> children, Node n) {
    if (n == null) {
      return children;
    }
    if (n.isLeaf) {
      children.add(n);
    } else {
      if (n.SE != null) {
        getChildren(children, n.SE);
      }
      if (n.SW != null) {
        getChildren(children, n.SW);
      }
      if (n.NE != null) {
        getChildren(children, n.NE);
      }
      if (n.NW != null) {
        getChildren(children, n.NW);
      }
    }
    return children;
  }

  private Node insert(Node n, float x, float y, CollisionComponent ge) {
    if (n == null) {
      Node node = new Node(x, y, ge);
      collisionMap.put(ge, node);
      return node;
    }
    n.isLeaf = false;
    if (x < n.x && y < n.y) {
      n.SW = insert(n.SW, x, y, ge);
    } else if (x < n.x && !(y < n.y)) {
      n.NW = insert(n.NW, x, y, ge);
    } else if (!(x < n.x) && y < n.y) {
      n.SE = insert(n.SE, x, y, ge);
    } else if (!(x < n.x) && !(y < n.y)) {
      n.NE = insert(n.NE, x, y, ge);
    }
    return n;
  }

  public CollisionComponent find(Quad q) {
    return find(root, q);
  }

  private CollisionComponent find(Node n, Quad q) {
    if (n == null) {
      return null;
    }

    Rectangle r = q.getBounds();

    float xmin = r.x;
    float ymin = r.y;
    float xmax = r.width + r.x;
    float ymax = r.height + r.y;

    if (r.contains(n.x, n.y) && n.isLeaf) {
      LOGGER.debug("Found: ({},{})", n.x, n.y);
      return n.value;
    }

    if (xmin < n.x && ymin < n.y) {
      return find(n.SW, q);
    }
    if (xmin < n.x && !(ymax <= n.y)) {
      return find(n.NW, q);
    }
    if (!(xmax <= n.x) && ymax < n.y) {
      return find(n.SE, q);
    }
    if (!(xmax <= n.x) && !(ymax <= n.y)) {
      return find(n.NE, q);
    }

    return null; // Handle case when no conditions match
  }

}
