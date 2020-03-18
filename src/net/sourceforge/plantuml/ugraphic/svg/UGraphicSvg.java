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
 */
package net.sourceforge.plantuml.ugraphic.svg;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.TikzFontDistortion;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.svg.SvgGraphics;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UCenteredCharacter;
import net.sourceforge.plantuml.ugraphic.UComment;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic2;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UImageSvg;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;

public class UGraphicSvg extends AbstractUGraphic<SvgGraphics> implements ClipContainer, UGraphic2 {

	private final StringBounder stringBounder;
	private final boolean textAsPath2;
	private final String target;

	public double dpiFactor() {
		return 1;
	}

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicSvg(this);
	}

	private UGraphicSvg(UGraphicSvg other) {
		super(other);
		this.stringBounder = other.stringBounder;
		this.textAsPath2 = other.textAsPath2;
		this.target = other.target;
		register();
	}

	public UGraphicSvg(boolean svgDimensionStyle, Dimension2D minDim, ColorMapper colorMapper, String backcolor,
			boolean textAsPath, double scale, String linkTarget, String hover, long seed, String preserveAspectRatio) {
		this(minDim, colorMapper, new SvgGraphics(svgDimensionStyle, minDim, backcolor, scale, hover, seed,
				preserveAspectRatio), textAsPath, linkTarget);
	}

	public UGraphicSvg(boolean svgDimensionStyle, Dimension2D minDim, ColorMapper colorMapper, boolean textAsPath,
			double scale, String linkTarget, String hover, long seed, String preserveAspectRatio) {
		this(minDim, colorMapper, new SvgGraphics(svgDimensionStyle, minDim, scale, hover, seed, preserveAspectRatio),
				textAsPath, linkTarget);
	}

	public UGraphicSvg(boolean svgDimensionStyle, Dimension2D minDim, ColorMapper mapper, HColorGradient gr,
			boolean textAsPath, double scale, String linkTarget, String hover, long seed, String preserveAspectRatio) {
		this(minDim, mapper, new SvgGraphics(svgDimensionStyle, minDim, scale, hover, seed, preserveAspectRatio),
				textAsPath, linkTarget);

		final SvgGraphics svg = getGraphicObject();
		svg.paintBackcolorGradient(mapper, gr);
	}

	@Override
	protected boolean manageHiddenAutomatically() {
		return false;
	}

	@Override
	protected void beforeDraw() {
		getGraphicObject().setHidden(getParam().isHidden());
	}

	@Override
	protected void afterDraw() {
		getGraphicObject().setHidden(false);
	}

	private UGraphicSvg(Dimension2D minDim, ColorMapper colorMapper, SvgGraphics svg, boolean textAsPath,
			String linkTarget) {
		super(colorMapper, svg);
		this.stringBounder = FileFormat.PNG.getDefaultStringBounder(TikzFontDistortion.getDefault());
		this.textAsPath2 = textAsPath;
		this.target = linkTarget;
		register();
	}

	private void register() {
		registerDriver(URectangle.class, new DriverRectangleSvg(this));
		if (textAsPath2) {
			registerDriver(UText.class, new DriverTextAsPathSvg(TextBlockUtils.getFontRenderContext(), this));
		} else {
			registerDriver(UText.class, new DriverTextSvg(getStringBounder(), this));
		}
		registerDriver(ULine.class, new DriverLineSvg(this));
		registerDriver(UPolygon.class, new DriverPolygonSvg(this));
		registerDriver(UEllipse.class, new DriverEllipseSvg(this));
		registerDriver(UImage.class, new DriverImagePng(this));
		registerDriver(UImageSvg.class, new DriverImageSvgSvg());
		registerDriver(UPath.class, new DriverPathSvg(this));
		registerDriver(DotPath.class, new DriverDotPathSvg());
		registerDriver(UCenteredCharacter.class, new DriverCenteredCharacterSvg());
	}

	public SvgGraphics getSvgGraphics() {
		return this.getGraphicObject();
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public void createXml(OutputStream os, String metadata) throws IOException {
		try {
			if (metadata != null) {
				getGraphicObject().addComment(metadata);
			}
			getGraphicObject().createXml(os);
		} catch (TransformerException e) {
			throw new IOException(e.toString());
		}
	}

	public void startUrl(Url url) {
		getGraphicObject().openLink(url.getUrl(), url.getTooltip(), target);
	}

	public void closeAction() {
		getGraphicObject().closeLink();
	}

	public void writeImageTOBEMOVED(OutputStream os, String metadata, int dpi) throws IOException {
		createXml(os, metadata);
	}

	@Override
	protected void drawComment(UComment comment) {
		getGraphicObject().addComment(comment.getComment());
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		if (propertyName.equalsIgnoreCase("SVG")) {
			return true;
		}
		return super.matchesProperty(propertyName);
	}

	// @Override
	// public String startHiddenGroup() {
	// getGraphicObject().startHiddenGroup();
	// return null;
	// }
	//
	// @Override
	// public String closeHiddenGroup() {
	// getGraphicObject().closeHiddenGroup();
	// return null;
	// }

}
