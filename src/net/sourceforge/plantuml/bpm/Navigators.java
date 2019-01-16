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
package net.sourceforge.plantuml.bpm;

public final class Navigators {

	private Navigators() {

	}

	public static <O> Navigator<O> iterate(final Chain<O> orig, final O from, final O to) {
		if (orig.compare(from, to) <= 0) {
			return orig.navigator(from);
		}
		return reverse(orig.navigator(from));
	}

	public static <O> Navigator<O> reverse(final Navigator<O> orig) {
		return new Navigator<O>() {

			public O next() {
				return orig.previous();
			}

			public O previous() {
				return orig.next();
			}

			public O get() {
				return orig.get();
			}

			public void set(O data) {
				orig.set(data);
			}

			public void insertBefore(O data) {
				throw new UnsupportedOperationException();
			}

			public void insertAfter(O data) {
				throw new UnsupportedOperationException();
			}
		};
	}

}
