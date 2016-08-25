/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.project2;

import java.util.List;

public abstract class RowUtils {

	public static Row overwrite(Row r1, Row r2) {
		return new RowOverwrite(r1, r2);
	}

	public static Row merge(Row r1, Row r2) {
		return new RowMerge(r1, r2);
	}

	public static Row merge(List<Row> rows) {
		Row result = rows.get(0);
		for (int i = 1; i < rows.size(); i++) {
			result = merge(result, rows.get(i));
		}
		return result;

	}

}
