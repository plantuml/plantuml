/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.tikz;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.PragmaKey;

public enum LatexEngine {

	LUALATEX, XELATEX, PDFLATEX, UNKNOWN_OR_NOT_INSTALLED, NONE;

	// Force NONE in tests (e.g. Vega) so rendering does not depend on whether a
	// LaTeX distribution happens to be installed on the build machine.
	public static boolean FORCE_NONE = false;

	private static final Map<String, Boolean> isInstalledCache = new ConcurrentHashMap<String, Boolean>();

	public static LatexEngine getSuggestedEngine(Pragma pragma) {
		if (FORCE_NONE)
			return NONE;

		final String value = pragma.getValue(PragmaKey.TEX_SYSTEM);
		if (value != null) {
			if ("lualatex".equalsIgnoreCase(value) && isInstalled("lualatex"))
				return LUALATEX;
			if ("xelatex".equalsIgnoreCase(value) && isInstalled("xelatex"))
				return XELATEX;
			if ("pdflatex".equalsIgnoreCase(value) && isInstalled("pdflatex"))
				return PDFLATEX;

			return UNKNOWN_OR_NOT_INSTALLED;
		}
		if (isInstalled("lualatex"))
			return LUALATEX;
		if (isInstalled("xelatex"))
			return XELATEX;
		// Last resort: pdflatex has no native unicode/fontspec support like lualatex
		// and xelatex, but it is often the only engine present on minimal TeX
		// installations, so it is worth trying before giving up on LaTeX/TikZ
		// output entirely.
		if (isInstalled("pdflatex"))
			return PDFLATEX;
		return NONE;
	}

	public String getCommand() {
		switch (this) {
		case LUALATEX:
			return "lualatex";
		case XELATEX:
			return "xelatex";
		case PDFLATEX:
			return "pdflatex";
		}
		throw new UnsupportedOperationException();
	}

	private static boolean isInstalled(String command) {
		return isInstalledCache.computeIfAbsent(command, LatexEngine::probeInstalled);
	}

	private static boolean probeInstalled(String command) {
		try {
			final Process process = new ProcessBuilder(command, "--version").redirectErrorStream(true).start();
			// Drain output so the process can terminate, then wait for it.
			drain(process.getInputStream());
			final int exit = process.waitFor();
			return exit == 0;
		} catch (IOException e) {
			// "Cannot run program": binary not on PATH
			return false;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}

	private static void drain(InputStream is) throws IOException {
		final byte[] buffer = new byte[4096];
		while (is.read(buffer) != -1) {
			// discard
		}
	}

}