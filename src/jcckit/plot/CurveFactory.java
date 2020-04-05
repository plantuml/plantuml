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

import jcckit.graphic.ClippingShape;

/**
 * Interface of a curve factory. A curve factory creates a new instance
 * of a {@link Curve}.
 * 
 * @author Franz-Josef Elmer
 */
public interface CurveFactory {
  /**
   * Creates a new curve instance.
   * @param curveIndex The index of the curve in the {@link Plot} to which
   *        it should belong.
   * @param numberOfCurves Number of curves. Will be needed to calculate the 
   *        y-coordinate of the legend symbol.
   * @param clippingShape Clipping shape applied to the curve.
   * @param legend The legend which will show the curve symbol.
   * @return an empty instance.
   */
  public Curve create(int curveIndex,  int numberOfCurves,
                      ClippingShape clippingShape, Legend legend);
}
