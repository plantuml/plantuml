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
package net.sourceforge.plantuml.style.parser;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Lexique implements Shareable {

	private boolean mutable;
	private Map<String, BigInteger> values;
	private BigInteger nextBitmask;

	private Lexique(boolean mutable, Map<String, BigInteger> values, BigInteger nextBitmask) {
		this.mutable = mutable;
		this.values = values;
		this.nextBitmask = nextBitmask;
	}

	public Lexique() {
		this(true, new HashMap<>(), BigInteger.ONE);
	}

	public boolean contains(String value) {
		return values.containsKey(value);
	}

	public Lexique add(String value) {
		if (values.containsKey(value))
			return this;

		if (mutable) {
			values.put(value, nextBitmask);
			nextBitmask = nextBitmask.shiftLeft(1);
			return this;
		}

		final Map<String, BigInteger> newValues = new HashMap<>(values);
		newValues.put(value, nextBitmask);
		return new Lexique(true, newValues, nextBitmask.shiftLeft(1));

	}

	@Override
	public boolean isMutable() {
		return mutable;
	}

	@Override
	public void froze() {
		this.mutable = false;
	}

	public BigInteger getBitmask(String value) {
		return values.get(value);
	}

	@Override
	public String toString() {
		return "mutable=" + mutable + " " + values;
	}
}
