/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License aint with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */
package net.sourceforge.plantuml.api;

import java.util.prefs.Preferences;

public class NumberAnalyzed implements INumberAnalyzed {

	private long nb;
	private long sum;
	private long min;
	private long max;
	private final String name;

	public NumberAnalyzed(String name) {
		this.name = name;
	}

	public NumberAnalyzed() {
		this("");
	}

	public void save(Preferences prefs) {
		if (name.length() == 0) {
			throw new UnsupportedOperationException();
		}
		prefs.putLong(name + ".nb", nb);
		prefs.putLong(name + ".sum", sum);
		prefs.putLong(name + ".min", min);
		prefs.putLong(name + ".max", max);
	}

	public static NumberAnalyzed load(String name, Preferences prefs) {
		final long nb = prefs.getLong(name + ".nb", 0);
		if (nb == 0) {
			return null;
		}
		final long sum = prefs.getLong(name + ".sum", 0);
		if (sum == 0) {
			return null;
		}
		final long min = prefs.getLong(name + ".min", 0);
		if (min == 0) {
			return null;
		}
		final long max = prefs.getLong(name + ".max", 0);
		if (max == 0) {
			return null;
		}
		return new NumberAnalyzed(name, nb, sum, min, max);
	}

	@Override
	public synchronized String toString() {
		return "sum=" + sum + " nb=" + nb + " min=" + min + " max=" + max + " mean=" + getMean();
	}

	private NumberAnalyzed(String name, long nb, long sum, long min, long max) {
		this(name);
		this.nb = nb;
		this.sum = sum;
		this.min = min;
		this.max = max;
	}

	public synchronized INumberAnalyzed getCopyImmutable() {
		final NumberAnalyzed copy = new NumberAnalyzed(name, nb, sum, min, max);
		return copy;
	}

	public synchronized void addValue(long v) {
		nb++;
		if (nb == 1) {
			sum = v;
			min = v;
			max = v;
			return;
		}
		sum += v;
		if (v > max) {
			max = v;
		}
		if (v < min) {
			min = v;
		}
	}

	public synchronized void add(NumberAnalyzed other) {
		synchronized (other) {
			this.sum += other.sum;
			this.nb += other.nb;
			this.min = Math.min(this.min, other.min);
			this.max = Math.max(this.max, other.max);
		}
	}

	synchronized public final long getNb() {
		return nb;
	}

	synchronized public final long getSum() {
		return sum;
	}

	synchronized public final long getMin() {
		return min;
	}

	synchronized public final long getMax() {
		return max;
	}

	synchronized public final long getMean() {
		if (nb == 0) {
			return 0;
		}
		return sum / nb;
	}

	public String getName() {
		return name;
	}

}
