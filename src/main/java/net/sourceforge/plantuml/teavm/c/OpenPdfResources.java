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
 */
package net.sourceforge.plantuml.teavm.c;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.openpdf.text.pdf.ResourceProvider;

// ::remove file when JAVA8

/**
 * Where OpenPDF finds its resources (AFM font metrics, glyph list, messages) in the
 * C build, which has no class loader: in the files embedded in the executable, under
 * "openpdf/" (copied from the OpenPDF jar by Gradle, see copyNativeSources).
 */
public final class OpenPdfResources implements ResourceProvider {

	public OpenPdfResources() {
	}

	@Override
	public InputStream open(String name) throws IOException {
		final byte[] data = EmbeddedFiles.read("openpdf/" + name);
		if (data == null)
			return null;

		return new ByteArrayInputStream(data);
	}

}
