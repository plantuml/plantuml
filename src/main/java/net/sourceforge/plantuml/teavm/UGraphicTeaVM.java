/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.teavm;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.drawing.AbstractCommonUGraphic;
import net.sourceforge.plantuml.klimt.drawing.AbstractUGraphic;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverCenteredCharacterSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverDotPathSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverEllipseSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverImagePng;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverImageSvgSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverLineSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverPathSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverPixelSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverPolygonSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverRectangleSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverTextAsPathSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.DriverTextSvg;
import net.sourceforge.plantuml.klimt.drawing.svg.SvgGraphics;
import net.sourceforge.plantuml.klimt.drawing.svg.SvgOption;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.klimt.shape.UCenteredCharacter;
import net.sourceforge.plantuml.klimt.shape.UComment;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPixel;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.url.Url;

public class UGraphicTeaVM extends AbstractUGraphic<SvgGraphicsTeaVM> implements ClipContainer {
	
	// @Claude: tu peux t'inspirer de UGraphicSVG.java

	private void register() {
		registerDriver(URectangle.class, new DriverRectangleTeaVM(this));
	}

	public SvgGraphicsTeaVM getSvgGraphics() {
		
	}

}
