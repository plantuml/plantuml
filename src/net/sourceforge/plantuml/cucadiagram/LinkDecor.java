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
 * Contribution :  Hisashi Miyashita
 * 
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactory;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryArrow;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryArrowAndCircle;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircle;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleConnect;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleCross;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleCrowfoot;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleLine;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCrowfoot;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryDiamond;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryDoubleLine;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryExtendsLike;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryHalfArrow;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryLineCrowfoot;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryNotNavigable;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryParenthesis;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryPlus;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactorySquarre;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryTriangle;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public enum LinkDecor {

	NONE(2, false, 0), EXTENDS(30, false, 2), COMPOSITION(15, true, 1.3), AGREGATION(15, false, 1.3),
	NOT_NAVIGABLE(1, false, 0.5),

	REDEFINES(30, false, 2), DEFINEDBY(30, false, 2),

	CROWFOOT(10, true, 0.8), CIRCLE_CROWFOOT(14, false, 0.8), CIRCLE_LINE(10, false, 0.8), DOUBLE_LINE(7, false, 0.7),
	LINE_CROWFOOT(10, false, 0.8),

	ARROW(10, true, 0.5), ARROW_TRIANGLE(10, true, 0.8), ARROW_AND_CIRCLE(10, false, 0.5),

	CIRCLE(0, false, 0.5), CIRCLE_FILL(0, false, 0.5), CIRCLE_CONNECT(0, false, 0.5),
	PARENTHESIS(0, false, OptionFlags.USE_INTERFACE_EYE2 ? 0.5 : 1.0), SQUARE(0, false, 0.5),

	CIRCLE_CROSS(0, false, 0.5), PLUS(0, false, 1.5), HALF_ARROW(0, false, 1.5), SQUARRE_toberemoved(30, false, 0);

	private final double arrowSize;
	private final int margin;
	private final boolean fill;

	private LinkDecor(int margin, boolean fill, double arrowSize) {
		this.margin = margin;
		this.fill = fill;
		this.arrowSize = arrowSize;
	}

	public int getMargin() {
		return margin;
	}

	public boolean isFill() {
		return fill;
	}

	public double getArrowSize() {
		return arrowSize;
	}

	public boolean isExtendsLike() {
		return this == EXTENDS || this == REDEFINES || this == DEFINEDBY;
	}

	public ExtremityFactory getExtremityFactory(HColor backgroundColor) {
		switch (this) {
		case PLUS:
			return new ExtremityFactoryPlus();
		case REDEFINES:
			return new ExtremityFactoryExtendsLike(backgroundColor, false);
		case DEFINEDBY:
			return new ExtremityFactoryExtendsLike(backgroundColor, true);
		case HALF_ARROW:
			return new ExtremityFactoryHalfArrow();
		case ARROW_TRIANGLE:
			return new ExtremityFactoryTriangle();
		case CROWFOOT:
			return new ExtremityFactoryCrowfoot();
		case CIRCLE_CROWFOOT:
			return new ExtremityFactoryCircleCrowfoot();
		case LINE_CROWFOOT:
			return new ExtremityFactoryLineCrowfoot();
		case CIRCLE_LINE:
			return new ExtremityFactoryCircleLine();
		case DOUBLE_LINE:
			return new ExtremityFactoryDoubleLine();
		case CIRCLE_CROSS:
			return new ExtremityFactoryCircleCross();
		case ARROW:
			return new ExtremityFactoryArrow();
		case ARROW_AND_CIRCLE:
			return new ExtremityFactoryArrowAndCircle();
		case NOT_NAVIGABLE:
			return new ExtremityFactoryNotNavigable();
		case AGREGATION:
			return new ExtremityFactoryDiamond(false, backgroundColor);
		case COMPOSITION:
			return new ExtremityFactoryDiamond(true, backgroundColor);
		case CIRCLE:
			return new ExtremityFactoryCircle(false);
		case CIRCLE_FILL:
			return new ExtremityFactoryCircle(true);
		case SQUARE:
			return new ExtremityFactorySquarre();
		case PARENTHESIS:
			return new ExtremityFactoryParenthesis();
		case CIRCLE_CONNECT:
			return new ExtremityFactoryCircleConnect();
		default:
			return null;
		}
	}
}
