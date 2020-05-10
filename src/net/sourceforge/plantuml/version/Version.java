/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.version;

import java.net.URL;
import java.util.Date;

public class Version {

	private static final int MAJOR_SEPARATOR = 1000000;

	public static int version() {
		return 1202009;
	}

	public static int versionPatched() {
		if (beta() != 0) {
			return version() + 1;
		}
		return version();
	}

	public static String versionString() {
		if (beta() != 0) {
			return dotted(version() + 1) + "beta" + beta();
		}
		return dotted(version());
	}

	public static String fullDescription() {
		return "PlantUML version " + Version.versionString() + " (" + Version.compileTimeString() + ")";
	}

	private static String dotted(int nb) {
		final String minor = "" + nb % MAJOR_SEPARATOR;
		final String major = "" + nb / MAJOR_SEPARATOR;
		return major + "." + minor.substring(0, 4) + "." + minor.substring(4);
	}

	public static String versionString(int size) {
		final StringBuilder sb = new StringBuilder(versionString());
		while (sb.length() < size) {
			sb.append(' ');
		}
		return sb.toString();
	}

	public static int beta() {
		final int beta = 0;
		return beta;
	}

	public static String etag() {
		return Integer.toString(version() % MAJOR_SEPARATOR - 201670, 36) + Integer.toString(beta(), 36);
	}

	public static String turningId() {
		return etag();
	}

	public static long compileTime() {
		return 1589107866768L;
	}

	public static String compileTimeString() {
		if (beta() != 0) {
			return "Unknown compile time";
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
