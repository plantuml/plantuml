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
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.project.graphic.GanttDiagram;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public class PSystemProject extends AbstractPSystem {

	private final Project project = new Project();
	private final Color background = Color.WHITE;

	public int getNbImages() {
		return 1;
	}

	public String getDescription() {
		return "(Project)";
	}

	public void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final GanttDiagram diagram = new GanttDiagram(project);
		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		if (fileFormat == FileFormat.PNG) {
			final BufferedImage im = createImage(diagram);
			PngIO.write(im, os, getMetadata(), 96);
		} else if (fileFormat == FileFormat.SVG) {
			final UGraphicSvg svg = new UGraphicSvg(HtmlColor.getAsHtml(background), false);
			diagram.draw(svg, 0, 0);
			svg.createXml(os);
		} else if (fileFormat == FileFormat.EPS) {
			final UGraphicEps eps = new UGraphicEps(EpsStrategy.getDefault());
			diagram.draw(eps, 0, 0);
			os.write(eps.getEPSCode().getBytes());
		} else {
			throw new UnsupportedOperationException();
		}
	}

	private BufferedImage createImage(GanttDiagram diagram) {
		EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, background);
		Graphics2D g2d = builder.getGraphics2D();
		UGraphicG2d ug = new UGraphicG2d(g2d, null, 1.0);

		final double height = diagram.getHeight(ug.getStringBounder());
		final double width = diagram.getWidth(ug.getStringBounder());

		g2d.dispose();

		builder = new EmptyImageBuilder(width, height, background);
		final BufferedImage im = builder.getBufferedImage();
		g2d = builder.getGraphics2D();

		ug = new UGraphicG2d(g2d, im, 1.0);
		diagram.draw(ug, 0, 0);
		g2d.dispose();
		return im;
	}

	public final Project getProject() {
		return project;
	}

}
