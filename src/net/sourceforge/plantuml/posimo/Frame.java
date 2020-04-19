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
 * 
 */
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class Frame implements Component {
	
	public Style[] getUsedStyles() {
		throw new UnsupportedOperationException();
	}

	public StyleSignature getDefaultStyleDefinition() {
		throw new UnsupportedOperationException();
	}


	private final List<? extends CharSequence> name;
	private final ISkinParam skinParam;
	private final Rose rose = new Rose();

	public Frame(List<? extends CharSequence> name, ISkinParam skinParam) {
		this.name = name;
		this.skinParam = skinParam;
	}

	public void drawU(UGraphic ug, Area area, Context2D context) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final HColor lineColor = rose.getHtmlColor(skinParam, ColorParam.packageBorder);
		ug = ug.apply(lineColor);
		ug = ug.apply(new HColorNone().bg());
		ug.apply(new UStroke(1.4)).draw(new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight()));

		final TextBlock textBlock = createTextBloc();
		textBlock.drawU(ug.apply(new UTranslate(2, 2)));

		final Dimension2D textDim = getTextDim(ug.getStringBounder());
		final double x = textDim.getWidth() + 6;
		final double y = textDim.getHeight() + 6;
		final UPolygon poly = new UPolygon();
		poly.addPoint(x, 0);
		poly.addPoint(x, y - 6);
		poly.addPoint(x - 6, y);
		poly.addPoint(0, y);
		poly.addPoint(0, 0);
		ug.apply(new UStroke(1.4)).draw(poly);

	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final Dimension2D dim = getTextDim(stringBounder);
		return dim.getHeight() + 8;
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		final Dimension2D dim = getTextDim(stringBounder);
		return dim.getWidth() + 8;
	}

	public Dimension2D getTextDim(StringBounder stringBounder) {
		final TextBlock bloc = createTextBloc();
		return bloc.calculateDimension(stringBounder);
	}

	private TextBlock createTextBloc() {
		final UFont font = skinParam.getFont(null, false, FontParam.PACKAGE);
		final HColor textColor = skinParam.getFontHtmlColor(null, FontParam.PACKAGE);
		// final TextBlock bloc = Display.create(name).create(new FontConfiguration(font, textColor,
		// skinParam.getHyperlinkColor(), skinParam.useUnderlineForHyperlink()), HorizontalAlignment.LEFT, new
		// SpriteContainerEmpty());
		// return bloc;
		throw new UnsupportedOperationException();
	}

	public final Dimension2D getPreferredDimension(StringBounder stringBounder) {
		final double w = getPreferredWidth(stringBounder);
		final double h = getPreferredHeight(stringBounder);
		return new Dimension2DDouble(w, h);
	}

}
