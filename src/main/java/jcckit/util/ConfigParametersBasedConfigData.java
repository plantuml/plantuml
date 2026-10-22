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
 *  An implementation of {@link ConfigData} based on two instances of
 *  {@link ConfigParameters}. The second one serves as a set of
 *  default parameters. It will be used if the first one has not the requested
 *  key-value pair.
 *
 *  @author Franz-Josef Elmer
 */
public class ConfigParametersBasedConfigData implements ConfigData {
  private ConfigParameters _config;
  private ConfigParameters _defaultConfig;

  /**
   * Creates an instance.
   * @param config A set of key-value pairs.
   * @param defaultConfig The default set of key-value pairs.
   */
  public ConfigParametersBasedConfigData(ConfigParameters config,
                                         ConfigParameters defaultConfig) {
    _config = config;
    _defaultConfig = defaultConfig;
  }

  /**
   * Returns the full key.
   * @param key A (relative) key. <tt>null</tt> is not allowed.
   * @return the full key including path.
   */
  public String getFullKey(String key) {
    return _config.getFullKey(key);
  }

  /**
   * Returns the value associated with this key.
   * @param key The relative key. <tt>null</tt> is not allowed.
   * @return the associated value. Will be <tt>null</tt> if no value exists
   *         for <tt>key</tt>.
   */
  public String get(String key) {
    String value = _config.get(key, null);
    return value == null ? _defaultConfig.get(key, null) : value;
  }

  /**
   * Returns the <tt>ConfigData</tt> object associated with this key.
   * @param key The relative key. <tt>null</tt> is not allowed.
   * @return the associated value. Will never return <tt>null</tt>.
   *         Instead an empty <tt>ConfigData</tt> is returned.
   */
  public ConfigData getNode(String key) {
    return new ConfigParametersBasedConfigData(_config.getNode(key),
                                               _defaultConfig.getNode(key));
  }
}
