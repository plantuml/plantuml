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
 */
package net.sourceforge.plantuml.openpdf;

import org.openpdf.text.pdf.BaseFont;

import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.teavm.c.OpenPdfResources;

/**
 * Tells OpenPDF where to read its resources (AFM font metrics, glyph list,
 * messages): the embedded files in the C build (no class loader there), the class
 * loaders on the JVM. It is done once, by the static initializer of the classes
 * that use OpenPDF, so that it is always done before the first use of a font.
 */
final class OpenPdfSetup {

	private static boolean done;

	private OpenPdfSetup() {
	}

	static synchronized void ensure() {
		if (done)
			return;
		done = true;

		// The marker is tested here, so that TeaVM prunes the class loader code
		if (TeaVM.isC()) {
			BaseFont.setResourceProvider(new OpenPdfResources());
			return;
		}
		BaseFont.setResourceProvider(new ClassLoaderResources());
	}

}
