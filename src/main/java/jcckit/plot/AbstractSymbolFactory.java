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
import jcckit.graphic.GraphicalElement;
import jcckit.util.ConfigParameters;
import jcckit.util.Factory;

/**
 * Abstract superclass of all {@link SymbolFactory SymbolFactories}.
 * Subclasses have to implement {@link #createPlainSymbol createPlainSymbol()}.
 *
 * @author Franz-Josef Elmer
 */
public abstract class AbstractSymbolFactory implements SymbolFactory {
  /** Size of all symbols. */
  protected final double _size;
  
  /** Attributes of all symbols. */
  protected final GraphicAttributes _attributes;

  /**
   * Creates an instance from the specified configuration parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>size = </tt>0.01</td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Size of the symbol in device-independent units.</td></tr>
   * <tr><td><tt>attributes</tt></td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Configuration parameters for the attributes of the symbol.
   *         <tt>className</tt> has to be a class which is an instance of
   *         {@link GraphicAttributes}.</td></tr>
   * </table>
   */
  public AbstractSymbolFactory(ConfigParameters config) {
    _size = config.getDouble(SIZE_KEY, DEFAULT_SIZE);
    _attributes = (GraphicAttributes) Factory.createOrGet(
                                        config.getNode(ATTRIBUTES_KEY), null);
  }

  /** 
   * Creates a symbol.
   * Evaluate <tt>hintFromPreviousPoint</tt> if it is a {@link AttributesHint}.
   * Calls {@link #createSymbol(GraphPoint, GraphicAttributes, Hint, Hint)}.
   * @param point Symbol position.
   * @param hintFromPreviousPoint Hint from the previous point.
   * @param hintFromPreviousCurve Hint from the previous curve.
   */
  public Symbol createSymbol(GraphPoint point, Hint hintFromPreviousPoint,
                             Hint hintFromPreviousCurve) {
    GraphicAttributes attributes = _attributes;
    Hint hintForNextPoint = hintFromPreviousPoint;
    if (hintFromPreviousPoint instanceof AttributesHint) {
      attributes = ((AttributesHint) hintFromPreviousPoint).getAttributes();
      hintForNextPoint 
          = ((AttributesHint) hintFromPreviousPoint).getNextHint();
    }
    return createSymbol(point,  attributes, hintForNextPoint,
                        hintFromPreviousCurve);
  }

  /**
   * Creates a symbol.
   * Uses {@link #createPlainSymbol createPlainSymbol()}.
   * @param point Symbol position.
   * @param attributes Symbol attributes.
   * @param hintForNextPoint Hint for the next point. Will be delivered
   *        unchanged in the return <tt>Symbol</tt> object.
   * @param hintFromPreviousCurve Hint from the previous curve.
   *        Will be delivered unchanged in the return <tt>Symbol</tt> object.
   *        Subclasses may override this behavior.
   */
  protected Symbol createSymbol(GraphPoint point, GraphicAttributes attributes,
                                Hint hintForNextPoint, 
                                Hint hintFromPreviousCurve) {
    return new Symbol(createPlainSymbol(point, _size, attributes),
                      hintForNextPoint, hintFromPreviousCurve);
  }

  /**
   * Creates a symbol for the legend at the specified position.
   * Uses {@link #createPlainSymbol createPlainSymbol()} 
   * @param centerPosition Center position of the symbol.
   * @param size The size of the symbol. Will be ignored because the value
   *         given in the constructor will be used.
   */
  public GraphicalElement createLegendSymbol(GraphPoint centerPosition,
                                             double size) {
    return createPlainSymbol(centerPosition, _size, _attributes);
  }

  /**
   * Creates the graphical element of the plain symbol.
   * @param centerPosition Center position of the symbol.
   * @param size The size of the symbol.
   * @param attributes The attributes of the symbol.
   */
  protected abstract GraphicalElement createPlainSymbol(
      GraphPoint centerPosition, double size, GraphicAttributes attributes);
}
