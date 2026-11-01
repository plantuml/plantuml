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
package net.sourceforge.plantuml.teavm.c;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.scilab.forge.jlatexmath.TeXResourceProvider;

// ::remove file when JAVA8 or __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__

/**
 * Where JLaTeXMath finds its resources (the XML files of the font metrics, the
 * symbols and the macros, and the glyph outlines of {@code glyphs.bin}) in the C
 * build, which has no class loader: in the files embedded in the executable, under
 * "jlatexmath/" (copied by Gradle from src/main/resources/org/scilab/forge/jlatexmath,
 * see copyNativeSources). On the JVM, the class loaders are used.
 */
public final class JlatexmathResources implements TeXResourceProvider {

	/** The folder of the resources of the library, in the class path. */
	static final String PACKAGE = "org/scilab/forge/jlatexmath/";

	public JlatexmathResources() {
	}

	/**
	 * The name of the embedded file of a resource of the library, or null if the
	 * name is not one of the library: "org/scilab/forge/jlatexmath/fonts/x.xml" is
	 * "jlatexmath/fonts/x.xml".
	 */
	static String embeddedName(String resourceName) {
		if (resourceName.startsWith(PACKAGE) == false)
			return null;

		return "jlatexmath/" + resourceName.substring(PACKAGE.length());
	}

	@Override
	public InputStream open(String name) throws IOException {
		final String embedded = embeddedName(name);
		if (embedded == null)
			return null;

		final byte[] data = EmbeddedFiles.read(embedded);
		if (data == null)
			return null;

		return new ByteArrayInputStream(data);
	}

}
