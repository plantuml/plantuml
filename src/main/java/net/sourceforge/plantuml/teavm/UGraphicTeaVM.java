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

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.AbstractCommonUGraphic;
import net.sourceforge.plantuml.klimt.drawing.AbstractUGraphic;
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

	private UGraphicTeaVM(StringBounder stringBounder) {
		super(stringBounder);
		register();
	}

	public static UGraphicTeaVM build(HColor defaultBackground, ColorMapper colorMapper, StringBounder stringBounder,
			SvgGraphicsTeaVM svg) {
		final UGraphicTeaVM result = new UGraphicTeaVM(stringBounder);
		result.copy(defaultBackground, colorMapper, svg);
		return result;
	}

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		final UGraphicTeaVM result = new UGraphicTeaVM(getStringBounder());
		result.copy(this);
		return result;
	}

	private void register() {
		registerDriver(URectangle.class, new DriverRectangleTeaVM(this));
		registerDriver(ULine.class, new DriverLineTeaVM(this));
		registerDriver(UPolygon.class, new DriverPolygonTeaVM(this));
		registerDriver(UEllipse.class, new DriverEllipseTeaVM(this));
		registerDriver(UText.class, new DriverTextTeaVM(this));
		registerDriver(UPath.class, new DriverPathTeaVM(this));

		// NOP drivers for shapes not yet implemented
		ignoreShape(DotPath.class);
		ignoreShape(UImage.class);
		ignoreShape(UImageSvg.class);
		ignoreShape(UCenteredCharacter.class);
		ignoreShape(UPixel.class);
	}

	public SvgGraphicsTeaVM getSvgGraphics() {
		return getGraphicObject();
	}

	@Override
	public void writeToStream(OutputStream os, String metadata, int dpi) throws IOException {
		final String svgString = getGraphicObject().toSvgString();
		os.write(svgString.getBytes("UTF-8"));
	}

	@Override
	public void startGroup(UGroup group) {
		// TODO: implement group support
	}

	@Override
	public void closeGroup() {
		// TODO: implement group support
	}

	@Override
	public void startUrl(Url url) {
		// TODO: implement URL/link support
	}

	@Override
	public void closeUrl() {
		// TODO: implement URL/link support
	}

	@Override
	protected void drawComment(UComment comment) {
		// TODO: implement comment support
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		if (propertyName.equalsIgnoreCase("TEAVM"))
			return true;
		if (propertyName.equalsIgnoreCase("SVG"))
			return true;
		return super.matchesProperty(propertyName);
	}

}
