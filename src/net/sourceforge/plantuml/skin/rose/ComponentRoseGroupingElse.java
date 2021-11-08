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
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorBackground;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class ComponentRoseGroupingElse extends AbstractTextualComponent {

	private final HColor groupBorder;
	private final HColor backgroundColor;
	private final double roundCorner;

	public ComponentRoseGroupingElse(Style style, HColor groupBorder, FontConfiguration smallFont, CharSequence comment,
			ISkinSimple spriteContainer, HColor backgroundColor, double roundCorner) {
		super(style, LineBreakStrategy.NONE, comment == null ? null : "[" + comment + "]", smallFont,
				HorizontalAlignment.LEFT, 5, 5, 1, spriteContainer, null, null);

		if (UseStyle.useBetaStyle()) {
			this.roundCorner = style.value(PName.RoundCorner).asInt();
			if (spriteContainer instanceof SkinParamBackcolored) {
				style = style.eventuallyOverride(PName.BackGroundColor,
						((SkinParamBackcolored) spriteContainer).getBackgroundColor());
			}
			this.groupBorder = style.value(PName.LineColor).asColor(spriteContainer.getThemeStyle(),
					getIHtmlColorSet());
			this.backgroundColor = style.value(PName.BackGroundColor).asColor(spriteContainer.getThemeStyle(),
					getIHtmlColorSet());
		} else {
			this.roundCorner = roundCorner;
			this.groupBorder = groupBorder;
			this.backgroundColor = backgroundColor;
		}
	}

	@Override
	protected void drawBackgroundInternalU(UGraphic ug, Area area) {
		if (backgroundColor instanceof HColorBackground) {
			return;
		}
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		ug = ug.apply(new HColorNone()).apply(backgroundColor.bg());
		final double width = dimensionToUse.getWidth();
		final double height = dimensionToUse.getHeight();
		final UShape rect;
		if (roundCorner == 0) {
			rect = new URectangle(width, height);
		} else {
			final UPath path = new UPath();
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
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		ug = ArrowConfiguration.stroke(ug, 2, 2, 1).apply(groupBorder);
		ug.apply(UTranslate.dy(1)).draw(ULine.hline(dimensionToUse.getWidth()));
		ug = ug.apply(new UStroke());
		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY())));
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder);
	}

}
