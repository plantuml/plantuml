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
 * Revision $Revision: 5804 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.cucadiagram.dot.DrawFile;
import net.sourceforge.plantuml.cucadiagram.dot.Lazy;
import net.sourceforge.plantuml.skin.UDrawable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.USegmentType;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class CircledCharacter implements UDrawable, TextBlock {

	private final String c;
	private final Font font;
	private final Color innerCircle;
	private final Color circle;
	private final Color fontColor;
	private final double radius;

	public CircledCharacter(char c, double radius, Font font, Color innerCircle, Color circle, Color fontColor) {
		this.c = "" + c;
		this.radius = radius;
		this.font = font;
		this.innerCircle = innerCircle;
		this.circle = circle;
		this.fontColor = fontColor;
	}

	public void draw(Graphics2D g2d, int x, int y, double dpiFactor) {
		drawU(new UGraphicG2d(g2d, null, 1.0), x, y);
	}

	public void drawU(UGraphic ug, double x, double y) {

		if (circle != null) {
			ug.getParam().setColor(circle);
		}

		ug.getParam().setBackcolor(innerCircle);

		ug.draw(x, y, new UEllipse(radius * 2, radius * 2));
		ug.getParam().setColor(fontColor);
		ug.centerChar(x + radius, y + radius, c.charAt(0), font);

	}

	final public double getPreferredWidth(StringBounder stringBounder) {
		return 2 * radius;
	}

	final public double getPreferredHeight(StringBounder stringBounder) {
		return 2 * radius;
	}

	public void drawU(UGraphic ug) {
		drawU(ug, 0, 0);
	}

	private PathIterator getPathIteratorCharacter(FontRenderContext frc) {
		final TextLayout textLayout = new TextLayout(c, font, frc);
		final Shape s = textLayout.getOutline(null);
		return s.getPathIterator(null);
	}

	public UPath getUPath(FontRenderContext frc) {
		final UPath result = new UPath();

		final PathIterator path = getPathIteratorCharacter(frc);

		final double coord[] = new double[6];
		while (path.isDone() == false) {
			final int code = path.currentSegment(coord);
			result.add(coord, USegmentType.getByCode(code));
			path.next();
		}

		return result;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getPreferredWidth(stringBounder), getPreferredHeight(stringBounder));
	}

	public void drawTOBEREMOVED(Graphics2D g2d, double x, double y) {
		throw new UnsupportedOperationException();
	}

	public DrawFile generateCircleCharacter(final Color background, final double dpiFactor) throws IOException {

		final Lazy<File> lpng = new Lazy<File>() {

			public File getNow() throws IOException {
				final File png = FileUtils.createTempFile("circle", ".png");
				final EmptyImageBuilder builder = new EmptyImageBuilder((int)(60 * dpiFactor), (int)(60 * dpiFactor), background, dpiFactor);
				BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();
				final StringBounder stringBounder = StringBounderUtils.asStringBounder(g2d);

				draw(g2d, 0, 0, dpiFactor);
				im = im.getSubimage(0, 0, (int) (getPreferredWidth(stringBounder) * dpiFactor) + 5,
						(int) (getPreferredHeight(stringBounder) * dpiFactor) + 1);

				ImageIO.write(im, "png", png);
				return png;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File epsFile = FileUtils.createTempFile("circle", ".eps");
				UGraphicEps.copyEpsToFile(CircledCharacter.this, epsFile);
				return epsFile;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				return UGraphicG2d.getSvgString(CircledCharacter.this);
			}

		};

		final Object signature = Arrays.asList("circle", c, font, innerCircle, circle, fontColor, radius, background,
				dpiFactor);

		return DrawFile.create(lpng, lsvg, leps, signature);
	}

}
