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
package net.sourceforge.plantuml.gantt.lang;

import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexPart;

public class Verbs {

	private final UBrexPart ubrex;

	public UBrexPart toUnicodeBracketedExpression() {
		return ubrex;
	}

	public Verbs(UBrexLeaf ubrex) {
		this.ubrex = ubrex;
	}

	public static Verbs are = new Verbs(new UBrexLeaf("are"));
	public static Verbs areColored = new Verbs(new UBrexLeaf("are 〇+〴s colo〇?ured"));
	public static Verbs displayOnSameRowAs = new Verbs(new UBrexLeaf("display〇?s 〇+〴s on 〇+〴s same 〇+〴s row 〇+〴s as"));
	public static Verbs ends = new Verbs(new UBrexLeaf("ends"));
	public static Verbs happens = new Verbs(new UBrexLeaf("happens"));
	public static Verbs is = new Verbs(new UBrexLeaf("is"));
	public static Verbs isColored = new Verbs(new UBrexLeaf("is 〇+〴s colo〇?ured"));
	public static Verbs isDeleted = new Verbs(new UBrexLeaf("is 〇+〴s deleted"));
	public static Verbs isDisplayedAs = new Verbs(new UBrexLeaf("is 〇+〴s displayed 〇+〴s as"));
	public static Verbs isOff = new Verbs(new UBrexLeaf("is 〇+〴s off"));
	public static Verbs isOn = new Verbs(new UBrexLeaf("is 〇+〴s on"));
	public static Verbs isOrAre = new Verbs(new UBrexLeaf("【is┇are】"));
	public static Verbs isOrAreNamed = new Verbs(new UBrexLeaf("【is┇are】〇+〴s named"));
	public static Verbs just = new Verbs(new UBrexLeaf("just"));
	public static Verbs linksTo = new Verbs(new UBrexLeaf("links 〇+〴s to"));
	public static Verbs occurs = new Verbs(new UBrexLeaf("occurs"));
	public static Verbs pauses = new Verbs(new UBrexLeaf("pauses"));
	public static Verbs requires = new Verbs(new UBrexLeaf("【lasts┇requires】"));
	public static Verbs starts = new Verbs(new UBrexLeaf("starts"));
	public static Verbs worksOn = new Verbs(new UBrexLeaf("works 〇+〴s on"));

}
