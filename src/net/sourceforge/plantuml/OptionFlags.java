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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

public class OptionFlags {

	private static final OptionFlags singleton = new OptionFlags();

	// static public final boolean PBBACK = false;
	// static public boolean GRAPHVIZCACHE = false;
	// static public final boolean TRACE_DOT = false;

	static public boolean ALLOW_INCLUDE = true;

	static public void setAllowIncludeFalse() {
		ALLOW_INCLUDE = false;
	}

	static public void setMaxPixel(int max) {
		ImageBuilder.setMaxPixel(max);
	}

	static public final boolean USE_HECTOR = false;
	static public boolean ADD_NICE_FOR_DOT = false;
	static public final boolean STRICT_SELFMESSAGE_POSITION = true;

	// static public final boolean USE_IF_VERTICAL = true;
	static public final boolean FORCE_TEOZ = false;
	static public final boolean USE_INTERFACE_EYE1 = false;
	static public final boolean USE_INTERFACE_EYE2 = false;
	// static public final boolean SWI2 = false;
	// static public final boolean USE_COMPOUND = false;
	static public final boolean OMEGA_CROSSING = false;

	// static public final boolean LINK_BETWEEN_FIELDS = true;

	public void reset() {
		reset(false);
		GraphvizUtils.setDotExecutable(null);
	}

	public final void setDotExecutable(String dotExecutable) {
		GraphvizUtils.setDotExecutable(dotExecutable);
	}

	private OptionFlags() {
		reset(true);
	}

	private void reset(boolean exit) {
		verbose = false;
		extractFromMetadata = false;
		word = false;
		systemExit = exit;
		gui = false;
		quiet = false;
		checkDotError = false;
		printFonts = false;
		// failOnError = false;
		encodesprite = false;
		// PIC_LINE = false;
	}

	public boolean useJavaInsteadOfDot() {
		return false;
	}

	private boolean verbose;
	private boolean extractFromMetadata;
	private boolean word;
	private boolean systemExit;
	private boolean gui;
	private boolean quiet;
	private boolean checkDotError;
	private boolean printFonts;
	private boolean encodesprite;
	private boolean dumpHtmlStats;
	private boolean dumpStats;
	private boolean loopStats;
	private boolean overwrite;
	private boolean enableStats = defaultForStats();
	private boolean stdLib;
	private boolean silentlyCompletelyIgnoreErrors;
	private boolean extractStdLib;
	private boolean clipboardLoop;
	private boolean clipboard;
	private String fileSeparator = "_";
	private long timeoutMs = 15 * 60 * 1000L; // 15 minutes
	private File logData;

	public static OptionFlags getInstance() {
		return singleton;
	}

	public final boolean isVerbose() {
		return verbose;
	}

	public final void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public final boolean isExtractFromMetadata() {
		return extractFromMetadata;
	}

	public final void setExtractFromMetadata(boolean extractFromMetadata) {
		this.extractFromMetadata = extractFromMetadata;
	}

	public final boolean isWord() {
		return word;
	}

	public final void setWord(boolean word) {
		this.word = word;
	}

	public final boolean isSystemExit() {
		return systemExit;
	}

	public final void setSystemExit(boolean systemExit) {
		this.systemExit = systemExit;
	}

	public final boolean isGui() {
		return gui;
	}

	public final void setGui(boolean gui) {
		this.gui = gui;
	}

	public final boolean isQuiet() {
		return quiet;
	}

	public final void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

	public final boolean isCheckDotError() {
		return checkDotError;
	}

	public final void setCheckDotError(boolean checkDotError) {
		this.checkDotError = checkDotError;
	}

	private final AtomicBoolean logDataInitized = new AtomicBoolean(false);

	public void logData(File file, Diagram system) {
		final String warnOrError = system.getWarningOrError();
		if (warnOrError == null) {
			return;
		}
		synchronized (logDataInitized) {
			if (logData == null && logDataInitized.get() == false) {
				final String s = GraphvizUtils.getenvLogData();
				if (s != null) {
					setLogData(new File(s));
				}
				logDataInitized.set(true);
			}

			if (logData == null) {
				return;
			}
			// final PSystemError systemError = (PSystemError) system;
			PrintStream ps = null;
			try {
				ps = new PrintStream(new FileOutputStream(logData, true));
				ps.println("Start of " + file.getName());
				ps.println(warnOrError);
				ps.println("End of " + file.getName());
				ps.println();
			} catch (FileNotFoundException e) {
				Log.error("Cannot open " + logData);
				e.printStackTrace();
			} finally {
				if (ps != null) {
					ps.close();
				}
			}
		}
	}

	public final void setLogData(File logData) {
		this.logData = logData;
		logData.delete();
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(logData));
			ps.println();
		} catch (FileNotFoundException e) {
			Log.error("Cannot open " + logData);
			e.printStackTrace();
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public final boolean isPrintFonts() {
		return printFonts;
	}

	public final void setPrintFonts(boolean printFonts) {
		this.printFonts = printFonts;
	}

	public final boolean isUseSuggestEngine2() {
		return false;
	}

	public final boolean isEncodesprite() {
		return encodesprite;
	}

	public final void setEncodesprite(boolean encodesprite) {
		this.encodesprite = encodesprite;
	}

	public final boolean isOverwrite() {
		return overwrite;
	}

	public final void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	public final String getFileSeparator() {
		return fileSeparator;
	}

	public final void setFileSeparator(String fileSeparator) {
		this.fileSeparator = fileSeparator;
	}

	public final boolean isDumpHtmlStats() {
		return dumpHtmlStats;
	}

	public final void setDumpHtmlStats(boolean value) {
		this.dumpHtmlStats = value;
	}

	public final boolean isDumpStats() {
		return dumpStats;
	}

	public final void setDumpStats(boolean dumpStats) {
		this.dumpStats = dumpStats;
	}

	public final boolean isLoopStats() {
		return loopStats;
	}

	public final void setLoopStats(boolean loopStats) {
		this.loopStats = loopStats;
	}

	private static boolean defaultForStats() {
		return isTrue(System.getProperty("PLANTUML_STATS")) || isTrue(System.getenv("PLANTUML_STATS"));
	}

	private static boolean isTrue(final String value) {
		return "on".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
	}

	public boolean isEnableStats() {
		return enableStats;
	}

	public void setEnableStats(boolean enableStats) {
		this.enableStats = enableStats;
	}

	public final long getTimeoutMs() {
		return timeoutMs;
	}

	public final void setTimeoutMs(long timeoutMs) {
		this.timeoutMs = timeoutMs;
	}

	public void setExtractStdLib(boolean extractStdLib) {
		this.extractStdLib = extractStdLib;
	}

	public boolean isExtractStdLib() {
		return extractStdLib;
	}

	public final boolean isClipboardLoop() {
		return clipboardLoop;
	}

	public final void setClipboardLoop(boolean clipboardLoop) {
		this.clipboardLoop = clipboardLoop;
	}

	public final boolean isClipboard() {
		return clipboard;
	}

	public final void setClipboard(boolean clipboard) {
		this.clipboard = clipboard;
	}

	public final boolean isStdLib() {
		return stdLib;
	}

	public final void setStdLib(boolean stdLib) {
		this.stdLib = stdLib;
	}

	public final boolean isSilentlyCompletelyIgnoreErrors() {
		return silentlyCompletelyIgnoreErrors;
	}

	public final void setSilentlyCompletelyIgnoreErrors(boolean silentlyCompletelyIgnoreErrors) {
		this.silentlyCompletelyIgnoreErrors = silentlyCompletelyIgnoreErrors;
	}
}
