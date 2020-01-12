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
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.AParentFolder;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.api.ApiWarning;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.TVariableScope;
import net.sourceforge.plantuml.version.Version;

public class Defines implements Truth {

	private final Map<String, String> environment = new LinkedHashMap<String, String>();
	private final Map<String, Define> values = new LinkedHashMap<String, Define>();

	@Deprecated
	@ApiWarning(willBeRemoved = "in next major release")
	public Defines() {
		environment.put("PLANTUML_VERSION", "" + Version.versionString());
	}

	@Override
	public String toString() {
		return values.keySet().toString() + " " + environment.keySet();
	}

	public static Defines createEmpty() {
		return new Defines();
	}

	public void copyTo(TMemory memory) throws EaterException {
		for (Entry<String, Define> ent : values.entrySet()) {
			final String name = ent.getKey();
			final Define def = ent.getValue();
			memory.putVariable(name, def.asTVariable(), TVariableScope.GLOBAL);
		}

	}

	public void overrideFilename(String filename) {
		if (filename != null) {
			environment.put("filename", filename);
			environment.put("filenameNoExtension", nameNoExtension(filename));
		}
	}

	public void importFrom(Defines other) {
		this.environment.putAll(other.environment);
		this.values.putAll(other.values);
		magic = null;
	}

	public Defines cloneMe() {
		final Defines result = new Defines();
		result.importFrom(this);
		return result;
	}

	public static Defines createWithFileName(File file) {
		if (file == null) {
			throw new IllegalArgumentException();
		}
		final Defines result = createEmpty();
		result.overrideFilename(file.getName());
		result.environment.put("filedate", new Date(file.lastModified()).toString());
		result.environment.put("dirpath", file.getAbsoluteFile().getParentFile().getAbsolutePath().replace('\\', '/'));
		return result;
	}

	public static Defines createWithMap(Map<String, String> init) {
		final Defines result = createEmpty();
		for (Map.Entry<String, String> ent : init.entrySet()) {
			result.environment.put(ent.getKey(), ent.getValue());
		}
		return result;
	}

	public String getEnvironmentValue(String key) {
		return this.environment.get(key);
	}

	private static String nameNoExtension(String name) {
		final int x = name.lastIndexOf('.');
		if (x == -1) {
			return name;
		}
		return name.substring(0, x);
	}

	public void define(String name, List<String> value, boolean emptyParentheses, AParentFolder currentDir) {
		values.put(name, new Define(name, value, emptyParentheses, currentDir));
		magic = null;
	}

	public boolean isDefine(String expression) {
		try {
			final EvalBoolean eval = new EvalBoolean(expression, this);
			return eval.eval();
		} catch (IllegalArgumentException e) {
			Log.info("Error in " + expression);
			return false;
		}
	}

	public boolean isTrue(String name) {
		for (String key : values.keySet()) {
			if (key.equals(name) || key.startsWith(name + "(")) {
				return true;
			}
		}
		return false;
	}

	public void undefine(String name) {
		values.remove(name);
		magic = null;
	}

	public List<String> applyDefines(String line) {
		// System.err.println("line=" + line + " " + values.size());
		line = manageDate(line);
		line = manageEnvironment(line);
		line = method1(line);
		// line = values.size() < 10 ? method1(line) : method2(line);
		return Arrays.asList(line.split("\n"));
	}

	private String method1(String line) {
		for (Define def : values.values()) {
			line = def.apply(line);
		}
		return line;
	}

	private Map<String, Collection<Define>> getAll() {
		final Map<String, Collection<Define>> result = new LinkedHashMap<String, Collection<Define>>();
		for (Define def : values.values()) {
			Collection<Define> tmp = result.get(def.getFunctionName());
			if (tmp == null) {
				tmp = new ArrayList<Define>();
				result.put(def.getFunctionName(), tmp);
			}
			tmp.add(def);
		}
		return result;
	}

	private Map<String, Collection<Define>> magic;

	private String method2(String line) {
		final Set<String> words = words(line);
		if (magic == null) {
			magic = getAll();

		}
		for (String w : words) {
			Collection<Define> tmp = magic.get(w);
			if (tmp == null) {
				continue;
			}
			for (Define def : tmp) {
				line = def.apply(line);
			}
		}
		return line;
	}

	private Set<String> words(String line) {
		final String ID = "[A-Za-z_][A-Za-z_0-9]*";
		Pattern p = Pattern.compile(ID);
		Matcher m = p.matcher(line);
		final Set<String> words = new HashSet<String>();
		while (m.find()) {
			words.add(m.group(0));
		}
		return words;
	}

	private String manageEnvironment(String line) {
		for (Map.Entry<String, String> ent : environment.entrySet()) {
			final String key = Pattern.quote("%" + ent.getKey() + "%");
			line = line.replaceAll(key, ent.getValue());
		}
		return line;
	}

	private static final String DATE = "(?i)%date(\\[(.+?)\\])?%";
	private final static Pattern datePattern = Pattern.compile(DATE);

	private String manageDate(String line) {
		final Matcher m = datePattern.matcher(line);
		if (m.find()) {
			final String format = m.group(2);
			String replace;
			if (format == null) {
				replace = new Date().toString();
			} else {
				try {
					replace = new SimpleDateFormat(format).format(new Date());
				} catch (Exception e) {
					replace = "(BAD DATE PATTERN:" + format + ")";
				}
			}
			line = line.replaceAll(DATE, replace);
		}
		return line;
	}

}
