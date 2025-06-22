/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
 *
 */
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public abstract class AbstractComponent implements Component {

	public final Style[] getUsedStyles() {
		throw new UnsupportedOperationException();
	}

	public StyleSignatureBasic getStyleSignature() {
		throw new UnsupportedOperationException();
	}

	private final Style style;
	private final ISkinParam skinParam;

	public AbstractComponent(Style style, ISkinParam skinParam) {
		this.style = style;
		this.skinParam = skinParam;
	}

	protected final Style getStyle() {
		return style;
	}

	protected final ISkinParam getSkinParam() {
		return skinParam;
	}

	protected HColorSet getIHtmlColorSet() {
		return skinParam.getIHtmlColorSet();
	}

	protected final Fashion getSymbolContext() {
		return style.getSymbolContext(getIHtmlColorSet());
	}

	protected final Fashion getSymbolContext(Colors colors) {
		return style.getSymbolContext(getIHtmlColorSet(), colors);
	}

	private final HColor getColor(PName name) {
		return style.value(name).asColor(getIHtmlColorSet());
	}

	protected final HColor getColorFont() {
		return getColor(PName.FontColor);
	}

	protected final HColor getColorLine() {
		return getColor(PName.LineColor);
	}

	protected final HColor getColorBackGround() {
		return getColor(PName.BackGroundColor);
	}

	protected final double getRoundCorner() {
		return style.value(PName.RoundCorner).asInt(false);
	}

	protected final double getDiagonalCorner() {
		return style.value(PName.DiagonalCorner).asInt(false);
	}	

	protected final UStroke getStroke() {
		return style.getStroke();
	}

	protected final double getShadowing() {
		return style.getShadowing();
	}

	protected final UFont getUFont() {
		return style.getUFont();
	}

	protected final FontConfiguration getFontConfiguration() {
		return style.getFontConfiguration(getIHtmlColorSet());
	}

	protected final HorizontalAlignment getHorizontalAlignment() {
		return style.getHorizontalAlignment();
	}

	abstract protected void drawInternalU(UGraphic ug, Area area);

	protected void drawBackgroundInternalU(UGraphic ug, Area area) {
	}

	public final void drawU(UGraphic ug, Area area, Context2D context) {
		ug = ug.apply(new UTranslate(getPaddingX(), getPaddingY()));
		if (context.isBackground())
			drawBackgroundInternalU(ug, area);
		else
			drawInternalU(ug, area);

	}

	public double getPaddingX() {
		return 0;
	}

	public double getPaddingY() {
		return 0;
	}

	public abstract double getPreferredWidth(StringBounder stringBounder);

	public abstract double getPreferredHeight(StringBounder stringBounder);

	public final XDimension2D getPreferredDimension(StringBounder stringBounder) {
		final double w = getPreferredWidth(stringBounder);
		final double h = getPreferredHeight(stringBounder);
		return new XDimension2D(w, h);
	}

}
