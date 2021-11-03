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

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidthCentered;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.FtileSwitchNude;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.FtileSwitchWithDiamonds;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.FtileSwitchWithManyLinks;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.FtileSwitchWithOneLink;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDecorateInLabel;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDecorateOutLabel;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class FtileFactoryDelegatorSwitch extends FtileFactoryDelegator {

	public FtileFactoryDelegatorSwitch(FtileFactory factory) {
		super(factory);
	}

	@Override
	public Ftile createSwitch(Swimlane swimlane, List<Branch> branches, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Display labelTest) {
		// return createNude(swimlane, branches);
		// return createWithDiamonds(swimlane, branches, labelTest);
		return createWithLinks(swimlane, branches, labelTest);
	}

	private Ftile createNude(Swimlane swimlane, List<Branch> branches) {
		final List<Ftile> ftiles = new ArrayList<>();
		for (Branch branch : branches) {
			ftiles.add(new FtileMinWidthCentered(branch.getFtile(), 30));
		}
		return new FtileSwitchNude(ftiles, swimlane);
	}

	private Ftile createWithDiamonds(Swimlane swimlane, List<Branch> branches, Display labelTest) {
		final List<Ftile> ftiles = new ArrayList<>();
		for (Branch branch : branches) {
			ftiles.add(new FtileMinWidthCentered(branch.getFtile(), 30));
		}
		final Ftile diamond1 = getDiamond1(swimlane, branches.get(0), labelTest);
		final Ftile diamond2 = getDiamond2(swimlane, branches.get(0));

		return new FtileSwitchWithDiamonds(ftiles, branches, swimlane, diamond1, diamond2, getStringBounder());
	}

	private Ftile createWithLinks(Swimlane swimlane, List<Branch> branches, Display labelTest) {
		final List<Ftile> ftiles = new ArrayList<>();
		final Ftile diamond1 = getDiamond1(swimlane, branches.get(0), labelTest);
		final Ftile diamond2 = getDiamond2(swimlane, branches.get(0));

		for (Branch branch : branches) {
			final Dimension2D dimLabelIn = branch.getTextBlockPositive().calculateDimension(getStringBounder());
			final Dimension2D dimLabelOut = branch.getTextBlockSpecial().calculateDimension(getStringBounder());
			ftiles.add(new FtileDecorateOutLabel(new FtileDecorateInLabel(branch.getFtile(), dimLabelIn), dimLabelOut));
		}

		final Rainbow arrowColor;
		if (UseStyle.useBetaStyle()) {
			final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
			arrowColor = Rainbow.build(style, skinParam().getIHtmlColorSet(), skinParam().getThemeStyle());
		} else {
			arrowColor = Rainbow.build(skinParam());
		}
		if (ftiles.size() == 1) {
			final FtileSwitchWithOneLink result = new FtileSwitchWithOneLink(ftiles, branches, swimlane, diamond1,
					diamond2, getStringBounder(), arrowColor);
			return result.addLinks(getStringBounder());
		}
		final FtileSwitchWithManyLinks result = new FtileSwitchWithManyLinks(ftiles, branches, swimlane, diamond1,
				diamond2, getStringBounder(), arrowColor);
		return result.addLinks(getStringBounder());

	}

	private Ftile getDiamond1(Swimlane swimlane, Branch branch0, Display test) {

		LineBreakStrategy lineBreak = LineBreakStrategy.NONE;
		final FontConfiguration fcDiamond;
		final HColor borderColor;
		final HColor backColor;
		if (UseStyle.useBetaStyle()) {
			final Style style = getDefaultStyleDefinitionDiamond().getMergedStyle(skinParam().getCurrentStyleBuilder());
			lineBreak = style.wrapWidth();
			fcDiamond = style.getFontConfiguration(skinParam().getThemeStyle(), skinParam().getIHtmlColorSet());
			borderColor = style.value(PName.LineColor).asColor(skinParam().getThemeStyle(),
					skinParam().getIHtmlColorSet());
			backColor = branch0.getColor() == null ? style.value(PName.BackGroundColor)
					.asColor(skinParam().getThemeStyle(), skinParam().getIHtmlColorSet()) : branch0.getColor();
		} else {
			fcDiamond = new FontConfiguration(skinParam(), FontParam.ACTIVITY_DIAMOND, null);
			borderColor = getRose().getHtmlColor(skinParam(), ColorParam.activityDiamondBorder);
			backColor = branch0.getColor() == null
					? getRose().getHtmlColor(skinParam(), ColorParam.activityDiamondBackground)
					: branch0.getColor();
		}

		final TextBlock tbTest;
		if (Display.isNull(test) || test.isWhite())
			tbTest = TextBlockUtils.empty(0, 0);
		else
			tbTest = test.create0(fcDiamond, branch0.skinParam().getDefaultTextAlignment(HorizontalAlignment.LEFT),
					branch0.skinParam(), lineBreak, CreoleMode.FULL, null, null);

		return new FtileDiamondInside(tbTest, branch0.skinParam(), backColor, borderColor, swimlane);
	}

	private Ftile getDiamond2(Swimlane swimlane, Branch branch0) {
		final HColor borderColor = getRose().getHtmlColor(skinParam(), ColorParam.activityDiamondBorder);
		final HColor backColor = branch0.getColor() == null
				? getRose().getHtmlColor(skinParam(), ColorParam.activityDiamondBackground)
				: branch0.getColor();

		return new FtileDiamondInside(TextBlockUtils.empty(0, 0), branch0.skinParam(), backColor, borderColor,
				swimlane);
	}

//	private HColor fontColor(FontParam param) {
//		return skinParam().getFontHtmlColor(null, param);
//	}

}
