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

import java.awt.geom.Dimension2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ftile.Hexagon;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.Parser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class GtileIfHexagon extends GtileIfSimple {

	private final List<Branch> branches;
	private final Gtile shape1;
	private final Gtile shape2;

	private final UTranslate positionShape1;
	private final UTranslate positionShape2;

	@Override
	public String toString() {
		return "GtileIfHexagon " + gtiles;
	}

	// ConditionalBuilder
	// FtileFactoryDelegatorIf

	public GtileIfHexagon(Swimlane swimlane, List<Gtile> gtiles, List<Branch> branches) {
		super(gtiles);

		final ConditionStyle conditionStyle = skinParam().getConditionStyle();
		final ConditionEndStyle conditionEndStyle = skinParam().getConditionEndStyle();

		this.branches = branches;

		final Branch branch0 = branches.get(0);

		final HColor borderColor;
		final HColor backColor;
		final FontConfiguration fcTest;

		if (UseStyle.useBetaStyle()) {
			final Style styleArrow = getDefaultStyleDefinitionArrow()
					.getMergedStyle(skinParam().getCurrentStyleBuilder());
			final Style styleDiamond = getDefaultStyleDefinitionDiamond()
					.getMergedStyle(skinParam().getCurrentStyleBuilder());
			borderColor = styleDiamond.value(PName.LineColor).asColor(skinParam().getThemeStyle(),
					skinParam().getIHtmlColorSet());
			backColor = branch0.getColor() == null ? styleDiamond.value(PName.BackGroundColor)
					.asColor(skinParam().getThemeStyle(), skinParam().getIHtmlColorSet()) : branch0.getColor();
//			arrowColor = Rainbow.build(styleArrow, skinParam().getIHtmlColorSet(), skinParam().getThemeStyle());
			fcTest = styleDiamond.getFontConfiguration(skinParam().getThemeStyle(), skinParam().getIHtmlColorSet());
//			fcArrow = styleArrow.getFontConfiguration(skinParam().getThemeStyle(), skinParam().getIHtmlColorSet());
		} else {
			final FontParam testParam = conditionStyle == ConditionStyle.INSIDE_HEXAGON ? FontParam.ACTIVITY_DIAMOND
					: FontParam.ARROW;

			borderColor = getRose().getHtmlColor(skinParam(), ColorParam.activityDiamondBorder);
			backColor = branch0.getColor() == null
					? getRose().getHtmlColor(skinParam(), ColorParam.activityDiamondBackground)
					: branch0.getColor();
//			arrowColor = Rainbow.build(skinParam());
			fcTest = new FontConfiguration(skinParam(), testParam, null)
					.changeColor(fontColor(FontParam.ACTIVITY_DIAMOND));
//			fcArrow = new FontConfiguration(skinParam(), FontParam.ARROW, null);
		}

		final Sheet sheet = Parser.build(fcTest, skinParam().getDefaultTextAlignment(HorizontalAlignment.LEFT),
				skinParam(), CreoleMode.FULL).createSheet(branch0.getLabelTest());
		final SheetBlock1 sheetBlock1 = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam().getPadding());
		final TextBlock tbTest = new SheetBlock2(sheetBlock1, Hexagon.asStencil(sheetBlock1), new UStroke());

		this.shape1 = new GtileDiamondInside(getStringBounder(), tbTest, skinParam(), backColor, borderColor, swimlane);
		this.shape2 = new GtileDiamondInside(getStringBounder(), TextBlockUtils.EMPTY_TEXT_BLOCK, skinParam(),
				backColor, borderColor, swimlane);

		final double height1 = shape1.calculateDimension(stringBounder).getHeight() + getSuppHeightMargin();

//		public GtileDiamondInside(StringBounder stringBounder, TextBlock label, ISkinParam skinParam, HColor backColor,
//				HColor borderColor, Swimlane swimlane) {

		for (ListIterator<UTranslate> it = positions.listIterator(); it.hasNext();) {
			final UTranslate tmp = it.next();
			it.set(tmp.compose(UTranslate.dy(height1)));
		}

		this.positionShape1 = this.getCoord(GPoint.NORTH).compose(shape1.getCoord(GPoint.NORTH).reverse());
		this.positionShape2 = this.getCoord(GPoint.SOUTH).compose(shape2.getCoord(GPoint.SOUTH).reverse());

	}

	private double getSuppHeightMargin() {
		if (branches.size() == 1)
			return 30;
		return 10;
	}

	@Override
	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final double height2 = shape2.calculateDimension(stringBounder).getHeight() + getSuppHeightMargin();
		final Dimension2D result = super.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(result, 0, height2);
	}

	private HColor fontColor(FontParam param) {
		return skinParam().getFontHtmlColor(null, param);
	}

	final public StyleSignature getDefaultStyleDefinitionActivity() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	final public StyleSignature getDefaultStyleDefinitionDiamond() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	final public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	@Override
	public void drawU(UGraphic ug) {
		super.drawU(ug);

		shape1.drawU(ug.apply(positionShape1));
		shape2.drawU(ug.apply(positionShape2));
	}

	@Override
	public Collection<GConnection> getInnerConnections() {
		if (branches.size() == 1) {
			final GConnection arrow1 = new GConnectionVerticalDown(positionShape1, shape1.getGPoint(GPoint.SOUTH),
					positions.get(0), gtiles.get(0).getGPoint(GPoint.NORTH), TextBlockUtils.EMPTY_TEXT_BLOCK);
			final GConnection arrow2 = new GConnectionVerticalDown(positions.get(0),
					gtiles.get(0).getGPoint(GPoint.SOUTH), positionShape2, shape2.getGPoint(GPoint.NORTH),
					TextBlockUtils.EMPTY_TEXT_BLOCK);
			return Arrays.asList(arrow1, arrow2);
		} else if (branches.size() == 2) {
			final GConnection arrow1 = new GConnectionHorizontalThenVerticalDown(positionShape1,
					shape1.getGPoint(GPoint.WEST), positions.get(0), gtiles.get(0).getGPoint(GPoint.NORTH),
					TextBlockUtils.EMPTY_TEXT_BLOCK);
			final GConnection arrow2 = new GConnectionHorizontalThenVerticalDown(positionShape1,
					shape1.getGPoint(GPoint.EAST), positions.get(1), gtiles.get(1).getGPoint(GPoint.NORTH),
					TextBlockUtils.EMPTY_TEXT_BLOCK);

			final GConnection arrow3 = new GConnectionVerticalDownThenHorizontal(positions.get(0),
					gtiles.get(0).getGPoint(GPoint.SOUTH), positionShape2, shape2.getGPoint(GPoint.WEST),
					TextBlockUtils.EMPTY_TEXT_BLOCK);
			final GConnection arrow4 = new GConnectionVerticalDownThenHorizontal(positions.get(1),
					gtiles.get(1).getGPoint(GPoint.SOUTH), positionShape2, shape2.getGPoint(GPoint.EAST),
					TextBlockUtils.EMPTY_TEXT_BLOCK);

			return Arrays.asList(arrow1, arrow2, arrow3, arrow4);
		}
		return super.getInnerConnections();
	}

}
