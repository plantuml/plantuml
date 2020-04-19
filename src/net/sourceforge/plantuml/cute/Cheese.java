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

public class Cheese implements CuteShape {

	private final MyDouble radius;
	private final MyDouble startAngle;
	private final MyDouble endAngle;
	private final RotationZoom rotationZoom;

	public Cheese(VarArgs varArgs) {
		this.radius = varArgs.getAsMyDouble("radius");
		this.startAngle = varArgs.getAsMyDouble("start").toRadians();
		this.endAngle = varArgs.getAsMyDouble("end").toRadians();
		this.rotationZoom = RotationZoom.none();
	}

	public Cheese(MyDouble radius, MyDouble startAngle, MyDouble endAngle, RotationZoom rotation) {
		this.radius = radius;
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		this.rotationZoom = rotation;
	}

	public void drawU(UGraphic ug) {
		final Balloon balloon = new Balloon(new Point2D.Double(), radius.getValue())
				.rotate(rotationZoom);

		final double angle1 = rotationZoom.applyRotation(startAngle.getValue());
		final double angle2 = rotationZoom.applyRotation(endAngle.getValue());

		final Point2D ptA = balloon.getPointOnCircle(angle1);
		final Point2D ptB = balloon.getPointOnCircle(angle2);

		final UPath path = new UPath();
		final Point2D ptA0;
		if (radius.hasCurvation()) {
			ptA0 = balloon.getSegmentCenterToPointOnCircle(angle1).getFromAtoB(radius.getCurvation(0));
			path.moveTo(ptA0);
		} else {
			ptA0 = null;
			path.moveTo(balloon.getCenter());
		}
		final Balloon insideA;
		if (startAngle.hasCurvation()) {
			insideA = balloon.getInsideTangentBalloon1(angle1, startAngle.getCurvation(0));
			final Point2D ptA1 = balloon.getSegmentCenterToPointOnCircle(angle1).getFromAtoB(
					radius.getValue() - startAngle.getCurvation(0));
			final Point2D ptA2 = balloon.getPointOnCirclePassingByThisPoint(insideA.getCenter());
			path.lineTo(ptA1);
			path.arcTo(ptA2, insideA.getRadius(), 0, 1);
		} else {
			insideA = null;
			path.lineTo(ptA);
		}
		final Balloon insideB;
		if (endAngle.hasCurvation()) {
			insideB = balloon.getInsideTangentBalloon2(angle2, endAngle.getCurvation(0));
			final Point2D ptB1 = balloon.getPointOnCirclePassingByThisPoint(insideB.getCenter());
			final Point2D ptB2 = balloon.getSegmentCenterToPointOnCircle(angle2).getFromAtoB(
					radius.getValue() - endAngle.getCurvation(0));

			path.arcTo(ptB1, balloon.getRadius(), 0, 1);
			path.arcTo(ptB2, insideB.getRadius(), 0, 1);
		} else {
			insideB = null;
			path.arcTo(ptB, balloon.getRadius(), 0, 1);
		}
		if (radius.hasCurvation()) {
			final Point2D ptB0 = balloon.getSegmentCenterToPointOnCircle(angle2).getFromAtoB(radius.getCurvation(0));
			path.lineTo(ptB0);
			path.arcTo(ptA0, radius.getCurvation(0), 0, 1);
		} else {
			path.lineTo(balloon.getCenter());
		}
		path.closePath();
		ug.draw(path);

	}

	public CuteShape rotateZoom(RotationZoom other) {
		return new Cheese(radius, startAngle, endAngle, rotationZoom.compose(other));
	}

}
