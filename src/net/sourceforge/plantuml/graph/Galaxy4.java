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
package net.sourceforge.plantuml.graph;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.geom.Point2DInt;
import net.sourceforge.plantuml.geom.PolylineBreakeable;
import net.sourceforge.plantuml.geom.SpiderWeb;

public class Galaxy4 {

	final private Board board;

	final private Map<ALink, PolylineBreakeable> lines = new LinkedHashMap<ALink, PolylineBreakeable>();
	final private SpiderWeb spiderWeb;

	public Galaxy4(Board board, int widthCell, int heightCell) {
		this.spiderWeb = new SpiderWeb(widthCell, heightCell);
		this.board = board;
	}

	public Point2DInt getMainPoint(int row, int col) {
		return spiderWeb.getMainPoint(row, col);
	}

	public PolylineBreakeable getPolyline(ALink link) {
		return lines.get(link);

	}

	public void addLink(ALink link) {
		final int rowStart = link.getNode1().getRow();
		final int rowEnd = link.getNode2().getRow();
		final int colStart = board.getCol(link.getNode1());
		final int colEnd = board.getCol(link.getNode2());

		final PolylineBreakeable polyline = spiderWeb.addPolyline(rowStart, colStart, rowEnd, colEnd);

		Log.info("link=" + link + " polyline=" + polyline);

		if (polyline == null) {
			Log.info("PENDING " + link + " " + polyline);
		} else {
			lines.put(link, polyline);
		}

	}

	public final Board getBoard() {
		return board;
	}

	public final Map<ALink, PolylineBreakeable> getLines() {
		return Collections.unmodifiableMap(lines);
	}

}
