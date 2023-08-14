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
    Vertex circle = new ShapeBuilder.Circle().setNumPoints(50).setRadius(160).build();
    Vertex square =
        new ShapeBuilder.Rectangle().setPosition(new Vector2f(60, 60)).setWidth(250).setHeight(500)
            .build();
    Vertex rand = new Vertex(GenerationUtils.generateNonRegularPolygon(160, 160, 6, 128, 128));
    ImageTestUtils.drawPolygon(System.getProperty("user.home") + "/Documents/test1.png", circle, square, rand);
    circle.print();
    square.print();
    CollisionData d = circle.checkCollision(square);
    CollisionData e = square.checkCollision(circle);
    long start = System.nanoTime();
    CollisionData a = rand.checkCollision(circle);
    long end = System.nanoTime();
    CollisionData p = rand.checkCollision(square);
    LOGGER.debug("Runtime: {}", end - start);
    assertTrue(d.isColliding());
    assertTrue(e.isColliding());
    assertTrue(a.isColliding());
    assertTrue(p.isColliding());
    LOGGER.debug("Collision: {}", d);
    LOGGER.debug("Collision: {}", e);
    LOGGER.debug("Collision: {}", a);
    LOGGER.debug("Collision: {}", p);

  }

  @Test
  public void testCollisionFullOverlap() {
    Vertex circle = new ShapeBuilder.Circle().setPosition(new Vector2f(65, 150)).setNumPoints(20).setRadius(55).build();
    Vertex square =
            new ShapeBuilder.Rectangle().setPosition(new Vector2f(60, 60)).setWidth(250).setHeight(500)
                    .build();
    ImageTestUtils.drawPolygon(System.getProperty("user.home") + "/Documents/test1.png", circle, square);
    circle.print();
    square.print();
    LOGGER.debug("Square");
    long start = System.nanoTime();
    CollisionData d = circle.checkCollision(square);
    long end = System.nanoTime();
    LOGGER.debug("Runtime: {}", end - start);
    LOGGER.debug("Circle");
    CollisionData e = square.checkCollision(circle);
    assertTrue(d.isColliding());
    assertTrue(e.isColliding());
    LOGGER.debug("Collision: {}", d);
    LOGGER.debug("Collision: {}", e);
  }
}
