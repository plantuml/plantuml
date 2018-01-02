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

import jcckit.data.DataPoint;
import jcckit.graphic.ClippingRectangle;
import jcckit.graphic.ClippingShape;
import jcckit.graphic.GraphPoint;
import jcckit.graphic.GraphicAttributes;
import jcckit.graphic.GraphicalComposite;
import jcckit.graphic.GraphicalElement;
import jcckit.graphic.LineAttributes;
import jcckit.graphic.Polygon;
import jcckit.graphic.Text;
import jcckit.transformation.CartesianTransformation;
import jcckit.transformation.Transformation;
import jcckit.util.ConfigParameters;

/**
 *  A Cartesian coordinate system. One or both axes can be logarithmic.
 *
 *  @author Franz-Josef Elmer
 */
public class CartesianCoordinateSystem implements CoordinateSystem {
  /** Configuration parameter key. */
  public static final String ORIGIN_KEY = "origin",
                             X_AXIS_KEY = "xAxis",
                             Y_AXIS_KEY = "yAxis";

  private final CartesianTransformation _transformation;
  private final GraphicalComposite _view;
  private final ClippingRectangle _clippingRectangle;

  /**
   * Creates an instance from the specified configuration parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>origin = 0.15,&nbsp;0.1</tt></td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>Position (in device-independent coordinates) of the lower-left 
   *         corner of the axis box.</td></tr>
   * <tr><td><tt>xAxis</tt></td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Parameters defining the x-axis. For definitions and default 
   *         values see {@link AxisParameters#createXAxis
   *         AxisParameters.createXAxis()}.</td></tr>
   * <tr><td><tt>yAxis</tt></td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Parameters defining the y-axis. For definitions and default 
   *         values see {@link AxisParameters#createYAxis
   *         AxisParameters.createYAxis()}.</td></tr>
   * </table>
   */
  public CartesianCoordinateSystem(ConfigParameters config) {
    this(new GraphPoint(config.getDoubleArray(ORIGIN_KEY,
                                              new double[] {0.15, 0.1})),
         AxisParameters.createXAxis(config.getNode(X_AXIS_KEY)),
         AxisParameters.createYAxis(config.getNode(Y_AXIS_KEY)));
  }

  /**
   * Creates an instance for the specified origin and parameters
   * of both axes.
   * @param origin Position (in device-independent coordinates) of the 
   *        lower-left corner of the axis box.
   * @param xAxisParameters Parameters of the x-axis.
   * @param yAxisParameters Parameters of the y-axis.
   */
  public CartesianCoordinateSystem(GraphPoint origin,
                                   AxisParameters xAxisParameters,
                                   AxisParameters yAxisParameters) {
    double x = origin.getX();
    double y = origin.getY();
    _transformation = new CartesianTransformation(xAxisParameters.logScale,
                                                  yAxisParameters.logScale,
        new DataPoint(xAxisParameters.minimum, yAxisParameters.minimum),
        new GraphPoint(x, y),
        new DataPoint(xAxisParameters.maximum, yAxisParameters.maximum),
        new GraphPoint(x + xAxisParameters.axisLength,
                       y + yAxisParameters.axisLength));
    _clippingRectangle = new ClippingRectangle(x, y,
                                               x + xAxisParameters.axisLength,
                                               y + yAxisParameters.axisLength);
    _view = new GraphicalComposite(null);
    createView(origin, xAxisParameters, yAxisParameters);
  }

  /** Creates the graphical representation of this coordinate system. */
  private void createView(GraphPoint origin,
                          AxisParameters xAxisParameters,
                          AxisParameters yAxisParameters) {
    double x0 = origin.getX();
    double x1 = x0 + xAxisParameters.axisLength;
    double y0 = origin.getY();
    double y1 = y0 + yAxisParameters.axisLength;
    GraphPoint lowerLeftCorner = new GraphPoint(x0, y0);
    GraphPoint upperLeftCorner = new GraphPoint(x0, y1);
    GraphPoint lowerRightCorner = new GraphPoint(x1, y0);
    GraphPoint upperRightCorner = new GraphPoint(x1, y1);
    LineAttributes xLineAttributes = xAxisParameters.axisAttributes;
    LineAttributes yLineAttributes = yAxisParameters.axisAttributes;
    createTicsAndGrid(true, y0, y1, xAxisParameters);
    createTicsAndGrid(false, x0, x1, yAxisParameters);
    addLine(lowerLeftCorner, lowerRightCorner, xLineAttributes);
    addLine(lowerLeftCorner, upperLeftCorner, yLineAttributes);
    addLine(upperLeftCorner, upperRightCorner, xLineAttributes);
    addLine(lowerRightCorner, upperRightCorner, yLineAttributes);
    createLabel(0.5 * (x0 + x1), y0, xAxisParameters);
    createLabel(x0, 0.5 * (y0 + y1), yAxisParameters);
  }

  private void createLabel(double x, double y, AxisParameters parameters) {
    if (parameters.axisLabel.length() > 0) {
      _view.addElement(new Text(
          new GraphPoint(x + parameters.axisLabelPosition.getX(),
                         y + parameters.axisLabelPosition.getY()),
          parameters.axisLabel, parameters.axisLabelAttributes));
    }
  }

  private void createTicsAndGrid(boolean isXAxis, double low, double high,
                                 AxisParameters parameters) {
    double[] tics = parameters.calculateTics();
    int offIndex = isXAxis ? 1 : 0;
    double[] point = new double[2]; // helper array
    for (int i = 0; i < tics.length; i++) {
      point[1 - offIndex] = tics[i];
      point[offIndex] = 1;
      GraphPoint gPoint1 = 
          _transformation.transformToGraph(new DataPoint(point[0], point[1]));
      point[0] = gPoint1.getX();
      point[1] = gPoint1.getY();
      point[offIndex] = high;
      gPoint1 = new GraphPoint(point[0], point[1]);
      point[offIndex] += parameters.ticLength;
      addLine(gPoint1, new GraphPoint(point[0], point[1]), 
                                      parameters.ticAttributes);
      point[offIndex] = low;
      GraphPoint gPoint2 = new GraphPoint(point[0], point[1]);
      if (parameters.grid) {
        addLine(gPoint1, gPoint2, parameters.gridAttributes);
      }
      point[offIndex] -= parameters.ticLength;
      addLine(gPoint2, new GraphPoint(point[0], point[1]), 
                                      parameters.ticAttributes);
      if (parameters.ticLabelFormat != null) {
        point[offIndex] += parameters.ticLength;
        point[0] += parameters.ticLabelPosition.getX();
        point[1] += parameters.ticLabelPosition.getY();
        _view.addElement(new Text(new GraphPoint(point[0], point[1]),
                                  parameters.ticLabelFormat.form(tics[i]), 
                                  parameters.ticLabelAttributes));
      }
    }
  }

  private void addLine(GraphPoint point1, GraphPoint point2,
                       GraphicAttributes attributes) {
    Polygon line = new Polygon(attributes, false);
    line.addPoint(point1);
    line.addPoint(point2);
    _view.addElement(line);
  }

  /** 
   * Returns the graphical representation of the coordinate system.
   * In each call the same instance is returned.
   */ 
  public GraphicalElement getView() {
    return _view;
  }

  /** 
   * Returns the clipping rectangle of specified by the axis.
   * In each call the same instance is returned.
   */ 
  public ClippingShape getClippingShape() {
    return _clippingRectangle;
  }

  /** 
   * Returns the transformation of data coordinates into the device-independent
   * coordinates of the axis box.
   * In each call the same instance is returned.
   */ 
  public Transformation getTransformation() {
    return _transformation;
  }
}
