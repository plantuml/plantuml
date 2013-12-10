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
import jcckit.graphic.GraphPoint;
import jcckit.graphic.GraphicAttributes;
import jcckit.graphic.GraphicalElement;
import jcckit.graphic.Rectangle;
import jcckit.util.ConfigParameters;

/**
 * A factory of bars. The bars are {@link Rectangle Rectangles}. 
 * Depending on the configuration parameters the bars can be 
 * horizontal or vertical. Bars of several curves can be side by side or
 * stacked. The bar length is determined by the x or y value of the 
 * curve point in device-independent coordinates. If the value is negative 
 * the bar goes into the negative direction. For stacked bars the values
 * should always be positive.
 * <p>
 * When used inside a {@link SimpleCurve} soft clipping should always be
 * switched off (see 
 * {@link SimpleCurve#SimpleCurve(ConfigParameters, int, int, ClippingShape, Legend)}). 
 *
 * @author Franz-Josef Elmer
 */
public class BarFactory extends AbstractSymbolFactory {
  /** Configuration parameter key. */
  public static final String STACKED_KEY = "stacked",
                             HORIZONTAL_BARS_KEY = "horizontalBars";

  private final boolean _stacked;
  private final boolean _horizontalBars;

  /**
   * Creates an instance from the specified configuration parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>horizontalBars = false</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>If <tt>true</tt> horizontal bars will be drawn. Otherwise 
   *         vertical bars are drawn.</td></tr>
   * <tr><td><tt>stacked = false</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>If <tt>true</tt> the bars of several curves will be 
   *         stacked.</td></tr>
   * </table>
   * In addition the configuration parameters of the
   * <a href="AbstractSymbolFactory.html#AbstractSymbolFactory(jcckit.util.ConfigParameters)">
   * constructor</a> of the superclass {@link AbstractSymbolFactory} apply.
   */
  public BarFactory(ConfigParameters config) {
    super(config);
    _horizontalBars = config.getBoolean(HORIZONTAL_BARS_KEY, false);
    _stacked = config.getBoolean(STACKED_KEY, false);
  }

  /**
   * Creates a bar at the specified point.
   * If <tt>hintFromPreviousCurve</tt>
   * is not an instance of {@link PositionHint} the values of 
   * origin and position will be (0,0).
   * @param hintFromPreviousCurve Hint from previous curve. Will be used
   *        to calculate symbol shape and hint for the next curve.
   */
  protected Symbol createSymbol(GraphPoint point, GraphicAttributes attributes,
                             Hint hintForNextPoint,
                             Hint hintFromPreviousCurve) {
    GraphPoint origin = new GraphPoint(null);
    GraphPoint position = origin;
    if (hintFromPreviousCurve instanceof PositionHint) {
      origin = ((PositionHint) hintFromPreviousCurve).getOrigin();
      position = ((PositionHint) hintFromPreviousCurve).getPosition();
    }
    double px = position.getX();
    double py = position.getY();
    double x = point.getX() - origin.getX();
    double y = point.getY() - origin.getY();
    if (_horizontalBars) {
      y = _size;
      position = new GraphPoint(px + 0.5 * x, point.getY() + py);
      px += _stacked ? x : 0;
      py += _stacked ? 0 : _size;
    } else {
      x = _size;
      position = new GraphPoint(point.getX() + px, py + 0.5 * y);
      px += _stacked ? 0 : _size;
      py += _stacked ? y : 0;
    }
    Hint hintForNextCurve = new PositionHint(origin, new GraphPoint(px, py));
    return new Symbol(new Rectangle(position, Math.abs(x), Math.abs(y), 
                                    attributes),
                      hintForNextPoint, hintForNextCurve);
  }

  /**
   *  Creates a symbol for the legend at the specified position.
   *  @param centerPosition Center position of the symbol.
   *  @param size The size of the symbol.
   */
  public GraphicalElement createLegendSymbol(GraphPoint centerPosition,
                                             double size) {
    return new Rectangle(centerPosition, size, size, _attributes);
  }
  
  /** 
   * Returns <tt>null</tt> because this method isn't needed but has to be
   * implemented.
   */
  protected GraphicalElement createPlainSymbol(
      GraphPoint centerPosition, double size, GraphicAttributes attributes) {
    return null;
  }
}
