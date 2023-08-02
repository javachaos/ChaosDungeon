package com.github.javachaos.chaosdungeons.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Input listener for simple key presses.
 */
public class InputListener extends MouseAdapter implements KeyListener {

  private final ShapeDrawer parent;

  /**
   * Create a new input listener.
   *
   * @param parent the ShapeDrawer GUI JFrame.
   */
  public InputListener(ShapeDrawer parent) {
    this.parent = parent;
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ALT) {
      parent.createNewShapes();
    }
    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
      parent.toggleDrawInfo();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    parent.toggleShowTriangulation();
  }
}
