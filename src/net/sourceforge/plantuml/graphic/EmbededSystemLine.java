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

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmbededDiagram;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PSystem;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UShape;

class EmbededSystemLine implements Line {

	final List<String> lines;

	public EmbededSystemLine(EmbededDiagram sys) {
		this.lines = sys.getLines();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		try {
			final BufferedImage im = getImage();
			return new Dimension2DDouble(im.getWidth(), im.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Dimension2DDouble(42, 42);
	}

	public void draw(Graphics2D g2d, double x, double y) {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug, double x, double y) {
		try {
			final BufferedImage im = getImage();
			final UShape image = new UImage(im);
			ug.draw(x, y, image);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private BufferedImage getImage() throws IOException, InterruptedException {
		final PSystem system = getSystem();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		system.exportDiagram(os, null, 0, new FileFormatOption(FileFormat.PNG));
		os.close();
		final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		final BufferedImage im = ImageIO.read(is);
		is.close();
		return im;
	}

	public HorizontalAlignement getHorizontalAlignement() {
		return HorizontalAlignement.LEFT;
	}

	private PSystem getSystem() throws IOException, InterruptedException {
		final BlockUml blockUml = new BlockUml(lines);
		return blockUml.getSystem();

	}

}
