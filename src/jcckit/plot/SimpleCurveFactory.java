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
import java.util.StringTokenizer;

import jcckit.graphic.ClippingShape;
import jcckit.util.ConfigParameters;
import jcckit.util.PropertiesBasedConfigData;

/**
 * Factory for {@link SimpleCurve SimpleCurves}.
 * 
 * @author Franz-Josef Elmer
 */
public class SimpleCurveFactory implements CurveFactory {
  /** Configuration parameter key. */
  public static final String DEFINITIONS_KEY = "definitions";

  private ConfigParameters[] _configs = new ConfigParameters[] 
      {new ConfigParameters(new PropertiesBasedConfigData(new Properties()))};

  /**
   * Creates an instance from the specified configuration parameter.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>definitions = </tt><i>one empty <tt>ConfigParameters<tt> 
   *         instance</i></td>
   *     <td><tt>String[]</tt></td><td>no</td>
   *     <td>Keys of subtrees defining {@link ConfigParameters}
   *         used by the {@link SimpleCurve#SimpleCurve constructor} of 
   *         {@link SimpleCurve}.</td></tr>
   * </table>
   */
  public SimpleCurveFactory(ConfigParameters config) {
    String value = config.get(DEFINITIONS_KEY, null);
    if (value != null) {
      StringTokenizer tokenizer = new StringTokenizer(value);
      _configs = new ConfigParameters[tokenizer.countTokens()];
      for (int i = 0; i < _configs.length; i++) {
        _configs[i] = config.getNode(tokenizer.nextToken());
      }
    }
  }

  /**
   * Creates an instance of {@link SimpleCurve}.
   * @param curveIndex Index of the curve. Will be used to select the 
   *        {@link ConfigParameters} object and the line attributes. 
   *        In addition it will be used to calculate the y-coordinate 
   *        of the legend symbol.
   * @param numberOfCurves Number of curves. Will be needed to calculate 
   *        the y-coordinate of the legend symbol.
   * @param clippingShape The clipping shape.
   * @param legend The legend. Will be needed to create the legend symbol.
   */
  public Curve create(int curveIndex,  int numberOfCurves,
                      ClippingShape clippingShape, Legend legend) {
    return new SimpleCurve(_configs[curveIndex % _configs.length], curveIndex,
                           numberOfCurves, clippingShape, legend);
  }
}
