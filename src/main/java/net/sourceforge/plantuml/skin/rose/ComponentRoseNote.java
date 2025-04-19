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
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Stencil;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicStencil;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.image.Opale;

final public class ComponentRoseNote extends AbstractTextualComponent implements Stencil {
    // ::remove folder when __HAXE__

	private final double paddingX;
	private final double paddingY;
	private final Fashion symbolContext;
	private final double roundCorner;
	private final HorizontalAlignment position;

	public ComponentRoseNote(Style style, Display strings, double paddingX, double paddingY,
			ISkinParam skinParam, HorizontalAlignment textAlignment, HorizontalAlignment position,
			Colors colors) {
		super(style, style.wrapWidth(), textAlignment == HorizontalAlignment.CENTER ? 15 : 6, 15, 5, skinParam,
				strings, true);
		this.paddingX = paddingX;
		this.paddingY = paddingY;
		this.position = position;
		this.symbolContext = style.getSymbolContext(getIHtmlColorSet(), colors);
		this.roundCorner = style.value(PName.RoundCorner).asInt(false);

	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		final double result = getTextWidth(stringBounder) + 2 * getPaddingX() + symbolContext.getDeltaShadow();
		return result;
	}

	@Override
	final public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 2 * getPaddingY() + symbolContext.getDeltaShadow();
	}

	@Override
	public double getPaddingX() {
		return paddingX;
	}

	@Override
	public double getPaddingY() {
		return paddingY;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {

		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);

		int x2 = (int) getTextWidth(stringBounder);
		final double diffX = area.getDimensionToUse().getWidth() - getPreferredWidth(stringBounder);
		if (diffX < 0) {
			throw new IllegalArgumentException();
		}
		if (area.getDimensionToUse().getWidth() > getPreferredWidth(stringBounder)) {
			x2 = (int) (area.getDimensionToUse().getWidth() - 2 * getPaddingX());
		}

		final UPath polygon = Opale.getPolygonNormal(x2, textHeight, roundCorner);
		polygon.setDeltaShadow(symbolContext.getDeltaShadow());

		ug = symbolContext.apply(ug);
		ug.draw(polygon);

		ug.draw(Opale.getCorner(x2, roundCorner));
		UGraphic ug2 = UGraphicStencil.create(ug, this, UStroke.simple());

		if (position == HorizontalAlignment.LEFT) {
			ug2 = ug2.apply(new UTranslate(getMarginX1(), getMarginY()));
		} else if (position == HorizontalAlignment.RIGHT) {
			ug2 = ug2.apply(
					new UTranslate(area.getDimensionToUse().getWidth() - getTextWidth(stringBounder), getMarginY()));
		} else {
			ug2 = ug2.apply(new UTranslate(getMarginX1() + diffX / 2, getMarginY()));
		}

		getTextBlock().drawU(ug2);

	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return 0;
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return getTextWidth(stringBounder);
	}

}
