/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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

import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GtileSplit extends GtileColumns {

	private final HColor lineColor;

	public GtileSplit(List<Gtile> gtiles, Swimlane singleSwimlane, HColor lineColor) {
		super(gtiles, singleSwimlane, 20);
		this.lineColor = lineColor;

	}

	final public StyleSignatureBasic getDefaultStyleDefinitionActivity() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	final static public StyleSignatureBasic getDefaultStyleDefinitionDiamond() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	final public StyleSignatureBasic getDefaultStyleDefinitionArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	@Override
	protected void drawUInternal(UGraphic ug) {
		super.drawUInternal(ug);

		final double x0 = gtiles.get(0).getCoord(GPoint.NORTH_HOOK).compose(getPosition(0)).getDx();

		final int last = gtiles.size() - 1;
		final double xLast = gtiles.get(last).getCoord(GPoint.NORTH_HOOK).compose(getPosition(last)).getDx();
		final ULine hline = ULine.hline(xLast - x0);

		ug = ug.apply(lineColor).apply(UStroke.withThickness(1.5));
		ug.apply(UTranslate.dx(x0)).draw(hline);

		final double y = getCoord(GPoint.SOUTH_BORDER).getDy();
		ug.apply(new UTranslate(x0, y)).draw(hline);

	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return super.calculateDimension(stringBounder).delta(0, 0);
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
