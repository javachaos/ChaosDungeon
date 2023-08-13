package com.github.javachaos.chaosdungeons.geometry;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.javachaos.chaosdungeons.collision.CollisionData;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import com.github.javachaos.chaosdungeons.geometry.util.ShapeBuilder;
import com.github.javachaos.chaosdungeons.geometry.utils.ImageTestUtils;
import org.joml.Vector2f;
import org.junit.jupiter.api.Test;

/**
 * Class to test the collision detection in SatCollisionDetection.java
 *
 * @author javachaos
 */
public class TestCollision {

  @Test
  public void testCollision() {
    Vertex circle = new ShapeBuilder.Circle().setRadius(100).build();
    Vertex square =
        new ShapeBuilder.Rectangle().setPosition(new Vector2f(60, 60)).setWidth(250).setHeight(500)
            .build();
    ImageTestUtils.drawPolygon(circle, "/home/fred/Documents/test1.png");
    ImageTestUtils.drawPolygon(square, "/home/fred/Documents/test2.png");
    circle.print();
    square.print();
    CollisionData d = SatCollisionDetector.checkCollision(circle, square);
    assertTrue(d.isColliding());
  }
}
