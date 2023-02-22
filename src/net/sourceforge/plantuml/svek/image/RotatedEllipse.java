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
package net.sourceforge.plantuml.svek.image;

import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;

public class RotatedEllipse {

	private final UEllipse ellipse;
	private final double beta;

	public RotatedEllipse(UEllipse ellipse, double beta) {
		this.ellipse = ellipse;
		this.beta = beta;
	}

	public double getA() {
		return ellipse.getWidth() / 2;
	}

	public double getB() {
		return ellipse.getHeight() / 2;
	}

	public double getBeta() {
		return beta;
	}

	public XPoint2D getPoint(double theta) {
		final double x = getA() * Math.cos(theta);
		final double y = getB() * Math.sin(theta);

		final double xp = x * Math.cos(beta) - y * Math.sin(beta);
		final double yp = x * Math.sin(beta) + y * Math.cos(beta);

		return new XPoint2D(xp, yp);
	}

	public double getOtherTheta(double theta1) {
		final double z = getPoint(theta1).getX();
		final double a = getA() * Math.cos(beta);
		final double b = getB() * Math.sin(beta);
		final double sum = 2 * a * z / (a * a + b * b);
		final double other = sum - Math.cos(theta1);
		return -Math.acos(other);
	}

	private double other(double[] all, double some) {
		final double diff0 = Math.abs(some - all[0]);
		final double diff1 = Math.abs(some - all[1]);

		if (diff0 > diff1) {
			return all[0];
		}
		return all[1];
	}

}
