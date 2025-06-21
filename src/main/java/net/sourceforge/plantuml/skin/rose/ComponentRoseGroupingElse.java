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

import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class ComponentRoseGroupingElse extends AbstractTextualComponent {

	private final HColor groupBorder;
	private final HColor backgroundColor;
	private final double roundCorner;
	private final boolean teoz;

	public ComponentRoseGroupingElse(boolean teoz, Style style, CharSequence comment, ISkinParam skinParam) {
		super(style, LineBreakStrategy.NONE, 5, 5, 1, skinParam, comment == null ? null : "[" + comment + "]");

		this.teoz = teoz;
		this.roundCorner = getRoundCorner();
		this.groupBorder = getColorLine();
		this.backgroundColor = teoz ? HColors.transparent() : getColorBackGround();
	}

	@Override
	protected void drawBackgroundInternalU(UGraphic ug, Area area) {
		if (backgroundColor.isTransparent())
			return;

		final XDimension2D dimensionToUse = area.getDimensionToUse();
		ug = ug.apply(HColors.none()).apply(backgroundColor.bg());
		final double width = dimensionToUse.getWidth();
		final double height = dimensionToUse.getHeight();
		final UShape rect;
		if (roundCorner == 0) {
			rect = URectangle.build(width, height);
		} else {
			final UPath path = UPath.none();
			path.moveTo(0, 0);
			path.lineTo(width, 0);

			path.lineTo(width, height - roundCorner / 2);
			path.arcTo(roundCorner / 2, roundCorner / 2, 0, 0, 1, width - roundCorner / 2, height);

			path.lineTo(roundCorner / 2, height);
			path.arcTo(roundCorner / 2, roundCorner / 2, 0, 0, 1, 0, height - roundCorner / 2);

			path.lineTo(0, 0);
			rect = path;
		}
		ug.draw(rect);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final XDimension2D dimensionToUse = area.getDimensionToUse();
		ug = ArrowConfiguration.stroke(ug, 2, 2, 1).apply(groupBorder);
		ug.apply(UTranslate.dy(1)).draw(ULine.hline(dimensionToUse.getWidth()));
		ug = ug.apply(UStroke.simple());
		if (teoz)
			getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY() + 2)));
		else
			getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY())));
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		if (teoz)
			return getTextHeight(stringBounder) + 16;
		else
			return getTextHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder);
	}

}
