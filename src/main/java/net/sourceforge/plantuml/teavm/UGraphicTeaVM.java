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
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UShapeKind;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.AbstractCommonUGraphic;
import net.sourceforge.plantuml.klimt.drawing.AbstractUGraphic;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
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
	// ::remove file when JAVA8
	
	// Every TeaVM driver is stateless -- not one of them takes a constructor argument -- so a
	// single table serves every UGraphicTeaVM ever created, built once at class initialization
	// rather than once per rendered diagram. It must stay immutable: useDrivers() marks it as
	// shared, so registerDriver refuses to touch it afterwards.
	private static final UDriver<?, SvgGraphicsTeaVM>[] DRIVERS = newDrivers();

	@SuppressWarnings("unchecked")
	private static UDriver<?, SvgGraphicsTeaVM>[] newDrivers() {
		// "new UDriver<?, SvgGraphicsTeaVM>[n]" is not legal Java -- generic array creation --
		// so the raw array is created and converted here, once.
		return new UDriver[UShapeKind.COUNT];
	}

	static {
		put(URectangle.class, new DriverRectangleTeaVM());
		put(ULine.class, new DriverLineTeaVM());
		put(UPolygon.class, new DriverPolygonTeaVM());
		put(UEllipse.class, new DriverEllipseTeaVM());
		put(UText.class, new DriverTextTeaVM());
		put(UPath.class, new DriverPathTeaVM());

		put(UImage.class, new DriverImageTeaVM());
		put(UImageSvg.class, new DriverImageSvgTeaVM());
		put(DotPath.class, new DriverDotPathTeaVM());

		put(UCenteredCharacter.class, new DriverCenteredCharacterTeaVM());

		// NOP drivers for shapes not yet implemented
		put(UPixel.class, noopDriver());
	}

	// SHAPE ties the class to its driver, exactly as registerDriver does, so a driver written for
	// another shape does not compile here. The second type argument of UDriver is the graphic
	// object -- SvgGraphicsTeaVM, what this UGraphic draws into -- not the UGraphic itself.
	private static <SHAPE extends UShape> void put(Class<SHAPE> cl, UDriver<SHAPE, SvgGraphicsTeaVM> driver) {
		DRIVERS[UShapeKind.of(cl).ordinal()] = driver;
	}

	private UGraphicTeaVM(StringBounder stringBounder) {
		super(stringBounder);
		useDrivers(DRIVERS);
	}

	public static UGraphicTeaVM build(HColor defaultBackground, ColorMapper colorMapper, StringBounder stringBounder,
			SvgGraphicsTeaVM svg) {
		final UGraphicTeaVM result = new UGraphicTeaVM(stringBounder);
		result.copy(defaultBackground, colorMapper, svg);
		return result;
	}

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicTeaVM(this);
	}

	// A copy takes its driver table from "other" (see AbstractUGraphic#copy) instead of building
	// one, so this constructor deliberately does not register anything.
	private UGraphicTeaVM(UGraphicTeaVM other) {
		super(other.getStringBounder());
		copy(other);
	}

	public SvgGraphicsTeaVM getSvgGraphics() {
		return getGraphicObject();
	}

	@Override
	public void writeToStream(OutputStream os, String metadata, int dpi) throws IOException {
		throw new UnsupportedOperationException("TEAVM442");
	}

	@Override
	public void startGroup(UGroup group) {
		getSvgGraphics().startGroup(group.asMap());
	}

	@Override
	public void closeGroup() {
		getSvgGraphics().closeGroup();
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
