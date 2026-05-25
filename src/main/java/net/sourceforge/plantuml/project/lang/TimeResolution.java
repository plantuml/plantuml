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
package net.sourceforge.plantuml.project.lang;

import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexPart;
import com.plantuml.ubrex.builder.UBrexUpto;

import net.sourceforge.plantuml.project.time.MonthUtils;

public abstract class TimeResolution {

	public static UBrexPart toUbrexA_DD_MONTH_YYYY(String year, String month, String day) {
		final UBrexPart digits1to2 = new UBrexLeaf("〇{1-2}〴d");
		final UBrexPart digits1to4 = new UBrexLeaf("〇{1-4}〴d");
		final UBrexPart monthUbrex = (UBrexPart) MonthUtils.getUbrex();
		return UBrexConcat.build( //
				new UBrexNamed(day, digits1to2), //
				new UBrexUpto(new UBrexLeaf("〴D"), new UBrexNamed(month, monthUbrex)), //
				new UBrexLeaf("〇+〴D"), //
				new UBrexNamed(year, digits1to4) //
		);
	}

	public static UBrexPart toUbrexB_YYYY_MM_DD(String year, String month, String day) {
		final UBrexPart digits1to2 = new UBrexLeaf("〇{1-2}〴d");
		final UBrexPart digits1to4 = new UBrexLeaf("〇{1-4}〴d");
		final UBrexPart nonDigit = new UBrexLeaf("〇+〴D");
		return UBrexConcat.build( //
				new UBrexNamed(year, digits1to4), //
				nonDigit, //
				new UBrexNamed(month, digits1to2), //
				nonDigit, //
				new UBrexNamed(day, digits1to2) //
		);
	}

	public static UBrexPart toUbrexC_MONTH_DD_YYYY(String year, String month, String day) {
		final UBrexPart digits1to2 = new UBrexLeaf("〇{1-2}〴d");
		final UBrexPart digits1to4 = new UBrexLeaf("〇{1-4}〴d");
		final UBrexPart monthUbrex = (UBrexPart) MonthUtils.getUbrex();
		final UBrexPart nonDigit = new UBrexLeaf("〇+〴D");
		return UBrexConcat.build( //
				new UBrexNamed(month, monthUbrex), //
				nonDigit, //
				new UBrexNamed(day, digits1to2), //
				nonDigit, //
				new UBrexNamed(year, digits1to4) //
		);
	}

}
