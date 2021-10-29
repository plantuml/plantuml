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
package net.sourceforge.plantuml.ugraphic.visio;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.creole.legacy.AtomText;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UCenteredCharacter;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UImageSvg;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class UGraphicVdx extends AbstractUGraphic<VisioGraphics> implements ClipContainer {

	public double dpiFactor() {
		return 1;
	}

	public UGraphicVdx(HColor defaultBackground, ColorMapper colorMapper, StringBounder stringBounder) {
		super(defaultBackground, colorMapper, stringBounder, new VisioGraphics());
		register();
	}

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicVdx(this);
	}

	private UGraphicVdx(UGraphicVdx other) {
		super(other);
		register();
	}

	private void register() {
		registerDriver(URectangle.class, new DriverRectangleVdx());
		registerDriver(UText.class, new DriverTextVdx(getStringBounder()));
		ignoreShape(AtomText.class);
		registerDriver(ULine.class, new DriverLineVdx());
		registerDriver(UPolygon.class, new DriverPolygonVdx());
		ignoreShape(UEllipse.class);
		ignoreShape(UImage.class);
		ignoreShape(UImageSvg.class);
		registerDriver(UPath.class, new DriverPathVdx());
		registerDriver(DotPath.class, new DriverDotPathVdx());
		ignoreShape(UCenteredCharacter.class);
	}

	@Override
	public void writeToStream(OutputStream os, String metadata, int dpi) throws IOException {
		getGraphicObject().createVsd(os);
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		if ("SPECIALTXT".equalsIgnoreCase(propertyName)) {
			return true;
		}
		return false;
	}

}
