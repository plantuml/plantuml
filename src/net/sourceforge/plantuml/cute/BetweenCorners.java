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

public class BetweenCorners {

	private final TriangleCorner corner1;
	private final TriangleCorner corner2;
	private final Tension tension;

	private Balloon inside1;
	private Balloon inside2;
	private Balloon contact;
	private Balloon apo;
	private Point2D apopt1;
	private Point2D apopt2;

	public BetweenCorners(TriangleCorner corner1, TriangleCorner corner2, Tension tension) {
		this.corner1 = corner1;
		this.corner2 = corner2;
		this.tension = tension;

		if (corner1.hasCurvation()) {
			inside1 = corner1.getBalloonInside();
		}
		if (corner2.hasCurvation()) {
			inside2 = corner2.getBalloonInside();
		}
		if (tension.isNone() == false) {
			contact = new Balloon(getTensionPoint(), getLength() / 1000.0);
		}

		if (inside1 != null && inside2 != null && contact != null) {
			apo = ApolloniusSolver2.solveApollonius(inside1, inside2, contact, 1, 1, 1);
			apopt1 = apo.getPointOnCirclePassingByThisPoint(inside1.getCenter());
			apopt2 = apo.getPointOnCirclePassingByThisPoint(inside2.getCenter());
		}

	}

	public Point2D getPointJ() {
		if (getCorner1().hasCurvation() == false) {
			return getCorner1().getO();
		}
		if (tension.isNone()) {
			return getCorner1().getOnSegmentA(getCorner1().getCurvation());
		}
		throw new UnsupportedOperationException();
	}

	public Point2D getPointK() {
		if (getCorner1().hasCurvation() == false) {
			return getCorner1().getO();
		}
		if (tension.isNone()) {
			return getCorner1().getOnSegmentB(getCorner1().getCurvation());
		}
		throw new UnsupportedOperationException();
	}

	private double getBalloonRadius() {
		return getCorner1().getBalloonInside().getRadius();
	}

	public void initPath(UPath path) {
		if (apo != null) {
			path.moveTo(apopt2);
		} else {
			path.moveTo(getPointK());
		}
	}

	public void addToPath(UPath path, int swepFlag) {
		if (apo != null) {
			path.arcTo(apopt1, getCorner1().getBalloonInside().getRadius(), 0, 1);
			path.arcTo(apopt2, apo.getRadius(), 0, 1);
			// } else if (getTension().isNone()) {
			// path.lineTo(getPointJ());
			// if (getCorner2().hasCurvation()) {
			// path.arcTo(getPointK(), getBalloonRadius(), 0, swepFlag);
			// }
			// } else {
			// // final int sweep_flag = 1;
			// path.arcTo(getPointJ(), getRadiusFuzzy1(), 0, swepFlag);
			// if (getCorner2().hasCurvation()) {
			// path.arcTo(getPointK(), getBalloonRadius(), 0, swepFlag);
			// }
			// }
		} else {
			path.lineTo(getPointJ());
			if (getCorner1().hasCurvation()) {
				path.arcTo(getPointK(), getBalloonRadius(), 0, swepFlag);
			}
		}
	}

	public void debugMe(UGraphic ug) {
		if (getCorner2().hasCurvation() == false) {
			return;
		}
		if (tension.isNone()) {
			return;
		}
		inside1.drawU(ug);
		inside2.drawU(ug);
		// getSegment().debugMe(ug);
		contact.drawU(ug);

		new Balloon(apopt1, 5).drawU(ug);
		new Balloon(apopt2, 5).drawU(ug);

		// getSegmentCross().debugMe(ug);

		apo.drawU(ug);
		//
		// final Point2D newCenter = getSegmentCross().getOrthoPoint(-50);
		// new Segment(newCenter, getCorner1().getBalloonInside().getCenter()).debugMe(ug);
		// new Segment(newCenter, getCorner2().getBalloonInside().getCenter()).debugMe(ug);

	}

	private double getRadiusFuzzy1() {
		final double a = getLength() / 2;
		final double b = getTension().getValue();
		final double radius = (a * a + b * b) / 2 / b;
		return radius;
	}

	private Segment getSegment() {
		return new Segment(getCorner1().getO(), getCorner2().getO());
	}

	private Point2D getTensionPoint() {
		return getSegment().getOrthoPoint(getTension().getValue());
	}

	private Segment getSegmentCross() {
		return new Segment(getCorner1().getCornerOrBalloonCenter(), getCorner2().getCornerOrBalloonCenter());
	}

	public Tension getTension() {
		return tension;
	}

	public TriangleCorner getCorner1() {
		return corner1;
	}

	public TriangleCorner getCorner2() {
		return corner2;
	}

	public double getLength() {
		return getSegment().getLength();
	}

}
