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
package net.sourceforge.plantuml.ugraphic.eps;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.UnusedSpace;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.skin.UDrawable;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;

public class UGraphicEps extends AbstractUGraphic<EpsGraphics> implements ClipContainer {

	final static Graphics2D imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB).createGraphics();
	private UClip clip;

	private final StringBounder stringBounder;

	public UGraphicEps(ColorMapper colorMapper, EpsStrategy strategy) {
		this(colorMapper, strategy, strategy.creatEpsGraphics());
	}

	private UGraphicEps(ColorMapper colorMapper, EpsStrategy strategy, EpsGraphics eps) {
		super(colorMapper, eps);
		stringBounder = StringBounderUtils.asStringBounder(imDummy);
		registerDriver(URectangle.class, new DriverRectangleEps(this));
		registerDriver(UText.class, new DriverTextEps(imDummy, this, strategy));
		registerDriver(ULine.class, new DriverLineEps(this));
		registerDriver(UPolygon.class, new DriverPolygonEps(this));
		registerDriver(UEllipse.class, new DriverEllipseEps());
		registerDriver(UImage.class, new DriverImageEps());
		registerDriver(DotPath.class, new DriverDotPathEps());
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

	public void setClip(UClip clip) {
		this.clip = clip == null ? null : clip.translate(getTranslateX(), getTranslateY());
	}

	public UClip getClip() {
		return clip;
	}

	public void centerChar(double x, double y, char c, UFont font) {
		final UnusedSpace unusedSpace = UnusedSpace.getUnusedSpace(font, c);

		final double xpos = x - unusedSpace.getCenterX() - 0.5;
		final double ypos = y - unusedSpace.getCenterY() - 0.5;

		final TextLayout t = new TextLayout("" + c, font.getFont(), imDummy.getFontRenderContext());
		getGraphicObject().setStrokeColor(getColorMapper().getMappedColor(getParam().getColor()));
		DriverTextEps.drawPathIterator(getGraphicObject(), xpos + getTranslateX(), ypos + getTranslateY(), t
				.getOutline(null).getPathIterator(null));

	}

	static public String getEpsString(ColorMapper colorMapper, EpsStrategy epsStrategy, UDrawable udrawable)
			throws IOException {
		final UGraphicEps ug = new UGraphicEps(colorMapper, epsStrategy);
		udrawable.drawU(ug);
		return ug.getEPSCode();
	}

	static public void copyEpsToFile(ColorMapper colorMapper, UDrawable udrawable, File f) throws IOException {
		final PrintWriter pw = new PrintWriter(f);
		final EpsStrategy epsStrategy = EpsStrategy.getDefault2();
		pw.print(UGraphicEps.getEpsString(colorMapper, epsStrategy, udrawable));
		pw.close();
	}

	public void setAntiAliasing(boolean trueForOn) {
	}

	public void setUrl(String url, String tooltip) {
		if (url == null) {
			getGraphicObject().closeLink();
		} else {
			getGraphicObject().openLink(url);
		}
	}

}
