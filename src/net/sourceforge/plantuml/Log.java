/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 6528 $
 *
 */
package net.sourceforge.plantuml;

public abstract class Log {

	private static final long start = System.currentTimeMillis();

	public synchronized static void debug(String s) {
	}

	public synchronized static void info(String s) {
		if (OptionFlags.getInstance().isVerbose()) {
			System.out.println(format(s));
		}
	}

	public synchronized static void error(String s) {
		System.err.println(s);
	}

	private static String format(String s) {
		final long delta = System.currentTimeMillis() - start;
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(delta / 1000L);
		sb.append(".");
		sb.append(String.format("%03d", delta % 1000L));
		sb.append(" - ");
		final long total = (Runtime.getRuntime().totalMemory()) / 1024 / 1024;
		final long free = (Runtime.getRuntime().freeMemory()) / 1024 / 1024;
		sb.append(total);
		sb.append(" Mo) ");
		sb.append(free);
		sb.append(" Mo - ");
		sb.append(s);
		return sb.toString();

	}
}
