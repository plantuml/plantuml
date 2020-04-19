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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.Instruction;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileBreak;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Genealogy;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.WeldingPoint;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class FtileFactoryDelegatorWhile extends FtileFactoryDelegator {

	public FtileFactoryDelegatorWhile(FtileFactory factory) {
		super(factory);
	}

	@Override
	public Ftile createWhile(Swimlane swimlane, Ftile whileBlock, Display test, Display yes, Display out,
			LinkRendering afterEndwhile, HColor color, Instruction specialOut, Ftile backward) {

		final HColor borderColor;
		final HColor backColor;
		final Rainbow arrowColor;
		final FontConfiguration fontArrow;
		final FontConfiguration fcTest;
		final ConditionStyle conditionStyle = skinParam().getConditionStyle();
		final FontParam testParam = conditionStyle == ConditionStyle.INSIDE ? FontParam.ACTIVITY_DIAMOND
				: FontParam.ARROW;
		if (SkinParam.USE_STYLES()) {
			final Style styleArrow = getDefaultStyleDefinitionArrow()
					.getMergedStyle(skinParam().getCurrentStyleBuilder());
			final Style styleDiamond = getDefaultStyleDefinitionDiamond()
					.getMergedStyle(skinParam().getCurrentStyleBuilder());
			borderColor = styleDiamond.value(PName.LineColor).asColor(skinParam().getIHtmlColorSet());
			backColor = styleDiamond.value(PName.BackGroundColor).asColor(skinParam().getIHtmlColorSet());
			arrowColor = Rainbow.build(styleArrow, skinParam().getIHtmlColorSet());
			fontArrow = styleArrow.getFontConfiguration(skinParam().getIHtmlColorSet());
			fcTest = styleDiamond.getFontConfiguration(skinParam().getIHtmlColorSet());
		} else {
			borderColor = getRose().getHtmlColor(skinParam(), ColorParam.activityDiamondBorder);
			backColor = color == null ? getRose().getHtmlColor(skinParam(), ColorParam.activityDiamondBackground)
					: color;
			arrowColor = Rainbow.build(skinParam());
			fontArrow = new FontConfiguration(skinParam(), FontParam.ARROW, null);
			fcTest = new FontConfiguration(skinParam(), testParam, null);
		}

		final LinkRendering endInlinkRendering = whileBlock.getOutLinkRendering();
		final Rainbow endInlinkColor = endInlinkRendering == null || endInlinkRendering.getRainbow().size() == 0
				? arrowColor
				: endInlinkRendering.getRainbow();

		Ftile result = FtileWhile.create(swimlane, whileBlock, test, borderColor, backColor, arrowColor, yes, out,
				endInlinkColor, afterEndwhile, fontArrow, getFactory(), conditionStyle, fcTest, specialOut, backward);

		final List<WeldingPoint> weldingPoints = whileBlock.getWeldingPoints();
		if (weldingPoints.size() > 0) {
			// printAllChild(repeat);

			final Genealogy genealogy = new Genealogy(result);

			for (WeldingPoint w : weldingPoints) {
				final FtileBreak ftileBreak = (FtileBreak) w;
				result = FtileUtils.addConnection(result, new Connection() {
					public void drawU(UGraphic ug) {
						final UTranslate tr1 = genealogy.getTranslate(ftileBreak, ug.getStringBounder());

						final Snake snake = new Snake(getFtile1().arrowHorizontalAlignment(), arrowColor,
								Arrows.asToLeft());
						snake.addPoint(tr1.getDx(), tr1.getDy());
						snake.addPoint(Diamond.diamondHalfSize, tr1.getDy());
						ug.draw(snake);
					}

					public Ftile getFtile1() {
						return ftileBreak;
					}

					public Ftile getFtile2() {
						return null;
					}

				});
			}
		}

		return result;
	}

}
