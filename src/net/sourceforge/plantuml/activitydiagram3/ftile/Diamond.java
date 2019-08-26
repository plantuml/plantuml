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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UPolygon;

public class Diamond {

	final static public double diamondHalfSize = 12;

	public static UPolygon asPolygon(double shadowing) {
		final UPolygon diams = new UPolygon();

		diams.addPoint(diamondHalfSize, 0);
		diams.addPoint(diamondHalfSize * 2, diamondHalfSize);
		diams.addPoint(diamondHalfSize, diamondHalfSize * 2);
		diams.addPoint(0, diamondHalfSize);
		diams.addPoint(diamondHalfSize, 0);

		// if (shadowing) {
		// diams.setDeltaShadow(3);
		// }
		diams.setDeltaShadow(shadowing);

		return diams;
	}

	public static UPolygon asPolygon(double shadowing, double width, double height) {
		final UPolygon diams = new UPolygon();

		diams.addPoint(diamondHalfSize, 0);
		diams.addPoint(width - diamondHalfSize, 0);
		diams.addPoint(width, height / 2);
		diams.addPoint(width - diamondHalfSize, height);
		diams.addPoint(diamondHalfSize, height);
		diams.addPoint(0, height / 2);
		diams.addPoint(diamondHalfSize, 0);

		// if (shadowing) {
		// diams.setDeltaShadow(3);
		// }
		diams.setDeltaShadow(shadowing);

		return diams;
	}

	public static Stencil asStencil(final TextBlock tb) {
		return new Stencil() {

			private final double getDeltaX(double height, double y) {
				final double p = y / height * 2;
				if (p <= 1) {
					return diamondHalfSize * p;
				}
				return diamondHalfSize * (2 - p);
			}

			public double getStartingX(StringBounder stringBounder, double y) {
				final Dimension2D dim = tb.calculateDimension(stringBounder);
				return -getDeltaX(dim.getHeight(), y);
			}

			public double getEndingX(StringBounder stringBounder, double y) {
				final Dimension2D dim = tb.calculateDimension(stringBounder);
				return dim.getWidth() + getDeltaX(dim.getHeight(), y);
			}
		};
	}

	public static UPolygon asPolygonFoo1(boolean shadowing, double width, double height) {
		final UPolygon diams = new UPolygon();

		diams.addPoint(width / 2, 0);
		diams.addPoint(width, height / 2);
		diams.addPoint(width / 2, height);
		diams.addPoint(0, height / 2);

		if (shadowing) {
			diams.setDeltaShadow(3);
		}

		return diams;
	}

}
