/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.api;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NiceNumber {

	public static int getNicer(final int value) {
		if (value <= 18) {
			return value;
		}
		if (value < 93) {
			return ((value + 2) / 5) * 5;
		}
		if (value < 100) {
			return ((value + 5) / 10) * 10;
		}
		int m = 1;
		double head = value;
		while (head >= 100) {
			head = head / 10.0;
			m *= 10;
		}
		return getNicer((int) Math.round(head)) * m;
	}

	public static String format(final long v) {
		final DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		df.setGroupingSize(3);
		df.setMaximumFractionDigits(0);
		final String t = df.format(v).replace(',', ' ');
		return t;
	}
}
