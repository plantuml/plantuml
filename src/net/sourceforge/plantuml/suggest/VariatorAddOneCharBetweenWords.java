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
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.suggest;

public class VariatorAddOneCharBetweenWords extends VariatorIteratorAdaptor {

	private final String data;
	private final char toAdd;
	private int i;

	public VariatorAddOneCharBetweenWords(String data, char toAdd) {
		this.data = data;
		this.toAdd = toAdd;
		i++;
		ensureBetweenWords();
	}

	private void ensureBetweenWords() {
		while (i < data.length() && inWord()) {
			i++;
		}

	}

	private boolean inWord() {
		return Character.isLetterOrDigit(data.charAt(i - 1)) && Character.isLetterOrDigit(data.charAt(i));
	}

	@Override
	Variator getVariator() {
		return new Variator() {
			public String getData() {
				if (i > data.length() - 1) {
					return null;
				}
				return data.substring(0, i) + toAdd + data.substring(i);
			}

			public void nextStep() {
				i++;
				ensureBetweenWords();
			}
		};
	}
}
