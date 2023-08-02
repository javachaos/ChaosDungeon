package com.github.javachaos.chaosdungeons.geometry.utils;

import com.github.javachaos.chaosdungeons.geometry.polygons.Vertex;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageTestUtils {
    public static void drawPolygon(Vertex poly, String filename) {
        List<Point2D> p = poly.getPoints();
        int WIDTH = 512;
        int HEIGHT = 512;
        final BufferedImage img = new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.WHITE);
        g2d.setPaint(Color.RED);
        g2d.setStroke(new BasicStroke(2.0f));
        drawShape(g2d, p);
        JPanel jp = new JPanel();
        jp.printAll(g2d);
        jp.setVisible(true);
        writeBufferedImageToFile(img,
                new File(filename));
    }

    private static void writeBufferedImageToFile(BufferedImage img, File f) {
        try {
            ImageIO.write(img, "PNG", f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void drawShape(Graphics2D g2d, List<Point2D> points) {
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        int i = 0;
        for (Point2D point : points) {
            xPoints[i] = (int) point.getX();
            yPoints[i] = (int) point.getY();
            i++;
        }
        g2d.drawPolygon(xPoints, yPoints, points.size());
        for (i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            String coordinatesText = "P[ " + i + ": x = " + point.getX() + ", y = " + point.getY() + " ]";
            g2d.drawString(coordinatesText, (int) point.getX(), (int) point.getY());
        }
    }
}
