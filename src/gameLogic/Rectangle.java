package gameLogic;

import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;

/**
 * The Rectangle class represents a rectangle defined by its top-left corner (x,
 * y),
 * its width, and its height. It provides a method to check if a point is
 * contained within the rectangle.
 * It is used to simplify hitbox checks
 */
public class Rectangle {
  public int y;
  public int x;
  public int width;
  public int height;

  public Rectangle(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Check if the given coordinates (x, y) are within the bounds of this
   * Rectangle,
   * while considering a scaling factor.
   *
   * @param x     the x-coordinate of the point to check (après scaling)
   * @param y     the y-coordinate of the point to check (après scaling)
   * @param scale the scaling factor to apply to the rectangle's dimensions
   * @return true if the point (x, y) is within the rectangle defined by (rectX,
   *         rectY, rectWidth, rectHeight) after scaling, false otherwise
   */
  public boolean contains(double x, double y) {
    return x >= this.x && x < this.x + this.width
        && y >= this.y && y < this.y + this.height;
  }

  /**
   * For debugging purposes, this method draws the rectangle on the given Graphics
   * context.
   *
   * @param g the Graphics object used to draw the rectangle
   */
  public void draw(Graphics2D g) {
    // Transparency effect
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    g.setColor(Color.GRAY);
    g.fillRect(getX(), getY(), getWidth(), getHeight());
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
