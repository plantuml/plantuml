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
package  jcckit.util;

import java.util.Vector;

/**
 *  A helper class for formatting numbers according to
 *  a <tt>printf</tt>-like format string. Each instance of
 *  this class is initialized by a format string for a
 *  single number.
 *
 *  @author Franz-Josef Elmer
 */
public class Format implements TicLabelFormat {
  /**
   *  Creates a new instance based of specified key-value pair of the
   *  specified configuration parameters.
   *  @param config Config parameters.
   *  @param key The key of the key-value pair in <tt>config</tt> containing
   *         the format string.
   *  @return <tt>null</tt> if undefined key-value pair or format string
   *          is an empty string.
   *  @throws FactoryException if the format string is invalid.
   */
  public static Format create(ConfigParameters config, String key) {
    Format result = null;
    String format = config.get(key, null);
    if (format != null && format.length() > 0) {
      try {
        result = new Format(format);
      } catch (Exception e) {
        throw new FactoryException(config, key, e);
      }
    }
    return result;
  }

  private final FormatElement[] _formatElements;
  private final Vector _staticParts;

  /**
   *  Creates an instance for the specified format string.
   *  The format string is an alternation of some static texts and
   *  format elements.
   *  A format element has to start with '%' and it must end with
   *  one of the following format descriptors:
   *  <table border=0 cellpadding=5>
   *    <tr><td><tt>d</tt></td>
   *        <td>decimal integer</td></tr>
   *    <tr><td><tt>o</tt></td>
   *        <td>octal integer</td></tr>
   *    <tr><td><tt>x</tt></td>
   *        <td>hex integer</td></tr>
   *    <tr><td><tt>f</tt></td>
   *        <td>floating point number with a fixed decimal point</td></tr>
   *    <tr><td><tt>e,&nbsp;E</tt></td>
   *        <td>floating point number in logarithmic format</td></tr>
   *    <tr><td><tt>g,&nbsp;G</tt></td>
   *        <td>floating point number rendered either in fixed-decimal
   *            format of logarithmic format depending on the size of
   *            the mantissa.</td></tr>
   *  </table>
   *  The characters between '%' and the decriptor are optional.
   *  They can be grouped into
   *  <ul><li>modifier<br>
   *          it is
   *          <ul><li>'-' if the formated result should be flushed left
   *              <li>'+' if the sign should be always appear
   *              <li>'0' if the leading space should be filled with zeros
   *          </ul>
   *      <li>width<br>
   *          a decimal number given the minimum number of characters
   *          of the result
   *      <li>precision
   *  </ul>
   *  A plain '%' is coded as '%%'.
   *  @param formatString The format string.
   *  @exception IllegalArgumentException if invalid format string.
   */
  public Format(String formatString) {
    _staticParts = new Vector();
    Vector formatElements = new Vector();
    StringBuffer part = new StringBuffer();
    boolean insideFormatElement = false;
    boolean atPercentSymbol = false;
    for (int i = 0, n = formatString.length(); i < n; i++) {
      char c = formatString.charAt(i);
      if (insideFormatElement) {
        part.append(c);
        if (FormatElement.DESCRIPTORS.indexOf(c) >= 0) {
          formatElements.addElement(new String(part));
          part.setLength(0);
          insideFormatElement = false;
        }
      } else if (atPercentSymbol) {
        atPercentSymbol = false;
        if (c != '%') {
          _staticParts.addElement(new String(part));
          part.setLength(0);
          insideFormatElement = true;
        }
        part.append(c);
        if (FormatElement.DESCRIPTORS.indexOf(c) >= 0) {
          formatElements.addElement(new String(part));
          part.setLength(0);
          insideFormatElement = false;
        }
      } else {
        if (c == '%') {
          atPercentSymbol = true;
        } else {
          part.append(c);
        }
      }
    }
    if (insideFormatElement) {
      formatElements.addElement(new String(part));
    } else {
      _staticParts.addElement(new String(part));
    }
    
    _formatElements = new FormatElement[formatElements.size()];
    for (int i = 0; i < _formatElements.length; i++) {
      _formatElements[i] 
          = new FormatElement((String) formatElements.elementAt(i));
    }
  }

  /**
   * Format a number.
   * If there are no format elements the numbers will be ignored. 
   * If there are more than one format elements the
   * additional format elements will be ignored and only the static parts
   * are taken.
   * @param number Number to be formated.
   * @return Formated number.
   */
  public String form(long number) {
    StringBuffer result = new StringBuffer();
    result.append(_staticParts.elementAt(0));
    if (_formatElements.length > 0) {
      _formatElements[0].form(result, number);
    }
    return appendRest(result);
  }
  
  /**
   * Format a number.
   * If there are no format elements the numbers will be ignored. 
   * If there are more than one format elements the
   * additional format elements will be ignored and only the static parts
   * are taken.
   * @param number Number to be formated.
   * @return Formated number.
   */
  public String form(double number) {
    StringBuffer result = new StringBuffer();
    result.append(_staticParts.elementAt(0));
    if (_formatElements.length > 0) {
      _formatElements[0].form(result, number);
    }
    return appendRest(result);
  }
  
  private String appendRest(StringBuffer buffer) {
    for (int i = 1, n = _staticParts.size(); i < n; i++) {
      buffer.append(_staticParts.elementAt(i));
    }
    return new String(buffer);
  }
  
  /**
   * Format an array of double numbers.
   * If there are less format elements than numbers the additional numbers
   * will be ignored. If there are less numbers than format elements the
   * additional format elements will be ignored and only the static parts
   * are taken.
   * @param numbers Numbers to be formated.
   * @return Formated numbers.
   */
  public String form(double[] numbers) {
    StringBuffer result = new StringBuffer();
    for (int i = 0, n = _staticParts.size(); i < n; i++) {
      result.append(_staticParts.elementAt(i));
      if (i < _formatElements.length && i < numbers.length) {
        _formatElements[i].form(result, numbers[i]);
      }
    }
    return new String(result);
  }
}
