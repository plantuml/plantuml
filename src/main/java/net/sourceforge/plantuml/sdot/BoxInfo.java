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
package net.sourceforge.plantuml.sdot;

import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agraphinfo_t;
import h.ST_boxf;
import h.ST_textlabel_t;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;

public class BoxInfo {

	private final XPoint2D upperRight;
	private final XPoint2D lowerLeft;

	private BoxInfo(XPoint2D upperRight, XPoint2D lowerLeft) {
		this.upperRight = upperRight;
		this.lowerLeft = lowerLeft;
	}

	public static BoxInfo fromTextlabel(ST_textlabel_t label) {
		final double x = label.pos.x;
		final double y = label.pos.y;
		final double width = label.dimen.x;
		final double height = label.dimen.y;

		final XPoint2D upperRight = new XPoint2D(x + width / 2, y - height / 2);
		final XPoint2D lowerLeft = new XPoint2D(x - width / 2, y + height / 2);
		return new BoxInfo(upperRight, lowerLeft);

	}

	public static BoxInfo fromNode(ST_Agnode_s node) {
		final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) node.data;
		final double width = data.width * 72;
		final double height = data.height * 72;
		final double x = data.coord.x;
		final double y = data.coord.y;

		final XPoint2D upperRight = new XPoint2D(x + width / 2, y - height / 2);
		final XPoint2D lowerLeft = new XPoint2D(x - width / 2, y + height / 2);
		return new BoxInfo(upperRight, lowerLeft);
	}

	public XDimension2D getDimension() {
		final double width = upperRight.getX() - lowerLeft.getX();
		final double height = lowerLeft.getY() - upperRight.getY();
		return new XDimension2D(width, height);
	}

	public static BoxInfo fromGraphInfo(ST_Agraphinfo_t data) {
		final ST_boxf bb = (ST_boxf) data.bb;
		final double llx = bb.LL.x;
		final double lly = bb.LL.y;
		final double urx = bb.UR.x;
		final double ury = bb.UR.y;

		final XPoint2D upperRight = new XPoint2D(urx, ury);
		final XPoint2D lowerLeft = new XPoint2D(llx, lly);
		return new BoxInfo(upperRight, lowerLeft);
	}

	public final XPoint2D getUpperRight() {
		return upperRight;
	}

	public final XPoint2D getLowerLeft() {
		return lowerLeft;
	}

}
