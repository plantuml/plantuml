/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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

public final class MagicArray {

	private final int data[];
	private final int size;
	private long lastUpdatedKey = -1;
	private int lastUpdatedValue;
	private long sum;
	private long maxSum;

	public MagicArray(int size) {
		this.data = new int[size];
		this.size = size;
	}

	synchronized public void incKey(long key) {
		incKey(key, 1);
	}

	synchronized public void incKey(long key, int delta) {
		if (key < lastUpdatedKey) {
			return;
		}
		if (key != lastUpdatedKey) {
			if (lastUpdatedKey != -1) {
				setValue(lastUpdatedKey, lastUpdatedValue);
				for (long i = lastUpdatedKey + 1; i < key; i++) {
					setValue(i, 0);
				}
			}
			lastUpdatedValue = 0;
			lastUpdatedKey = key;
		}
		lastUpdatedValue += delta;
	}

	private void setValue(long key, int value) {
		final int i = (int) (key % size);
		sum += value - data[i];
		if (sum > maxSum) {
			maxSum = sum;
		}
		data[i] = value;
	}

	synchronized public long getSum() {
		return sum;
	}

	synchronized public long getMaxSum() {
		return maxSum;
	}

	private long getSumSlow() {
		long tmp = 0;
		for (int d : data) {
			tmp += d;
		}
		return tmp;
	}

}
