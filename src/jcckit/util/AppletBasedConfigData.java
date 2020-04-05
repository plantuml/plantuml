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

import java.applet.Applet;

/**
 *  Implementation of {@link FlatConfigData} based on
 *  <tt>java.applet.Applet</tt>.
 *
 *  @author Franz-Josef Elmer
 */
public class AppletBasedConfigData extends FlatConfigData {
  private final Applet _applet;

  /**
   * Creates an instance based on the specified applet.
   * The path is undefined.
   */
  public AppletBasedConfigData(Applet applet) {
    this(applet, null);
  }

  /** Creates an instance based on the specified properties and path. */
  private AppletBasedConfigData(Applet applet, String path) {
    super(path);
    _applet = applet;
  }

  protected String getValue(String fullKey) {
    return _applet.getParameter(fullKey);
  }

  protected ConfigData createConfigData(String path) {
    return new AppletBasedConfigData(_applet, path);
  }
}
