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
package jcckit.graphic;

import jcckit.util.ConfigParameters;
import jcckit.util.FactoryException;

/**
 *  Anchor of a graphical element. There exist only the three
 *  instances {@link #LEFT_BOTTOM}, {@link #CENTER}, and
 *  {@link #RIGHT_TOP}.
 *  <p>
 *  The anchor factor can be used in a position formular. Its value
 *  for the three instances reads:
 *  <p>
 *  <center>
 *  <table border=1 cellpadding=5>
 *  <tr><th>Instance</th><th>Factor</th></tr>
 *  <tr><td><tt>LEFT_BOTTOM</tt></td><td>0</td></tr>
 *  <tr><td><tt>CENTER</tt></td><td>1</td></tr>
 *  <tr><td><tt>RIGHT_TOP</tt></td><td>2</td></tr>
 *  </table>
 *  </center>
 *
 *  @author Franz-Josef Elmer
 */
public class Anchor {
  /** Anchor constant. */
  public static final Anchor LEFT_BOTTOM = new Anchor(0),
                             CENTER = new Anchor(1),
                             RIGHT_TOP = new Anchor(2);
  private static final String LEFT_VALUE = "left",
                              RIGHT_VALUE = "right",
                              CENTER_VALUE = "center",
                              TOP_VALUE = "top",
                              BOTTOM_VALUE = "bottom";

  /**
   *  Returns form the specified configuration parameters the
   *  horizontal anchor defined by the specified key or the
   *  specified default value.
   *  @param config Configuration parameters.
   *  @param key The key of the anchor. <tt>null</tt> is not allowed.
   *  @param defaultValue The default value.
   *  @return one of the three instances of <tt>Anchor</tt>.
   *  @throws FactoryException if the value of <tt>key</tt> is
   *          neither <tt>left</tt>, <tt>center</tt>,
   *          nor <tt>right</tt>. 
   *          Note, that {@link FactoryException#getClassName()} 
   *          returns the invalid value.
   */
  public static Anchor getHorizontalAnchor(ConfigParameters config, String key,
                                           Anchor defaultValue) {
    Anchor result = defaultValue;
    String anchor = config.get(key, null);
    if (anchor != null) {
      if (anchor.equals(LEFT_VALUE)) {
        result = Anchor.LEFT_BOTTOM;
      } else if (anchor.equals(CENTER_VALUE)) {
        result = Anchor.CENTER;
      } else if (anchor.equals(RIGHT_VALUE)) {
        result = Anchor.RIGHT_TOP;
      } else {
        throw new FactoryException(config, key, "Invalid horizontal anchor.");
      }
    }
    return result;
  }

  /**
   *  Returns form the specified configuration parameters the
   *  vertical anchor defined by the specified key or the
   *  specified default value.
   *  @param config Configuration parameters.
   *  @param key The key of the anchor. <tt>null</tt> is not allowed.
   *  @param defaultValue The default value.
   *  @return one of the three instances of <tt>Anchor</tt>.
   *  @throws FactoryException if the value of <tt>key</tt> is
   *          neither <tt>top</tt>, <tt>center</tt>,
   *          nor <tt>bottom</tt>.
   *          Note, that {@link FactoryException#getClassName()} 
   *          returns the invalid value.
   */
  public static Anchor getVerticalAnchor(ConfigParameters config, String key,
                                  Anchor defaultValue) {
    Anchor result = defaultValue;
    String anchor = config.get(key, null);
    if (anchor != null) {
      if (anchor.equals(BOTTOM_VALUE)) {
        result = Anchor.LEFT_BOTTOM;
      } else if (anchor.equals(CENTER_VALUE)) {
        result = Anchor.CENTER;
      } else if (anchor.equals(TOP_VALUE)) {
        result = Anchor.RIGHT_TOP;
      } else {
        throw new FactoryException(config, key, "Invalid vertcal anchor.");
      }
    }
    return result;
  }

  private final int _factor;

  private Anchor(int factor) {
    _factor = factor;
  }

  /** Returns the factor. */
  public int getFactor() {
    return _factor;
  }
}

