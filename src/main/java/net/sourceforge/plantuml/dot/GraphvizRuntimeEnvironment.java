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
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.vizjs.GraphvizJs;
import net.sourceforge.plantuml.vizjs.VizJsEngine;

public class GraphvizRuntimeEnvironment {

	public static final String VIZJS = "vizjs";

	private final static GraphvizRuntimeEnvironment singleton = new GraphvizRuntimeEnvironment();

	private final Map<File, GraphvizVersion> map = new ConcurrentHashMap<File, GraphvizVersion>();
	private String dotExecutable;
	private String dotVersion;

	private GraphvizRuntimeEnvironment() {
	}

	public static GraphvizRuntimeEnvironment getInstance() {
		return singleton;
	}

	private static boolean isWindows() {
		return SFile.separatorChar == '\\';
	}

	@DuplicateCode(reference = "GraphvizVersions")
	public GraphvizVersion getVersion(File f) {
		if (f == null)
			return null;

		GraphvizVersion result = map.get(f);
		if (result != null)
			return result;

		result = new GraphvizVersionFinder(new File(f.getAbsolutePath())).getVersion();
		map.put(f, result);
		return result;
	}

	@DuplicateCode(reference = "GraphvizUtils")
	public final String getDotExecutableForTest() {
		return dotExecutable;
	}

	@DuplicateCode(reference = "GraphvizUtils")
	public final void setDotExecutable(String value) {
		dotExecutable = value == null ? null : value.trim();
	}

	@DuplicateCode(reference = "GraphvizUtils")
	public String getenvGraphvizDot() {
		if (StringUtils.isNotEmpty(dotExecutable))
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(dotExecutable);

		final String env = System.getProperty("GRAPHVIZ_DOT");
		if (StringUtils.isNotEmpty(env))
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(env);

		final String getenv = System.getenv("GRAPHVIZ_DOT");
		if (StringUtils.isNotEmpty(getenv))
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(getenv);

		return null;
	}

	@DuplicateCode(reference = "GraphvizUtils")
	public Graphviz createForSystemDot(ISkinParam skinParam, String dotString, String... type) {
		Graphviz result = createWithFactory(skinParam, dotString, type);
		if (result != null) {
			return result;
		}
		if (useVizJs(skinParam)) {
			Log.info(() -> "Using " + VIZJS);
			return new GraphvizJs(dotString);
		}
		if (isWindows())
			result = new GraphvizWindowsOld(skinParam, dotString, type);
		else
			result = new GraphvizLinux(skinParam, dotString, type);

		if (result.getExeState() != ExeState.OK && VizJsEngine.isOk()) {
			final Graphviz result2 = result;
			Log.info(() -> "Error with file " + result2.getDotExe() + ": " + result2.getExeState().getTextMessage());
			Log.info(() -> "Using " + VIZJS);
			return new GraphvizJs(dotString);
		}
		return result;

	}

	@DuplicateCode(reference = "GraphvizUtils")
	public Graphviz create(ISkinParam skinParam, String dotString, String... type) {
		Graphviz result = createWithFactory(skinParam, dotString, type);
		if (result != null) {
			return result;
		}
		if (useVizJs(skinParam)) {
			Log.info(() -> "Using " + VIZJS);
			return new GraphvizJs(dotString);
		}
		if (isWindows())
			result = new GraphvizWindowsLite(skinParam, dotString, type);
		else
			result = new GraphvizLinux(skinParam, dotString, type);

		if (result.getExeState() != ExeState.OK && VizJsEngine.isOk()) {
			final Graphviz result2 = result;
			Log.info(() -> "Error with file " + result2.getDotExe() + ": " + result2.getExeState().getTextMessage());
			Log.info(() -> "Using " + VIZJS);
			return new GraphvizJs(dotString);
		}
		return result;

	}

	@DuplicateCode(reference = "GraphvizUtils")
	private Graphviz createWithFactory(ISkinParam skinParam, String dotString, String... type) {
		Iterator<GraphvizFactory> iterator = ServiceLoader.load(GraphvizFactory.class).iterator();
		while (iterator.hasNext()) {
			final GraphvizFactory factory = iterator.next();
			final Graphviz graphviz = factory.create(skinParam, dotString, type);
			if (graphviz != null) {
				Log.info(
						() -> "Using " + graphviz.getClass().getName() + " created by " + factory.getClass().getName());
				return graphviz;
			}
		}
		return null;
	}

	@DuplicateCode(reference = "GraphvizUtils")
	public boolean useVizJs(ISkinParam skinParam) {
		if (skinParam != null && skinParam.isUseVizJs() && VizJsEngine.isOk())
			return true;

		if (VIZJS.equalsIgnoreCase(GraphvizRuntimeEnvironment.getInstance().getenvGraphvizDot()) && VizJsEngine.isOk())
			return true;

		return false;
	}

	@DuplicateCode(reference = "GraphvizUtils")
	public File getDotExe() {
		return create(null, "png").getDotExe();
	}

	@DuplicateCode(reference = "GraphvizUtils")
	public String dotVersion() throws IOException, InterruptedException {
		if (dotVersion == null) {
			final File dotExe = getDotExe();
			final ExeState exeState = ExeState.checkFile(dotExe);
			if (exeState == ExeState.OK)
				dotVersion = create(null, "png").dotVersion();
			else
				dotVersion = "Error:" + exeState.getTextMessage(dotExe);

		}
		return dotVersion;
	}

	@DuplicateCode(reference = "GraphvizUtils")
	public boolean graphviz244onWindows() {
		try {
			return create(null, "png").graphviz244onWindows();
		} catch (Exception e) {
			Logme.error(e);
			return false;
		}
	}

	@DuplicateCode(reference = "GraphvizUtils")
	public int retrieveVersion(String s) {
		if (s == null)
			return -1;

		final Pattern p = Pattern.compile("\\s(\\d+)\\.(\\d\\d?)\\D");
		final Matcher m = p.matcher(s);
		if (m.find() == false)
			return -1;

		return 100 * Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2));
	}

	@DuplicateCode(reference = "GraphvizUtils")
	public int getDotVersion() throws IOException, InterruptedException {
		return retrieveVersion(dotVersion());
	}

}
