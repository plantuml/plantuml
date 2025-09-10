/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.cli;

import java.util.AbstractMap;
import java.util.Map;

import net.sourceforge.plantuml.utils.Peeker;

public class CliFlagValued {

	private final CliFlag flag;
	private final Object value;

	private CliFlagValued(CliFlag flag, Object value) {
		this.flag = flag;
		this.value = value;
	}

	public CliFlag getFlag() {
		return flag;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return flag + ":=" + value;
	}

	public static CliFlagValued parse(Peeker<String> peeker) {
		String tmp = peeker.peek(0);
		for (CliFlag flag : CliFlag.values()) {

			if (flag.match(tmp) == false)
				continue;

			final Arity type = flag.getType();
			if (type == Arity.BOOLEAN)
				return new CliFlagValued(flag, Boolean.TRUE);

			if (type == Arity.SINGLE_VALUE) {
				peeker.jump();
				final String s = peeker.peek(0);
				return new CliFlagValued(flag, s);
			}

			if (type == Arity.KEY_VALUE) {
				final String s = peeker.peek(0).substring(2);
				final int eqIndex = s.indexOf('=');
				String k;
				String v;
				if (eqIndex == -1) {
					k = s;
					v = null;
				} else {
					k = s.substring(0, eqIndex);
					v = s.substring(eqIndex + 1);
				}
				return new CliFlagValued(flag, new AbstractMap.SimpleImmutableEntry<>(k, v));
			}

			if (type == Arity.KEY_OPTIONAL_COLON_VALUE) {
				final int colon = tmp.indexOf(':');
				final String value;
				if (colon < 0) {
					value = null;
				} else {
					final String tail = tmp.substring(colon + 1);
					value = tail.isEmpty() ? null : tail;
				}
				return new CliFlagValued(flag, value);
			}

		}
		return null;
	}

}
