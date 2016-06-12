/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 */
package net.sourceforge.plantuml.version;

import java.util.List;

import net.sourceforge.plantuml.CharSequence2;

public class IteratorCounter2Impl implements IteratorCounter2 {

	private final List<CharSequence2> data;
	private int nb;

	public IteratorCounter2Impl(List<CharSequence2> data) {
		this(data, 0);
	}

	private IteratorCounter2Impl(List<CharSequence2> data, int nb) {
		this.data = data;
		this.nb = nb;
	}

	public int currentNum() {
		return nb;
	}

	public boolean hasNext() {
		return nb < data.size();
	}

	public CharSequence2 next() {
		return data.get(nb++);
	}

	public CharSequence2 peek() {
		return data.get(nb);
	}

	public CharSequence2 peekPrevious() {
		if (nb == 0) {
			return null;
		}
		return data.get(nb - 1);
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public IteratorCounter2 cloneMe() {
		return new IteratorCounter2Impl(data, nb);
	}

}
