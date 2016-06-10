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
import jcckit.graphic.GraphicAttributes;
import jcckit.graphic.GraphicalComposite;
import jcckit.graphic.GraphicalElement;
import jcckit.graphic.Rectangle;
import jcckit.util.ConfigParameters;
import jcckit.util.Factory;

/**
 * Symbol factory for creating symbols with error bars. It wraps 
 * a {@link SymbolFactory} for creating the symbol. The error bars
 * are {@link Rectangle Rectangles}. 
 * <p>
 * Curves with error bars are based on <em>two</em> 
 * {@link jcckit.data.DataCurve DataCurves}:
 * <ol><li>The plain curve.
 *     <li>An instance which stores the errors in <i>x</i> and <i>y</i>.
 *         It is assumed that the errors are positive values defining
 *         the error symmetrically around the curve points.
 * </ol>
 * <p>
 * The <tt>ErrorBarFactory</tt> needs an instance of {@link PositionHint}
 * as initial {@link Hint} for the next curve. Its origin must be
 * the origin of the data coordinate system in device-independent coordinates.
 * The position of <tt>PositionHint</tt> must be undefined.
 * 
 * @author Franz-Josef Elmer
 */
public class ErrorBarFactory implements SymbolFactory {
  /** Configuration parameter key. */
  public static final String SYMBOL_FACTORY_KEY = "symbolFactory";

  private final SymbolFactory _symbolFactory;
  private final GraphicAttributes _attributes;
  private final double _size;

  /**
   * Creates an instance from the specified configuration parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>symbolFactory = null</tt></td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Definition of the wrapped {@link SymbolFactory} which generates
   *         the curve symbol without bars. By default an empty
   *         {@link GraphicalComposite} will be created.</td></tr>
   * <tr><td><tt>size = 0</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Width of the error bars.</td></tr>
   * <tr><td><tt>attributes = null</tt></td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Definition of the {@link GraphicAttributes} of the error 
   *         bars.</td></tr>
   * </table>
   */
  public ErrorBarFactory(ConfigParameters config) {
    _symbolFactory = (SymbolFactory) Factory.createOrGet(
        config.getNode(SYMBOL_FACTORY_KEY), null);
    _size = config.getDouble(SIZE_KEY, 0);
    _attributes = (GraphicAttributes) Factory.createOrGet(
        config.getNode(ATTRIBUTES_KEY), null);
  }

  /**
   * Creates the legend symbol. Calls the wrapped {@link SymbolFactory}
   * or returns an empty instance of {@link GraphicalComposite} if undefined.
   */
  public GraphicalElement createLegendSymbol(GraphPoint centerPosition,
                                             double size) {
    return _symbolFactory == null ? new GraphicalComposite(null)
              : _symbolFactory.createLegendSymbol(centerPosition, size);
  }

  /**
   * Creates either the curve symbol or the error bars. Error bars are
   * created when <tt>hintFromPreviousCurve</tt> is an instance of
   * {@link PositionHint} and its position attribute is not <tt>null</tt>.
   * Otherwise the curve symbol is created. The position attributes stores
   * the curve point (in device-independent coordinates). The origin is
   * always as set in the initial <tt>PositionHint</tt>. The hint for
   * the next curve wrapped by the returned <tt>Symbol</tt> is always
   * a <tt>PositionHint</tt>.
   */
  public Symbol createSymbol(GraphPoint point, Hint hintFromPreviousPoint,
                             Hint hintFromPreviousCurve) {
    GraphPoint origin = new GraphPoint(null);
    GraphPoint position = null;
    if (hintFromPreviousCurve instanceof PositionHint) {
      origin = ((PositionHint) hintFromPreviousCurve).getOrigin();
      position = ((PositionHint) hintFromPreviousCurve).getPosition();
    }
    if (position == null) {
      if (_symbolFactory == null) {
        return new Symbol(new GraphicalComposite(null), hintFromPreviousPoint,
                          new PositionHint(origin, point));
      } else {
        return _symbolFactory.createSymbol(point, hintFromPreviousPoint,
                                           new PositionHint(origin, point));
      }
    } else {
      double xError = point.getX() - origin.getX();
      double yError = point.getY() - origin.getY();
      GraphicalComposite errorBars = new GraphicalComposite(null);
      if (xError > 0) {
        errorBars.addElement(new Rectangle(position, 2 * xError, _size, 
                                           _attributes));
      }
      if (yError > 0) {
        errorBars.addElement(new Rectangle(position, _size, 2 * yError, 
                                           _attributes));
      }
      return new Symbol(errorBars, hintFromPreviousPoint, 
                        new PositionHint(origin, null));
    }
  }
}
