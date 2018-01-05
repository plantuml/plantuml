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

/**
 * 
 * 
 * @author Franz-Josef Elmer
 */
class FormatElement {
  /** All descriptor characters. */
  static final String DESCRIPTORS = "doxfeEgG";
  private static final String INT_DESCRIPTORS = "dox";
  private static final int INT_DESCRIPTOR = 0;
  private static final int FLOAT_DESCRIPTOR = 1;
  /**
   *  Calculate the integer power of a floating point number.
   *  @param n Exponent.
   *  @param x Number.
   *  @return <tt>x^n</tt>.
   */
  private static final double power(double x, int n) {
    return n < 0 ? 1.0 / power2(x, -n) : power2(x, n);
  }

  /** Calculate <tt>x^n</tt> recursively assuming <tt>n > 0</tt>. */
  private static final double power2(double x, int n) {
    switch (n) {
    case 0: return 1;
    case 1: return x;
    default:
      double p = power2(x, n / 2);
      return p * p * power2(x, n % 2);
    }
  }

  private final char _descriptor;
  private final int _descriptorType;
  private final double _tenToPrecision;
  private boolean _decimalPoint;
  private boolean _flushLeft;
  private boolean _leadingZeros;
  private boolean _alwaysSign;
  private int _width;
  private int _precision;

  /** Creates an instance for the specified format string. */
  FormatElement(String formatString) {
    int len = formatString.length() - 1;
    _descriptor = formatString.charAt(len);
    if (DESCRIPTORS.indexOf(_descriptor) < 0) {
      throw new IllegalArgumentException("Format element '" + formatString
          + "' does not ends with one of the following characters: "
          + DESCRIPTORS);
    }
    _descriptorType = INT_DESCRIPTORS.indexOf(_descriptor) >= 0
                                ? INT_DESCRIPTOR : FLOAT_DESCRIPTOR;
    if (formatString.length() > 1) {
      switch (formatString.charAt(0)) {
      case '-':
        _flushLeft = true;
        formatString = formatString.substring(1);
        break;
      case '0':
        _leadingZeros = true;
        formatString = formatString.substring(1);
        break;
      case '+':
        _alwaysSign = true;
        formatString = formatString.substring(1);
        break;
      }
      len = formatString.length() - 1;
      int index = formatString.indexOf('.');
      _decimalPoint = index >= 0;
      int last = _decimalPoint ? index : len;
      if (last > 0) {
        _width = Integer.parseInt(formatString.substring(0, last));
      }
      if (_decimalPoint) {
        index++;
        if (index < len) {
          _precision = Integer.parseInt(formatString.substring(index, len));
        }
      }
    }
    _tenToPrecision = power(10, _precision);
  }

  /**
   *  Format a number in accordance of the format string
   *  given at the initialisation of this instance.
   *  @param buffer Buffer to which the formated output will be appended.
   *  @param number Number to be formated.
   */
  public void form(StringBuffer buffer, long number) {
    if (_descriptorType == FLOAT_DESCRIPTOR) {
      form(buffer, (double) number);
    } else {
      // Format absolut value in the right base
      buffer.append(form(number < 0,
                         Long.toString(Math.abs(number),
                                _descriptor == 'o' ? 8
                                          : (_descriptor == 'x' ? 16 : 10)),
                         ""));
    }
  }

  /**
   *  Format a number in accordance of the format string
   *  given at the initialisation of this instance.
   *  @param buffer Buffer to which the formated output will be appended.
   *  @param number Number to be formated.
   */
  public void form(StringBuffer buffer, double number) {
    if (_descriptorType == INT_DESCRIPTOR) {
      form(buffer, (long) Math.floor(number + 0.5));
    } else if (_descriptor == 'f') {
      buffer.append(formF(number));
    } else if (_descriptor == 'e' || _descriptor == 'E') {
      buffer.append(formE(number));
    } else if (_descriptor == 'g' || _descriptor == 'G') {
      String formF = formF(number);
      String formE = formE(number);
      buffer.append(formF.length() > formE.length() ? formE : formF);
    } 
  }

  private String form(boolean negativeValue, String intPart, String fracPart) {
    int len = intPart.length() + fracPart.length();

    // Buffer holding the result
    StringBuffer result = new StringBuffer();
    int count = 0;

    // add sign if necessary
    if (_alwaysSign || negativeValue) {
      result.append(negativeValue ? '-' : '+');
      count++;
    }

    // add zeros  if necessary
    if (_leadingZeros) {
      for (int i = count + len; i < _width; i++) {
        result.append('0');
        count++;
      }
    }

    // add number
    result.append(intPart).append(fracPart);
    count += len;

    // add spaces if necessary
    if (_flushLeft) {
      for (; count < _width; count++) {
        result.append(' ');
      }
    } else {
      for (; count < _width; count++) {
        result.insert(0, ' ');
      }
    }

    return new String(result);
  }

  /** Format floating point number with exponent. */
  private String formE(double number) {
    // format absolute mantisse
    int exponent = 0;
    String zeros = "00000000000000000000000".substring(0, _precision + 1);
    if (number != 0) {
      exponent = (int) Math.floor(Math.log(Math.abs(number)) / Math.log(10));
      double mantisse = Math.floor(Math.abs(number * power(10.0,
                                            _precision - exponent)) + 0.5);
      if (mantisse >= 10 * _tenToPrecision) {
        exponent++;
        mantisse = Math.floor(Math.abs(number * power(10.0,
                                          _precision - exponent)) + 0.5);
      }
      zeros = Long.toString((long) mantisse);
    }

    // make fractional part
    StringBuffer fracPart = new StringBuffer();
    if (_decimalPoint) {
      fracPart.append('.').append(zeros.substring(1));
    }

    // make exponent
    fracPart.append(Character.isLowerCase(_descriptor) ? 'e': 'E')
            .append(exponent < 0 ? '-' : '+');
    exponent = Math.abs(exponent);
    for (int i = 0, n = fracPart.length(); i < 3; i++) {
      fracPart.insert(n, Character.forDigit(exponent % 10, 10));
      exponent /= 10;
    }

    return form(number < 0, zeros.substring(0, 1), new String(fracPart));
  }

  /** Format floating point number. */
  private String formF(double number) {
    // Format absolut value
    double multiplier = number < 0 ? - _tenToPrecision : _tenToPrecision;
    String digits 
        = Long.toString((long) Math.floor(number * multiplier + 0.5));
    String intPart = digits;
    StringBuffer fracPart = new StringBuffer();
    if (_decimalPoint) {
      int len = digits.length() - _precision;
      fracPart.append('.').append(digits.substring(Math.max(0, len)));
      if (len > 0) {
        intPart = digits.substring(0, len);
      } else {
        intPart = "0";
        for (; len < 0; len++) {
          fracPart.insert(1, '0');
        }
      }
    }

    return form(number < 0, intPart, new String(fracPart));
  }
}
