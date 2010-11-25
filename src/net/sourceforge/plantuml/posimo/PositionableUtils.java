/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4236 $
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

	static public Positionable addMargin(final Positionable pos, final double widthMargin, final double heightMargin) {
		return new Positionable() {

			public Point2D getPosition() {
				final Point2D p = pos.getPosition();
				return new Point2D.Double(p.getX() - widthMargin, p.getY() - heightMargin);
			}

			public Dimension2D getSize() {
				return Dimension2DDouble.delta(pos.getSize(), 2 * widthMargin, 2 * heightMargin);
			}
		};
	}

	static Rectangle2D move(Rectangle2D rect, double dx, double dy) {
		return new Rectangle2D.Double(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());
	}

}
