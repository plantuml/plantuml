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
import net.sourceforge.plantuml.Pragma;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.ConditionalBuilder;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class FtileFactoryDelegatorIf extends FtileFactoryDelegator {

	private final Pragma pragma;

	public FtileFactoryDelegatorIf(FtileFactory factory, Pragma pragma) {
		super(factory);
		this.pragma = pragma;
	}

	@Override
	public Ftile createIf(Swimlane swimlane, List<Branch> thens, Branch elseBranch, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Url url) {

		final ConditionStyle conditionStyle = skinParam().getConditionStyle();
		final ConditionEndStyle conditionEndStyle = skinParam().getConditionEndStyle();
		final Branch branch0 = thens.get(0);

		final HColor borderColor;
		final HColor backColor;
		final Rainbow arrowColor;
		final FontConfiguration fcTest;
		final FontParam testParam = conditionStyle == ConditionStyle.INSIDE ? FontParam.ACTIVITY_DIAMOND
				: FontParam.ARROW;
		final FontConfiguration fcArrow;
		if (SkinParam.USE_STYLES()) {
			final Style styleArrow = getDefaultStyleDefinitionArrow().getMergedStyle(
					skinParam().getCurrentStyleBuilder());
			final Style styleDiamond = getDefaultStyleDefinitionDiamond().getMergedStyle(
					skinParam().getCurrentStyleBuilder());
			borderColor = styleDiamond.value(PName.LineColor).asColor(skinParam().getIHtmlColorSet());
			backColor = branch0.getColor() == null ? styleDiamond.value(PName.BackGroundColor).asColor(
					skinParam().getIHtmlColorSet()) : branch0.getColor();
			arrowColor = Rainbow.build(styleArrow, skinParam().getIHtmlColorSet());
			fcTest = styleDiamond.getFontConfiguration(skinParam().getIHtmlColorSet());
			fcArrow = styleArrow.getFontConfiguration(skinParam().getIHtmlColorSet());
		} else {
			borderColor = getRose().getHtmlColor(skinParam(), ColorParam.activityDiamondBorder);
			backColor = branch0.getColor() == null ? getRose().getHtmlColor(skinParam(),
					ColorParam.activityDiamondBackground) : branch0.getColor();
			arrowColor = Rainbow.build(skinParam());
			fcTest = new FontConfiguration(skinParam(), testParam, null)
					.changeColor(fontColor(FontParam.ACTIVITY_DIAMOND));
			fcArrow = new FontConfiguration(skinParam(), FontParam.ARROW, null);
		}

		if (thens.size() > 1) {
			if (pragma.useVerticalIf()/* OptionFlags.USE_IF_VERTICAL */)
				return FtileIfLongVertical.create(swimlane, borderColor, backColor, arrowColor, getFactory(),
						conditionStyle, thens, elseBranch, fcArrow, topInlinkRendering, afterEndwhile);
			return FtileIfLongHorizontal.create(swimlane, borderColor, backColor, arrowColor, getFactory(),
					conditionStyle, thens, elseBranch, fcArrow, topInlinkRendering, afterEndwhile, fcTest);
		}
		return ConditionalBuilder.create(swimlane, borderColor, backColor, arrowColor, getFactory(), conditionStyle,
				conditionEndStyle, thens.get(0), elseBranch, skinParam(), getStringBounder(), fcArrow, fcTest, url);
	}

	private HColor fontColor(FontParam param) {
		return skinParam().getFontHtmlColor(null, param);
	}

}
