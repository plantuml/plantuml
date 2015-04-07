/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 6600 $
 *
 */
package net.sourceforge.plantuml.pdf;
import java.io.File;
import java.lang.reflect.Method;

public class PdfConverter {

	public static void convert(File svgFile, File pdfFile) {

		if (svgFile.exists() == false) {
			throw new IllegalArgumentException();
		}
		pdfFile.delete();
		if (pdfFile.exists()) {
			throw new IllegalStateException();
		}

		try {
			final Class<?> clSVGConverter = Class.forName("org.apache.batik.apps.rasterizer.SVGConverter");

			final Object converter = clSVGConverter.newInstance();

			final Class<?> clDestinationType = Class.forName("org.apache.batik.apps.rasterizer.DestinationType");
			final Object pdf = clDestinationType.getField("PDF").get(null);
			final Method setDestinationType = clSVGConverter.getMethod("setDestinationType", clDestinationType);

			setDestinationType.invoke(converter, pdf);

			final String[] path = new String[] { svgFile.getAbsolutePath() };
			final Method setSources = clSVGConverter.getMethod("setSources", path.getClass());
			setSources.invoke(converter, new Object[] { path });
			final Method setDst = clSVGConverter.getMethod("setDst", pdfFile.getClass());
			setDst.invoke(converter, new Object[] { pdfFile });
			final Method execute = clSVGConverter.getMethod("execute");
			execute.invoke(converter);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnsupportedOperationException();
		}
		if (pdfFile.exists() == false) {
			throw new IllegalStateException();
		}
	}
}
