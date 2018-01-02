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

import java.util.Properties;

import jcckit.graphic.BasicGraphicAttributes;
import jcckit.graphic.GraphPoint;
import jcckit.graphic.LineAttributes;
import jcckit.graphic.ShapeAttributes;
import jcckit.graphic.TextAttributes;
import jcckit.util.ConfigData;
import jcckit.util.ConfigParameters;
import jcckit.util.ConfigParametersBasedConfigData;
import jcckit.util.Factory;
import jcckit.util.Format;
import jcckit.util.PropertiesBasedConfigData;
import jcckit.util.TicLabelFormat;
import jcckit.util.Util;

/**
 * Helper class with various parameters defining an axis.
 * This helper class is used by {@link CartesianCoordinateSystem}
 * to set up a coordinate systems.
 * <p>
 * This class holds more than a dozen parameters. There are two factory
 * methods creating instances for x- and y-axis based on
 * {@link ConfigParameters}. They differ in their default parameters for 
 * those axes. 
 * <p>
 * Note, that there is a direct access of these parameters without getters
 * and setters but only for classes in the package <tt>jcckit.plot</tt>.
 * 
 * @author Franz-Josef Elmer
 */
public class AxisParameters {
  /** Configuration parameter key. */
  public static final String LOG_SCALE_KEY = "logScale",
                             MINIMUM_KEY = "minimum",
                             MAXIMUM_KEY = "maximum",
                             AXIS_LENGTH_KEY = "axisLength",
                             AXIS_ATTRIBUTES_KEY = "axisAttributes",
                             AXIS_LABEL_KEY = "axisLabel",
                             AXIS_LABEL_POSITION_KEY = "axisLabelPosition",
                             AXIS_LABEL_ATTRIBUTES_KEY = "axisLabelAttributes",
                             AUTOMATIC_TIC_CALCULATION_KEY
                                  = "automaticTicCalculation",
                             MINIMUM_TIC_KEY = "minimumTic",
                             MAXIMUM_TIC_KEY = "maximumTic",
                             NUMBER_OF_TICS_KEY = "numberOfTics",
                             TIC_LENGTH_KEY = "ticLength",
                             TIC_ATTRIBUTES_KEY = "ticAttributes",
                             TIC_LABEL_FORMAT_KEY = "ticLabelFormat",
                             TIC_LABEL_POSITION_KEY = "ticLabelPosition",
                             TIC_LABEL_ATTRIBUTES_KEY = "ticLabelAttributes",
                             GRID_KEY = "grid",
                             GRID_ATTRIBUTES_KEY = "gridAttributes";

  private static final double LN10 = Math.log(10);

  /** If <tt>true</tt> the scale is logarithmic otherwise linear. */
  boolean logScale;
  /** Minimum data value represented by the axis. */
  double minimum;
  /** Maximum data value represented by the axis. */
  double maximum;
  /** Length of the axis in device-independent graphical units. */
  double axisLength;
  /**
   *  Line attributes of the axis.
   *  Can be <tt>null</tt> which means default attributes.
   */
  LineAttributes axisAttributes;

  boolean automaticTicCalculation;
  double minimumTic;
  double maximumTic;
  int numberOfTics;
  /**
   *  Length of the tics in device-independent graphical units.
   *  If 0 no tics and tics label will be drawn.
   */
  double ticLength;
  /**
   *  Attributes of the tics.
   *  Can be <tt>null</tt> which means default attributes.
   */
  LineAttributes ticAttributes;
  /** Tic label formatter. */
  TicLabelFormat ticLabelFormat;
  /** Position of the tic label relative to the tic. */
  GraphPoint ticLabelPosition;
  /** Text attributes of the tic labels. */
  TextAttributes ticLabelAttributes;

  /** If <tt>true</tt> grid lines are drawn. */
  boolean grid;
  /**
   *  Attributes of the grid lines.
   *  Can be <tt>null</tt> which means default attributes.
   */
  LineAttributes gridAttributes;

  /** Axis label. */
  String axisLabel;
  /** Position of the axis label relative to the center of the axis. */
  GraphPoint axisLabelPosition;
  /** Text attributes of the axis label. */
  TextAttributes axisLabelAttributes;

  /**
   *  Calculate the tics based on <tt>minimumTic</tt>, <tt>maximumTic</tt>,
   *  and <tt>numberOfTics</tt>. If <tt>automaticTicCalculation == true</tt>
   *  appropriated values for these fields are calculated.
   */
  double[] calculateTics() {
    if (automaticTicCalculation) {
      calculateTicsParameters();
    }
    double[] result = new double[numberOfTics];
    if (numberOfTics > 0) {
      double b = Util.log(minimumTic, logScale);
      double a = Util.log(maximumTic, logScale);
      a = numberOfTics > 1 ? (a - b) / (numberOfTics - 1) : 0;
      for (int i = 0; i < result.length; i++) {
        result[i] = Util.exp(a * i + b, logScale);
      }
      result[0] = adjust(minimum, result[0]);
      result[numberOfTics - 1] = adjust(maximum, result[numberOfTics - 1]);
    }
    return result;
  }

  private void calculateTicsParameters() {
    double min = Math.min(minimum, maximum);
    double max = Math.max(minimum, maximum);
    if (logScale) {
      int minExponent = (int) (199.9999 + Math.log(min) / LN10) - 199;
      int maxExponent = (int) (200.0001 + Math.log(max) / LN10) - 200;
      minimumTic = Math.exp(LN10 * minExponent);
      maximumTic = Math.exp(LN10 * maxExponent);
      numberOfTics = maxExponent - minExponent + 1;
    } else {
      int baseExponent = (int) (199.69 + Math.log(max - min) / LN10) - 200;
      double base = 0.2 * Math.exp(LN10 * baseExponent);
      do 
      {
        base *= 5;
        int minInt = (int) (999999.999999 + min / base) - 999999;
        int maxInt = (int) (1000000.000001 + max / base) - 1000000;
        minimumTic = minInt * base;
        maximumTic = maxInt * base;
        numberOfTics = maxInt - minInt + 1;
      } while (numberOfTics > 11);
    }
  }

  /**
   * Returns <tt>adjustingValue</tt> if <tt>value</tt> is very close
   * to <tt>adjustingValue</tt>. Otherwise <tt>value</tt> is returned.
   */ 
  private static double adjust(double adjustingValue, double value) {
    return value != 0 && Math.abs(adjustingValue / value - 1) < 1e-11
                                              ? adjustingValue : value;
  }

  /** 
   * Returns a <tt>Properties</tt> object with those default parameters
   * which are common for x- and y-axis.
   */ 
  private static Properties createDefaultAxisProperties() {
    Properties p = new Properties();
    p.put(LOG_SCALE_KEY, "false");
    p.put(MINIMUM_KEY, "0");
    p.put(MAXIMUM_KEY, "1");
    p.put(AXIS_LENGTH_KEY, "0.8");
    p.put(AXIS_ATTRIBUTES_KEY + '/' + Factory.CLASS_NAME_KEY,
          ShapeAttributes.class.getName());
    p.put(AXIS_LABEL_KEY, "x");
    p.put(AXIS_LABEL_POSITION_KEY, "0 -0.05");
    p.put(AXIS_LABEL_ATTRIBUTES_KEY + '/' + Factory.CLASS_NAME_KEY,
          BasicGraphicAttributes.class.getName());
    p.put(AXIS_LABEL_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.HORIZONTAL_ANCHOR_KEY, "center");
    p.put(AUTOMATIC_TIC_CALCULATION_KEY, "true");
    p.put(TIC_LENGTH_KEY, "0.01");
    p.put(TIC_ATTRIBUTES_KEY + '/' + Factory.CLASS_NAME_KEY,
          ShapeAttributes.class.getName());
    p.put(TIC_LABEL_POSITION_KEY, "0 -0.01");
    p.put(TIC_LABEL_FORMAT_KEY, "%1.1f");
    p.put(TIC_LABEL_ATTRIBUTES_KEY + '/' + Factory.CLASS_NAME_KEY,
          BasicGraphicAttributes.class.getName());
    p.put(TIC_LABEL_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.HORIZONTAL_ANCHOR_KEY, "center");
    p.put(TIC_LABEL_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.VERTICAL_ANCHOR_KEY, "top");
    p.put(GRID_KEY, "false");
    p.put(GRID_ATTRIBUTES_KEY + '/' + Factory.CLASS_NAME_KEY,
          ShapeAttributes.class.getName());
    return p;
  }

  /**
   * Returns a <tt>Properties</tt> object of the default parameters for 
   * an x-axis. 
   */ 
  private static Properties createDefaultXAxisProperties() {
    Properties p = createDefaultAxisProperties();
    p.put(AXIS_LABEL_KEY, "x");
    p.put(AXIS_LABEL_POSITION_KEY, "0 -0.05");
    p.put(AXIS_LABEL_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.VERTICAL_ANCHOR_KEY, "top");
    p.put(TIC_LABEL_POSITION_KEY, "0 -0.01");
    p.put(TIC_LABEL_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.HORIZONTAL_ANCHOR_KEY, "center");
    p.put(TIC_LABEL_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.VERTICAL_ANCHOR_KEY, "top");
    return p;
  }

  /** 
   * Creates an x axis based on the specified configuration parameters.
   * All numbers (lengths, fontsizes, linethicknesses, etc.) are in
   * device-independent units.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>automaticTicCalculation = true</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>Has to be <tt>true</tt> if the tics should be calculated
   *         automatically.</td></tr>
   * <tr><td><tt>axisAttributes = </tt>default values of 
   *         {@link ShapeAttributes}</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Attributes of the axis box.</td></tr>
   * <tr><td><tt>axisLabel = x</tt></td>
   *     <td><tt>String</tt></td><td>no</td>
   *     <td>Axis label.</td></tr>
   * <tr><td><tt>axisLabelAttributes = </tt>default values of 
   *         {@link BasicGraphicAttributes} with a text anchor CENTER 
   *         TOP.</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Text attributes of axis label.</td></tr>
   * <tr><td><tt>axisLabelPosition = 0 -0.05</tt></td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>Position of the anchor of the axis 
   *         label relative to the center of the x-axis line.</td></tr>
   * <tr><td><tt>axisLength = 0.8</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Length of the x-axis.</td></tr>
   * <tr><td><tt>grid = false</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>If <tt>true</tt> grid lines will be drawn through the axis 
   *         tics.</td></tr>
   * <tr><td><tt>gridAttributes  = </tt>default values of 
   *         {@link ShapeAttributes}</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Attributes of the grid lines.</td></tr>
   * <tr><td><tt>logScale = false</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>If <tt>true</tt> the axis will be logarithmic. Otherwise
   *         the axis is linear.</td></tr>
   * <tr><td><tt>maximum = 1</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>The corresponding data value of one end of the axis.</td></tr>
   * <tr><td><tt>maximumTic = </tt>result from automatic calculation</td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>The corresponding data value of the tic nearest the maximum end
   *         of the axis.</td></tr>
   * <tr><td><tt>minimum = 0</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>The corresponding data value of one end of the axis.</td></tr>
   * <tr><td><tt>minimumTic = </tt>result from automatic calculation</td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>The corresponding data value of the tic nearest the minimum end
   *         of the axis.</td></tr>
   * <tr><td><tt>numberOfTics = </tt>result from automatic calculation</td>
   *     <td><tt>int</tt></td><td>no</td>
   *     <td>Number of tics. The tics between the minimum and maximum tic
   *         are spaced equidistantly.</td></tr>
   * <tr><td><tt>ticAttributes = </tt>default values of 
   *         {@link ShapeAttributes}</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Attributes of the tics.</td></tr>
   * <tr><td><tt>ticLabelAttributes = </tt>default values of 
   *         {@link BasicGraphicAttributes} with a text anchor CENTER 
   *         TOP.</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Text attributes of tic labels.</td></tr>
   * <tr><td><tt>ticLabelFormat = %1.1f</tt></td>
   *     <td><tt>String</tt> or <tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Defines rendering of the tic label. By default a 
   *         <tt>printf</tt>-like format string is given (see {@link Format}).
   *         Note, that an empty string means that tic labels are dropped.
   *         <p>
   *         For non-numerical rendering an implementation of a
   *         {@link TicLabelFormat} can be specified (e.g.
   *         {@link TicLabelMap}). Note, that a configuration sub tree with
   *         a <tt>className</tt> key-value pair overwrites any string
   *         definition.</td></tr>
   * <tr><td><tt>ticLabelPosition = 0 -0.01</tt></td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>Position of the anchor of the tic label relative to the
   *         tic position on the axis.</td></tr>
   * <tr><td><tt>ticLength = 0.01</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Length of the tics. Negative/positive values mean tics 
   *         inside/outside the box.</td></tr>
   * </table>
   */
  public static AxisParameters createXAxis(ConfigParameters config) {
    return createAxis(config, createDefaultXAxisProperties());
  }

  /**
   * Returns a <tt>Properties</tt> object of the default parameters for 
   * an x-axis. 
   */ 
  private static Properties createDefaultYAxisProperties() {
    Properties p = createDefaultAxisProperties();
    p.put(AXIS_LENGTH_KEY, "0.45");
    p.put(AXIS_LABEL_KEY, "y");
    p.put(AXIS_LABEL_POSITION_KEY, "-0.1 0");
    p.put(AXIS_LABEL_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.VERTICAL_ANCHOR_KEY, "bottom");
    p.put(AXIS_LABEL_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.ORIENTATION_ANGLE_KEY, "90");
    p.put(TIC_LABEL_POSITION_KEY, "-0.01 0");
    p.put(TIC_LABEL_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.HORIZONTAL_ANCHOR_KEY, "right");
    p.put(TIC_LABEL_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.VERTICAL_ANCHOR_KEY, "center");
    return p;
  }

  /** 
   * Creates an y axis based on the specified configuration parameters. 
   * All numbers (lengths, fontsizes, linethicknesses, etc.) are in
   * device-independent units.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>automaticTicCalculation = true</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>Has to be <tt>true</tt> if the tics should be calculated
   *         automatically.</td></tr>
   * <tr><td><tt>axisAttributes = </tt>default values of 
   *         {@link ShapeAttributes}</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Attributes of the axis box.</td></tr>
   * <tr><td><tt>axisLabel = y</tt></td>
   *     <td><tt>String</tt></td><td>no</td>
   *     <td>Axis label.</td></tr>
   * <tr><td><tt>axisLabelAttributes = </tt>default values of 
   *         {@link BasicGraphicAttributes} with a text anchor CENTER 
   *         BOTTOM and the text rotated by 90 degree.</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Text attributes of axis label.</td></tr>
   * <tr><td><tt>axisLabelPosition = -0.1 0</tt></td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>Position of the anchor of the axis 
   *         label relative to the center of the y-axis line.</td></tr>
   * <tr><td><tt>axisLength = 0.45</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Length of the y-axis.</td></tr>
   * <tr><td><tt>grid = false</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>If <tt>true</tt> grid lines will be drawn through the axis 
   *         tics.</td></tr>
   * <tr><td><tt>gridAttributes  = </tt>default values of 
   *         {@link ShapeAttributes}</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Attributes of the grid lines.</td></tr>
   * <tr><td><tt>logScale = false</tt></td>
   *     <td><tt>boolean</tt></td><td>no</td>
   *     <td>If <tt>true</tt> the axis will be logarithmic. Otherwise
   *         the axis is linear.</td></tr>
   * <tr><td><tt>maximum = 1</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>The corresponding data value of one end of the axis.</td></tr>
   * <tr><td><tt>maximumTic = </tt>result from automatic calculation</td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>The corresponding data value of the tic nearest the maximum end
   *         of the axis.</td></tr>
   * <tr><td><tt>minimum = 0</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>The corresponding data value of one end of the axis.</td></tr>
   * <tr><td><tt>minimumTic = </tt>result from automatic calculation</td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>The corresponding data value of the tic nearest the minimum end
   *         of the axis.</td></tr>
   * <tr><td><tt>numberOfTics = </tt>result from automatic calculation</td>
   *     <td><tt>int</tt></td><td>no</td>
   *     <td>Number of tics. The tics between the minimum and maximum tic
   *         are spaced equidistantly.</td></tr>
   * <tr><td><tt>ticAttributes = </tt>default values of 
   *         {@link ShapeAttributes}</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Attributes of the tics.</td></tr>
   * <tr><td><tt>ticLabelAttributes = </tt>default values of 
   *         {@link BasicGraphicAttributes} with a text anchor RIGHT CENTER.
   *         </td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Text attributes of tic labels.</td></tr>
   * <tr><td><tt>ticLabelFormat = %1.1f</tt></td>
   *     <td><tt>String</tt></td><td>no</td>
   *     <td>Defines rendering of the tic label. By default a 
   *         <tt>printf</tt>-like format string is given (see {@link Format}).
   *         Note, that an empty string means that tic labels are dropped.
   *         <p>
   *         For non-numerical rendering an implementation of a
   *         {@link TicLabelFormat} can be specified (e.g.
   *         {@link TicLabelMap}). Note, that a configuration sub tree with
   *         a <tt>className</tt> key-value pair overwrites any string
   *         definition.</td></tr>
   * <tr><td><tt>ticLabelPosition = -0.01 0</tt></td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>Position of the anchor of the tic label relative to the
   *         tic position on the axis.</td></tr>
   * <tr><td><tt>ticLength = 0.01</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Length of the tics. Negative/positive values mean tics 
   *         inside/outside the box.</td></tr>
   * </table>
   */
  public static AxisParameters createYAxis(ConfigParameters config) {
    return createAxis(config, createDefaultYAxisProperties());
  }

  private static AxisParameters createAxis(ConfigParameters config,
                                           Properties p) {
    ConfigData cd = new PropertiesBasedConfigData(p);
    ConfigParameters c = new ConfigParameters(cd);
    cd = new ConfigParametersBasedConfigData(config, c);
    c = new ConfigParameters(cd);

    AxisParameters a = new AxisParameters();
    a.logScale = c.getBoolean(LOG_SCALE_KEY);
    a.minimum = c.getDouble(MINIMUM_KEY);
    a.maximum = c.getDouble(MAXIMUM_KEY);
    a.axisLength = c.getDouble(AXIS_LENGTH_KEY);
    a.axisAttributes
        = (LineAttributes) Factory.create(c.getNode(AXIS_ATTRIBUTES_KEY));
    a.axisLabel = c.get(AXIS_LABEL_KEY);
    a.axisLabelPosition
        = new GraphPoint(c.getDoubleArray(AXIS_LABEL_POSITION_KEY));
    a.axisLabelAttributes = (TextAttributes) Factory.create(
                                c.getNode(AXIS_LABEL_ATTRIBUTES_KEY));
    a.ticLength = c.getDouble(TIC_LENGTH_KEY);
    a.automaticTicCalculation
        = c.getBoolean(AUTOMATIC_TIC_CALCULATION_KEY);
    if (!a.automaticTicCalculation) {
      a.calculateTicsParameters(); // calculate default parameters
      a.minimumTic = c.getDouble(MINIMUM_TIC_KEY, a.minimumTic);
      a.maximumTic = c.getDouble(MAXIMUM_TIC_KEY, a.maximumTic);
      a.numberOfTics = c.getInt(NUMBER_OF_TICS_KEY, a.numberOfTics);
    }
    a.ticAttributes
        = (LineAttributes) Factory.create(c.getNode(TIC_ATTRIBUTES_KEY));
    a.ticLabelFormat = createTicLabelFormat(c);
    a.ticLabelPosition
        = new GraphPoint(c.getDoubleArray(TIC_LABEL_POSITION_KEY));
    a.ticLabelAttributes = (TextAttributes) Factory.create(
                                c.getNode(TIC_LABEL_ATTRIBUTES_KEY));
    a.grid = c.getBoolean(GRID_KEY);
    a.gridAttributes
        = (LineAttributes) Factory.create(c.getNode(GRID_ATTRIBUTES_KEY));
    return a;
  }

  private static TicLabelFormat createTicLabelFormat(ConfigParameters c)
  {
    TicLabelFormat result = Format.create(c, TIC_LABEL_FORMAT_KEY);
    ConfigParameters node = c.getNode(TIC_LABEL_FORMAT_KEY);
    if (node.get(Factory.CLASS_NAME_KEY, null) != null) {
      result = (TicLabelFormat) Factory.create(node);
    }
    return result;
  }
}
