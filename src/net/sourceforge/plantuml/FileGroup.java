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
package net.sourceforge.plantuml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class FileGroup {

	private final List<File> result = new ArrayList<File>();
	private final String pattern;
	private final List<String> excluded;
	private final Option option;

	private final static Pattern2 predirPath = MyPattern.cmpile("^([^*?]*[/\\\\])?(.*)$");

	public FileGroup(String pattern, List<String> excluded, Option option) {
		this.pattern = pattern;
		this.excluded = excluded;
		this.option = option;
		if (pattern.indexOf("*") == -1 && pattern.indexOf("?") == -1) {
			initNoStar();
		} else if (pattern.indexOf("**") != -1) {
			recurse();
		} else {
			initWithSimpleStar();
		}
		Collections.sort(result);

	}

	private void recurse() {
		final Matcher2 m = predirPath.matcher(pattern);
		final boolean ok = m.find();
		if (ok == false) {
			throw new IllegalArgumentException();
		}
		final File parent;
		if (m.group(1) == null) {
			parent = new File(".");
		} else {
			parent = new File(m.group(1));
		}
		initWithDoubleStar(parent);
	}

	private void initNoStar() {
		final File f = new File(pattern);
		if (f.isDirectory()) {
			addSimpleDirectory(f);
		} else if (f.isFile()) {
			addResultFile(f);
		}
	}

	private void addResultFile(final File f) {
		final String path = getNormalizedPath(f);
		for (String x : excluded) {
			if (path.matches(toRegexp(x))) {
				return;
			}
		}
		result.add(f);
	}

	private void addSimpleDirectory(File dir) {
		if (OptionFlags.getInstance().isWord()) {
			addSimpleDirectory(dir, "(?i)^.*_extr\\d+\\.txt$");
		} else {
			addSimpleDirectory(dir, option.getPattern());
		}
	}

	private void addSimpleDirectory(File dir, String pattern) {
		if (dir.isDirectory() == false) {
			throw new IllegalArgumentException("dir=" + dir);
		}
		for (File f : dir.listFiles()) {
			if (f.getName().matches(pattern)) {
				addResultFile(f);
			}
		}
	}

	private static String getNormalizedPath(File f) {
		return f.getPath().replace('\\', '/');
	}

	private final static Pattern2 noStarInDirectory = MyPattern.cmpile("^(?:([^*?]*)[/\\\\])?([^/\\\\]*)$");

	private void initWithSimpleStar() {
		assert pattern.indexOf("**") == -1;
		final Matcher2 m = noStarInDirectory.matcher(pattern);
		if (m.find()) {
			File dir = new File(".");
			if (m.group(1) != null) {
				final String dirPart = m.group(1);
				dir = new File(dirPart);
			}

			final String filesPart = m.group(2);
			addSimpleDirectory(dir, toRegexp(filesPart));
		} else {
			recurse();
		}

	}

	private void initWithDoubleStar(File currentDir) {
		for (File f : currentDir.listFiles()) {
			if (f.isDirectory()) {
				initWithDoubleStar(f);
			} else if (f.isFile()) {
				final String path = getNormalizedPath(f);
				if (path.matches(toRegexp(pattern))) {
					addResultFile(f);
				}

			}
		}

	}

	public List<File> getFiles() {
		return Collections.unmodifiableList(result);
	}

	public static String toRegexp(String pattern) {
		pattern = pattern.replace("\\", "/");
		pattern = pattern.replace(".", "\\.");
		pattern = pattern.replace("?", "[^/]");
		pattern = pattern.replace("/**/", "(/|/.{0,}/)");
		pattern = pattern.replace("**", ".{0,}");
		pattern = pattern.replace("*", "[^/]{0,}");
		pattern = "(?i)^(\\./)?" + pattern + "$";
		return pattern;
	}

}
