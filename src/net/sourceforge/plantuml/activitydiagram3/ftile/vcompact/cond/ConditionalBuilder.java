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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidth;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
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
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ConditionalBuilder {

	private final Swimlane swimlane;
	private final HtmlColor borderColor;
	private final HtmlColor backColor;
	private final UFont fontArrow;
	private final UFont fontTest;
	private final HtmlColor arrowColor;
	private final FtileFactory ftileFactory;
	private final ConditionStyle conditionStyle;
	private final Branch branch1;
	private final Branch branch2;
	private final ISkinParam skinParam;
	private final StringBounder stringBounder;

	private final Ftile tile1;
	private final Ftile tile2;

	public ConditionalBuilder(Swimlane swimlane, HtmlColor borderColor, HtmlColor backColor, UFont fontArrow,
			UFont fontTest, HtmlColor arrowColor, FtileFactory ftileFactory, ConditionStyle conditionStyle,
			Branch branch1, Branch branch2, ISkinParam skinParam, StringBounder stringBounder) {
		this.swimlane = swimlane;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.fontArrow = fontArrow;
		this.fontTest = fontTest;
		this.arrowColor = arrowColor;
		this.ftileFactory = ftileFactory;
		this.conditionStyle = conditionStyle;
		this.branch1 = branch1;
		this.branch2 = branch2;
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;

		this.tile1 = new FtileMinWidth(branch1.getFtile(), 30);
		this.tile2 = new FtileMinWidth(branch2.getFtile(), 30);

	}

	static public Ftile create(Swimlane swimlane, HtmlColor borderColor, HtmlColor backColor, UFont fontArrow,
			UFont fontTest, HtmlColor arrowColor, FtileFactory ftileFactory, ConditionStyle conditionStyle,
			Branch branch1, Branch branch2, ISkinParam skinParam, StringBounder stringBounder) {
		final ConditionalBuilder builder = new ConditionalBuilder(swimlane, borderColor, backColor, fontArrow,
				fontTest, arrowColor, ftileFactory, conditionStyle, branch1, branch2, skinParam, stringBounder);
		return builder.createWithLinks();
		// return builder.createWithDiamonds();
		// return builder.createNude();

	}

	private Ftile createNude() {
		return new FtileIfNude(tile1, tile2, swimlane);

	}

	private Ftile createWithDiamonds() {
		final Ftile diamond1 = getDiamond1();
		final Ftile diamond2 = getDiamond2();
		final FtileIfWithDiamonds ftile = new FtileIfWithDiamonds(diamond1, tile1, tile2, diamond2, swimlane,
				stringBounder);
		final Dimension2D label1 = getLabelBranch1().calculateDimension(stringBounder);
		final Dimension2D label2 = getLabelBranch2().calculateDimension(stringBounder);
		final double diff1 = ftile.computeMarginNeedForBranchLabe1(stringBounder, label1);
		final double diff2 = ftile.computeMarginNeedForBranchLabe2(stringBounder, label2);
		Ftile result = FtileUtils.addHorizontalMargin(ftile, diff1, diff2);
		final double suppHeight = ftile.computeVerticalMarginNeedForBranchs(stringBounder, label1, label2);
		result = FtileUtils.addVerticalMargin(result, suppHeight, 0);
		return result;
	}

	private Ftile createWithLinks() {
		final Ftile diamond1 = getDiamond1();
		final Ftile diamond2 = getDiamond2();
		final Ftile tmp1 = FtileUtils.addHorizontalMargin(tile1, 10);
		final Ftile tmp2 = FtileUtils.addHorizontalMargin(tile2, 10);
		final FtileIfWithLinks ftile = new FtileIfWithLinks(diamond1, tmp1, tmp2, diamond2, swimlane, arrowColor,
				stringBounder);
		final Dimension2D label1 = getLabelBranch1().calculateDimension(stringBounder);
		final Dimension2D label2 = getLabelBranch2().calculateDimension(stringBounder);
		final double diff1 = ftile.computeMarginNeedForBranchLabe1(stringBounder, label1);
		final double diff2 = ftile.computeMarginNeedForBranchLabe2(stringBounder, label2);
		final double suppHeight = ftile.computeVerticalMarginNeedForBranchs(stringBounder, label1, label2);
		Ftile result = ftile.addLinks(branch1, branch2, stringBounder);
		result = FtileUtils.addHorizontalMargin(result, diff1, diff2);
		result = FtileUtils.addVerticalMargin(result, suppHeight, 0);
		return result;
	}

	private Ftile getDiamond1() {
		final Display labelTest = branch1.getLabelTest();
		final TextBlock tb1 = getLabelBranch1();
		final TextBlock tb2 = getLabelBranch2();

		final FontConfiguration fcTest = new FontConfiguration(fontTest, fontColor(), skinParam.getHyperlinkColor(),
				skinParam.useUnderlineForHyperlink());
		final Sheet sheet = new CreoleParser(fcTest, HorizontalAlignment.LEFT, skinParam, CreoleMode.FULL)
				.createSheet(labelTest);
		final SheetBlock1 sheetBlock1 = new SheetBlock1(sheet, 0, skinParam.getPadding());
		final TextBlock tbTest = new SheetBlock2(sheetBlock1, Diamond.asStencil(sheetBlock1), new UStroke(1.5));

		final Ftile diamond1;
		if (conditionStyle == ConditionStyle.INSIDE) {
			diamond1 = new FtileDiamondInside(tile1.shadowing(), backColor, borderColor, swimlane, tbTest)
					.withWestAndEast(tb1, tb2);
		} else if (conditionStyle == ConditionStyle.DIAMOND) {
			diamond1 = new FtileDiamond(tile1.shadowing(), backColor, borderColor, swimlane).withNorth(tbTest)
					.withWestAndEast(tb1, tb2);
		} else {
			throw new IllegalStateException();
		}
		return diamond1;
	}

	private TextBlock getLabelBranch2() {
		final TextBlock tb2 = branch2.getLabelPositive().create(fcArrow(), HorizontalAlignment.LEFT, ftileFactory,
				CreoleMode.SIMPLE_LINE);
		return tb2;
	}

	private TextBlock getLabelBranch1() {
		final TextBlock tb1 = branch1.getLabelPositive().create(fcArrow(), HorizontalAlignment.LEFT, ftileFactory,
				CreoleMode.SIMPLE_LINE);
		return tb1;
	}

	private Ftile getDiamond2() {
		final FontConfiguration fcArrow = fcArrow();

		final Ftile diamond2;
		if (hasTwoBranches()) {
			final Display out1 = LinkRendering.getDisplay(branch1.getFtile().getOutLinkRendering());
			final TextBlock tbout1 = out1 == null ? null : out1.create(fcArrow, HorizontalAlignment.LEFT, ftileFactory,
					CreoleMode.SIMPLE_LINE);
			final Display out2 = LinkRendering.getDisplay(branch2.getFtile().getOutLinkRendering());
			final TextBlock tbout2 = out2 == null ? null : out2.create(fcArrow, HorizontalAlignment.LEFT, ftileFactory,
					CreoleMode.SIMPLE_LINE);
			diamond2 = new FtileDiamond(tile1.shadowing(), backColor, borderColor, swimlane).withWest(tbout1).withEast(
					tbout2);
		} else {
			// diamond2 = new FtileEmpty(tile1.shadowing(), Diamond.diamondHalfSize * 2, Diamond.diamondHalfSize * 2,
			// swimlane, swimlane);
			diamond2 = new FtileEmpty(tile1.shadowing(), 0, Diamond.diamondHalfSize / 2, swimlane, swimlane);
		}
		return diamond2;
	}

	public boolean hasTwoBranches() {
		return tile1.calculateDimension(stringBounder).hasPointOut()
				&& tile2.calculateDimension(stringBounder).hasPointOut();
	}

	private FontConfiguration fcArrow() {
		return new FontConfiguration(fontArrow, fontColor(), skinParam.getHyperlinkColor(),
				skinParam.useUnderlineForHyperlink());
	}

	private HtmlColor fontColor() {
		return skinParam.getFontHtmlColor(FontParam.ACTIVITY_DIAMOND, null);
	}

}
