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
package net.sourceforge.plantuml.braille;

import static net.sourceforge.plantuml.ugraphic.ImageBuilder.plainPngBuilder;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UCenteredCharacter;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;

// https://www.branah.com/braille-translator
public class UGraphicBraille extends AbstractUGraphic<BrailleGrid> implements ClipContainer {

	public static final int QUANTA = 4;

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicBraille(this);
	}

	public UGraphicBraille(HColor defaultBackground, ColorMapper colorMapper, StringBounder stringBounder) {
		super(defaultBackground, colorMapper, stringBounder, new BrailleGrid(QUANTA));
		register();
	}

	private UGraphicBraille(UGraphicBraille other) {
		super(other);
		register();
	}

	// public UGraphicBraille(ColorMapper colorMapper, String backcolor, boolean
	// textAsPath, double scale, String
	// linkTarget) {
	// this(colorMapper, new SvgGraphics(backcolor, scale), textAsPath, linkTarget);
	// }
	//
	// public UGraphicBraille(ColorMapper colorMapper, boolean textAsPath, double
	// scale, String linkTarget) {
	// this(colorMapper, new SvgGraphics(scale), textAsPath, linkTarget);
	// }
	//
	// public UGraphicBraille(ColorMapper mapper, HtmlColorGradient gr, boolean
	// textAsPath, double scale, String
	// linkTarget) {
	// this(mapper, new SvgGraphics(scale), textAsPath, linkTarget);
	//
	// final SvgGraphics svg = getGraphicObject();
	// svg.paintBackcolorGradient(mapper, gr);
	// }

	private void register() {
		ignoreShape(URectangle.class);
		registerDriver(URectangle.class, new DriverRectangleBraille(this));
		registerDriver(UText.class, new DriverTextBraille());
		registerDriver(ULine.class, new DriverLineBraille(this));
		registerDriver(UPolygon.class, new DriverPolygonBraille(this));
		ignoreShape(UEllipse.class);
		ignoreShape(UImage.class);
		ignoreShape(UPath.class);
		registerDriver(DotPath.class, new DriverDotPathBraille());
		registerDriver(UCenteredCharacter.class, new DriverCenteredCharacterBraille());
	}

	@Override
	public void writeToStream(OutputStream os, String metadata, int dpi) throws IOException {
		plainPngBuilder(new BrailleDrawer(getGraphicObject()))
				.metadata(metadata)
				.write(os);
	}
}
