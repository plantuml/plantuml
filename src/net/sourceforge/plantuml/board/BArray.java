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
package net.sourceforge.plantuml.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BArray implements Iterable<BNode> {

	private final Map<String, BNode> data = new HashMap<String, BNode>();
	private int maxX;
	private int maxY;

	public void put(BNode node) {
		final String key = getKey(node.getX(), node.getStage());
		if (data.containsKey(key)) {
			throw new IllegalArgumentException();
		}
		data.put(key, node);
		this.maxX = Math.max(this.maxX, node.getX());
		this.maxY = Math.max(this.maxY, node.getStage());
	}

	public BNode getCell(int x, int y) {
		final String key = getKey(x, y);
		return data.get(key);
	}

	private String getKey(int x, int y) {
		return "" + x + ";" + y;
	}

	public Iterator<BNode> iterator() {
		return Collections.unmodifiableCollection(data.values()).iterator();
	}

	public final int getMaxX() {
		return maxX;
	}

	public final int getMaxY() {
		return maxY;
	}

}
