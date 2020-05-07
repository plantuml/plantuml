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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileGeometry extends Dimension2D {

	private final double width;
	private final double height;
	private final double left;
	private final double inY;
	private final double outY;

	public Point2D getPointA() {
		return new Point2D.Double(left, inY);
	}

	public Point2D getPointIn() {
		return new Point2D.Double(left, inY);
	}

	public Point2D getPointB() {
		if (outY == Double.MIN_NORMAL) {
			throw new UnsupportedOperationException();
		}
		return new Point2D.Double(width, (inY + outY) / 2);
	}

	public Point2D getPointC() {
		if (outY == Double.MIN_NORMAL) {
			throw new UnsupportedOperationException();
		}
		return new Point2D.Double(left, outY);
	}

	public Point2D getPointD() {
		if (outY == Double.MIN_NORMAL) {
			throw new UnsupportedOperationException();
		}
		return new Point2D.Double(0, (inY + outY) / 2);
	}

	public Point2D getPointOut() {
		if (outY == Double.MIN_NORMAL) {
			throw new UnsupportedOperationException();
		}
		return new Point2D.Double(left, outY);
	}

	public FtileGeometry(Dimension2D dim, double left, double inY) {
		this(dim.getWidth(), dim.getHeight(), left, inY);
	}

	public FtileGeometry(double width, double height, double left, double inY) {
		this(width, height, left, inY, Double.MIN_NORMAL);
	}

	@Override
	public String toString() {
		return "[" + width + "x" + height + " left=" + left + "]";
	}

	@Override
	public void setSize(double width, double height) {
		throw new UnsupportedOperationException();
	}

	public FtileGeometry(double width, double height, double left, double inY, double outY) {
		this.left = left;
		this.inY = inY;
		this.outY = outY;
		this.width = width;
		this.height = height;
	}

	public FtileGeometry incHeight(double northHeight) {
		return new FtileGeometry(width, height + northHeight, left, inY, outY);
	}

//	public FtileGeometry incInnerHeight(double northHeight) {
//		return new FtileGeometry(width, height + northHeight, left, inY, outY + northHeight);
//	}

	public FtileGeometry(Dimension2D dim, double left, double inY, double outY) {
		this(dim.getWidth(), dim.getHeight(), left, inY, outY);
	}

	public boolean hasPointOut() {
		return outY != Double.MIN_NORMAL;
	}

	public FtileGeometry withoutPointOut() {
		return new FtileGeometry(width, height, left, inY);
	}

	public FtileGeometry translate(UTranslate translate) {
		final double dx = translate.getDx();
		final double dy = translate.getDy();
		if (this.outY == Double.MIN_NORMAL) {
			return new FtileGeometry(width, height, left + dx, inY + dy);
		}
		return new FtileGeometry(width, height, left + dx, inY + dy, outY + dy);
	}

	public final double getInY() {
		return inY;
	}

	public final double getLeft() {
		return left;
	}

	public final double getRight() {
		return width - left;
	}

	public double getOutY() {
		return outY;
	}

	public final double getWidth() {
		return width;
	}

	public final double getHeight() {
		return height;
	}

	public FtileGeometry addDim(double deltaWidth, double deltaHeight) {
		return new FtileGeometry(width + deltaWidth, height + deltaHeight, left, inY, outY + deltaHeight);
	}

	public FtileGeometry addMarginX(double marginx) {
		return new FtileGeometry(width + 2 * marginx, height, left + marginx, inY, outY);
	}

	public FtileGeometry addMarginX(double margin1, double margin2) {
		return new FtileGeometry(width + margin1 + margin2, height, left + margin1, inY, outY);
	}

	public FtileGeometry fixedHeight(double fixedHeight) {
		return new FtileGeometry(width, fixedHeight, left, inY, outY);
	}

	public FtileGeometry appendBottom(FtileGeometry other) {
		return new FtileGeometryMerger(this, other).getResult();
	}

	public FtileGeometry ensureHeight(double newHeight) {
		if (this.height > newHeight) {
			return this;
		}
		return fixedHeight(newHeight);
	}

	private FtileGeometry ensureRightStrange(double newRight) {
		final double right = this.width - this.left;
		if (right > newRight) {
			return this;
		}
		// return addMarginX(0, newRight - right);
		return addMarginX(0, newRight);
	}

}
