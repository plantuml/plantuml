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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.crash.ReportLog;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SecurityProfile;
import net.sourceforge.plantuml.security.SecurityUtils;

public class GraphvizUtils {
	// ::remove file when __CORE__

	private static int DOT_VERSION_LIMIT = 226;


	private static final ThreadLocal<Integer> limitSize = new ThreadLocal<>();

	public static void removeLocalLimitSize() {
		limitSize.remove();
	}

	public static void setLocalImageLimit(int value) {
		limitSize.set(value);
	}

	public static int getenvImageLimit() {
		final Integer local = limitSize.get();
		if (local != null)
			return local;

		final String env = SecurityUtils.getenv("PLANTUML_LIMIT_SIZE");
		if (StringUtils.isNotEmpty(env) && env.matches("\\d+"))
			return Integer.parseInt(env);

		return 4096;
	}

	public static String getenvDefaultConfigFilename() {
		return SecurityUtils.getenv("PLANTUML_DEFAULT_CONFIG_FILENAME");
	}

	public static String getenvLogData() {
		return SecurityUtils.getenv("PLANTUML_LOGDATA");
	}


	static public int addDotStatus(ReportLog result, boolean withRichText) {
		String red = "";
		String bold = "";
		if (withRichText) {
			red = "<b><color:red>";
			bold = "<b>";
		}

		int error = 0;

		if (GraphvizRuntimeEnvironment.getInstance().useVizJs(null)) {
			result.add("VizJs library is used!");
			try {
				final String err = getTestCreateSimpleFile();
				if (err == null)
					result.add(bold + "Installation seems OK. File generation OK");
				else
					result.add(red + err);

			} catch (Exception e) {
				result.add(red + e.toString());
				Logme.error(e);
				error = -1;
			}
			return error;
		}

		final File dotExe = GraphvizRuntimeEnvironment.getInstance().getDotExe();
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE) {
			final String ent = GraphvizRuntimeEnvironment.getInstance().getenvGraphvizDot();
			if (ent == null)
				result.add("The environment variable GRAPHVIZ_DOT has not been set");
			else
				result.add("The environment variable GRAPHVIZ_DOT has been set to " + ent);

			result.add("Dot executable is " + dotExe);
		}
		final ExeState exeState = ExeState.checkFile(dotExe);

		if (exeState == ExeState.OK) {
			try {
				final String version = GraphvizRuntimeEnvironment.getInstance().dotVersion();
				result.add("Dot version: " + version);
				final int v = GraphvizRuntimeEnvironment.getInstance().getDotVersion();
				if (v == -1) {
					result.add("Warning : cannot determine dot version");
					error = -2;
				} else if (v < DOT_VERSION_LIMIT) {
					result.add(bold + "Warning : Your dot installation seems old");
					result.add(bold + "Some diagrams may have issues");
					error = -3;
				} else {
					final String err = getTestCreateSimpleFile();
					if (err == null) {
						result.add(bold + "Installation seems OK. File generation OK");
					} else {
						result.add(red + err);
						error = -4;
					}
				}
			} catch (Exception e) {
				result.add(red + e.toString());
				Logme.error(e);
				error = -5;
			}
		} else {
			result.add(red + "Error: " + exeState.getTextMessage());
			result.add("Error: only sequence diagrams will be generated");
			error = -6;
		}

		return error;
	}

	static String getTestCreateSimpleFile() throws IOException {
		final Graphviz graphviz2 = GraphvizRuntimeEnvironment.getInstance().create(null, "digraph foo { test; }", "svg");
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ProcessState state = graphviz2.createFile3(baos);
		if (state.differs(ProcessState.TERMINATED_OK()))
			return "Error: timeout " + state;

		final byte data[] = baos.toByteArray();

		if (data.length == 0)
			return "Error: dot generates empty file. Check you dot installation.";

		final String s = new String(data);
		if (s.indexOf("<svg") == -1)
			return "Error: dot generates unreadable SVG file. Check you dot installation.";

		return null;
	}

}
