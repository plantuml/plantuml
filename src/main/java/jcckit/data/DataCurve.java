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
package jcckit.data;

import jcckit.util.ConfigParameters;

/**
 *  A curve is a {@link DataContainer} of {@link DataPoint DataPoints}.
 *
 *  @author Franz-Josef Elmer
 */
public class DataCurve extends DataContainer implements DataElement {
  /** Config parameter key. */
  public static final String X_KEY = "x",
                             Y_KEY = "y",
                             TITLE_KEY = "title";

  private final String _title;
  private DataContainer _container;

  /** Creates an empty instance with the specified title. */
  public DataCurve(String title) {
    _title = title;
  }

  /**
   * Creates an instance from the specified config parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>title = </tt><i>empty string</i></td>
   *     <td><tt>String</tt></td><td>no</td>
   *     <td>Curve title.</td></tr>
   * <tr><td><tt>x</tt></td><td><tt>double[]</tt></td><td>yes</td>
   *     <td>x-coordinates of the curve points.</td></tr>
   * <tr><td><tt>y</tt></td><td><tt>double[]</tt></td><td>yes</td>
   *     <td>y-coordinates of the curve points.</td></tr>
   * </table>
   */
  public DataCurve(ConfigParameters config) {
    this(config.get(TITLE_KEY, ""));
    double[] xPoints = config.getDoubleArray(X_KEY);
    double[] yPoints = config.getDoubleArray(Y_KEY);
    int n = Math.min(xPoints.length, yPoints.length);
    for (int i = 0; i < n; i++) {
      addElement(new DataPoint(xPoints[i], yPoints[i]));
    }
  }

  /**
   * Returns the {@link DataPlot} containing this curve.
   */
  public DataContainer getContainer() {
    return _container;
  }


  /**
   * Sets the {@link DataPlot} where this is a curve of.
   */
  public void setContainer(DataContainer container) {
    _container = container;
  }

  /** Returns the title of this curve. */
  public String getTitle() {
    return _title;
  }

  /** 
   * Returns <tt>true</tt> if <tt>element</tt> is an instance of
   * {@link DataPoint}.
   */
  protected boolean isValid(DataElement element) {
    return element instanceof DataPoint;
  }
}
