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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *  Implementation of {@link FlatConfigData} based on
 *  <tt>java.util.Properties</tt>.
 *
 *  @author Franz-Josef Elmer
 */
public class PropertiesBasedConfigData extends FlatConfigData {
  private final Properties _properties;

  /**
   * Creates an instance from the specified <tt>.properties</tt> file.
   * @param fileName File name of the <tt>.properties</tt> file relative
   *        to the working directory or absolute.
   * @throws IOException if the <tt>.properties</tt> does not exist or could
   *         not be read.
   */
  public PropertiesBasedConfigData(String fileName) throws IOException {
    super(null);
    _properties = new Properties();
    _properties.load(new FileInputStream(fileName));
  }

  /**
   * Creates an instance based on the specified properties.
   * The path is undefined.
   */
  public PropertiesBasedConfigData(Properties properties) {
    this(properties, null);
  }

  /** Creates an instance based on the specified properties and path. */
  private PropertiesBasedConfigData(Properties properties, String path) {
    super(path);
    _properties = properties;
  }

  /**
   * Returns the value for the specified full key. The call will be delegated
   * to the wrapped <tt>java.util.properties</tt> object.
   * @param fullKey The full key including path. <tt>null</tt> is not allowed.
   * @return the value or <tt>null</tt> if not found.
   */
  protected String getValue(String fullKey) {
    return _properties.getProperty(fullKey);
  }

  /**
   * Returns a new instance of <tt>PropertiesBasedConfigData</tt> 
   * for the specified full path. The wrapped <tt>java.util.Properties</tt>
   * will be the same as of this instance.
   * @param path The full path.
   * @return a new instance.
   */
  protected ConfigData createConfigData(String path) {
    return new PropertiesBasedConfigData(_properties, path);
  }
}
