/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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

import java.awt.geom.AffineTransform;
import java.io.Serializable;

import net.sourceforge.plantuml.graphic.StringBounder;

/**
 * A FileFormat with some parameters.
 * 
 * 
 * @author Arnaud Roques
 * 
 */
public class FileFormatOption implements Serializable {

	private final FileFormat fileFormat;
	private final AffineTransform affineTransform;
	private final boolean withMetadata;
	private final boolean useRedForError;
	private final String svgLinkTarget;

	public FileFormatOption(FileFormat fileFormat) {
		this(fileFormat, null, true, false, "_top", false);
	}

	public StringBounder getDefaultStringBounder() {
		return fileFormat.getDefaultStringBounder();
	}

	public String getSvgLinkTarget() {
		return svgLinkTarget;
	}

	public final boolean isWithMetadata() {
		return withMetadata;
	}

	public FileFormatOption(FileFormat fileFormat, boolean withMetadata) {
		this(fileFormat, null, false, false, "_top", false);
	}

	private FileFormatOption(FileFormat fileFormat, AffineTransform at, boolean withMetadata, boolean useRedForError,
			String svgLinkTarget, boolean debugsvek) {
		this.fileFormat = fileFormat;
		this.affineTransform = at;
		this.withMetadata = withMetadata;
		this.useRedForError = useRedForError;
		this.svgLinkTarget = svgLinkTarget;
		this.debugsvek = debugsvek;
	}

	public FileFormatOption withUseRedForError() {
		return new FileFormatOption(fileFormat, affineTransform, withMetadata, true, svgLinkTarget, debugsvek);
	}

	public FileFormatOption withSvgLinkTarget(String target) {
		return new FileFormatOption(fileFormat, affineTransform, withMetadata, useRedForError, target, debugsvek);
	}

	@Override
	public String toString() {
		return fileFormat.toString() + " " + affineTransform;
	}

	public final FileFormat getFileFormat() {
		return fileFormat;
	}

	public AffineTransform getAffineTransform() {
		return affineTransform;
	}

	public final boolean isUseRedForError() {
		return useRedForError;
	}

	private boolean debugsvek = false;

	public void setDebugSvek(boolean debugsvek) {
		this.debugsvek = debugsvek;
	}

	public boolean isDebugSvek() {
		return debugsvek;
	}

}
