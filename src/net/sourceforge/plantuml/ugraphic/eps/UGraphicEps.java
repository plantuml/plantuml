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
package net.sourceforge.plantuml.ugraphic.eps;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.TikzFontDistortion;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UCenteredCharacter;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic2;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

public class UGraphicEps extends AbstractUGraphic<EpsGraphics> implements ClipContainer, UGraphic2 {

	private final StringBounder stringBounder;

	private final EpsStrategy strategyTOBEREMOVED;

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicEps(this);
	}

	protected UGraphicEps(UGraphicEps other) {
		super(other);
		this.stringBounder = other.stringBounder;
		this.strategyTOBEREMOVED = other.strategyTOBEREMOVED;
		register(strategyTOBEREMOVED);
	}

	public UGraphicEps(ColorMapper colorMapper, EpsStrategy strategy) {
		this(colorMapper, strategy, strategy.creatEpsGraphics());
	}

	private UGraphicEps(ColorMapper colorMapper, EpsStrategy strategy, EpsGraphics eps) {
		super(colorMapper, eps);
		this.strategyTOBEREMOVED = strategy;
		this.stringBounder = FileFormat.PNG.getDefaultStringBounder(TikzFontDistortion.getDefault());
		register(strategy);
	}

	private void register(EpsStrategy strategy) {
		registerDriver(URectangle.class, new DriverRectangleEps(this));
		registerDriver(UText.class, new DriverTextEps(this, strategy));
		registerDriver(ULine.class, new DriverLineEps(this));
		registerDriver(UPolygon.class, new DriverPolygonEps(this));
		registerDriver(UEllipse.class, new DriverEllipseEps(this));
		registerDriver(UImage.class, new DriverImageEps(this));
		registerDriver(UPath.class, new DriverPathEps());
		registerDriver(DotPath.class, new DriverDotPathEps());
		registerDriver(UCenteredCharacter.class, new DriverCenteredCharacterEps());
	}

	public void close() {
		getEpsGraphics().close();
	}

	public String getEPSCode() {
		return getEpsGraphics().getEPSCode();
	}

	public EpsGraphics getEpsGraphics() {
		return this.getGraphicObject();
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public void drawEps(String eps, double x, double y) {
		this.getGraphicObject().drawEps(eps, x, y);
	}

	static public String getEpsString(ColorMapper colorMapper, EpsStrategy epsStrategy, UDrawable udrawable)
			throws IOException {
		final UGraphicEps ug = new UGraphicEps(colorMapper, epsStrategy);
		udrawable.drawU(ug);
		return ug.getEPSCode();
	}

	public void startUrl(Url url) {
		getGraphicObject().openLink(url.getUrl());
	}

	public void closeAction() {
		getGraphicObject().closeLink();
	}

	public void writeImageTOBEMOVED(OutputStream os, String metadata, int dpi) throws IOException {
		os.write(getEPSCode().getBytes());
	}

}
