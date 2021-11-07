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
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class GtileSplit extends GtileColumns {

	private final HColor lineColor;

	public GtileSplit(List<Gtile> gtiles, Swimlane singleSwimlane, HColor lineColor) {
		super(gtiles, singleSwimlane);
		this.lineColor = lineColor;

	}

	private static Gtile getShape1(Swimlane swimlane, HColor color, StringBounder stringBounder, ISkinParam skinParam,
			Gtile first) {
		final double start = first.getCoord(GPoint.NORTH_HOOK).getDx();
		final GtileHLine tmp = new GtileHLine(stringBounder, skinParam, swimlane, color);
		return tmp;
	}

	private static Gtile getShape2(Swimlane swimlane, HColor color, StringBounder stringBounder, ISkinParam skinParam) {
		final GtileHLine tmp = new GtileHLine(stringBounder, skinParam, swimlane, color);
		return tmp;
	}

	final public StyleSignature getDefaultStyleDefinitionActivity() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	final static public StyleSignature getDefaultStyleDefinitionDiamond() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	final public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	@Override
	protected void drawUInternal(UGraphic ug) {
		super.drawUInternal(ug);
		gtiles.get(0).getCoord(GPoint.NORTH_HOOK);
		ug.apply(lineColor).apply(new UStroke(1.5))
				.apply(UTranslate.dx(gtiles.get(0).getCoord(GPoint.NORTH_HOOK).getDx())).apply(positions.get(0))
				.draw(ULine.hline(10));
	}

	@Override
	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return Dimension2DDouble.delta(super.calculateDimension(stringBounder), 0, 0);
	}

//	@Override
//	public Collection<GConnection> getInnerConnections() {
//
//		final GConnection arrow1 = new GConnectionVerticalDown(getPos1(), tile1.getGPoint(GPoint.SOUTH_HOOK), getPos2(),
//				tile2.getGPoint(GPoint.NORTH_HOOK), TextBlockUtils.EMPTY_TEXT_BLOCK);
//		final GConnection arrow2 = new GConnectionVerticalDown(getPos2(), tile2.getGPoint(GPoint.SOUTH_HOOK), getPos3(),
//				tile3.getGPoint(GPoint.NORTH_HOOK), TextBlockUtils.EMPTY_TEXT_BLOCK);
//
//		final double xright = calculateDimension(stringBounder).getWidth();
//
//		final GConnection arrow3 = new GConnectionLeftThenVerticalThenRight(getPos1(), tile1.getGPoint(GPoint.EAST_HOOK),
//				getPos3(), tile3.getGPoint(GPoint.EAST_HOOK), xright, TextBlockUtils.EMPTY_TEXT_BLOCK);
//
//		return Arrays.asList(arrow1, arrow2, arrow3);
//	}

}
