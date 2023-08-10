package com.github.javachaos.chaosdungeons.collision;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.github.javachaos.chaosdungeons.ecs.components.CollisionComponent;
import com.github.javachaos.chaosdungeons.ecs.entities.GameEntity;
import com.github.javachaos.chaosdungeons.geometry.polygons.Quad;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuadTree {

  private static final Logger LOGGER = LogManager.getLogger(QuadTree.class);

  private static Map<GameEntity, Node> collisionMap = new HashMap<>();

  private Node root;

  public static class Node {
    float x;
    float y;
    Node NW, NE, SE, SW;
    boolean isLeaf = true;
    GameEntity value;
    public GameEntity getValue() {
      return value;
    }
    Node(float x, float y, GameEntity ge) {
      this.value = ge;
      this.x = x;
      this.y = y;
    }
  }

  public void insert(float x, float y, GameEntity value) {
    root = insert(root, x, y, value);
  }

  public void updateNode(float ox, float oy, float x, float y, GameEntity ge) {
    Vertex s = null;
    CollisionComponent cc = null;
    if (ge != null)
      cc = ge.getCollisionComponent();
    if (cc != null) {
      s = cc.getShape();
    }
    Rectangle r = null;
    if(s != null) {
      r = s.getBounds();
    }
    if (r != null) {
      List<Node> c =
          getChildren(new LinkedList<>(), find(new Quad(x, y, ox, oy)));
        remove(ox, oy);
        c.forEach(child -> insert(child.x, child.y, child.value));
        insert(x, y, ge);
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

  public void remove(float x, float y) {
    root = remove(root, x, y);
  }

  private Node remove(Node n, float x, float y) {
    if (n == null) {
      return null;
    }

    // Check if the current node's coordinates match the target coordinates
    if (eq(n.x, x) && eq(n.y, y)) {
      collisionMap.keySet().remove(n.value);
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

  private Node insert(Node n, float x, float y, GameEntity cc) {
    if (n == null) {
      Node node = new Node(x, y, cc);
      collisionMap.put(cc, node);
      return node;
    }
    n.isLeaf = false;
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

  public Node find(Quad q) {
    return find(root, q);
  }

  private Node find(Node n, Quad q) {
    if (n == null) {
      return null;
    }

    Rectangle r = q.getBounds();

    float xmin = r.x;
    float ymin = r.y;
    float xmax = r.width + r.x;
    float ymax = r.height + r.y;

    if (r.contains(n.x, n.y) && n.isLeaf) {
      //LOGGER.debug("Found: ({},{})", n.x, n.y);
      return n;
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
