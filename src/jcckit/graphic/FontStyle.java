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

import java.util.Hashtable;

import jcckit.util.ConfigParameters;
import jcckit.util.FactoryException;

/**
 *  Font style constants.
 *  This class is based on the typesafe enumeration pattern.
 *
 *  @author Franz-Josef Elmer
 */
public class FontStyle {
  private static final Hashtable REPOSITORY = new Hashtable();
  static final String NORMAL_TXT = "normal",
                      BOLD_TXT = "bold",
                      ITALIC_TXT = "italic",
                      BOLD_ITALIC_TXT = "bold italic";
  /** Font style constant. */
  public static final FontStyle NORMAL = new FontStyle(NORMAL_TXT),
                                BOLD = new FontStyle(BOLD_TXT),
                                ITALIC = new FontStyle(ITALIC_TXT),
                                BOLD_ITALIC = new FontStyle(BOLD_ITALIC_TXT);

  private final String _description;

  /** Non-public constructor to control the number of instances. */
  private FontStyle(String description) {
    _description = description;
    REPOSITORY.put(description, this);
  }

  /**
   *  Returns from the specified configuration parameters the font style
   *  defined by the specified key or the specified default value.
   *  @param config Configuration parameters.
   *  @param key The key of the font style.
   *  @param defaultValue The default value.
   *  @return one of the four instances of <tt>FontStyle</tt>.
   *  @throws FactoryException if the value of the key-value pair denoted
   *          by <tt>key</tt> is neither <tt>normal</tt>, <tt>bold</tt>, 
   *          <tt>italic</tt>, nor <tt>bold italic</tt>,
   *          Note, that {@link FactoryException#getClassName()} 
   *          returns the invalid value.
   */
  public static FontStyle getFontStyle(ConfigParameters config, String key,
                                       FontStyle defaultValue) {
    FontStyle result = defaultValue;
    String value = config.get(key, null);
    if (value != null) {
      result = (FontStyle) REPOSITORY.get(value);
      if (result == null) {
        throw new FactoryException(config, key, "Invalid font style.");
      }
    }
    return result;
  }

  /** Returns a human readable description for pretty printing. */
  public String toString() {
    return _description;
  }
}
