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
 * Revision $Revision: 5746 $
 *
 */
package net.sourceforge.plantuml;

public class OptionFlags {
	
	static public final boolean PBBACK = false;

	void reset() {
		keepTmpFiles = false;
		verbose = false;
		metadata = false;
		word = false;
		debugDot = false;
		forceGd = false;
		forceCairo = false;
		quiet = false;
		dotExecutable = null;
	}

	public boolean useJavaInsteadOfDot() {
		return false;
	}

	private static final OptionFlags singleton = new OptionFlags();

	private boolean keepTmpFiles = false;
	private boolean verbose = false;
	private boolean metadata = false;
	private boolean word = false;
	private boolean systemExit = true;
	private boolean debugDot = false;
	private boolean forceGd = false;
	private boolean forceCairo = false;
	private String dotExecutable = null;
	private boolean gui = false;
	private boolean quiet = false;

	private OptionFlags() {
		reset();
	}

	public static OptionFlags getInstance() {
		return singleton;
	}

	public final boolean isKeepTmpFiles() {
		return keepTmpFiles;
	}

	public final void setKeepTmpFiles(boolean keepTmpFiles) {
		this.keepTmpFiles = keepTmpFiles;
	}

	public final boolean isVerbose() {
		return verbose;
	}

	public final void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public final boolean isMetadata() {
		return metadata;
	}

	public final void setMetadata(boolean metadata) {
		this.metadata = metadata;
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

	public final boolean isDebugDot() {
		return debugDot;
	}

	public final void setDebugDot(boolean debugDot) {
		this.debugDot = debugDot;
	}

	public final String getDotExecutable() {
		return dotExecutable;
	}

	public final void setDotExecutable(String dotExecutable) {
		this.dotExecutable = dotExecutable;
	}

	public final boolean isGui() {
		return gui;
	}

	public final void setGui(boolean gui) {
		this.gui = gui;
	}

	public final boolean isForceGd() {
		return forceGd;
	}

	public final void setForceGd(boolean forceGd) {
		this.forceGd = forceGd;
	}

	public final boolean isForceCairo() {
		return forceCairo;
	}

	public final void setForceCairo(boolean forceCairo) {
		this.forceCairo = forceCairo;
	}

	public final boolean isQuiet() {
		return quiet;
	}

	public final void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

}
