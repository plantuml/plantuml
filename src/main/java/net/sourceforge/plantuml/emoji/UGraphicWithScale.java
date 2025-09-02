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
package net.sourceforge.plantuml.emoji;

import java.awt.geom.AffineTransform;

import net.sourceforge.plantuml.klimt.UChange;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;

public class UGraphicWithScale {

	final private UGraphic ug;
	final private AffineTransform at;
	final private double angle;
	final private double scale;
	final private ColorResolver colorResolver;

	public UGraphicWithScale(UGraphic ug, ColorResolver colorResolver, double scale) {
		this(updateColor(ug, colorResolver), colorResolver, AffineTransform.getScaleInstance(scale, scale), 0, scale);
	}

	private static UGraphic updateColor(UGraphic ug, ColorResolver colorResolver) {
		final HColor color = colorResolver.getDefaultColor();
		return ug.apply(color).apply(color.bg());
	}

	private UGraphicWithScale(UGraphic ug, ColorResolver colorResolver, AffineTransform at, double angle,
			double scale) {
		this.ug = ug;
		this.colorResolver = colorResolver;
		this.at = at;
		this.angle = angle;
		this.scale = scale;
	}

	public UGraphic getUg() {
		return ug;
	}

	public UGraphicWithScale apply(UChange change) {
		return new UGraphicWithScale(ug.apply(change), colorResolver, at, angle, scale);
	}

	public HColor getTrueColor(String code) {
		return colorResolver.getTrueColor(code);
	}

	public HColor getDefaultColor() {
		return colorResolver.getDefaultColor();
	}

	public UGraphicWithScale applyScale(double changex, double changey) {
		if (changex != changey)
			throw new IllegalArgumentException();
		final AffineTransform copy = new AffineTransform(at);
		copy.scale(changex, changey);
		return new UGraphicWithScale(ug, colorResolver, copy, angle, 1 * changex);
	}

	public void draw(UShape shape) {
		ug.draw(shape);
	}

	public UGraphicWithScale applyRotate(double delta_angle, double x, double y) {
		final AffineTransform copy = new AffineTransform(at);
		copy.rotate(delta_angle * Math.PI / 180, x, y);
		return new UGraphicWithScale(ug, colorResolver, copy, this.angle + delta_angle, this.scale);
	}

	public UGraphicWithScale applyTranslate(double x, double y) {
		final AffineTransform copy = new AffineTransform(at);
		copy.translate(x, y);
		return new UGraphicWithScale(ug, colorResolver, copy, angle, this.scale);
	}

	public AffineTransform getAffineTransform() {
		return at;
	}

	public UGraphicWithScale applyMatrix(double v1, double v2, double v3, double v4, double v5, double v6) {
		final AffineTransform copy = new AffineTransform(at);
		copy.concatenate(new AffineTransform(new double[] { v1, v2, v3, v4, v5, v6 }));
		return new UGraphicWithScale(ug, colorResolver, copy, angle, this.scale);
	}

	public final double getAngle() {
		return angle;
	}

	public double getEffectiveScale() {
		final double scaleX = at.getScaleX();
		final double scaleY = at.getScaleY();
		if (scaleX == scaleY)
			return scale * scaleX;
		return scale * Math.sqrt(scaleX * scaleY);
	}

	public double getInitialScale() {
		return scale;
	}

}
