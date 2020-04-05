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

import java.util.StringTokenizer;

import jcckit.util.ConfigParameters;
import jcckit.util.TicLabelFormat;

/**
 * Map of number intervals onto a text label. The map is defined by a
 * map description string provided by configuration data.
 * <p>
 * The map description is a list
 * of conditions separated by ';'. The conditions are tested from left to
 * right until a condition is fulfilled for the tic value. If no condition
 * is fullfilled a '?' will be returned.
 * <p>
 * A condition description has one of the following forms:
 * <pre><tt><i>&lt;label&gt;</i></tt></pre>
 * <pre><tt><i>&lt;number&gt;</i>=<i>&lt;label&gt;</i></tt></pre>
 * <pre><tt><i>&lt;number1&gt;</i>:<i>&lt;number2&gt;</i>=<i>&lt;label&gt;</i></tt></pre>
 * <p>
 * The first type of condition is always fulfilled. It will return
 * <tt><i>&lt;label&gt;</i></tt>. This is a kind of else condtion
 * which is put at the end of the condition list.
 * <p>
 * The second form maps a particular number onto a label. In order to be
 * equal with the sepcified number the tic value should not deviate more
 * than 1 ppm (part per millions) from <tt><i>&lt;number&gt;</i>.
 * <p>
 * The third form maps an interval onto a label. The condition reads
 * <p>
 * <tt><i>&lt;number1&gt;</i></tt>&nbsp;&lt;=&nbsp;tic&nbsp;label&nbsp;&lt;&nbsp;<tt><i>&lt;number2&gt;</i></tt>
 * <p>
 * Examples:
 * <pre><tt>
 * 1=monday;2=tuesday;3=wednesday;4=thursday;5=friday;6=saturday;7=sunday 
 * 0.5:1.5=I; 1.5:2.5 = II; 2.5:3.5 = III; the rest 
 * </tt></pre>
 * 
 * @author Franz-Josef Elmer
 */
public class TicLabelMap implements TicLabelFormat {
  public static final String MAP_KEY = "map";
  
  private static class MapItem {
    private double _min = Double.MIN_VALUE;
    private double _max = Double.MAX_VALUE;
    private final String label;
    public MapItem(String item) {
      int index = item.indexOf('=');
      if (index < 0) {
        label = item;
      } else {
        label = item.substring(index + 1).trim();
        item = item.substring(0, index).trim();
        index = item.indexOf(':');
        if (index < 0) {
          _min = new Double(item).doubleValue();
          _max = _min == 0 ? Double.MIN_VALUE : _min * 1.000001d;
          _min = _min * 0.999999d;
          if (_min > _max) {
            double z = _min;
            _min = _max;
            _max = z;
          }
        } else {
          _min = new Double(item.substring(0, index)).doubleValue();
          _max = new Double(item.substring(index + 1)).doubleValue();
        }
      }
    }
    public boolean isInside(double value) {
      return value >= _min && value < _max; 
    }
  }
  
  private final MapItem[] _map;
  
  /**
   * Creates an instance from the specified configuration parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>map</tt></td>
   *     <td><tt>String</tt></td><td>yes</td>
   *     <td>Map description as explained above.</td></tr>
   * </table>
   */
  public TicLabelMap(ConfigParameters config) {
    StringTokenizer tokenizer = new StringTokenizer(config.get(MAP_KEY), ";");
    _map = new MapItem[tokenizer.countTokens()];
    for (int i = 0; i < _map.length; i++)
    {
      String item = tokenizer.nextToken();
      try {
        _map[i] = new MapItem(item.trim());
      } catch (NumberFormatException e) {
        throw new NumberFormatException("Item '" + item + "' of " 
            + config.getFullKey(MAP_KEY) + " has an invalid number.");
      }
    }
  }
  
  /**
   * Maps the specified tic value onto a text label in accordance
   * with the map description.
   */
  public String form(double ticValue) {
    String result = "?";
    for (int i = 0; i < _map.length; i++) {
      if (_map[i].isInside(ticValue)) {
        result = _map[i].label;
        break;
      }
    }
    return result;
  }
}
