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
package jcckit.util;

import java.awt.Color;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;

/**
 *  Read-only class for hierarchically organized key-value pairs.
 *  The key is always a string. The following value types are
 *  supported:
 *  <ul><li><tt>String</tt>
 *      <li><tt>boolean</tt>
 *      <li><tt>int</tt>
 *      <li><tt>double</tt>
 *      <li><tt>double[]</tt>
 *      <li><tt>Color</tt>
 *      <li><tt>ConfigParameters</tt>
 *  </ul>
 *  <p>
 *  In accordance with the Strategy design pattern the retrieval of
 *  a key-value pair is delegated to an instance of
 *  {@link ConfigData}.
 *
 *  @author Franz-Josef Elmer
 */
public class ConfigParameters {
  private final ConfigData _configData;

  /** Creates an instance from the specified <tt>ConfigData</tt> object. */
  public ConfigParameters(ConfigData configData) {
    _configData = configData;
  }

  /**
   * Returns the full key.
   * @return the path concatenated with <tt>key</tt>.
   * @see ConfigData#getFullKey
   */
  public String getFullKey(String key) {
    return _configData.getFullKey(key);
  }

  /**
   * Returns the string value associated with the specified key.
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @return the corresponding value. Will always be not <tt>null</tt>.
   * @throws IllegalArgumentException if no value exists for <tt>key</tt>.
   *         The exception message is the full key.
   */
  public String get(String key) {
    String result = _configData.get(key);
    if (result == null) {
      throw new IllegalArgumentException(getFullKey(key));
    }
    return result;
  }

  /**
   * Returns the string value associated with the specified key or 
   * <tt>defaultValue</tt> if undefined.
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @param defaultValue The default value. Can be <tt>null</tt>.
   * @return the corresponding value or <tt>defaultValue</tt>.
   */
  public String get(String key, String defaultValue) {
    String result = _configData.get(key);
    if (result == null) {
      result = defaultValue;
    }
    return result;
  }

  /**
   * Returns the boolean associated with the specified key.
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @return <tt>true</tt> if the value is "true" otherwise <tt>false</tt>.
   * @throws IllegalArgumentException if no value exists for <tt>key</tt>.
   *         The exception message is the full key.
   * @throws NumberFormatException if the value is neither "true" nor "false".
   */
  public boolean getBoolean(String key) {
    return parseBoolean(get(key), key);
  }
  
  /**
   * Returns the boolean associated with the specified key.
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @param defaultValue The default value. Can be <tt>null</tt>.
   * @return <tt>true</tt> if the value is "true" otherwise <tt>false</tt>.
   * @throws NumberFormatException if the value is neither "true" nor "false".
   */
  public boolean getBoolean(String key, boolean defaultValue) {
    String value = _configData.get(key);
    return value == null ? defaultValue : parseBoolean(value, key);
  }
  
  private boolean parseBoolean(String value, String key) {
    if (value.equals("true")) {
      return true;
    } else if (value.equals("false")) {
      return false;
    } else {
      throw createNumberFormatException("boolean", value, key);
    }
  }
  
  private NumberFormatException createNumberFormatException(String text, 
                                                            String value, 
                                                            String key) {
    return new NumberFormatException("Not a " + text + ": " + getFullKey(key) 
                                     + " = " + value);
  }

  /**
   * Returns the integer associated with the specified key.
   * The value can be either 
   * <ul><li>a decimal number (starting with a non-zero digit), 
   *     <li>a hexadecimal number (starting with <tt>0x</tt>), or 
   *     <li>an octal number (starting with zero).
   * </ul>
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @return the integer value.
   * @throws IllegalArgumentException if no value exists for <tt>key</tt>.
   *         The exception message is the full key.
   * @throws NumberFormatException if the value is not a number.
   *         The exception message contains the full key and the invalid value.
   */
  public int getInt(String key) {
    return parseInt(get(key), key);
  }

  /**
   * Returns the integer associated with the specified key or 
   * <tt>defaultValue</tt> if no key-value pair exists for the specified key.
   * The value can be either 
   * <ul><li>a decimal number (starting with a non-zero digit), 
   *     <li>a hexadecimal number (starting with <tt>0x</tt>), or 
   *     <li>an octal number (starting with zero).
   * </ul>
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @param defaultValue The default value. Can be <tt>null</tt>.
   * @return the integer value.
   * @throws NumberFormatException if the value exists but is not a number.
   *         The exception message contains the full key and the invalid value.
   */
  public int getInt(String key, int defaultValue) {
    String value = _configData.get(key);
    return value == null ? defaultValue : parseInt(value, key);
  }

  private int parseInt(String value, String key) {
    try {
      return Integer.decode(value).intValue();
    } catch (NumberFormatException e) {
      throw createNumberFormatException("number", value, key);
    }
  }

  /**
   * Returns the double associated with the specified key.
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @return the double value.
   * @throws IllegalArgumentException if no value exists for <tt>key</tt>.
   *         The exception message is the full key.
   * @throws NumberFormatException if the value is not a valid number.
   *         The exception message contains the full key and the invalid value.
   */
  public double getDouble(String key) {
    return parseDouble(get(key), key);
  }

  /**
   * Returns the double associated with the specified key or 
   * <tt>defaultValue</tt> if no key-value pair exists for the specified key.
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @param defaultValue The default value. Can be <tt>null</tt>.
   * @return the double value.
   * @throws NumberFormatException if the value exists but is not a valid 
   *         number.
   *         The exception message contains the full key and the invalid value.
   */
  public double getDouble(String key, double defaultValue) {
    String value = _configData.get(key);
    return value == null ? defaultValue : parseDouble(value, key);
  }

  private double parseDouble(String value, String key) {
    try {
      return new Double(value).doubleValue();
    } catch (NumberFormatException e) {
      throw createNumberFormatException("number", value, key);
    }
  }

  /**
   * Returns the array of doubles associated with the specified key.
   * The numbers are separated by whitespaces.
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @return the array of double values.
   * @throws IllegalArgumentException if no value exists for <tt>key</tt>.
   *         The exception message is the full key.
   * @throws NumberFormatException if the value exists but is not a
   *         sequence of number. The exception message contains
   *         the full key and the invalid value.
   */
  public double[] getDoubleArray(String key) {
    return parseDoubleArray(get(key), key);
  }

  /**
   * Returns the array of doubles associated with the specified key
   * or <tt>defaultValue</tt> if no key-value pair exists for
   * the specified key. The numbers are separated by whitespaces.
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @param defaultValue The default value. Can be <tt>null</tt>.
   * @return the array of double values.
   * @throws NumberFormatException if the value exists but is not a
   *         sequence of number. The exception message contains
   *         the full key and the invalid value.
   */
  public double[] getDoubleArray(String key, double[] defaultValue) {
    String value = _configData.get(key);
    return value == null ? defaultValue : parseDoubleArray(value, key);
  }

  private double[] parseDoubleArray(String value, String key) {
    try {
      StringTokenizer tokenizer = new StringTokenizer(value);
      double[] result = new double[tokenizer.countTokens()];
      for (int i = 0; i < result.length; i++) {
        result[i] = new Double(tokenizer.nextToken()).doubleValue();
      }
      return result;
    } catch (NumberFormatException e) {
      throw createNumberFormatException("sequence of numbers", value, key);
    }
  }

  /**
   * Returns the color associated with the specified key.
   * The color is coded as 
   * <ul><li>a decimal number (starting with a non-zero digit), 
   *     <li>a hexadecimal number (starting with <tt>0x</tt>), or 
   *     <li>an octal number (starting with zero).
   * </ul>
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @return the color.
   * @throws NumberFormatException if the value exists but is not a number.
   *         The exception message contains the full key and the invalid value.
   */
  public Color getColor(String key) {
    return parseColor(get(key), key);
  }

  /**
   * Returns the color associated with the specified key or the specified
   * default value if no key-value pair exists for the specified key.
   * The color is coded as 
   * <ul><li>a decimal number (starting with a non-zero digit), 
   *     <li>a hexadecimal number (starting with <tt>0x</tt>), or 
   *     <li>an octal number (starting with zero).
   * </ul>
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @param defaultValue The default value. Can be <tt>null</tt>.
   * @return the color or <tt>null</tt> if the value is an empty string.
   * @throws NumberFormatException if the value exists but is not a number.
   *         The exception message contains the full key and the invalid value.
   */
  public Color getColor(String key, Color defaultValue) {
    String value = _configData.get(key);
    return value == null ? defaultValue : parseColor(value, key);
  }

  private Color parseColor(String value, String key) {
    try {
      return value.length() == 0 ? null : decodeInternal(value);
    } catch (NumberFormatException e) {
      throw createNumberFormatException("number", value, key);
    }
  }

private Color decodeInternal(String value) {
	if (HtmlColorUtils.getColorIfValid(value)!=null) {
		  return new ColorMapperIdentity().getMappedColor(HtmlColorUtils.getColorIfValid(value));
	}
	return Color.decode(value);
}
  
  /**
   * Returns the child node associated with the specified key.
   * This method returns in any case a non-<tt>null</tt> result.
   * @param key The (relative) key. <tt>null</tt> is not allowed.
   * @return the corresponding child node which may be empty.
   */
  public ConfigParameters getNode(String key) {
    return new ConfigParameters(_configData.getNode(key));
  }
}
