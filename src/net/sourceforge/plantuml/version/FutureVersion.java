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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.utils.SignatureUtils;

public class FutureVersion {
	// ::remove folder when __HAXE__

	public static String versionString() {
		readVersionTxtIfNeeded();
		return versionCache.get(0);
	}

	public static long compileTime() {
		readVersionTxtIfNeeded();
		return compileTimeCache.get(0);
	}

	private static List<String> versionCache = new CopyOnWriteArrayList<String>();
	private static List<Long> compileTimeCache = new CopyOnWriteArrayList<Long>();
	private static List<String> etagCache = new CopyOnWriteArrayList<String>();

	private static void readVersionTxtIfNeeded() {
		if (compileTimeCache.size() == 0 || versionCache.size() == 0) {
			try {
				final InputStream is = FutureVersion.class.getResourceAsStream("version.txt");
				if (is != null)
					try (final BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
						versionCache.add(br.readLine());
						compileTimeCache.add(Long.parseLong(br.readLine()));
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (versionCache.size() == 0)
				versionCache.add("Unknown version");
			if (compileTimeCache.size() == 0)
				compileTimeCache.add(0L);
		}

	}

	public static String fullDescription() {
		return "PlantUML version " + FutureVersion.versionString() + " (" + FutureVersion.compileTimeString() + ")";
	}

	public static String versionString(int size) {
		final StringBuilder sb = new StringBuilder(versionString());
		while (sb.length() < size)
			sb.append(' ');

		return sb.toString();
	}

	public static String etag() {
		if (etagCache.size() == 0)
			etagCache.add(SignatureUtils.getMD5Hex(versionString()));

		return etagCache.get(0);
	}

	public static String turningId() {
		return etag();
	}

	public static String compileTimeString() {
		return new Date(FutureVersion.compileTime()).toString();
	}

	// ::comment when __CORE__
	public static String getJarPath() {
		try {
			final ClassLoader loader = FutureVersion.class.getClassLoader();
			if (loader == null)
				return "No ClassLoader?";

			final SURL url = SURL.create(loader.getResource("net/sourceforge/plantuml/version/Version.class"));
			if (url == null)
				return "No URL?";

			String fullpath = url.toString();
			fullpath = fullpath.replaceAll("net/sourceforge/plantuml/version/Version\\.class", "");
			return fullpath;
		} catch (Throwable t) {
			Logme.error(t);
			return t.toString();
		}
	}
	// ::done

}
