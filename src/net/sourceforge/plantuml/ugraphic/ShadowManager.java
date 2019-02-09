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
package net.sourceforge.plantuml.ugraphic;

import java.awt.Color;

public class ShadowManager {
	
	// http://www.w3schools.com/svg/svg_feoffset.asp

	private final int c1;
	private final int c2;

	public ShadowManager(int c1, int c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	public double[] getShadowDeltaPoints(double deltaShadow, double diff, double[] points) {
		assert points.length % 2 == 0;
		double cx = 0;
		double cy = 0;
		for (int i = 0; i < points.length; i += 2) {
			cx += points[i];
			cy += points[i + 1];
		}
		final int nbPoints = points.length / 2;

		cx = cx / nbPoints;
		cy = cy / nbPoints;

		final double[] result = new double[points.length];
		for (int i = 0; i < result.length; i += 2) {
			final double diffx = points[i] > cx ? -diff : diff;
			final double diffy = points[i + 1] > cy ? -diff : diff;
			result[i] = points[i] + diffx + deltaShadow;
			result[i + 1] = points[i + 1] + diffy + deltaShadow;
		}
		return result;
	}

	public Color getColor(double delta, double total) {
		final int c = (int) (c2 + 1.0 * delta / total * (c1 - c2));
		return new Color(c, c, c);
	}

}
