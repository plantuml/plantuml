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

import java.util.StringTokenizer;

import jcckit.util.ConfigParameters;

/**
 *  A plot is a {@link DataContainer} of {@link DataCurve DataCurves}.
 *
 *  @author Franz-Josef Elmer
 */
public class DataPlot extends DataContainer {
  /** Config parameter key. */
  public static final String CURVES_KEY = "curves",
                             DATA_KEY = "data";

  /** Creates an empty instance. */
  public DataPlot() {}

  /**
   * Creates an instance from the specified config parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>curves</tt></td><td><tt>String[]</tt></td><td>yes</td>
   *     <td>List of keys denoting data curves. Each key refers to
   *         config parameters used in the 
   *         <a href="DataCurve.html#DataCurve(jcckit.util.ConfigParameters)">
   *         constructor</a> of {@link DataCurve}.</td></tr>
   * </table>
   */
  public DataPlot(ConfigParameters config) {
    StringTokenizer tokenizer = new StringTokenizer(config.get(CURVES_KEY));
    while (tokenizer.hasMoreTokens()) {
      addElement(new DataCurve(config.getNode(tokenizer.nextToken())));
    }
  }

  /**
   * Convenient method to create a <tt>DataPlot</tt> based on the specified
   * config parameters. It is a short-cut of 
   * <tt>new DataPlot(config.getNode("data"))</tt>.
   */
  public static DataPlot create(ConfigParameters config) {
    return new DataPlot(config.getNode(DATA_KEY));
  }

  /** 
   * Returns <tt>true</tt> if <tt>element</tt> is an instance of
   * {@link DataCurve}.
   */
  protected boolean isValid(DataElement element) {
    return element instanceof DataCurve;
  }
}

