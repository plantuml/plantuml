/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 4207 $
 *
 */
package net.sourceforge.plantuml.eps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramFileMaker;

public class SvgToEpsConverter {

	private final Inkscape inkscape;
	private final File svgFile;

	public SvgToEpsConverter(String svg) throws IOException {
		if (svg == null) {
			throw new IllegalArgumentException();
		}
		this.inkscape = InkscapeUtils.create();
		this.svgFile = CucaDiagramFileMaker.createTempFile("convert", ".svg");
		final PrintWriter pw = new PrintWriter(svgFile);
		pw.println(svg);
		pw.close();
	}

	public SvgToEpsConverter(File svgFile) {
		if (svgFile == null) {
			throw new IllegalArgumentException();
		}
		this.inkscape = InkscapeUtils.create();
		this.svgFile = svgFile;
	}

	public void createEps(File epsFile) throws IOException, InterruptedException {
		inkscape.createEps(svgFile, epsFile);
	}


	public void createEps(OutputStream os) throws IOException, InterruptedException {
		final File epsFile = CucaDiagramFileMaker.createTempFile("eps", ".eps");
		createEps(epsFile);
		int read;
		final InputStream is = new FileInputStream(epsFile);
		while ((read = is.read()) != -1) {
			os.write(read);
		}
		is.close();
		if (OptionFlags.getInstance().isKeepTmpFiles() == false) {
			epsFile.delete();
		}
	}

}
