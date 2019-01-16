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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;

public class PositionableUtils {

	static public Rectangle2D convert(Positionable positionable) {
		final Point2D position = positionable.getPosition();
		final Dimension2D size = positionable.getSize();
		return new Rectangle2D.Double(position.getX(), position.getY(), size.getWidth(), size.getHeight());
	}

	static public boolean contains(Positionable positionable, Point2D p) {
		final Point2D position = positionable.getPosition();
		final Dimension2D size = positionable.getSize();
		final double width = size.getWidth();
		final double height = size.getHeight();

		if (p.getX() < position.getX()) {
			return false;
		}
		if (p.getX() > position.getX() + width) {
			return false;
		}
		if (p.getY() < position.getY()) {
			return false;
		}
		if (p.getY() > position.getY() + height) {
			return false;
		}
		return true;
	}

	static public boolean intersect(Positionable big, Positionable small) {
		final Rectangle2D bigR = convert(big);
		final Rectangle2D smallR = convert(small);
		return bigR.intersects(smallR);
		// final Point2D pt = small.getPosition();
		// final Dimension2D dim = small.getSize();
		//
		// if (contains(big, pt)) {
		// return true;
		// }
		// if (contains(big, new Point2D.Double(pt.getX() + dim.getWidth(),
		// pt.getY()))) {
		// return true;
		// }
		// if (contains(big, new Point2D.Double(pt.getX() + dim.getWidth(),
		// pt.getY() + dim.getHeight()))) {
		// return true;
		// }
		// if (contains(big, new Point2D.Double(pt.getX(), pt.getY() +
		// dim.getHeight()))) {
		// return true;
		// }
		// return false;
	}

	//
	// public boolean intersect(Positionable p) {
	// return intersect(p.getPosition(), p.getSize());
	// }

	static public Positionable addMargin(final Positionable pos, final double widthMargin, final double heightMargin) {
		return new Positionable() {

			public Point2D getPosition() {
				final Point2D p = pos.getPosition();
				return new Point2D.Double(p.getX() - widthMargin, p.getY() - heightMargin);
			}

			public Dimension2D getSize() {
				return Dimension2DDouble.delta(pos.getSize(), 2 * widthMargin, 2 * heightMargin);
			}

			public void moveSvek(double deltaX, double deltaY) {
				pos.moveSvek(deltaX, deltaY);
			}
		};
	}

	static Rectangle2D move(Rectangle2D rect, double dx, double dy) {
		return new Rectangle2D.Double(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());
	}

	static public Point2D getCenter(Positionable p) {
		final Point2D pt = p.getPosition();
		final Dimension2D dim = p.getSize();
		return new Point2D.Double(pt.getX() + dim.getWidth() / 2, pt.getY() + dim.getHeight() / 2);
	}

	static public Positionable move(Positionable p, double deltaX, double deltaY) {
		final Point2D pt = p.getPosition();
		final Dimension2D dim = p.getSize();
		return new PositionableImpl(pt.getX() + deltaX, pt.getY() + deltaY, dim);

	}

	public static Positionable moveAwayFrom(Positionable fixe, Positionable toMove) {
		final Point2D centerFixe = getCenter(fixe);
		final Point2D centerToMove = getCenter(toMove);
		// final Point2D pt = toMove.getPosition();
		// return new PositionableImpl(pt.getX() + 20, pt.getY(),
		// toMove.getSize());

		final double deltaX = centerToMove.getX() - centerFixe.getX();
		final double deltaY = centerToMove.getY() - centerFixe.getY();

		double min = 0.0;
		if (doesIntersectWithThisCoef(fixe, toMove, deltaX, deltaY, min) == false) {
			throw new IllegalArgumentException();
		}
		double max = 0.1;
		while (doesIntersectWithThisCoef(fixe, toMove, deltaX, deltaY, max)) {
			max = max * 2;
		}
		for (int i = 0; i < 5; i++) {
			assert doesIntersectWithThisCoef(fixe, toMove, deltaX, deltaY, min);
			assert doesIntersectWithThisCoef(fixe, toMove, deltaX, deltaY, max) == false;
			final double candidat = (min + max) / 2.0;
			if (doesIntersectWithThisCoef(fixe, toMove, deltaX, deltaY, candidat)) {
				min = candidat;
			} else {
				max = candidat;
			}
			// Log.println("min=" + min + " max=" + max);
		}
		final double candidat = (min + max) / 2.0;
		return move(toMove, deltaX * candidat, deltaY * candidat);

	}

	private static boolean doesIntersectWithThisCoef(Positionable fixe, Positionable toMove, double deltaX,
			double deltaY, double c) {
		final Positionable result = move(toMove, deltaX * c, deltaY * c);
		return intersect(fixe, result);
	}

}
