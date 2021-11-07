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
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class GtileIfHexagon extends GtileColumns {

	private final Gtile shape1;
	private final List<Branch> branches;
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
		super(gtiles, swimlane);
		if (branches.size() <= 1)
			throw new IllegalArgumentException();

		this.branches = branches;

		this.shape1 = getShape1(swimlane);
		this.shape2 = Gtiles.diamondEmpty(swimlane, getStringBounder(), skinParam(), getDefaultStyleDefinitionDiamond(),
				branches.get(0).getColor());

		final double height1 = shape1.calculateDimension(stringBounder).getHeight();

		for (ListIterator<UTranslate> it = positions.listIterator(); it.hasNext();) {
			final UTranslate tmp = it.next();
			it.set(tmp.compose(UTranslate.dy(height1)));
		}

		final Dimension2D totalDim = calculateDimensionRaw(stringBounder);
		this.positionShape1 = UTranslate.dx(totalDim.getWidth() / 2)
				.compose(shape1.getCoord(GPoint.NORTH_BORDER).reverse());
		this.positionShape2 = new UTranslate(totalDim.getWidth() / 2, totalDim.getHeight())
				.compose(shape2.getCoord(GPoint.SOUTH_BORDER).reverse());

	}

	@Override
	protected UTranslate getCoordImpl(String name) {
		if (name.equals(GPoint.NORTH_HOOK))
			return shape1.getCoord(name).compose(positionShape1);
		if (name.equals(GPoint.SOUTH_HOOK))
			return shape2.getCoord(name).compose(positionShape2);
		return super.getCoordImpl(name);
	}

	private Gtile getShape1(Swimlane swimlane) {
		GtileHexagonInside tmp = Gtiles.hexagonInside(swimlane, getStringBounder(), skinParam(),
				getDefaultStyleDefinitionDiamond(), branches.get(0).getColor(), branches.get(0).getLabelTest());
		final TextBlock tmp0 = branches.get(0).getTextBlockPositive();
		final TextBlock tmp1 = branches.get(1).getTextBlockPositive();
		return Gtiles.withSouthMargin(tmp.withWestLabel(tmp0).withEastLabel(tmp1), 10);
	}

	@Override
	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D toto1 = calculateDimensionRaw(stringBounder);
		final Dimension2D toto2 = shape1.calculateDimension(stringBounder);
		return MathUtils.max(toto1, toto2);
	}

	private Dimension2D calculateDimensionRaw(StringBounder stringBounder) {
		final double height2 = shape2.calculateDimension(stringBounder).getHeight();
		final Dimension2D nude = super.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(nude, 0, height2);
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
	protected void drawUInternal(UGraphic ug) {
		super.drawUInternal(ug);

		ug.apply(positionShape1).draw(shape1);
		ug.apply(positionShape2).draw(shape2);
	}

//	private TextBlock getLabelPositive(Branch branch) {
//		final LineBreakStrategy labelLineBreak = LineBreakStrategy.NONE;
//
//		final FontConfiguration fontConfiguration;
//		if (UseStyle.useBetaStyle()) {
//			final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
//			fontConfiguration = style.getFontConfiguration(skinParam().getThemeStyle(), skinParam().getIHtmlColorSet());
//		} else {
//			fontConfiguration = new FontConfiguration(skinParam(), FontParam.ARROW, null);
//		}
//
//		return branch.getDisplayPositive().create0(fontConfiguration, HorizontalAlignment.LEFT, skinParam(),
//				labelLineBreak, CreoleMode.SIMPLE_LINE, null, null);
//	}

	@Override
	public Collection<GConnection> getInnerConnections() {
		if (branches.size() == 2) {
			final GConnection arrow1 = new GConnectionHorizontalThenVerticalDown(positionShape1,
					shape1.getGPoint(GPoint.WEST_HOOK), positions.get(0), gtiles.get(0).getGPoint(GPoint.NORTH_HOOK),
					TextBlockUtils.EMPTY_TEXT_BLOCK);
			final GConnection arrow2 = new GConnectionHorizontalThenVerticalDown(positionShape1,
					shape1.getGPoint(GPoint.EAST_HOOK), positions.get(1), gtiles.get(1).getGPoint(GPoint.NORTH_HOOK),
					TextBlockUtils.EMPTY_TEXT_BLOCK);

			final GConnection arrow3 = new GConnectionVerticalDownThenHorizontal(positions.get(0),
					gtiles.get(0).getGPoint(GPoint.SOUTH_HOOK), positionShape2, shape2.getGPoint(GPoint.WEST_HOOK),
					TextBlockUtils.EMPTY_TEXT_BLOCK);
			final GConnection arrow4 = new GConnectionVerticalDownThenHorizontal(positions.get(1),
					gtiles.get(1).getGPoint(GPoint.SOUTH_HOOK), positionShape2, shape2.getGPoint(GPoint.EAST_HOOK),
					TextBlockUtils.EMPTY_TEXT_BLOCK);

			return Arrays.asList(arrow1, arrow2, arrow3, arrow4);
		}
		return super.getInnerConnections();
	}

}
