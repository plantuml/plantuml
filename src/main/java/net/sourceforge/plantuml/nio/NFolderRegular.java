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
package net.sourceforge.plantuml.nio;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import net.sourceforge.plantuml.security.SFile;

public class NFolderRegular implements NFolder {

	private final Path dir;

	public NFolderRegular(Path dir) {
		this.dir = Objects.requireNonNull(dir);
	}

	@Override
	public InputFile getInputFile(Path nameOrPath) throws IOException {
		final SFile result;
		if (nameOrPath.isAbsolute())
			result = SFile.fromFile(nameOrPath.toFile());
		else
			result = SFile.fromFile(dir.resolve(nameOrPath).toFile());

		if (result.canRead())
			return result;
		
		return null;

	}

	@Override
	public OutputFile getOutputFile(Path nameOrPath) throws IOException {
		throw new UnsupportedOperationException(nameOrPath.toString());
	}

	@Override
	public NFolder getSubfolder(Path nameOrPath) throws IOException {
		final Path sub = dir.resolve(nameOrPath).normalize();
		return new NFolderRegular(sub);
	}

	@Override
	public String toString() {
		return "NFolderRegular[" + dir + "]";
	}

}
