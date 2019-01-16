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
package net.sourceforge.plantuml.api;

public final class CountRate {

	private final MagicArray lastMinute = new MagicArray(60);
	private final MagicArray lastHour = new MagicArray(60);
	private final MagicArray lastDay = new MagicArray(140);

	public void increment() {
		final long now = System.currentTimeMillis();
		lastMinute.incKey(now / 1000L);
		lastHour.incKey(now / (60 * 1000L));
		lastDay.incKey(now / (10 * 60 * 1000L));
	}

	public void increment(int value) {
		final long now = System.currentTimeMillis();
		lastMinute.incKey(now / 1000L, value);
		lastHour.incKey(now / (60 * 1000L), value);
		lastDay.incKey(now / (10 * 60 * 1000L), value);
	}

	public long perMinute() {
		return lastMinute.getSum();
	}

	public long perHour() {
		return lastHour.getSum();
	}

	public long perDay() {
		return lastDay.getSum();
	}

	public long perMinuteMax() {
		return lastMinute.getMaxSum();
	}

	public long perHourMax() {
		return lastHour.getMaxSum();
	}

	public long perDayMax() {
		return lastDay.getMaxSum();
	}

}
