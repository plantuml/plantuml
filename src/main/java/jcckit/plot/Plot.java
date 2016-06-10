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

import java.util.Vector;

import jcckit.data.DataCurve;
import jcckit.data.DataEvent;
import jcckit.data.DataListener;
import jcckit.data.DataPlot;
import jcckit.data.DataPoint;
import jcckit.graphic.ClippingShape;
import jcckit.graphic.GraphPoint;
import jcckit.graphic.GraphicalComposite;
import jcckit.graphic.GraphicalElement;
import jcckit.transformation.Transformation;
import jcckit.util.ConfigParameters;
import jcckit.util.Factory;

/**
 * A plot is determined by a {@link CoordinateSystem}, {@link Curve Curves}, 
 * an optional annotation layer and an optional {@link Legend}. When rendered
 * these components are draw in this order.
 * <p>
 * Registrated {@link PlotListener PlotListeners} will be informed
 * when the plot changes.
 * <p>
 * A {@link DataPlot} can be connected with a <tt>Plot</tt> instance.
 * This is done with the method {@link #connect connect()} which registrates
 * this <tt>Plot</tt> instance as
 * a {@link DataListener} at the connected <tt>DataPlot</tt>.
 * After an received {@link DataEvent DataEvents} has been handled 
 * the registrated <tt>PlotListeners</tt> will receive a 
 * {@link PlotEvent} of the type {@link PlotEventType#DATA_PLOT_CHANGED}.
 *
 * @author Franz-Josef Elmer
 */
public class Plot implements DataListener {
  /** Configuration parameter key. */
  public static final String COORDINATE_SYSTEM_KEY = "coordinateSystem",
                             CURVE_FACTORY_KEY = "curveFactory",
                             LEGEND_VISIBLE_KEY = "legendVisible",
                             LEGEND_KEY = "legend",
                             INITIAL_HINT_FOR_NEXT_CURVE_KEY
                                      = "initialHintForNextCurve";
  private final Vector _plotListeners = new Vector();
  private DataPlot _dataPlot;
  private final CurveFactory _curveFactory;
  private final Vector _curves = new Vector();
  private final Vector _nextCurveHints = new Vector();
  private final Hint _initialHintForNextCurve;
  private final Legend _legend;
  private final boolean _legendVisibility;

  private GraphicalElement _coordinateSystemView;
  private ClippingShape _clippingShape;
  private Transformation _transformation;
  private GraphicalElement _annotation;
  private GraphicalComposite _legendView = new GraphicalComposite(null);

  /**
   * Creates an instance from the specified configuration parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>coordinateSystem = </tt>{@link CartesianCoordinateSystem}</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Definition of the {@link CoordinateSystem}.</td></tr>
   * <tr><td><tt>curveFactory = </tt>{@link SimpleCurveFactory}</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Definition of the {@link CurveFactory}.</td></tr>
   * <tr><td><tt>initialHintForNextCurve = null</tt></td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Definition of the initial {@link Hint} which is needed by some
   *         {@link SymbolFactory SymbolFactories} like {@link BarFactory}.
   *         </td></tr>
   * <tr><td><tt>legend = </tt>default values of {@link Legend}</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Configuration parameters of a {@link Legend}.</td></tr>
   * <tr><td><tt>legendVisible = true</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>If <tt>true</tt> the {@link Legend} will be created.</td></tr>
   * </table>
   */
  public Plot(ConfigParameters config) {
    CoordinateSystem coordinateSystem = (CoordinateSystem) Factory.create(
        config.getNode(COORDINATE_SYSTEM_KEY),
        CartesianCoordinateSystem.class.getName());
    setCoordinateSystem(coordinateSystem);
    _curveFactory = (CurveFactory) Factory.create(
        config.getNode(CURVE_FACTORY_KEY),
        SimpleCurveFactory.class.getName());
    _initialHintForNextCurve = (Hint) Factory.createOrGet(
                    config.getNode(INITIAL_HINT_FOR_NEXT_CURVE_KEY), null);
    _legend = new Legend(config.getNode(LEGEND_KEY));
    _legendVisibility = config.getBoolean(LEGEND_VISIBLE_KEY, true);
  }

  /**
   * Sets the coordinate system. All curves will be regenerated and a 
   * {@link PlotEvent} of type {@link PlotEventType#COODINATE_SYSTEM_CHANGED}
   * will be fired.
   *  
   * @param coordinateSystem New coordinate system.
   */
  public void setCoordinateSystem(CoordinateSystem coordinateSystem)
  {
    _coordinateSystemView = coordinateSystem.getView();
    _clippingShape = coordinateSystem.getClippingShape();
    _transformation = coordinateSystem.getTransformation();
    if (_dataPlot != null)
    {
      generateCurves(_dataPlot);
    }
    notifyListeners(
        new PlotEvent(this, PlotEventType.COODINATE_SYSTEM_CHANGED, null));
  }

  /**
   * Adds the specified {@link PlotListener}. Does nothing if
   * already added.
   */
  public void addPlotListener(PlotListener listener) {
    if (!_plotListeners.contains(listener)) {
      _plotListeners.addElement(listener);
    }
  }

  /**
   * Removes the specfied {@link PlotListener}. Does nothing if
   * already removed.
   */
  public void removePlotListener(PlotListener listener) {
    _plotListeners.removeElement(listener);
  }

  /** 
   * Sends all registrated {@link PlotListener PlotListeners} 
   * the specified event.
   */
  protected void notifyListeners(PlotEvent event) {
    for (int i = 0, n = _plotListeners.size(); i < n; i++) {
      ((PlotListener) _plotListeners.elementAt(i)).plotChanged(event);
    }
  }

  /**
   * Connect the specified {@link DataPlot} with this instance.
   * <p>
   * If this <tt>Plot</tt> instance is already connected with a 
   * <tt>DataPlot</tt> the connection will be released and a
   * {@link PlotEvent} of the type {@link PlotEventType#DATA_PLOT_DISCONNECTED}
   * will be sent to all registrated {@link PlotListener PlotListeners}.
   * <p>
   * It registers itself at <tt>dataPlot</tt> and
   * all its {@link DataCurve DataCurves}.
   * <p>
   * Finally all curves will be generated and a <tt>PlotEvent</tt>
   * of the type {@link PlotEventType#DATA_PLOT_CONNECTED} will be transmitted.
   * @param dataPlot Data to be connected with this plot instance.
   *        Can be <tt>null</tt> in order to disconnect this instance from
   *        any <tt>DataPlot</tt>.
   */
  public void connect(DataPlot dataPlot) {
    if (_dataPlot != null) {
      _dataPlot.removeDataListener(this);
      notifyListeners(new PlotEvent(this, PlotEventType.DATA_PLOT_DISCONNECTED,
                                    _dataPlot));
    }
    _dataPlot = dataPlot;
    if (_dataPlot != null)
    {
      _dataPlot.addDataListener(this);
      generateCurves(_dataPlot);
      notifyListeners(new PlotEvent(this, PlotEventType.DATA_PLOT_CONNECTED,
                                    _dataPlot));
    }
  }
  
  /**
   * Transforms a point from device-independent coordinates into
   * data coordinates.
   * @param point Point in device-independent coordinates.
   * @return transform <tt>point</tt>.
   */
  public DataPoint transform(GraphPoint point) {
    return _transformation.transformToData(point);
  }

  /**
   * Creates a graphical representation of the complete plot.
   * @return <tt>GraphicalComposite</tt> containing the views of the
   * coordinate system, the curves, and optionally the legend (in this order).
   */
  public GraphicalComposite getCompletePlot() {
    GraphicalComposite result = new GraphicalComposite(null);
    result.addElement(_coordinateSystemView);
    GraphicalElement[] curves = getCurves();
    for (int i = 0; i < curves.length; i++) {
      result.addElement(curves[i]);
    }
    if (_annotation != null) {
      result.addElement(_annotation);
    }
    if (_legendVisibility) {
      result.addElement(getLegend());
    }
    return result;
  }

  /** Returns the view of the coordinate system. */
  public GraphicalElement getCoordinateSystem() {
    return _coordinateSystemView;
  }

  /** Returns the graphical representations of all curves. */
  public GraphicalElement[] getCurves() {
    synchronized (_curves) {
      GraphicalElement[] curves = new GraphicalElement[_curves.size()];
      for (int i = 0; i < curves.length; i++) {
        curves[i] = ((Curve) _curves.elementAt(i)).getView();
      }
      return curves;
    }
  }
  
  /**
   * Returns the annotation layer.
   * @return <tt>null</tt> if no annotation layer.
   */
  public GraphicalElement getAnnotation()
  {
    return _annotation;
  }
  
  /**
   * Sets the annotation layer.
   * @param annotation Any kind of graphics which will be drawn on the
   *        top of the curves but may be covered by the legend. 
   *        Can be <tt>null</tt>.
   */
  public void setAnnotation(GraphicalElement annotation)
  {
    _annotation = annotation;
  }

  /** Returns <tt>true</tt> if the legend is visible. */  
  public boolean isLegendVisible() {
    return _legendVisibility;
  }

  /** Returns the graphical representations of the legend. */
  public GraphicalElement getLegend() {
    return _legendView;
  }

  /**
   * Handles the received {@link DataEvent} and notifies 
   * {@link PlotListener PlotListeners} by an event of the type
   * {@link PlotEventType#DATA_CURVE_CHANGED} or
   * {@link PlotEventType#DATA_PLOT_CHANGED}. The following table shows what
   * this method does:
   * <table border=1 cellpadding=5>
   * <tr><th>Source of <tt>event</tt></th>
   *     <th>All hints for the next curve are <tt>null</tt>?</th>
   *     <th>Action</th><th>Type of sent {@link PlotEvent}</th></tr>
   * <tr><td>{@link DataCurve}</td><td>Yes</td><td>Recreate changed curve.<td>
   *     <td><tt>DATA_CURVE_CHANGED</tt></td></tr>
   * <tr><td>{@link DataCurve}</td><td>No</td><td>Recreate changed curve
   *         and all curves with large curve index.<td>
   *     <td><tt>DATA_PLOT_CHANGED</tt></td></tr>
   * <tr><td>{@link DataPlot}</td><td>-</td><td>Recreate all curves
   *         and {@link Legend} view.<td>
   *     <td><tt>DATA_PLOT_CHANGED</tt></td></tr>
   * </table>
   */
  public void dataChanged(DataEvent event) {
    Integer index = new Integer(0);
    PlotEventType type = PlotEventType.DATA_PLOT_CHANGED;
    synchronized (_curves) {
      int numberOfCurves = _curves.size();
      if (event.getContainer() instanceof DataCurve 
          && numberOfCurves == _dataPlot.getNumberOfElements()) {
        DataCurve curve = (DataCurve) event.getContainer();
        index = new Integer(curve.getContainer().getIndexOf(curve));
        type = PlotEventType.DATA_CURVE_CHANGED;
        fillCurve(index.intValue(), curve);
        if (index.intValue() < numberOfCurves - 1) {
          Vector curveHints 
              = (Vector) _nextCurveHints.elementAt(index.intValue());
          for (int i = 0, n = curveHints.size(); i < n; i++) {
            if (curveHints.elementAt(i) != null) {
              type = PlotEventType.DATA_PLOT_CHANGED;
              for (int j = index.intValue()+1; j < numberOfCurves; j++) {
                fillCurve(j, (DataCurve) _dataPlot.getElement(j));
              }
              break;
            }
          }
        }
      } else {
        generateCurves(_dataPlot);
      }
    }
    notifyListeners(new PlotEvent(Plot.this, type, index));
  } 

  /**
   * Generates all curves based on the specified data.
   * In addition the legend view is created.
   */
  private void generateCurves(DataPlot dataPlot) {
    synchronized (_curves) {
      _legendView = new GraphicalComposite(null);
      _legendView.addElement(_legend.getBox());
      _curves.setSize(0);
      _nextCurveHints.setSize(0);
      for (int i = 0, n = dataPlot.getNumberOfElements(); i < n; i++) {
        Curve curve = _curveFactory.create(i, n, _clippingShape, _legend);
        _curves.addElement(curve);
        _nextCurveHints.addElement(new Vector());
        DataCurve dataCurve = (DataCurve) dataPlot.getElement(i);
        _legendView.addElement(curve.getLegendSymbol());
        _legendView.addElement(
            _legend.createCurveTitle(i, n, dataCurve.getTitle()));
        fillCurve(i, dataCurve);
      }
    }
  }

  private void fillCurve(int curveIndex, DataCurve dataCurve) {
    Vector curveHints = (Vector) _nextCurveHints.elementAt(curveIndex);
    Curve curve = (Curve) _curves.elementAt(curveIndex);
    curve.removeAllPoints();
    for (int i = 0, n = dataCurve.getNumberOfElements(); i < n; i++) {
      setHintForNextCurve(curveHints, i, 
          curve.addPoint(_transformation.transformToGraph(
                                          (DataPoint) dataCurve.getElement(i)),
                         getHintForNextCurve(curveIndex - 1, i)));
    }
  }
  
  private Hint getHintForNextCurve(int curveIndex, int pointIndex) {
    Hint result = _initialHintForNextCurve;
    if (curveIndex >= 0) {
      Vector curveHints = (Vector) _nextCurveHints.elementAt(curveIndex);
      result = pointIndex < curveHints.size() ?
                   (Hint) curveHints.elementAt(pointIndex)
                   : getHintForNextCurve(curveIndex - 1, pointIndex);
    }
    return result;
  }
  
  private void setHintForNextCurve(Vector curveHints, int pointIndex, 
                                   Hint hint) {
    while (curveHints.size() <= pointIndex) {
      curveHints.addElement(_initialHintForNextCurve);
    }
    curveHints.setElementAt(hint, pointIndex);
  }
}
