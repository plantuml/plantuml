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
 * Revision $Revision: 7211 $
 *
 */
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramFileMaker;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.UnusedSpace;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.skin.UDrawable;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPixel;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public class UGraphicG2d extends AbstractUGraphic<Graphics2D> {

	private final BufferedImage bufferedImage;

	private final double dpiFactor;

	public UGraphicG2d(ColorMapper colorMapper, Graphics2D g2d, BufferedImage bufferedImage, double dpiFactor) {
		super(colorMapper, g2d);
		this.dpiFactor = dpiFactor;
		if (dpiFactor != 1.0) {
			final AffineTransform at = g2d.getTransform();
			at.concatenate(AffineTransform.getScaleInstance(dpiFactor, dpiFactor));
			g2d.setTransform(at);
		}
		this.bufferedImage = bufferedImage;
		registerDriver(URectangle.class, new DriverRectangleG2d());
		registerDriver(UText.class, new DriverTextG2d());
		registerDriver(ULine.class, new DriverLineG2d());
		registerDriver(UPixel.class, new DriverPixelG2d());
		registerDriver(UPolygon.class, new DriverPolygonG2d());
		registerDriver(UEllipse.class, new DriverEllipseG2d());
		registerDriver(UImage.class, new DriverImageG2d());
		registerDriver(DotPath.class, new DriverDotPathG2d());
		registerDriver(UPath.class, new DriverPathG2d());
	}

	public StringBounder getStringBounder() {
		return StringBounderUtils.asStringBounder(getGraphicObject());
	}

	public final BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setClip(UClip uclip) {
		if (uclip == null) {
			getGraphicObject().setClip(null);
		} else {
			final Shape clip = new Rectangle2D.Double(uclip.getX() + getTranslateX(), uclip.getY() + getTranslateY(),
					uclip.getWidth(), uclip.getHeight());
			getGraphicObject().setClip(clip);
		}
	}

	public void centerChar(double x, double y, char c, UFont font) {
		final UnusedSpace unusedSpace = UnusedSpace.getUnusedSpace(font, c);

		getGraphicObject().setColor(getColorMapper().getMappedColor(getParam().getColor()));
		final double xpos = x - unusedSpace.getCenterX();
		final double ypos = y - unusedSpace.getCenterY() - 0.5;

		getGraphicObject().setFont(font.getFont());
		getGraphicObject().drawString("" + c, (float) (xpos + getTranslateX()), (float) (ypos + getTranslateY()));
		// getGraphicObject().drawString("" + c, Math.round(xpos +
		// getTranslateX()), Math.round(ypos + getTranslateY()));
	}

	static public String getSvgString(ColorMapper colorMapper, UDrawable udrawable) throws IOException {
		final UGraphicSvg ug = new UGraphicSvg(colorMapper, false);
		udrawable.drawU(ug);
		return CucaDiagramFileMaker.getSvg(ug);
	}

	protected final double getDpiFactor() {
		return dpiFactor;
	}

	public void setAntiAliasing(boolean trueForOn) {
		if (trueForOn) {
			getGraphicObject().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			getGraphicObject().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}

	}

	public void setUrl(String url, String tooltip) {
	}

}
