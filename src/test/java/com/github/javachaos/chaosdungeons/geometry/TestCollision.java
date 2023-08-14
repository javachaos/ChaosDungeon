package com.github.javachaos.chaosdungeons.geometry;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.javachaos.chaosdungeons.collision.CollisionData;
import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;
import com.github.javachaos.chaosdungeons.geometry.util.ShapeBuilder;
import com.github.javachaos.chaosdungeons.geometry.utils.ImageTestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.junit.jupiter.api.Test;

/**
 * Class to test the collision detection in SatCollisionDetection.java
 *
 * @author javachaos
 */
public class TestCollision {

  private static final Logger LOGGER = LogManager.getLogger(TestCollision.class);

  @Test
  public void testCollisionPartialOverlap() {
    Vertex circle = new ShapeBuilder.Circle().setNumPoints(100).setRadius(55).build();
    Vertex square =
        new ShapeBuilder.Rectangle().setPosition(new Vector2f(60, 60)).setWidth(250).setHeight(500)
            .build();
    ImageTestUtils.drawPolygon(System.getProperty("user.home") + "/Documents/test1.png", circle, square);
    circle.print();
    square.print();
    CollisionData d = circle.checkCollision(square);
    CollisionData e = square.checkCollision(circle);
    assertTrue(d.isColliding());
    assertTrue(e.isColliding());
    LOGGER.debug("Collision: {}", d);
    LOGGER.debug("Collision: {}", e);
  }

  @Test
  public void testCollisionFullOverlap() {
    Vertex circle = new ShapeBuilder.Circle().setPosition(new Vector2f(65, 150)).setNumPoints(100).setRadius(55).build();
    Vertex square =
            new ShapeBuilder.Rectangle().setPosition(new Vector2f(60, 60)).setWidth(250).setHeight(500)
                    .build();
    ImageTestUtils.drawPolygon(System.getProperty("user.home") + "/Documents/test1.png", circle, square);
    circle.print();
    square.print();
    LOGGER.debug("Square");
    CollisionData d = circle.checkCollision(square);
    LOGGER.debug("Circle");
    CollisionData e = square.checkCollision(circle);
    assertTrue(d.isColliding());
    assertTrue(e.isColliding());
    LOGGER.debug("Collision: {}", d);
    LOGGER.debug("Collision: {}", e);
  }
}
