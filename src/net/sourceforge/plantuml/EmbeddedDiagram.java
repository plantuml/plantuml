/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml;

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.Line;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UImageSvg;
import net.sourceforge.plantuml.ugraphic.UShape;

public class EmbeddedDiagram implements CharSequence {

	private final Display system;

	public EmbeddedDiagram(Display system) {
		this.system = system;
	}

	public int length() {
		return toString().length();
	}

	public char charAt(int index) {
		return toString().charAt(index);
	}

	public CharSequence subSequence(int start, int end) {
		return toString().subSequence(start, end);
	}

	public Draw asDraw(ISkinSimple skinParam) {
		return new Draw(skinParam);
	}

	public class Draw extends AbstractTextBlock implements Line, Atom {
		private BufferedImage image;
		private final ISkinSimple skinParam;

		public List<Atom> splitInTwo(StringBounder stringBounder, double width) {
			throw new UnsupportedOperationException(getClass().toString());
		}
		
		private Draw(ISkinSimple skinParam) {
			this.skinParam = skinParam;
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
				final boolean isSvg = ug.matchesProperty("SVG");
				if (isSvg) {
					final String imageSvg = getImageSvg();
					final SvgString svg = new SvgString(imageSvg, 1);
					ug.draw(new UImageSvg(svg));
					return;
				}
				final BufferedImage im = getImage();
				final UShape image = new UImage(im);
				ug.draw(image);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		private String getImageSvg() throws IOException, InterruptedException {
			final Diagram system = getSystem();
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			system.exportDiagram(os, 0, new FileFormatOption(FileFormat.SVG));
			os.close();
			return new String(os.toByteArray());
		}

		private BufferedImage getImage() throws IOException, InterruptedException {
			if (image == null) {
				image = getImageSlow();
			}
			return image;
		}

		private BufferedImage getImageSlow() throws IOException, InterruptedException {
			final Diagram system = getSystem();
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			system.exportDiagram(os, 0, new FileFormatOption(FileFormat.PNG));
			os.close();
			final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			final BufferedImage im = ImageIO.read(is);
			is.close();
			return im;
		}

		public HorizontalAlignment getHorizontalAlignment() {
			return HorizontalAlignment.LEFT;
		}

		private Diagram getSystem() throws IOException, InterruptedException {
			final BlockUml blockUml = new BlockUml(system.as2(), Defines.createEmpty(), skinParam, null);
			return blockUml.getDiagram();

		}
	}

}
