/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 4826 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;
import java.io.IOException;

public class GraphvizUtils {

	private static boolean isWindows() {
		return File.separatorChar == '\\';
	}

	public static Graphviz create(String dotString, String... type) {
		if (isWindows()) {
			return new GraphvizWindows(dotString, type);
		}
		return new GraphvizLinux(dotString, type);
	}

	static public File getDotExe() {
		return create(null, "png").getDotExe();
	}

	public static String getenvGraphvizDot() {
		final String env = System.getProperty("GRAPHVIZ_DOT");
		if (env != null) {
			return env;
		}
		return System.getenv("GRAPHVIZ_DOT");
	}

	private static String dotVersion = null;

	public static String dotVersion() throws IOException, InterruptedException {
		if (dotVersion == null) {
			if (GraphvizUtils.getDotExe() == null) {
				dotVersion = "Error: Dot not installed";
			} else if (GraphvizUtils.getDotExe().exists() == false) {
				dotVersion = "Error: " + GraphvizUtils.getDotExe().getAbsolutePath() + " does not exist";
			} else if (GraphvizUtils.getDotExe().isFile() == false) {
				dotVersion = "Error: " + GraphvizUtils.getDotExe().getAbsolutePath() + " is not a file";
			} else if (GraphvizUtils.getDotExe().canRead() == false) {
				dotVersion = "Error: " + GraphvizUtils.getDotExe().getAbsolutePath() + " cannot be read";
			} else {
				dotVersion = create(null, "png").dotVersion();
			}
		}
		return dotVersion;
	}
}
