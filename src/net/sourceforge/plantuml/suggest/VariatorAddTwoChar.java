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
 * Revision $Revision: 4975 $
 *
 */
package net.sourceforge.plantuml.suggest;

public class VariatorAddTwoChar extends VariatorIteratorAdaptor {

	private final String data;
	private final char toAdd;
	private int i;
	private int j = 1;

	public VariatorAddTwoChar(String data, char toAdd) {
		this.data = data;
		this.toAdd = toAdd;
	}

	@Override
	Variator getVariator() {
		return new Variator() {
			public String getData() {
				if (i >= data.length()) {
					return null;
				}
				return data.substring(0, i) + toAdd + data.substring(i, j) + toAdd + data.substring(j);
			}

			public void nextStep() {
				j++;
				if (j > data.length()) {
					i++;
					j = i + 1;
				}
			}
		};
	}
}
