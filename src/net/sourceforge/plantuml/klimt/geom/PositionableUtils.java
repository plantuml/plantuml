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
package net.sourceforge.plantuml.klimt.geom;

public class PositionableUtils {
	// ::remove file when __HAXE__

	static private XRectangle2D convert(Positionable positionable) {
		final XPoint2D position = positionable.getPosition();
		final XDimension2D size = positionable.getSize();
		return new XRectangle2D(position.getX(), position.getY(), size.getWidth(), size.getHeight());
	}

	static public boolean intersect(Positionable big, Positionable small) {
		final XRectangle2D bigR = convert(big);
		final XRectangle2D smallR = convert(small);
		return bigR.intersects(smallR);
	}

	static public Positionable addMargin(final Positionable pos, final double widthMargin, final double heightMargin) {
		return new Positionable() {

			public XPoint2D getPosition() {
				final XPoint2D p = pos.getPosition();
				return new XPoint2D(p.getX() - widthMargin, p.getY() - heightMargin);
			}

			public XDimension2D getSize() {
				return pos.getSize().delta(2 * widthMargin, 2 * heightMargin);
			}

			public void moveDelta(double deltaX, double deltaY) {
				pos.moveDelta(deltaX, deltaY);
			}
		};
	}

	static private XPoint2D getCenter(Positionable p) {
		final XPoint2D pt = p.getPosition();
		final XDimension2D dim = p.getSize();
		return new XPoint2D(pt.getX() + dim.getWidth() / 2, pt.getY() + dim.getHeight() / 2);
	}

	static private Positionable move(Positionable p, double deltaX, double deltaY) {
		final XPoint2D pt = p.getPosition();
		final XDimension2D dim = p.getSize();
		return new PositionableImpl(pt.getX() + deltaX, pt.getY() + deltaY, dim);

	}

	public static Positionable moveAwayFrom(Positionable fixe, Positionable toMove) {
		final XPoint2D centerFixe = getCenter(fixe);
		final XPoint2D centerToMove = getCenter(toMove);
		// final XPoint2D pt = toMove.getPosition();
		// return new PositionableImpl(pt.getX() + 20, pt.getY(),
		// toMove.getSize());

		final double deltaX = centerToMove.getX() - centerFixe.getX();
		final double deltaY = centerToMove.getY() - centerFixe.getY();

		double min = 0.0;
		if (doesIntersectWithThisCoef(fixe, toMove, deltaX, deltaY, min) == false)
			throw new IllegalArgumentException();

		double max = 0.1;
		while (doesIntersectWithThisCoef(fixe, toMove, deltaX, deltaY, max))
			max = max * 2;

		for (int i = 0; i < 5; i++) {
			assert doesIntersectWithThisCoef(fixe, toMove, deltaX, deltaY, min);
			assert doesIntersectWithThisCoef(fixe, toMove, deltaX, deltaY, max) == false;
			final double candidat = (min + max) / 2.0;
			if (doesIntersectWithThisCoef(fixe, toMove, deltaX, deltaY, candidat))
				min = candidat;
			else
				max = candidat;

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
