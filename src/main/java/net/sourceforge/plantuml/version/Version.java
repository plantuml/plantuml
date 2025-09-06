/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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

import java.util.Date;

import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.utils.SignatureUtils;

public class Version {
	// ::remove folder when __HAXE__

	// Warning, "version" should be the same in gradle.properties and Version.java
	// Any idea anyone how to magically synchronize those :-) ?
	private static final String version = "1.2025.5beta4";

	public static String versionString() {
		return version;
	}

	public static String fullDescription() {
		return "PlantUML version " + Version.versionString() + " (" + Version.compileTimeString() + ")";
	}

	public static String versionString(int size) {
		final StringBuilder sb = new StringBuilder(versionString());
		while (sb.length() < size)
			sb.append(' ');

		return sb.toString();
	}

	public static int beta() {
		final int x = version.indexOf("beta");
		if (x == -1)
			return 0;
		return Integer.parseInt(version.substring(x + "beta".length()));
	}

	public static String etag() {
		return SignatureUtils.getMD5Hex(version);
	}

	public static String turningId() {
		return etag();
	}

	public static long compileTime() {
		return 1751108965675L;
	}

	public static String compileTimeString() {
		if (version.contains("beta"))
			return "Unknown compile time";

		return new Date(Version.compileTime()).toString();
	}

	// ::comment when __CORE__
	public static String getJarPath() {
		try {
			final ClassLoader loader = Version.class.getClassLoader();
			if (loader == null) {
				return "No ClassLoader?";
			}
			final SURL url = SURL.create(loader.getResource("net/sourceforge/plantuml/version/Version.class"));
			if (url == null) {
				return "No URL?";
			}
			String fullpath = url.toString();
			fullpath = fullpath.replaceAll("net/sourceforge/plantuml/version/Version\\.class", "");
			return fullpath;
		} catch (Throwable t) {
			Logme.error(t);
			return t.toString();
		}
	}

	public static void main(String[] args) {
		final String s = "return " + System.currentTimeMillis() + "L;";
		System.out.print(s);

	}
	// ::done

}
