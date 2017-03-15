/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmbededDiagram;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UShape;

class AtomEmbededSystem implements Atom {

	final private List<CharSequence2> lines2;

	public AtomEmbededSystem(EmbededDiagram sys) {
		this.lines2 = sys.getLines().as2();
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
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

	public void drawU(UGraphic ug) {
		try {
			final BufferedImage im = getImage();
			final UShape image = new UImage(im);
			ug.draw(image);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private BufferedImage getImage() throws IOException, InterruptedException {
		final Diagram system = getSystem();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		system.exportDiagram(os, 0, new FileFormatOption(FileFormat.PNG));
		os.close();
		final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		final BufferedImage im = ImageIO.read(is);
		is.close();
		return im;
	}

	// public HorizontalAlignment getHorizontalAlignment() {
	// return HorizontalAlignment.LEFT;
	// }
	//
	private Diagram getSystem() throws IOException, InterruptedException {
		final BlockUml blockUml = new BlockUml(lines2, 0);
		return blockUml.getDiagram();
	}
	
}
