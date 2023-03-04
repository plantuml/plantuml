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
package net.sourceforge.plantuml.file;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.security.SFile;

public class SuggestedFile {
	// ::remove file when __CORE__

	private final FileFormat fileFormat;
	private final int initialCpt;
	private final SFile outputFile;

	private SuggestedFile(SFile outputFile, FileFormat fileFormat, int initialCpt) {
		if (outputFile.getName().endsWith(fileFormat.getFileSuffix())) {
			throw new IllegalArgumentException();
		}
		this.outputFile = outputFile;
		this.fileFormat = fileFormat;
		this.initialCpt = initialCpt;
	}

	public SuggestedFile withPreprocFormat() {
		return new SuggestedFile(outputFile, FileFormat.PREPROC, initialCpt);
	}

	@Override
	public String toString() {
		return outputFile.getPrintablePath() + "[" + initialCpt + "]";
	}

	public static SuggestedFile fromOutputFile(SFile outputFile, FileFormat fileFormat) {
		return fromOutputFile(outputFile, fileFormat, 0);
	}

	public static SuggestedFile fromOutputFile(java.io.File outputFile, FileFormat fileFormat) {
		return fromOutputFile(outputFile, fileFormat, 0);
	}

	public SFile getParentFile() {
		return outputFile.getParentFile();
	}

	public String getName() {
		return outputFile.getName();
	}

	public SFile getFile(int cpt) {
		final String newName = fileFormat.changeName(outputFile.getName(), initialCpt + cpt);
		return outputFile.getParentFile().file(newName);
	}

	public static SuggestedFile fromOutputFile(SFile outputFile, FileFormat fileFormat, int initialCpt) {
		return new SuggestedFile(outputFile, fileFormat, initialCpt);
	}

	public static SuggestedFile fromOutputFile(java.io.File outputFile, FileFormat fileFormat, int initialCpt) {
		return new SuggestedFile(SFile.fromFile(outputFile), fileFormat, initialCpt);
	}

	public SFile getTmpFile() {
		return getParentFile().file(getName() + ".tmp");
	}

}
