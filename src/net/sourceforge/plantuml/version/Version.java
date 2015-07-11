/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 16588 $
 *
 */
package net.sourceforge.plantuml.version;

import java.net.URL;
import java.util.Date;

public class Version {

	public static int version() {
		return 8028;
	}

	public static String versionString() {
		if (beta() != 0) {
			return "" + (version() + 1) + "beta" + beta();
		}
		return "" + version();
	}

	public static String versionString(int size) {
		final StringBuilder sb = new StringBuilder(versionString());
		while (sb.length() < size) {
			sb.append(' ');
		}
		return sb.toString();
	}

	private static int beta() {
		final int beta = 0;
		return beta;
	}

	private static long compileTime() {
		return 1436554841134L;
	}

	public static String compileTimeString() {
		if (beta() != 0) {
			return versionString();
		}
		return new Date(Version.compileTime()).toString();
	}

	public static String getJarPath() {
		try {
			final ClassLoader loader = Version.class.getClassLoader();
			if (loader == null) {
				return "No ClassLoader?";
			}
			final URL url = loader.getResource("net/sourceforge/plantuml/version/Version.class");
			if (url == null) {
				return "No URL?";
			}
			String fullpath = url.toString();
			fullpath = fullpath.replaceAll("net/sourceforge/plantuml/version/Version\\.class", "");
			return fullpath;
		} catch (Throwable t) {
			t.printStackTrace();
			return t.toString();
		}
	}

}
