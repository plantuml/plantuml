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

import java.awt.Color;

import jcckit.graphic.ClippingShape;
import jcckit.graphic.GraphPoint;
import jcckit.graphic.GraphicalComposite;
import jcckit.graphic.GraphicalElement;
import jcckit.graphic.LineAttributes;
import jcckit.graphic.Polygon;
import jcckit.graphic.ShapeAttributes;
import jcckit.util.ConfigParameters;
import jcckit.util.Factory;

/**
 *  A simple curve is the basic implementation of the {@link Curve} interface.
 * 
 *  @author Franz-Josef Elmer
 */
public class SimpleCurve implements Curve {
  /** Configuration parameter key. */
  public static final String SYMBOL_FACTORY_KEY = "symbolFactory",
                             WITH_LINE_KEY = "withLine",
                             SOFT_CLIPPING_KEY = "softClipping",
                             LINE_ATTRIBUTES_KEY = "lineAttributes",
                             INITIAL_HINT_FOR_NEXT_POINT_KEY
                                      = "initialHintForNextPoint";  
  private final ClippingShape _clippingShape;
  private final SymbolFactory _symbolFactory;
  private final GraphicalComposite _symbols;
  private final GraphicalComposite _completeCurve;
  private final GraphicalElement _legendSymbol;
  private final Hint _initialHintForNextPoint;
  private final Polygon _curve;
  private final boolean _softClipping;
  private Hint _hintForNextPoint;

  /**
   * Creates a new curve. The parameter <tt>config</tt> contains:
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>initialHintForNextPoint = null</tt></td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Definition of an initial {@link Hint} for the first curve point.
   *         </td></tr>
   * <tr><td><tt>lineAttributes = </tt>a {@link ShapeAttributes}
   *         instances with default values and line colors based on
   *         the formula <tt>Color.getHSBColor(curveIndex/6,1,0.8)</tt></td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Configuration parameters of an instances of 
   *         {@link jcckit.graphic.GraphicAttributes} for the 
   *         {@link Polygon Polygons} connecting curve points.</td></tr>
   * <tr><td><tt>symbolFactory = null</tt></td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Configuration parameters defining an instances of 
   *         {@link SymbolFactory} for the {@link Symbol Symbols} 
   *         decorating curve points.</td></tr>
   * <tr><td><tt>softClipping = true</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>If <tt>true</tt> no explicit clipping takes
   *         place but the symbol is not drawn if the corresponding curve
   *         point is outside the axis box.<br> 
   *         If <tt>false</tt> the symbol is
   *         drawn in any case but it may be clipped by the axis box.
   *         Soft-clipping should be set to <tt>false</tt> if the 
   *         symbols are not located around the curve point (like for bars).
   *         </td></tr>
   * <tr><td><tt>withLine = true</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>If <tt>true</tt> curve points are connected by a 
   *         {@link jcckit.graphic.Polygon}.</td></tr>
   * </table>
   * @param config Configuration parameters described above.
   * @param curveIndex Index of this curve in the collection of curves
   *        defining a {@link Plot}. 
   * @param numberOfCurves Number of curves in this collection.
   * @param clippingShape Clipping shape. Can be <tt>null</tt>. 
   * @param legend Legend. Will be used to calculate the legend symbol.
   * @throws IllegalArgumentException if <tt>symbolFactory == null</tt> and
   *         <tt>withLine == false</tt>.
   *
   */
  public SimpleCurve(ConfigParameters config, int curveIndex, 
                     int numberOfCurves, ClippingShape clippingShape, 
                     Legend legend) {
    _symbolFactory = (SymbolFactory) Factory.createOrGet(
        config.getNode(SYMBOL_FACTORY_KEY), null);
    boolean withLine = config.getBoolean(WITH_LINE_KEY, true);
    LineAttributes lineAttributes = (LineAttributes) Factory.createOrGet(
        config.getNode(LINE_ATTRIBUTES_KEY),
        new ShapeAttributes(null, Color.getHSBColor((curveIndex % 6) / 6f, 
                                                    1f, 0.8f), 
                            0, null));
    if (_symbolFactory != null || withLine) {
      _clippingShape = clippingShape;
      _completeCurve = new GraphicalComposite(null);
      if (withLine) {
        GraphicalComposite container = new GraphicalComposite(clippingShape);
        _curve = new Polygon(lineAttributes, false);
        container.addElement(_curve);
       _completeCurve.addElement(container);
      } else {
        _curve = null;
      }
      _softClipping = config.getBoolean(SOFT_CLIPPING_KEY, true);
      if (_symbolFactory != null) {
        _symbols = new GraphicalComposite(_softClipping ? null 
                                                        : clippingShape);
        _completeCurve.addElement(_symbols);
      } else {
        _symbols = null;
      }
    } else {
      throw new IllegalArgumentException(
          "Either a SymbolFactory must exist or withLines == true.");
    }
    _hintForNextPoint = _initialHintForNextPoint 
        = (Hint) Factory.createOrGet(
            config.getNode(INITIAL_HINT_FOR_NEXT_POINT_KEY), null);
    _legendSymbol = legend.createSymbol(curveIndex, numberOfCurves, 
                                        _symbolFactory, withLine, 
                                        lineAttributes);
  }

  /**
   * Returns the graphical representation of a curve. 
   * @return always the same instance.
   */
  public GraphicalElement getView() {
    return _completeCurve;
  }

  /** Returns the legend symbol. */
  public GraphicalElement getLegendSymbol() {
    return _legendSymbol;
  }

  /** Appends a new point to the curve if inside the clipping shape. */
  public Hint addPoint(GraphPoint point, Hint hintFromPreviousCurve) {
    if (_curve != null) {
      _curve.addPoint(point);
    }
    Hint hintForNextCurve = hintFromPreviousCurve;
    if (_symbolFactory != null) {
      Symbol symbol = _symbolFactory.createSymbol(point, _hintForNextPoint,
                                                  hintFromPreviousCurve);
      if (_clippingShape == null || !_softClipping 
          || _clippingShape.isInside(point)) {
        _symbols.addElement(symbol.getSymbol());
      }
      _hintForNextPoint = symbol.getHintForNextPoint();
      hintForNextCurve = symbol.getHintForNextCurve();
    }
    return hintForNextCurve;
  }

  public void removeAllPoints() {
    if (_curve != null) {
      _curve.removeAllPoints();
    }
    if (_symbols != null) {
      _symbols.removeAllElements();
    }
    _hintForNextPoint = _initialHintForNextPoint;
  }
}
