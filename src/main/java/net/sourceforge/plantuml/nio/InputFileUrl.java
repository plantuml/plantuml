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
import java.io.InputStream;

import net.sourceforge.plantuml.security.SURL;

public class InputFileUrl implements InputFile {

	private final SURL url;

	public InputFileUrl(SURL url) {
		this.url = url;
	}

	@Override
	public InputStream newInputStream() throws IOException {
		final InputStream is = url.openStream();
		if (is == null)
			throw new IOException("Cannot open URL " + url);
		return url.openStream();
	}

	@Override
	public NFolder getParentFolder() throws IOException {
		throw new UnsupportedOperationException();
	}

}
