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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum Month {

	JAN(31), FEB(28), MAR(31), APR(30), MAY(31), JUN(30), JUL(31), AUG(31), SEP(30), OCT(31), NOV(30), DEC(31);

	final private int nbDays;

	private Month(int nbDays) {
		this.nbDays = nbDays;
	}

	public final int getNbDays(int year) {
		if (this == FEB && year % 4 == 0) {
			return 29;
		}
		return nbDays;
	}

	public final int getNum() {
		return ordinal() + 1;
	}

	public final int getNumNormal() {
		return ordinal();
	}

	public Month next() {
		if (this == DEC) {
			return null;
		}
		final List<Month> all = new ArrayList<Month>(EnumSet.allOf(Month.class));
		return all.get(getNum());
	}

	public Month prev() {
		if (this == JAN) {
			return null;
		}
		final List<Month> all = new ArrayList<Month>(EnumSet.allOf(Month.class));
		return all.get(getNum() - 2);
	}

	public static Month fromNum(int num) {
		if (num < 1 || num > 12) {
			throw new IllegalArgumentException();
		}
		final List<Month> all = new ArrayList<Month>(EnumSet.allOf(Month.class));
		return all.get(num - 1);
	}
}
