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
package net.sourceforge.plantuml.cucadiagram;

import java.util.List;

class SquareMaker<O extends Object> {

	public void putInSquare(List<O> data, SquareLinker<O> linker) {
		final int branch = computeBranch(data.size());
		int headBranch = 0;
		for (int i = 1; i < data.size(); i++) {
			final int dist = i - headBranch;
			final O ent2 = data.get(i);
			if (dist == branch) {
				final O ent1 = data.get(headBranch);
				linker.topDown(ent1, ent2);
				headBranch = i;
			} else {
				final O ent1 = data.get(i - 1);
				linker.leftRight(ent1, ent2);
			}
		}

	}

	static int computeBranch(final int size) {
		final double sqrt = Math.sqrt(size);
		final int r = (int) sqrt;
		if (r * r == size) {
			return r;
		}
		return r + 1;
	}

	static int getBottomLeft(final int size) {
		final int s = computeBranch(size);
		final int line = (size - 1) / s;
		return line * s;
	}

	// static int getBottomLeft(final int size) {
	// final int s = computeBranch(size);
	// int result = s * (s - 1);
	// while (result >= size) {
	// result -= s;
	// }
	// return result;
	// }

}
