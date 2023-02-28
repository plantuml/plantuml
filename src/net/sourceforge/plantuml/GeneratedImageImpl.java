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
package net.sourceforge.plantuml;

import java.io.File;
import java.io.IOException;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;

public class GeneratedImageImpl implements GeneratedImage {
	// ::remove file when __HAXE__

	private final SFile pngFile;
	private final String description;
	private final BlockUml blockUml;
	private final int status;

	public final int getStatus() {
		return status;
	}

	public GeneratedImageImpl(SFile pngFile, String description, BlockUml blockUml, int status) {
		this.blockUml = blockUml;
		this.pngFile = pngFile;
		this.description = description;
		this.status = status;
	}

	public File getPngFile() {
		return pngFile.conv();
	}

	public String getDescription() {
		return description;
	}

	public int lineErrorRaw() {
		final Diagram system = blockUml.getDiagram();
		if (system instanceof PSystemError) {
			return ((PSystemError) system).getLineLocation().getPosition();
		}
		return -1;
	}

	@Override
	public String toString() {
		return pngFile.getPrintablePath() + " " + description;
	}

	public int compareTo(GeneratedImage this2) {
		try {
			final int cmp = this.getPngFile().getCanonicalPath().compareTo(this2.getPngFile().getCanonicalPath());
			if (cmp != 0) {
				return cmp;
			}
		} catch (IOException e) {
			Logme.error(e);
		}
		return this.description.compareTo(this2.getDescription());
	}

	@Override
	public int hashCode() {
		return pngFile.hashCode() + description.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final GeneratedImageImpl this2 = (GeneratedImageImpl) obj;
		return this2.pngFile.equals(this.pngFile) && this2.description.equals(this.description);
	}

	public BlockUml getBlockUml() {
		return blockUml;
	}
}
