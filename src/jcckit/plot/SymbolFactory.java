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
 * Interface of a symbol factory. A symbol is a {@link GraphicalElement}
 * or {@link jcckit.graphic.GraphicalComposite}. A symbol factory creates
 * the same type of symbols. In general, they have all the same size.
 * But they are distinguished from each other by their positions. 
 * In addition they may also differ in other properties which will
 * be determined by {@link Hint Hints}. 
 *
 * @author Franz-Josef Elmer
 */
public interface SymbolFactory {
  /** Common configuration parameter key need by implementing classes. */
  public static final String SIZE_KEY = "size",
                             ATTRIBUTES_KEY = "attributes";

  /** Default size of a symbol = 0.01. */
  public static final double DEFAULT_SIZE = 0.01;

  /**
   * Creates a symbol for the specified point taking into account
   * the specified hints.
   * @param point The position of the symbol. In general it is a transformation
   *        of a corresponding {@link jcckit.data.DataPoint} into a
   *        {@link GraphPoint}.
   * @param hintFromPreviousPoint Hint from the previous point of the same
   *        {@link Curve} or <tt>null</tt>.
   * @param hintFromPreviousCurve Hint from the previous 
   *        {@link Curve} or <tt>null</tt>.
   */
  public Symbol createSymbol(GraphPoint point, Hint hintFromPreviousPoint,
                             Hint hintFromPreviousCurve);

  /**
   *  Creates a symbol for the legend at the specified position.
   *  @param centerPosition Center position of the symbol.
   *  @param size The size of the symbol. Will not be used if the symbol
   *         of the curve points have all the same size. In this case
   *         the symbol for the legend has the size of the curve symbols.
   */
  public GraphicalElement createLegendSymbol(GraphPoint centerPosition,
                                             double size);
}
