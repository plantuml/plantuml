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
 * Revision $Revision: 6009 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;

public class GraphicStrings {

	private final Color background;

	private final Font font;

	private final Color green;

	private final List<String> strings;

	private final BufferedImage image;

	private final GraphicPosition position;

	private final boolean disableTextAliasing;

	public GraphicStrings(List<String> strings) {
		this(strings, new Font("SansSerif", Font.BOLD, 14), new Color(Integer.parseInt("33FF02", 16)), Color.BLACK,
				null, null, false);
	}

	public GraphicStrings(List<String> strings, BufferedImage image) {
		this(strings, new Font("SansSerif", Font.BOLD, 14), new Color(Integer.parseInt("33FF02", 16)), Color.BLACK,
				image, null, false);
	}

	public GraphicStrings(List<String> strings, Font font, Color green, Color background, boolean disableTextAliasing) {
		this(strings, font, green, background, null, null, disableTextAliasing);
	}

	public GraphicStrings(List<String> strings, Font font, Color green, Color background, BufferedImage image,
			GraphicPosition position, boolean disableTextAliasing) {
		this.strings = strings;
		this.font = font;
		this.green = green;
		this.background = background;
		this.image = image;
		this.position = position;
		this.disableTextAliasing = disableTextAliasing;
	}

	public void writeImage(OutputStream os, FileFormatOption fileFormat) throws IOException {
		writeImage(os, null, fileFormat);
	}

	public void writeImage(OutputStream os, String metadata, FileFormatOption fileFormatOption) throws IOException {
		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		if (fileFormat == FileFormat.PNG) {
			final BufferedImage im = createImage();
			PngIO.write(im, os, metadata, 96);
		} else if (fileFormat == FileFormat.SVG || fileFormat == FileFormat.EPS_VIA_SVG) {
			final UGraphicSvg svg = new UGraphicSvg(HtmlColor.getAsHtml(background), false);
			drawU(svg);
			svg.createXml(os);
		} else if (fileFormat == FileFormat.ATXT || fileFormat == FileFormat.UTXT) {
			final UGraphicTxt txt = new UGraphicTxt();
			drawU(txt);
			txt.getCharArea().print(new PrintStream(os));
		} else {
			throw new UnsupportedOperationException();
		}
	}

	private BufferedImage createImage() {
		EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, background);
		// BufferedImage im = builder.getBufferedImage();
		Graphics2D g2d = builder.getGraphics2D();

		final Dimension2D size = drawU(new UGraphicG2d(g2d, null, 1.0));
		g2d.dispose();

		builder = new EmptyImageBuilder(size.getWidth(), size.getHeight(), background);
		final BufferedImage im = builder.getBufferedImage();
		g2d = builder.getGraphics2D();
		if (disableTextAliasing) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		drawU(new UGraphicG2d(g2d, null, 1.0));
		g2d.dispose();
		return im;
	}

	public Dimension2D drawU(final UGraphic ug) {
		final TextBlock textBlock = TextBlockUtils.create(strings, new FontConfiguration(font, green),
				HorizontalAlignement.LEFT);
		Dimension2D size = textBlock.calculateDimension(ug.getStringBounder());
		textBlock.drawU(ug, 0, 0);

		if (image != null) {
			if (position == GraphicPosition.BOTTOM) {
				ug.draw((size.getWidth() - image.getWidth()) / 2, size.getHeight(), new UImage(image));
				size = new Dimension2DDouble(size.getWidth(), size.getHeight() + image.getHeight());
			} else if (position == GraphicPosition.BACKGROUND_CORNER) {
				ug.draw(size.getWidth() - image.getWidth(), size.getHeight() - image.getHeight(), new UImage(image));
			}
		}
		return size;
	}

}
