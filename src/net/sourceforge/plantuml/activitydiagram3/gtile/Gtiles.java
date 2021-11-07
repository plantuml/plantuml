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
package net.sourceforge.plantuml.activitydiagram3.gtile;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Hexagon;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.Parser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class Gtiles {

	private static final Rose rose = new Rose();

	static public GtileHexagonInside hexagonInside(Swimlane swimlane, StringBounder stringBounder, ISkinParam skinParam,
			StyleSignature styleSignature, HColor color, Display label) {
		final ConditionStyle conditionStyle = skinParam.getConditionStyle();

		final HColor borderColor;
		final HColor backColor;
		final FontConfiguration fcTest;

		if (UseStyle.useBetaStyle()) {
			final Style style = styleSignature.getMergedStyle(skinParam.getCurrentStyleBuilder());
			borderColor = style.value(PName.LineColor).asColor(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet());
			backColor = color == null ? style.value(PName.BackGroundColor).asColor(skinParam.getThemeStyle(),
					skinParam.getIHtmlColorSet()) : color;
			fcTest = style.getFontConfiguration(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet());
		} else {
			final FontParam testParam = conditionStyle == ConditionStyle.INSIDE_HEXAGON ? FontParam.ACTIVITY_DIAMOND
					: FontParam.ARROW;

			borderColor = rose.getHtmlColor(skinParam, ColorParam.activityDiamondBorder);
			backColor = color == null ? rose.getHtmlColor(skinParam, ColorParam.activityDiamondBackground) : color;
			fcTest = new FontConfiguration(skinParam, testParam, null)
					.changeColor(fontColor(skinParam, FontParam.ACTIVITY_DIAMOND));
		}

		final Sheet sheet = Parser
				.build(fcTest, skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT), skinParam, CreoleMode.FULL)
				.createSheet(label);
		final SheetBlock1 sheetBlock1 = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam.getPadding());
		final TextBlock tbTest = new SheetBlock2(sheetBlock1, Hexagon.asStencil(sheetBlock1), new UStroke());

		return new GtileHexagonInside(stringBounder, tbTest, skinParam, backColor, borderColor, swimlane);
	}

	static public AbstractGtileRoot diamondEmpty(Swimlane swimlane, StringBounder stringBounder, ISkinParam skinParam,
			StyleSignature styleSignature, HColor color) {
		final HColor borderColor;
		final HColor backColor;

		if (UseStyle.useBetaStyle()) {
			final Style style = styleSignature.getMergedStyle(skinParam.getCurrentStyleBuilder());
			borderColor = style.value(PName.LineColor).asColor(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet());
			backColor = color == null ? style.value(PName.BackGroundColor).asColor(skinParam.getThemeStyle(),
					skinParam.getIHtmlColorSet()) : color;
		} else {
			borderColor = rose.getHtmlColor(skinParam, ColorParam.activityDiamondBorder);
			backColor = color == null ? rose.getHtmlColor(skinParam, ColorParam.activityDiamondBackground) : color;
		}

		return new GtileHexagonInside(stringBounder, TextBlockUtils.EMPTY_TEXT_BLOCK, skinParam, backColor, borderColor,
				swimlane);
	}

	private static HColor fontColor(ISkinParam skinParam, FontParam param) {
		return skinParam.getFontHtmlColor(null, param);
	}

	static public Gtile withSouthMargin(Gtile orig, double south) {
		return new GtileWithMargin((AbstractGtileRoot) orig, 0, south, 0);

	}

	static public Gtile withNorthMargin(Gtile orig, double north) {
		return new GtileWithMargin((AbstractGtileRoot) orig, north, 0, 0);

	}

	public static Gtile withIncomingArrow(Gtile orig, double margin) {
		return new GtileWithIncomingArrow((AbstractGtileRoot) orig, margin);
	}

	public static Gtile withOutgoingArrow(Gtile orig, double margin) {
		return new GtileWithOutgoingArrow((AbstractGtileRoot) orig, margin);
	}

}
