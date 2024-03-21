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
package net.sourceforge.plantuml.decoration;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.svek.extremity.MiddleCircleCircledMode;
import net.sourceforge.plantuml.svek.extremity.MiddleFactory;
import net.sourceforge.plantuml.svek.extremity.MiddleFactoryCircle;
import net.sourceforge.plantuml.svek.extremity.MiddleFactoryCircleCircled;
import net.sourceforge.plantuml.svek.extremity.MiddleFactorySubset;

public enum LinkMiddleDecor {

	NONE, CIRCLE, CIRCLE_CIRCLED, CIRCLE_CIRCLED1, CIRCLE_CIRCLED2, SUBSET, SUPERSET;

	public MiddleFactory getMiddleFactory(HColor backColor, HColor diagramBackColor) {
		if (this == CIRCLE)
			return new MiddleFactoryCircle(backColor);

		if (this == CIRCLE_CIRCLED)
			return new MiddleFactoryCircleCircled(MiddleCircleCircledMode.BOTH, backColor, diagramBackColor);

		if (this == CIRCLE_CIRCLED1)
			return new MiddleFactoryCircleCircled(MiddleCircleCircledMode.MODE1, backColor, diagramBackColor);

		if (this == CIRCLE_CIRCLED2)
			return new MiddleFactoryCircleCircled(MiddleCircleCircledMode.MODE2, backColor, diagramBackColor);
		
		if (this == SUBSET)
			return new MiddleFactorySubset(false);

		if (this == SUPERSET)
			return new MiddleFactorySubset(true);

		throw new UnsupportedOperationException();
	}

	public LinkMiddleDecor getInversed() {
		if (this == CIRCLE_CIRCLED1)
			return CIRCLE_CIRCLED2;
		else if (this == CIRCLE_CIRCLED2)
			return CIRCLE_CIRCLED1;

		return this;
	}

}