/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 15913 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.BiColor;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont2;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

final public class ComponentRoseNote extends AbstractTextualComponent implements Stencil {

	private final int cornersize = 10;
	private final HtmlColor back;
	private final HtmlColor foregroundColor;
	private final double paddingX;
	private final double paddingY;
	private final double deltaShadow;
	private final UStroke stroke;

	public ComponentRoseNote(BiColor biColor, UFont2 font, Display strings,
			double paddingX, double paddingY, ISkinSimple spriteContainer, double deltaShadow, UStroke stroke) {
		super(strings, font, HorizontalAlignment.LEFT, 6, 15, 5, spriteContainer, 0, true,
				null, null);
		this.back = biColor.getYellowBack();
		this.foregroundColor = biColor.getRedBorder();
		this.paddingX = paddingX;
		this.paddingY = paddingY;
		this.deltaShadow = deltaShadow;
		this.stroke = stroke;
	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		final double result = getTextWidth(stringBounder) + 2 * getPaddingX() + deltaShadow;
		return result;
	}

	@Override
	final public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 2 * getPaddingY() + deltaShadow;
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

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, textHeight);
		polygon.addPoint(x2, textHeight);
		polygon.addPoint(x2, cornersize);
		polygon.addPoint(x2 - cornersize, 0);
		polygon.addPoint(0, 0);
		polygon.setDeltaShadow(deltaShadow);

		ug = ug.apply(new UChangeBackColor(back)).apply(new UChangeColor(foregroundColor));
		ug = ug.apply(stroke);
		ug.draw(polygon);

		ug.apply(new UTranslate(x2 - cornersize, 0)).draw(new ULine(0, cornersize));
		ug.apply(new UTranslate(x2, cornersize)).draw(new ULine(-cornersize, 0));
		UGraphic ug2 = new UGraphicStencil(ug, this, new UStroke());
		ug2 = ug2.apply(new UTranslate(getMarginX1() + diffX / 2, getMarginY()));

		getTextBlock().drawU(ug2);

	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return 0;
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return getTextWidth(stringBounder);
	}

}
