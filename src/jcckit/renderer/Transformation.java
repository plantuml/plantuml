/*
 * Copyright 2003-2004, Franz-Josef Elmer, All rights reserved
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details
 * (http://www.gnu.org/copyleft/lesser.html).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jcckit.renderer;

import java.awt.Graphics2D;

import jcckit.graphic.Anchor;
import jcckit.graphic.ClippingRectangle;
import jcckit.graphic.GraphPoint;

/**
 *  Transformation between device-independent coordinates
 *  and standard Java coordinates. The aspect-ratio will
 *  be the same. The position in the canvas is determined by a
 *  {@link jcckit.graphic.Rectangle Rectangle} defining a (virtual)
 *  paper which is placed in the canvas according to an anchor point.
 *  Depending on the aspect ratio  of the canvas the paper width or
 *  height occupies the canvas width or height.
 *
 *  @author Franz-Josef Elmer
 */
public class Transformation {
  private final double _scale, _x0, _y0;
  
  public String toString() {
		return "_scale=" + _scale + " _x0=" + _x0 + " _y0=" + _y0;
	}

  /**
   *  Creates an instance for the specified canvas size, paper size,
   *  and anchor points of the paper.
   *  @param width Width of the canvas.
   *  @param height Height of the canvas.
   *  @param paper Rectangle defining the paper in device-independent
   *         coordinates.
   *  @param horizontalAnchor Horizontal anchor of the paper in the canvas.
   *  @param verticalAnchor Vertical anchor of the paper in the canvas.
   */
  public Transformation(int width, int height, ClippingRectangle paper,
                        Anchor horizontalAnchor, Anchor verticalAnchor) {
    double pWidth = paper.getMaxX() - paper.getMinX();
    double pHeight = paper.getMaxY() - paper.getMinY();
    _scale = Math.min(width / pWidth, height / pHeight);
    _x0 = 0.5 * horizontalAnchor.getFactor() * (width - _scale * pWidth)
          - _scale * paper.getMinX();
    _y0 = 0.5 * verticalAnchor.getFactor() * (_scale * pHeight - height)
          + height + _scale * + paper.getMinY();
  }

  /** Transforms the device-independent x coordinate into Java coordinates. */
  public int transformX(double x) {
    return trim(_scale * x + _x0);
  }

  /** Transforms the device-independent y coordinate into Java coordinates. */
  public int transformY(double y) {
    return trim(_y0 - _scale * y);
  }

  /** Transforms the device-independent width into Java width. */
  public int transformWidth(double width) {
    return trim(_scale * width + 0.5);
  }

  /** Transforms the device-independent height into Java height. */
  public int transformHeight(double height) {
    return trim(_scale * height + 0.5);
  }
  
  private static int trim(double number)
  {
    return number > Short.MAX_VALUE 
              ? Short.MAX_VALUE 
              : (number < Short.MIN_VALUE ? Short.MIN_VALUE : (int) number); 
  }

  /**
   *  Transforms a point in Java coordinates back into device-independent
   *  coordinates.
   */
  public GraphPoint transformBack(int x, int y) {
    return new GraphPoint((x - _x0) / _scale, (_y0 - y) / _scale);
  }

public void apply(Graphics2D g) {
	g.translate(_x0, _y0);
	g.scale(_scale, -_scale);
	
}
}

