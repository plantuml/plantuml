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
package net.sourceforge.plantuml.graphic;

import net.sourceforge.plantuml.BackSlash;

public class Text implements HtmlCommand {

	private final String text;

	public static final Text TEXT_BS_BS_N = new Text(BackSlash.BS_BS_N);

	Text(String text) {
		this.text = text.replaceAll("\\\\\\[", "[").replaceAll("\\\\\\]", "]");
		if (text.indexOf(BackSlash.CHAR_NEWLINE) != -1) {
			throw new IllegalArgumentException();
		}
		if (text.length() == 0) {
			throw new IllegalArgumentException();
		}
	}

	public String getText() {
		assert text.length() > 0;
		return text;
	}

	public boolean isNewline() {
		return text.equals(BackSlash.BS_BS_N);
	}
}
