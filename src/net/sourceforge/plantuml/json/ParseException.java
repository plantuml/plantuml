/*******************************************************************************
 * Copyright (c) 2013, 2016 EclipseSource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package net.sourceforge.plantuml.json;

/**
 * An unchecked exception to indicate that an input does not qualify as valid JSON.
 */
@SuppressWarnings("serial") // use default serial UID
public class ParseException extends RuntimeException {

  private final Location location;

  ParseException(String message, Location location) {
    super(message + " at " + location);
    this.location = location;
  }

  /**
   * Returns the location at which the error occurred.
   *
   * @return the error location
   */
  public Location getLocation() {
    return location;
  }

  /**
   * Returns the absolute character index at which the error occurred. The offset of the first
   * character of a document is 0.
   *
   * @return the character offset at which the error occurred, will be &gt;= 0
   * @deprecated Use {@link #getLocation()} instead
   */
  @Deprecated
  public int getOffset() {
    return location.offset;
  }

  /**
   * Returns the line number in which the error occurred. The number of the first line is 1.
   *
   * @return the line in which the error occurred, will be &gt;= 1
   * @deprecated Use {@link #getLocation()} instead
   */
  @Deprecated
  public int getLine() {
    return location.line;
  }

  /**
   * Returns the column number at which the error occurred, i.e. the number of the character in its
   * line. The number of the first character of a line is 1.
   *
   * @return the column in which the error occurred, will be &gt;= 1
   * @deprecated Use {@link #getLocation()} instead
   */
  @Deprecated
  public int getColumn() {
    return location.column;
  }

}
