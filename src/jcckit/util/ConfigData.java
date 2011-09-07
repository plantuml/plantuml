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
 *  Interface for hierarchically managed key-value pairs. The key is
 *  always a string which contains any kind of printable character except
 *  '/', '=', ':', and whitespace characters like ' ' and '\t'. 
 *  The value is either a string or a <tt>ConfigData</tt> object.
 *  <p>
 *  This interface will be used by {@link ConfigParameters} in accordance
 *  with the Strategy design pattern.
 *
 *  @author Franz-Josef Elmer
 */
public interface ConfigData {
  /**
   * Returns the full key.
   * @param key A (relative) key. <tt>null</tt> is not allowed.
   * @return the full key including path.
   */
  public String getFullKey(String key);

  /**
   * Returns the value associated with this key.
   * @param key The relative key. <tt>null</tt> is not allowed.
   * @return the associated value. Will be <tt>null</tt> if no value exists
   *         for <tt>key</tt>.
   */
  public String get(String key);

  /**
   * Returns the <tt>ConfigData</tt> object associated with this key.
   * @param key The relative key. <tt>null</tt> is not allowed.
   * @return the associated value. Will never return <tt>null</tt>.
   *         Instead an empty <tt>ConfigData</tt> is returned.
   */
  public ConfigData getNode(String key);
}
