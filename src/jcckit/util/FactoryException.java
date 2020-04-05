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

import java.lang.reflect.InvocationTargetException;

/**
 *  Exception thrown in the case of an error during creation of a new
 *  object by {@link Factory#create}.
 *
 *  @author Franz-Josef Elmer
 */
public class FactoryException extends RuntimeException {
  private final String _fullKey;
  private final String _className;
  private final Object _reason;

  /**
   *  Creates a new instance based on the specified configuration parameters
   *  and reason object.
   *  <p>
   *  If <tt>reason</tt> is an instance of <tt>InvocationTargetException</tt> 
   *  it will be replaced by the wrapped <tt>Throwable</tt>.
   *  @param configParameters Configuration parameters from which the 
   *         <tt>className</tt> will be extracted (if it exists, otherwise
   *         <tt>null</tt> will be taken).
   *  @param reason The reason causing this exception. Most often an
   *         an exception.
   */
  public FactoryException(ConfigParameters configParameters, String key,
                          Object reason) {
    _fullKey = configParameters.getFullKey(key);
    _className = configParameters.get(key, null);
    if (reason instanceof InvocationTargetException) {
      reason = ((InvocationTargetException) reason).getTargetException();
    }
    _reason = reason;
  }

  /** Returns the full class name key. */
  public String getFullKey() {
    return _fullKey;
  }

  /** Returns the fully qualified class name. */
  public String getClassName() {
    return _className;
  }

  /** Returns the reason object causing this exception. */
  public Object getReason() {
    return _reason;
  }

  /**
   * Renders this instance as follows: <tt>jcckit.util.FactoryException: 
   * <i>full key</i> = <i>class name</i>: <i>reason</i></tt>.
   */
  public String toString() {
    return getClass().getName() + ": " + _fullKey + " = " + _className
           + ": " + _reason;
  }
}
