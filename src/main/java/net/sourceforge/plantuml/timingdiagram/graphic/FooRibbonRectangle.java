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
 */
package net.sourceforge.plantuml.timingdiagram.graphic;

import java.util.List;

import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.timingdiagram.ChangeState;
import net.sourceforge.plantuml.timingdiagram.TimeConstraint;
import net.sourceforge.plantuml.timingdiagram.TimingNote;
import net.sourceforge.plantuml.timingdiagram.TimingRuler;
import net.sourceforge.plantuml.utils.Position;

public class FooRibbonRectangle extends AbstractFooRibbon {

	public FooRibbonRectangle(TimingRuler ruler, ISkinParam skinParam, List<TimingNote> notes, int suggestedHeight,
			Style style, List<TimeConstraint> constraints) {
		super(ruler, skinParam, suggestedHeight, style, notes, constraints);
	}

	@Override
	protected void drawHexa(UGraphic ug, double len, ChangeState change) {
		final URectangle shape = URectangle.build(len, getRibbonHeight());
		change.getContext(skinParam, style).apply(ug).draw(shape);
	}

	@Override
	protected void drawPentaA(UGraphic ug, double len, ChangeState change) {
		final PentaAShapeRectangle shape = PentaAShapeRectangle.create(len, getRibbonHeight(),
				getContextWithInitialColors(change));
		shape.drawU(ug);
	}

	@Override
	protected void drawPentaB(UGraphic ug, double len, ChangeState change) {
		final PentaBShapeRectangle shape = PentaBShapeRectangle.create(len, getRibbonHeight(),
				change.getContext(skinParam, style));
		shape.drawU(ug);
	}

	@Override
	protected double getHeightForConstraints(StringBounder stringBounder) {
		return Math.max(5, super.getHeightForConstraints(stringBounder));
	}

	@Override
	public double getFullHeight(StringBounder stringBounder) {
		return getHeightForConstraints(stringBounder) + getHeightForTopComment(stringBounder)
				+ getHeightForNotes(stringBounder, Position.TOP) + getRibbonHeight()
				+ getHeightForNotes(stringBounder, Position.BOTTOM) + BOTTOM_MARGIN;
	}

}
