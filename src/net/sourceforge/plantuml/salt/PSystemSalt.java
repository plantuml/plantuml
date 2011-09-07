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
 * Revision $Revision: 4041 $
 *
 */
package net.sourceforge.plantuml.salt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class PSystemSalt extends AbstractPSystem {

	private final List<String> data;

	public PSystemSalt(List<String> data) {
		this.data = data;
	}

	public void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormat)
			throws IOException {

		final Element salt = SaltUtils.createElement(data);

		EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		Graphics2D g2d = builder.getGraphics2D();

		final Dimension2D size = salt.getPreferredDimension(new UGraphicG2d(new ColorMapperIdentity(), g2d, null, 1.0)
				.getStringBounder(), 0, 0);
		g2d.dispose();

		builder = new EmptyImageBuilder(size.getWidth() + 6, size.getHeight() + 6, Color.WHITE);
		final BufferedImage im = builder.getBufferedImage();
		g2d = builder.getGraphics2D();
		g2d.translate(3, 3);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		final UGraphic ug = new UGraphicG2d(new ColorMapperIdentity(), g2d, null, 1.0);
		ug.getParam().setColor(HtmlColor.BLACK);
		salt.drawU(ug, 0, 0, 0, new Dimension2DDouble(size.getWidth(), size.getHeight()));
		salt.drawU(ug, 0, 0, 1, new Dimension2DDouble(size.getWidth(), size.getHeight()));
		g2d.dispose();

		// Writes the off-screen image into a PNG file
		ImageIO.write(im, "png", os);
	}

	public String getDescription() {
		return "(Salt)";
	}

}
