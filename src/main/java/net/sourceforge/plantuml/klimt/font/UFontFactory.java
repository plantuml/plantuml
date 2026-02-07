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
package net.sourceforge.plantuml.klimt.font;

import java.awt.Font;

public class UFontFactory {

	public static UFont build(String fullDefinition, int fontStyle, int fontSize) {
		final FontStack fontStack = new FontStack(fullDefinition);
		return new UFontImpl(fontStack, fontStyle, fontSize);
	}

	public static UFont serif(int size) {
		return build("Serif", Font.PLAIN, size);
	}

	public static UFont sansSerif(int size) {
		return build("SansSerif", Font.PLAIN, size);
	}

	public static UFont courier(int size) {
		return build("Courier", Font.PLAIN, size);
	}

	public static UFont byDefault(int size) {
		return sansSerif(12);
	}

	public static UFont monospaced(int size) {
		return build("Monospaced", Font.PLAIN, size);
	}

}
