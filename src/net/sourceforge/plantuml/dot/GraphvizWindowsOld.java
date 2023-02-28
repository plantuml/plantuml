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
package net.sourceforge.plantuml.dot;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.windowsdot.WindowsDotArchive;

class GraphvizWindowsOld extends AbstractGraphviz {
	// ::remove file when __CORE__

	static private File specificDotExe;

	@Override
	protected File specificDotExe() {
		synchronized (GraphvizWindowsOld.class) {
			if (specificDotExe == null) {
				specificDotExe = specificDotExeSlow();
			}
			if (specificDotExe == null)
				specificDotExe = WindowsDotArchive.getInstance().getWindowsExeLite();

			return specificDotExe;
		}
	}

	public boolean graphviz244onWindows() {
		try {
			return GraphvizUtils.getDotVersion() == 244;
		} catch (Exception e) {
			Logme.error(e);
			return false;
		}
	}

	private File specificDotExeSlow() {
		for (File tmp : new File("c:/").listFiles(new FileFilter() {
			public boolean accept(java.io.File pathname) {
				return pathname.isDirectory() && pathname.canRead();
			}
		})) {
			final File result = searchInDir(tmp);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	private static File searchInDir(final File dir) {
		if (dir.exists() == false || dir.isDirectory() == false) {
			return null;
		}
		final List<File> dots = new ArrayList<>();
		final File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(java.io.File pathname) {
				return pathname.isDirectory() && StringUtils.goLowerCase(pathname.getName()).startsWith("graphviz");
			}
		});
		if (files == null) {
			return null;
		}
		for (File f : files) {
			final File result = new File(new File(f, "bin"), "dot.exe");
			if (result.exists() && result.canRead()) {
				dots.add(result.getAbsoluteFile());
			}
			final File result2 = new File(new File(f, "release/bin"), "dot.exe");
			if (result2.exists() && result2.canRead()) {
				dots.add(result2.getAbsoluteFile());
			}
		}
		return higherVersion(dots);
	}

	static File higherVersion(List<File> dots) {
		if (dots.size() == 0) {
			return null;
		}
		Collections.sort(dots, Collections.reverseOrder());
		return dots.get(0);
	}

	GraphvizWindowsOld(ISkinParam skinParam, String dotString, String... type) {
		super(skinParam, dotString, type);
	}

	@Override
	protected String getExeName() {
		return "dot.exe";
	}

}
