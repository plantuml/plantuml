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
package jcckit.plot;

import jcckit.graphic.GraphPoint;
import jcckit.graphic.GraphicalElement;

/**
 * A curve is defined by a sequence of points in device-independent
 * coordinates. The points can be decorated by symbols and/or 
 * connected by lines.{@link Hint Hints} are used to determine additional
 * properties of the symbol. This is especially important for
 * charts with bars.
 * <p>
 * In accordance with the Factory Method Pattern
 * the symbols are created by a {@link SymbolFactory}.
 *
 * @author Franz-Josef Elmer
 */
public interface Curve {
  /**
   * Returns the graphical representation of a curve.
   * Different invocations of this method might return
   * different instances.
   * This is especially true after adding, inserting, removing, or
   * repplacing a point of the curve.
   */
  public GraphicalElement getView();

  /** 
   * Returns a symbol which can be used to create the legend for the curve. 
   * For example, it should return a horizontal line with the symbol
   * in the middle if the curve is a line with points decorated by symbols.
   */
  public GraphicalElement getLegendSymbol();

  /** 
   * Appends a new point to the curve. 
   * @param point Position in device-independent coordinates. 
   * @param hintFromPreviousCurve Hint which may be used to calculate
   *        the corresponding {@link GraphicalElement}.
   * @return hint for next curve.
   */
  public Hint addPoint(GraphPoint point, Hint hintFromPreviousCurve);

  /** Removes all points from the curve. */
  public void removeAllPoints();
}
