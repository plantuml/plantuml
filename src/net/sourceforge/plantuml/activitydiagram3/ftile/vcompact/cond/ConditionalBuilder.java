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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidthCentered;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileWithUrl;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileIfDown;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class ConditionalBuilder {

	private final Swimlane swimlane;
	private final HColor borderColor;
	private final HColor backColor;
	private final Rainbow arrowColor;
	private final FtileFactory ftileFactory;
	private final ConditionStyle conditionStyle;
	private final ConditionEndStyle conditionEndStyle;
	private final Branch branch1;
	private final Branch branch2;
	private final ISkinParam skinParam;
	private final StringBounder stringBounder;
	private final FontConfiguration fontArrow;
	private final FontConfiguration fontTest;

	private final Ftile tile1;
	private final Ftile tile2;
	private final Url url;

	public StyleSignature getDefaultStyleDefinitionDiamond() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	public ConditionalBuilder(Swimlane swimlane, HColor borderColor, HColor backColor, Rainbow arrowColor,
			FtileFactory ftileFactory, ConditionStyle conditionStyle, ConditionEndStyle conditionEndStyle,
			Branch branch1, Branch branch2, ISkinParam skinParam, StringBounder stringBounder,
			FontConfiguration fontArrow, FontConfiguration fontTest, Url url) {
		if (SkinParam.USE_STYLES()) {
			final Style styleArrow = getDefaultStyleDefinitionArrow()
					.getMergedStyle(skinParam.getCurrentStyleBuilder());
			final Style styleDiamond = getDefaultStyleDefinitionDiamond().getMergedStyle(
					skinParam.getCurrentStyleBuilder());
			this.borderColor = styleDiamond.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
			this.backColor = styleDiamond.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
			this.arrowColor = Rainbow
					.fromColor(styleArrow.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet()), null);
			this.fontTest = styleDiamond.getFontConfiguration(skinParam.getIHtmlColorSet());
			this.fontArrow = styleArrow.getFontConfiguration(skinParam.getIHtmlColorSet());
		} else {
			this.borderColor = borderColor;
			this.backColor = backColor;
			this.arrowColor = arrowColor;
			this.fontTest = fontTest;
			this.fontArrow = fontArrow;
		}
		this.ftileFactory = ftileFactory;
		this.swimlane = swimlane;
		this.conditionStyle = conditionStyle;
		this.conditionEndStyle = conditionEndStyle;
		this.branch1 = branch1;
		this.branch2 = branch2;
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;
		this.url = url;

		this.tile1 = new FtileMinWidthCentered(branch1.getFtile(), 30);
		this.tile2 = new FtileMinWidthCentered(branch2.getFtile(), 30);

	}

	static public Ftile create(Swimlane swimlane, HColor borderColor, HColor backColor, Rainbow arrowColor,
			FtileFactory ftileFactory, ConditionStyle conditionStyle, ConditionEndStyle conditionEndStyle,
			Branch branch1, Branch branch2, ISkinParam skinParam, StringBounder stringBounder,
			FontConfiguration fcArrow, FontConfiguration fcTest, Url url) {
		final ConditionalBuilder builder = new ConditionalBuilder(swimlane, borderColor, backColor, arrowColor,
				ftileFactory, conditionStyle, conditionEndStyle, branch1, branch2, skinParam, stringBounder, fcArrow,
				fcTest, url);
		if (isEmptyOrOnlySingleStopOrSpot(branch2) && isEmptyOrOnlySingleStopOrSpot(branch1) == false) {
			return builder.createDown(builder.branch1, builder.branch2);
		}
		if (branch1.isEmpty() && branch2.isOnlySingleStopOrSpot()) {
			return builder.createDown(builder.branch1, builder.branch2);
		}
		if (isEmptyOrOnlySingleStopOrSpot(branch1) && isEmptyOrOnlySingleStopOrSpot(branch2) == false) {
			return builder.createDown(builder.branch2, builder.branch1);
		}
		if (branch2.isEmpty() && branch1.isOnlySingleStopOrSpot()) {
			return builder.createDown(builder.branch2, builder.branch1);
		}
		return builder.createWithLinks();
		// return builder.createWithDiamonds();
		// return builder.createNude();
	}

	private static boolean isEmptyOrOnlySingleStopOrSpot(Branch branch) {
		return branch.isEmpty() || branch.isOnlySingleStopOrSpot();
	}

	private Ftile createDown(Branch branch1, Branch branch2) {
		final Ftile tile1 = new FtileMinWidthCentered(branch1.getFtile(), 30);
		final Ftile tile2 = new FtileMinWidthCentered(branch2.getFtile(), 30);
		final TextBlock tb1 = getLabelPositive(branch1);
		final TextBlock tb2 = getLabelPositive(branch2);
		final Ftile diamond1 = getDiamond1(false, tb1, tb2);
		final Ftile diamond2 = getDiamond2(branch1, branch2, true);
		if (branch2.isOnlySingleStopOrSpot()) {
			return FtileIfDown.create(diamond1, diamond2, swimlane, FtileUtils.addHorizontalMargin(tile1, 10),
					arrowColor, conditionEndStyle, ftileFactory, branch2.getFtile(),
					branch2.getInlinkRenderingColorAndStyle());
		}
		if (branch1.isOnlySingleStopOrSpot()) {
			return FtileIfDown.create(diamond1, diamond2, swimlane, FtileUtils.addHorizontalMargin(tile2, 10),
					arrowColor, conditionEndStyle, ftileFactory, branch1.getFtile(),
					branch1.getInlinkRenderingColorAndStyle());
		}
		if (branch1.isEmpty()) {
			return FtileIfDown.create(diamond1, diamond2, swimlane, FtileUtils.addHorizontalMargin(tile2, 10),
					arrowColor, conditionEndStyle, ftileFactory, null, null);
		}
		return FtileIfDown.create(diamond1, diamond2, swimlane, FtileUtils.addHorizontalMargin(tile1, 10), arrowColor,
				conditionEndStyle, ftileFactory, null, branch2.getInlinkRenderingColorAndStyle());
	}

	private Ftile createNude() {
		return new FtileIfNude(tile1, tile2, swimlane);

	}

	private Ftile createWithDiamonds() {
		final Ftile diamond1 = getDiamond1(true);
		final Ftile diamond2 = getDiamond2(branch1, branch2, false);
		final FtileIfWithDiamonds ftile = new FtileIfWithDiamonds(diamond1, tile1, tile2, diamond2, swimlane,
				stringBounder);
		final Dimension2D label1 = getLabelPositive(branch1).calculateDimension(stringBounder);
		final Dimension2D label2 = getLabelPositive(branch2).calculateDimension(stringBounder);
		final double diff1 = ftile.computeMarginNeedForBranchLabe1(stringBounder, label1);
		final double diff2 = ftile.computeMarginNeedForBranchLabe2(stringBounder, label2);
		Ftile result = FtileUtils.addHorizontalMargin(ftile, diff1, diff2);
		final double suppHeight = ftile.computeVerticalMarginNeedForBranchs(stringBounder, label1, label2);
		result = FtileUtils.addVerticalMargin(result, suppHeight, 0);
		return result;
	}

	private Ftile createWithLinks() {
		Ftile diamond1 = getDiamond1(true);
		if (url != null) {
			diamond1 = new FtileWithUrl(diamond1, url);
		}
		final Ftile diamond2 = getDiamond2(branch1, branch2, false);
		final Ftile tmp1 = FtileUtils.addHorizontalMargin(tile1, 10);
		final Ftile tmp2 = FtileUtils.addHorizontalMargin(tile2, 10);
		final FtileIfWithLinks ftile = new FtileIfWithLinks(diamond1, tmp1, tmp2, diamond2, swimlane, arrowColor,
				conditionEndStyle, stringBounder);
		final Dimension2D label1 = getLabelPositive(branch1).calculateDimension(stringBounder);
		final Dimension2D label2 = getLabelPositive(branch2).calculateDimension(stringBounder);
		final double diff1 = ftile.computeMarginNeedForBranchLabe1(stringBounder, label1);
		final double diff2 = ftile.computeMarginNeedForBranchLabe2(stringBounder, label2);
		final double suppHeight = ftile.computeVerticalMarginNeedForBranchs(stringBounder, label1, label2);
		Ftile result = ftile.addLinks(branch1, branch2, stringBounder);
		result = FtileUtils.addHorizontalMargin(result, diff1, diff2);
		result = FtileUtils.addVerticalMargin(result, suppHeight, 0);
		return result;
	}

	private Ftile getDiamond1(boolean eastWest) {
		return getDiamond1(eastWest, getLabelPositive(branch1), getLabelPositive(branch2));
	}

	private Ftile getDiamond1(boolean eastWest, TextBlock tb1, TextBlock tb2) {
		final Display labelTest = branch1.getLabelTest();

		final Sheet sheet = new CreoleParser(fontTest, skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT),
				skinParam, CreoleMode.FULL).createSheet(labelTest);
		final SheetBlock1 sheetBlock1 = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam.getPadding());
		final TextBlock tbTest = new SheetBlock2(sheetBlock1, Diamond.asStencil(sheetBlock1), tile1.getThickness());

		final Ftile diamond1;
		if (conditionStyle == ConditionStyle.INSIDE) {
			if (eastWest) {
				diamond1 = new FtileDiamondInside(tile1.skinParam(), backColor, borderColor, swimlane, tbTest)
						.withWestAndEast(tb1, tb2);
			} else {
				diamond1 = new FtileDiamondInside(tile1.skinParam(), backColor, borderColor, swimlane, tbTest)
						.withSouth(tb1).withEast(tb2);
			}
		} else if (conditionStyle == ConditionStyle.DIAMOND) {
			if (eastWest) {
				diamond1 = new FtileDiamond(tile1.skinParam(), backColor, borderColor, swimlane).withNorth(tbTest)
						.withWestAndEast(tb1, tb2);
			} else {
				diamond1 = new FtileDiamond(tile1.skinParam(), backColor, borderColor, swimlane).withNorth(tbTest)
						.withSouth(tb1).withEast(tb2);
			}
		} else {
			throw new IllegalStateException();
		}
		return diamond1;
	}

	private TextBlock getLabelPositive(Branch branch) {
		return branch.getLabelPositive().create7(fontArrow, HorizontalAlignment.LEFT, ftileFactory.skinParam(),
				CreoleMode.SIMPLE_LINE);
	}

	private Ftile getDiamond2(Branch branch1, Branch branch2, boolean useNorth) {
		final Ftile diamond2;
		if (conditionEndStyle == ConditionEndStyle.HLINE) {
			return new FtileEmpty(tile1.skinParam(), 0, Diamond.diamondHalfSize, swimlane, swimlane);
		}
		// else use default ConditionEndStyle.DIAMOND
		if (hasTwoBranches()) {
			final Display out1 = branch1.getFtile().getOutLinkRendering().getDisplay();
			final TextBlock tbout1 = out1 == null ? null : out1.create7(fontArrow, HorizontalAlignment.LEFT,
					ftileFactory.skinParam(), CreoleMode.SIMPLE_LINE);
			final Display out2 = branch2.getFtile().getOutLinkRendering().getDisplay();
			final TextBlock tbout2 = out2 == null ? null : out2.create7(fontArrow, HorizontalAlignment.LEFT,
					ftileFactory.skinParam(), CreoleMode.SIMPLE_LINE);
			FtileDiamond tmp = new FtileDiamond(tile1.skinParam(), backColor, borderColor, swimlane);
			tmp = useNorth ? tmp.withNorth(tbout1) : tmp.withWest(tbout1);
			tmp = tmp.withEast(tbout2);
			diamond2 = tmp;
		} else {
			diamond2 = new FtileEmpty(tile1.skinParam(), 0, Diamond.diamondHalfSize / 2, swimlane, swimlane);
		}
		return diamond2;
	}

	public boolean hasTwoBranches() {
		return tile1.calculateDimension(stringBounder).hasPointOut()
				&& tile2.calculateDimension(stringBounder).hasPointOut();
	}

	// private HtmlColor fontColor() {
	// return skinParam.getFontHtmlColor(FontParam.ACTIVITY_DIAMOND, null);
	// }

}
