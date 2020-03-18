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

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.TikzFontDistortion;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.creole.atom.AtomText;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UCenteredCharacter;
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

public class UGraphicVdx extends AbstractUGraphic<VisioGraphics> implements ClipContainer, UGraphic2 {

	private final StringBounder stringBounder;

	public double dpiFactor() {
		return 1;
	}

	private UGraphicVdx(ColorMapper colorMapper, VisioGraphics visio) {
		super(colorMapper, visio);
		this.stringBounder = FileFormat.PNG.getDefaultStringBounder(TikzFontDistortion.getDefault());
		register();

	}

	public UGraphicVdx(ColorMapper colorMapper) {
		this(colorMapper, new VisioGraphics());

	}

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicVdx(this);
	}

	private UGraphicVdx(UGraphicVdx other) {
		super(other);
		this.stringBounder = other.stringBounder;
		register();
	}

	private void register() {
		registerDriver(URectangle.class, new DriverRectangleVdx());
		registerDriver(UText.class, new DriverTextVdx(stringBounder));
		registerDriver(AtomText.class, new DriverNoneVdx());
		registerDriver(ULine.class, new DriverLineVdx());
		registerDriver(UPolygon.class, new DriverPolygonVdx());
		registerDriver(UEllipse.class, new DriverNoneVdx());
		registerDriver(UImage.class, new DriverNoneVdx());
		registerDriver(UImageSvg.class, new DriverNoneVdx());
		registerDriver(UPath.class, new DriverUPathVdx());
		registerDriver(DotPath.class, new DriverDotPathVdx());
		registerDriver(UCenteredCharacter.class, new DriverNoneVdx());
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public void startUrl(Url url) {
	}

	public void closeAction() {
	}

	public void writeImageTOBEMOVED(OutputStream os, String metadata, int dpi) throws IOException {
		createVsd(os);
	}

	public void createVsd(OutputStream os) throws IOException {
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
