package com.github.javachaos.chaosdungeons.geometry;

import static org.junit.jupiter.api.Assertions.assertFalse;
import com.github.javachaos.chaosdungeons.collision.Collision;
import com.github.javachaos.chaosdungeons.collision.Polygon;
import com.github.javachaos.chaosdungeons.geometry.util.ShapeBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.junit.jupiter.api.Test;

/**
 * Class to test the collision detection in SatCollisionDetection.java
 *
 * @author javachaos
 */
class TestCollision {

  private static final Logger LOGGER = LogManager.getLogger(TestCollision.class);

  @Test
  void testCollisionPartialOverlap() {
    Polygon circle = new ShapeBuilder.Circle().setNumPoints(50).setRadius(160).build();
    Polygon square =
        new ShapeBuilder.Rectangle().setPosition(new Vector2f(60, 60)).setWidth(250).setHeight(500)
            .build();
    Polygon rand = new Polygon(GenerationUtils.generateNonRegularPolygon(160, 160, 6, 128, 128));
// ImageTestUtils.drawPolygon(System.getProperty("user.home") + "/Documents/test1.png", circle, square, rand);
//    circle.print();
//    square.print();
    Collision d = circle.checkCollision(square);
    Collision e = square.checkCollision(circle);
    long start = System.nanoTime();
    Collision a = rand.checkCollision(circle);
    long end = System.nanoTime();
    Collision p = rand.checkCollision(square);
    LOGGER.debug("Runtime: {}", end - start);
    assertFalse(d.isColliding());
    assertFalse(e.isColliding());
    assertFalse(a.isColliding());
    assertFalse(p.isColliding());
    LOGGER.debug("Collision: {}", d);
    LOGGER.debug("Collision: {}", e);
    LOGGER.debug("Collision: {}", a);
    LOGGER.debug("Collision: {}", p);
  }

  @Test
  void testCollisionFullOverlap() {
    Polygon circle = new ShapeBuilder.Circle().setPosition(new Vector2f(65, 150)).setNumPoints(20).setRadius(55).build();
    Polygon square =
            new ShapeBuilder.Rectangle().setPosition(new Vector2f(60, 60)).setWidth(250).setHeight(500)
                    .build();
//    ImageTestUtils.drawPolygon(System.getProperty("user.home") + "/Documents/test1.png", circle, square);
//    circle.print();
//    square.print();
    LOGGER.debug("Square");
    long start = System.nanoTime();
    Collision d = circle.checkCollision(square);
    long end = System.nanoTime();
    LOGGER.debug("Runtime: {}", end - start);
    LOGGER.debug("Circle");
    Collision e = square.checkCollision(circle);
    assertFalse(d.isColliding());
    assertFalse(e.isColliding());
    LOGGER.debug("Collision: {}", d);
    LOGGER.debug("Collision: {}", e);
  }
}
