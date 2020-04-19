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
package net.sourceforge.plantuml.skin;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class ActorStickMan extends AbstractTextBlock implements TextBlock {

	private final double armsY = 8;
	private final double armsLenght = 13;
	private final double bodyLenght = 27;
	private final double legsX = 13;
	private final double legsY = 15;
	private final double headDiam = 16;

	private final SymbolContext symbolContext;

	ActorStickMan(SymbolContext symbolContext) {
		this.symbolContext = symbolContext;
	}

	public void drawU(UGraphic ug) {

		final double startX = Math.max(armsLenght, legsX) - headDiam / 2.0 + thickness();

		final UEllipse head = new UEllipse(headDiam, headDiam);
		final double centerX = startX + headDiam / 2;

		final UPath path = new UPath();
		path.moveTo(0, 0);
		path.lineTo(0, bodyLenght);
		path.moveTo(-armsLenght, armsY);
		path.lineTo(armsLenght, armsY);
		path.moveTo(0, bodyLenght);
		path.lineTo(-legsX, bodyLenght + legsY);
		path.moveTo(0, bodyLenght);
		path.lineTo(legsX, bodyLenght + legsY);
		if (symbolContext.getDeltaShadow() != 0) {
			head.setDeltaShadow(symbolContext.getDeltaShadow());
			path.setDeltaShadow(symbolContext.getDeltaShadow());
		}

		ug = symbolContext.apply(ug);
		ug.apply(new UTranslate(startX, thickness())).draw(head);
		ug.apply(new UTranslate(centerX, headDiam + thickness())).apply(new HColorNone().bg()).draw(path);
	}

	private double thickness() {
		return symbolContext.getStroke().getThickness();
	}

	public double getPreferredWidth() {
		return Math.max(armsLenght, legsX) * 2 + 2 * thickness();
	}

	public double getPreferredHeight() {
		return headDiam + bodyLenght + legsY + 2 * thickness() + symbolContext.getDeltaShadow() + 1;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getPreferredWidth(), getPreferredHeight());
	}
}
