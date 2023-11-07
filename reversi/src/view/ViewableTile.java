package view;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * add javadoc.
 */
public class ViewableTile {
  //x and y represent the position of the tile in the window in which it will be drawn
  private final int x;
  private final int y;
  //q and r represent the logical axial coordinates of the Tile object in the model
  //that this ViewableTile corresponds to
  private final int q;
  private final int r;
  private Color color;
  private final Hexagon2D hexagon;

  /**
   * add javadoc.
   * @param color
   * @param x
   * @param y
   * @param radius
   * @param q
   * @param r
   */
  public ViewableTile(Color color, int x, int y, int radius, int q, int r) {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("Coordinates cannot be negative.");
    }
    this.color = Objects.requireNonNull(color);
    this.x = x;
    this.y = y;
    this.q = q;
    this.r = r;

    this.hexagon = new Hexagon2D(x, y, radius);
  }

  public void draw(Graphics g) {
    g.setColor(this.color);
    Graphics2D g2d = (Graphics2D) g;
    g2d.draw(hexagon);
  }

  public void setColor(Color color) {
    this.color = Objects.requireNonNull(color);
  }

  //we can invoke these! pressTile could change the color for us, for example
  public void pressTile() {
    //
  }

  //we can make it so that when we release a tile, for example,
  //it knows what to do
  public void releaseTile() {
    //
  }

  public boolean containsPoint(Point2D point) {
    return this.hexagon.contains(point);
    //TODO confirm this works without contains() explicitly in Hexagon2D
  }

  //TODO make interface and put all these getters in it but make sure they're necessary
  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getXCoordinate() {
    return this.q;
  }

  public int getYCoordinate() {
    return this.r;
  }
}