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
package net.sourceforge.plantuml.cute;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;

public class Stick implements CuteShape {

	private final double width;
	private final double height;
	private final RotationZoom rotationZoom;

	public Stick(VarArgs varArgs) {
		final Point2D dim = varArgs.getAsPoint("dimension");
		this.width = dim.getX();
		this.height = dim.getY();
		this.rotationZoom = RotationZoom.none();
	}

	private Stick(double width, double height, RotationZoom rotation) {
		this.width = width;
		this.height = height;
		this.rotationZoom = rotation;
	}

	public void drawU(UGraphic ug) {
		if (width > height) {
			drawRotate1(ug);
		} else {
			drawRotate2(ug);
		}
	}

	private void drawRotate1(UGraphic ug) {
		assert width > height;
		final UPath path = new UPath();
		final double small = height / 2;
		path.moveTo(rotationZoom.getPoint(small, 0));
		path.lineTo(rotationZoom.getPoint(width - small, 0));
		path.arcTo(rotationZoom.getPoint(width - small, height), small, 0, 1);
		path.lineTo(rotationZoom.getPoint(small, height));
		path.arcTo(rotationZoom.getPoint(small, 0), small, 0, 1);
		path.closePath();
		ug.draw(path);
	}

	private void drawRotate2(UGraphic ug) {
		assert height > width;
		final UPath path = new UPath();
		final double small = width / 2;
		path.moveTo(rotationZoom.getPoint(width, small));
		path.lineTo(rotationZoom.getPoint(width, height - small));
		path.arcTo(rotationZoom.getPoint(0, height - small), small, 0, 1);
		path.lineTo(rotationZoom.getPoint(0, small));
		path.arcTo(rotationZoom.getPoint(width, small), small, 0, 1);
		path.closePath();
		ug.draw(path);
	}

	public Stick rotateZoom(RotationZoom other) {
		return new Stick(width, height, this.rotationZoom.compose(other));
	}

}
