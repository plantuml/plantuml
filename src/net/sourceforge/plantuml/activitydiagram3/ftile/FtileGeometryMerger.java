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

public class FtileGeometryMerger {

	private final FtileGeometry result;

	public FtileGeometryMerger(FtileGeometry geo1, FtileGeometry geo2) {
		final double left = Math.max(geo1.getLeft(), geo2.getLeft());
		final double dx1 = left - geo1.getLeft();
		final double dx2 = left - geo2.getLeft();
		final double width = Math.max(geo1.getWidth() + dx1, geo2.getWidth() + dx2);
		final double height = geo1.getHeight() + geo2.getHeight();

		if (geo2.hasPointOut()) {
			result = new FtileGeometry(width, height, left, geo1.getInY(), geo2.getOutY() + geo1.getHeight());
		} else {
			result = new FtileGeometry(width, height, left, geo1.getInY());
		}
	}

	public final FtileGeometry getResult() {
		return result;
	}
}
