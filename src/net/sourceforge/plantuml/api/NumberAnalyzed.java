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

public class NumberAnalyzed implements INumberAnalyzed {

	private int nb;
	private int sum;
	private int min;
	private int max;

	public NumberAnalyzed() {

	}

	private NumberAnalyzed(int nb, int sum, int min, int max) {
		this.nb = nb;
		this.sum = sum;
		this.min = min;
		this.max = max;
	}

	public synchronized INumberAnalyzed getCopyImmutable() {
		final NumberAnalyzed copy = new NumberAnalyzed(nb, sum, min, max);
		return copy;
	}

	public synchronized void addValue(int v) {
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

	synchronized public final int getNb() {
		return nb;
	}

	synchronized public final int getSum() {
		return sum;
	}

	synchronized public final int getMin() {
		return min;
	}

	synchronized public final int getMax() {
		return max;
	}

	synchronized public final int getMean() {
		if (nb == 0) {
			return 0;
		}
		return sum / nb;
	}

}
