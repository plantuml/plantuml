/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License aint with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */
package net.sourceforge.plantuml.api;

import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicLong;
import java.util.prefs.Preferences;

import net.sourceforge.plantuml.Log;

public class NumberAnalyzed2 implements INumberAnalyzed {

	private static final int SLIDING_WINDOW = 512;

	private final AtomicLong nb = new AtomicLong();
	private final AtomicLong sum = new AtomicLong();
	private final AtomicLong min = new AtomicLong();
	private final AtomicLong max = new AtomicLong();
	private final AtomicLong sumOfSquare = new AtomicLong();
	// See https://fossies.org/linux/haproxy/include/proto/freq_ctr.h
	private final AtomicLong sliddingSum = new AtomicLong();
	private final String name;

	public NumberAnalyzed2(String name) {
		this.name = name;
	}

	public void reset() {
		this.nb.set(0);
		this.sum.set(0);
		this.min.set(0);
		this.max.set(0);
		this.sumOfSquare.set(0);
		this.sliddingSum.set(0);
	}

	public NumberAnalyzed2() {
		this("");
	}

	public final void save(Preferences prefs) {
		if (name.length() == 0) {
			throw new UnsupportedOperationException();
		}
		prefs.put(name + ".saved", getSavedString());
	}

	protected String getSavedString() {
		final String value = longToString(nb) + ";" + longToString(sum) + ";" + longToString(min) + ";"
				+ longToString(max) + ";" + longToString(sumOfSquare) + ";" + longToString(sliddingSum);
		return value;
	}

	protected final String longToString(AtomicLong val) {
		return Long.toString(val.get(), 36);
	}

	public static NumberAnalyzed2 load(String name, Preferences prefs) {
		final String value = prefs.get(name + ".saved", "");
		if (value.length() == 0) {
			System.err.println("Cannot load " + name);
			return null;
		}
		try {
			final StringTokenizer st = new StringTokenizer(value, ";");
			return new NumberAnalyzed2(name, Long.parseLong(st.nextToken(), 36), Long.parseLong(st.nextToken(), 36),
					Long.parseLong(st.nextToken(), 36), Long.parseLong(st.nextToken(), 36), Long.parseLong(
							st.nextToken(), 36), Long.parseLong(st.nextToken(), 36));
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Error reading " + value);
			return null;
		}
	}

	@Override
	public String toString() {
		return "sum=" + sum + " nb=" + nb + " min=" + min + " max=" + max + " mean=" + getMean();
	}

	protected NumberAnalyzed2(String name, long nb, long sum, long min, long max, long sumOfSquare, long sliddingSum) {
		this(name);
		this.nb.set(nb);
		this.sum.set(sum);
		this.min.set(min);
		this.max.set(max);
		this.sumOfSquare.set(sumOfSquare);
		this.sliddingSum.set(sliddingSum);
	}

	public INumberAnalyzed getCopyImmutable() {
		final NumberAnalyzed2 copy = new NumberAnalyzed2(name, nb.get(), sum.get(), min.get(), max.get(),
				sumOfSquare.get(), sliddingSum.get());
		return copy;
	}

	public void addValue(long v) {
		nb.incrementAndGet();
		if (nb.get() == 1) {
			min.set(v);
			max.set(v);
		} else if (v > max.get()) {
			max.set(v);
		} else if (v < min.get()) {
			min.set(v);
		}
		sum.addAndGet(v);
		sumOfSquare.addAndGet(v * v);
		sliddingSum.set(sliddingSum.get() * (SLIDING_WINDOW - 1) / SLIDING_WINDOW + v);
	}

	public void add(NumberAnalyzed2 other) {
		this.sum.addAndGet(other.sum.get());
		this.nb.addAndGet(other.nb.get());
		this.min.set(Math.min(this.min.get(), other.min.get()));
		this.max.set(Math.max(this.max.get(), other.max.get()));
	}

	public final long getNb() {
		return nb.get();
	}

	public final long getSum() {
		return sum.get();
	}

	public final long getMin() {
		return min.get();
	}

	public final long getMax() {
		return max.get();
	}

	public final long getMean() {
		if (nb.get() == 0) {
			return 0;
		}
		// Bad
		return sum.get() / nb.get();
	}

	public final long getSliddingMean() {
		if (nb.get() == 0) {
			return 0;
		}
		if (nb.get() < SLIDING_WINDOW) {
			return sum.get() / nb.get();
		}
		// Bad
		return sliddingSum.get() / nb.get();
	}

	final public String getName() {
		return name;
	}

}
