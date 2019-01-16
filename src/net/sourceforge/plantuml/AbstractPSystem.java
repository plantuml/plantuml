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

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ProtectedCommand;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.stats.StatsUtilsIncrement;
import net.sourceforge.plantuml.version.License;
import net.sourceforge.plantuml.version.Version;

public abstract class AbstractPSystem implements Diagram {

	private UmlSource source;
	private Scale scale;

	private String getVersion() {
		final StringBuilder toAppend = new StringBuilder();
		toAppend.append("PlantUML version ");
		toAppend.append(Version.versionString());
		toAppend.append("(" + Version.compileTimeString() + ")\n");
		toAppend.append("(" + License.getCurrent() + " source distribution)\n");
		for (String name : OptionPrint.interestingProperties()) {
			toAppend.append(name);
			toAppend.append(BackSlash.CHAR_NEWLINE);
		}
		return toAppend.toString();
	}

	final public String getMetadata() {
		if (source == null) {
			return getVersion();
		}
		return source.getPlainString() + BackSlash.NEWLINE + getVersion();
	}

	final public UmlSource getSource() {
		return source;
	}

	final public long seed() {
		if (source == null) {
			return 42;
		}
		return getSource().seed();
	}

	final public void setSource(UmlSource source) {
		this.source = source;
	}

	public int getNbImages() {
		return 1;
	}

	public DisplayPositionned getTitle() {
		if (source == null) {
			return DisplayPositionned.single(Display.empty(), HorizontalAlignment.CENTER, VerticalAlignment.TOP);
		}
		return DisplayPositionned.single(source.getTitle(), HorizontalAlignment.CENTER, VerticalAlignment.TOP);
	}

	public String getWarningOrError() {
		return null;
	}

	public String checkFinalError() {
		return null;
	}

	public void makeDiagramReady() {
	}

	public boolean isOk() {
		return true;
	}

	public CommandExecutionResult executeCommand(Command cmd, BlocLines lines) {
		cmd = new ProtectedCommand(cmd);
		return cmd.execute(this, lines);
	}

	public boolean hasUrl() {
		return false;
	}

	final public ImageData exportDiagram(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final long now = System.currentTimeMillis();
		try {
			return exportDiagramNow(os, index, fileFormatOption, seed());
		} finally {
			if (OptionFlags.getInstance().isEnableStats()) {
				StatsUtilsIncrement.onceMoreGenerate(System.currentTimeMillis() - now, getClass(),
						fileFormatOption.getFileFormat());
			}
		}
	}

	final public void setScale(Scale scale) {
		this.scale = scale;
	}

	final public Scale getScale() {
		return scale;
	}

	protected abstract ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption,
			long seed) throws IOException;

}
