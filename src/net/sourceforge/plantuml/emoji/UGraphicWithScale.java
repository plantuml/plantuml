/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.emoji;

import java.awt.geom.AffineTransform;

import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UShape;

public class UGraphicWithScale {

	final private UGraphic ug;
	final private AffineTransform at;
	final private double angle;

	public UGraphicWithScale(UGraphic ug, double scale) {
		this(ug, AffineTransform.getScaleInstance(scale, scale), 0);
	}

	private UGraphicWithScale(UGraphic ug, AffineTransform at, double angle) {
		this.ug = ug;
		this.at = at;
		this.angle = angle;
	}

	public UGraphic getUg() {
		return ug;
	}

	public UGraphicWithScale apply(UChange change) {
		return new UGraphicWithScale(ug.apply(change), at, angle);
	}

	public UGraphicWithScale applyScale(double changex, double changey) {
		final AffineTransform copy = new AffineTransform(at);
		copy.scale(changex, changey);
		return new UGraphicWithScale(ug, copy, angle);
	}

	public void draw(UShape shape) {
		ug.draw(shape);
	}

	public UGraphicWithScale applyRotate(double delta_angle, double x, double y) {
		final AffineTransform copy = new AffineTransform(at);
		copy.rotate(delta_angle * Math.PI / 180, x, y);
		return new UGraphicWithScale(ug, copy, this.angle + delta_angle);
	}

	public UGraphicWithScale applyTranslate(double x, double y) {
		final AffineTransform copy = new AffineTransform(at);
		copy.translate(x, y);
		return new UGraphicWithScale(ug, copy, angle);
	}

	public AffineTransform getAffineTransform() {
		return at;
	}

	public UGraphicWithScale applyMatrix(double v1, double v2, double v3, double v4, double v5, double v6) {
		final AffineTransform copy = new AffineTransform(at);
		copy.concatenate(new AffineTransform(new double[] { v1, v2, v3, v4, v5, v6 }));
		return new UGraphicWithScale(ug, copy, angle);
	}

	public final double getAngle() {
		return angle;
	}

}
