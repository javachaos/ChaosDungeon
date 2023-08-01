package com.github.javachaos.chaosdungeons.geometry;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.javachaos.chaosdungeons.geometry.GenerationUtils.generateNonRegularPolygon;

class ShapeDrawer extends JFrame {

    private boolean showTriangulation = false;
    private List<Point2D> points1 = null;
    private List<Point2D> points2 = null;
    private List<Triangle> trigs = null;
    private boolean drawInfo;

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private final BufferedImage img = new BufferedImage(WIDTH, HEIGHT,
    BufferedImage.TYPE_INT_RGB);

    public ShapeDrawer() {
        setSize(WIDTH, HEIGHT);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTriangulation = !showTriangulation;
                repaint();
            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ALT) {
                    createNewShapes();
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    drawInfo = !drawInfo;
                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public ShapeDrawer(List<Triangle> trigs) {
        this();
        this.trigs = trigs;
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public ShapeDrawer(Triangle trigs) {
        this();
        this.trigs = new ArrayList<>();
        this.trigs.add(trigs);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public ShapeDrawer(List<Point2D> points1, List<Point2D> points2) {
        this();
        this.points1 = points1;
        this.points2 = points2;
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public ShapeDrawer(ArrayList<Point2D> points1) {
        this();
        this.points1 = points1;
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public ShapeDrawer(Vertex polygon) {
        this((ArrayList<Point2D>)polygon.getPoints());
    }

    public ShapeDrawer(Triangle t1, Triangle t2) {
        this();
        this.points1 = new ArrayList<>(3);
        this.points2 = new ArrayList<>(3);
        points1.add(t1.a);
        points1.add(t1.b);
        points1.add(t1.c);
        points2.add(t2.a);
        points2.add(t2.b);
        points2.add(t2.c);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void createNewShapes() {
        points1 = generateNonRegularPolygon(100, 100,9, 100, 100);
        points2 = generateNonRegularPolygon(250, 250,9, 100, 100);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        ////TODO finish this
        //Graphics2D g2d = (Graphics2D) g;

        if (showTriangulation) {
            Vertex polygon = new Vertex(points1);
            List<Triangle> triangles1 = DelaunayTriangulation.delaunayTriangulation(points1);
            List<Edge> edges = DelaunayTriangulation.constrainDelaunayTriangulation(triangles1, polygon);
            List<Triangle> triangles2 = DelaunayTriangulation.delaunayTriangulation(points2);

            // for (Triangle t : triangles1) {
            //     g2d.setColor(Color.GREEN);
            //     drawShape(g2d, t.getPoints());
            //     g2d.setPaint(Color.GREEN);
            // }
            for (Edge e : edges) {
                g2d.setColor(Color.GREEN);
                //g2d.draw(new Line2D.Double(e.a, e.b));
                drawShape(g2d, List.of(e.a, e.b));
                g2d.setPaint(Color.GREEN);
            }
            for (Triangle t : triangles2) {
                g2d.setColor(Color.GREEN);
                drawShape(g2d, t.getPoints());
                g2d.setPaint(Color.GREEN);
            }
        } else {
            // Draw shape 1
            if (points1 != null) {
                g2d.setColor(Color.RED);
                drawShape(g2d, points1);
                g2d.setPaint(Color.RED);
            }
            if (points2 != null) {
                // Draw shape 2
                g2d.setColor(Color.BLUE);
                drawShape(g2d, points2);
                g2d.setPaint(Color.BLUE);
            }
            if (trigs != null) {
                for (Triangle t : trigs) {
                    g2d.setColor(Color.GREEN);
                    drawShape(g2d, t.getPoints());
                    g2d.setPaint(Color.GREEN);
                }
            }
        }
        this.getContentPane().printAll(g2d);
    }

    private void drawShape(Graphics2D g2d, List<Point2D> points) {
        int OFFSET = 100;
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        int i = 0;

        for (Point2D point : points) {
            xPoints[i] = (int) point.getX() + OFFSET;
            yPoints[i] = (int) point.getY() + OFFSET;
            i++;
        }

        g2d.drawPolygon(xPoints, yPoints, points.size());

        // Print the coordinates of each point on the paint canvas
        for (i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            String coordinatesText = "Point " + i + ": x = " + point.getX() + ", y = " + point.getY();

            // Adjust the text position for each point to avoid overlapping
            int textX = (int) point.getX() + 5 + OFFSET;
            int textY = (int) point.getY() + 15 * i + OFFSET;

            if (drawInfo) {
                g2d.drawString(coordinatesText, textX, textY);
            }
        }
    }
}

