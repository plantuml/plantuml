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
 */
package net.sourceforge.plantuml.version;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;

public class PSystemVersion extends AbstractPSystem {

	private final List<String> strings = new ArrayList<String>();
	private final BufferedImage image;

	PSystemVersion(boolean withImage, List<String> args) throws IOException {
		strings.addAll(args);
		if (withImage) {
			final InputStream is = getClass().getResourceAsStream("logo.png");
			image = ImageIO.read(is);
			is.close();
		} else {
			image = null;
		}
	}

	public List<File> createFiles(File suggestedFile, FileFormatOption fileFormat) throws IOException,
			InterruptedException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(suggestedFile);
			getGraphicStrings().writeImage(os, fileFormat);
		} finally {
			if (os != null) {
				os.close();
			}
		}
		return Arrays.asList(suggestedFile);
	}

	public void createFile(OutputStream os, int index, FileFormatOption fileFormat) throws IOException {
		getGraphicStrings().writeImage(os, fileFormat);
	}

	public static PSystemVersion createShowVersion() throws IOException {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.version() + "</b> (" + new Date(Version.compileTime()) + ")");
		strings.add(" ");

		strings.addAll(GraphvizUtils.getTestDotStrings(true));
		strings.add(" ");
		final Properties p = System.getProperties();
		strings.add(p.getProperty("java.runtime.name"));
		strings.add(p.getProperty("java.vm.name"));
		strings.add(p.getProperty("java.runtime.version"));
		strings.add(p.getProperty("os.name"));
		return new PSystemVersion(true, strings);
	}

	public static PSystemVersion createShowAuthors() throws IOException {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.version() + "</b> (" + new Date(Version.compileTime()) + ")");
		strings.add(" ");
		strings.add("<u>Original idea</u>: Arnaud Roques");
		strings.add("<u>Word Macro</u>: Alain Bertucat & Matthieu Sabatier");
		strings.add("<u>Eclipse Plugin</u>: Claude Durif & Anne Pecoil");
		strings.add("<u>Site design</u>: Raphael Cotisson");
		strings.add("<u>Logo</u>: Benjamin Croizet");

		strings.add(" ");
		strings.add("http://plantuml.sourceforge.net");
		strings.add(" ");
		return new PSystemVersion(true, strings);
	}

	public static PSystemVersion createTestDot() throws IOException {
		final List<String> strings = new ArrayList<String>();
		strings.addAll(GraphvizUtils.getTestDotStrings(true));
		return new PSystemVersion(false, strings);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final Font font = new Font("SansSerif", Font.PLAIN, 12);
		return new GraphicStrings(strings, font, Color.BLACK, Color.WHITE, image, GraphicPosition.BACKGROUND_CORNER,
				false);
		// return new GraphicStrings(strings, font, Color.BLACK, Color.WHITE,
		// false);
	}

	public String getDescription() {
		return "(Version)";
	}

}
