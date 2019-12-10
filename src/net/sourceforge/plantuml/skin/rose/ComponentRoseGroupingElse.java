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
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorTransparent;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseGroupingElse extends AbstractTextualComponent {

	private final HtmlColor groupBorder;
	private final HtmlColor backgroundColor;

	public ComponentRoseGroupingElse(Style style, HtmlColor groupBorder, FontConfiguration smallFont,
			CharSequence comment, ISkinSimple spriteContainer, HtmlColor backgroundColor) {
		super(style, LineBreakStrategy.NONE, comment == null ? null : "[" + comment + "]", smallFont,
				HorizontalAlignment.LEFT, 5, 5, 1, spriteContainer, null, null);
		if (SkinParam.USE_STYLES()) {
			if (spriteContainer instanceof SkinParamBackcolored) {
				style = style.eventuallyOverride(PName.BackGroundColor,
						((SkinParamBackcolored) spriteContainer).getBackgroundColor());
			}
			this.groupBorder = style.value(PName.LineColor).asColor(getIHtmlColorSet());
			this.backgroundColor = style.value(PName.BackGroundColor).asColor(getIHtmlColorSet());
		} else {
			this.groupBorder = groupBorder;
			this.backgroundColor = backgroundColor;
		}
	}

	@Override
	protected void drawBackgroundInternalU(UGraphic ug, Area area) {
		if (backgroundColor instanceof HtmlColorTransparent) {
			return;
		}
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final URectangle rect = new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight());
		ug.apply(new UChangeColor(null)).apply(new UChangeBackColor(backgroundColor)).draw(rect);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		ug = ArrowConfiguration.stroke(ug, 2, 2, 1).apply(new UChangeColor(groupBorder));
		ug.apply(new UTranslate(0, 1)).draw(new ULine(dimensionToUse.getWidth(), 0));
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
