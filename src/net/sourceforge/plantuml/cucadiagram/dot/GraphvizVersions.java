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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GraphvizVersions {

	private final static GraphvizVersions singleton = new GraphvizVersions();

	private final Map<File, GraphvizVersion> map = new ConcurrentHashMap<File, GraphvizVersion>();

	private GraphvizVersions() {
	}

	public static GraphvizVersions getInstance() {
		return singleton;
	}

	public GraphvizVersion getVersion(File f) {
		if (f == null) {
			return null;
		}
		GraphvizVersion result = map.get(f);
		if (result != null) {
			return result;
		}
		result = checkVersionSlow(f.getAbsolutePath());
		map.put(f, result);
		return result;
	}

	static GraphvizVersion checkVersionSlow(String pathExecutable) {
		final GraphvizVersionFinder finder = new GraphvizVersionFinder(new File(pathExecutable));
		return finder.getVersion();
	}

}
