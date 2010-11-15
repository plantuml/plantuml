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
package net.sourceforge.plantuml.ugraphic.svg;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.UnusedSpace;
import net.sourceforge.plantuml.svg.SvgGraphics;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;

public class UGraphicSvg extends AbstractUGraphic<SvgGraphics> implements ClipContainer {

	final static Graphics2D imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB).createGraphics();
	private UClip clip;

	private final StringBounder stringBounder;

	public UGraphicSvg(String backcolor, boolean textAsPath) {
		this(new SvgGraphics(backcolor), textAsPath);
	}

	public UGraphicSvg(boolean textAsPath) {
		this(new SvgGraphics(), textAsPath);
	}

	private UGraphicSvg(SvgGraphics svg, boolean textAsPath) {
		super(svg);
		stringBounder = StringBounderUtils.asStringBounder(imDummy);
		registerDriver(URectangle.class, new DriverRectangleSvg(this));
		textAsPath = false;
		if (textAsPath) {
			registerDriver(UText.class, new DriverTextAsPathSvg(imDummy.getFontRenderContext(), this));
		} else {
			registerDriver(UText.class, new DriverTextSvg(getStringBounder(), this));
		}
		registerDriver(ULine.class, new DriverLineSvg(this));
		registerDriver(UPolygon.class, new DriverPolygonSvg(this));
		registerDriver(UEllipse.class, new DriverEllipseSvg());
		registerDriver(UImage.class, new DriverImageSvg());
		registerDriver(UPath.class, new DriverPathSvg(this));
	}

	public SvgGraphics getSvgGraphics() {
		return this.getGraphicObject();
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public void createXml(OutputStream os) throws IOException {
		try {
			getGraphicObject().createXml(os);
		} catch (TransformerException e) {
			throw new IOException(e.toString());
		}
	}

	public void setClip(UClip clip) {
		this.clip = clip == null ? null : clip.translate(getTranslateX(), getTranslateY());
	}

	public UClip getClip() {
		return clip;
	}

	public void centerCharOld(double x, double y, char c, Font font) {
		final UText uText = new UText("" + c, new FontConfiguration(font, getParam().getColor()));
		final UnusedSpace unusedSpace = UnusedSpace.getUnusedSpace(font, c);
		draw(x - unusedSpace.getCenterX() + getTranslateX(), y - unusedSpace.getCenterY() + getTranslateY(), uText);
	}

	public void centerChar(double x, double y, char c, Font font) {
		final UnusedSpace unusedSpace = UnusedSpace.getUnusedSpace(font, c);

		final double xpos = x - unusedSpace.getCenterX() - 0.5;
		final double ypos = y - unusedSpace.getCenterY() - 0.5;

		final TextLayout t = new TextLayout("" + c, font, imDummy.getFontRenderContext());
		getGraphicObject().setStrokeColor(HtmlColor.getAsHtml(getParam().getColor()));
		DriverTextAsPathSvg.drawPathIterator(getGraphicObject(), xpos + getTranslateX(), ypos + getTranslateY(), t
				.getOutline(null).getPathIterator(null));
	}

}
