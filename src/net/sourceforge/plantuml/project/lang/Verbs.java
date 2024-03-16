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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexLeaf;

public class Verbs {

	public static IRegex are = new RegexLeaf("are");
	public static IRegex areColored = new RegexLeaf("are[%s]+colou?red");
	public static IRegex displayOnSameRowAs = new RegexLeaf("displays?[%s]+on[%s]+same[%s]+row[%s]+as");
	public static IRegex ends = new RegexLeaf("ends");
	public static IRegex happens = new RegexLeaf("happens");
	public static IRegex is = new RegexLeaf("is");
	public static IRegex isColored = new RegexLeaf("is[%s]+colou?red");
	public static IRegex isDeleted = new RegexLeaf("is[%s]+deleted");
	public static IRegex isDisplayedAs = new RegexLeaf("is[%s]+displayed[%s]+as");
	public static IRegex isOff = new RegexLeaf("is[%s]+off");
	public static IRegex isOn = new RegexLeaf("is[%s]+on");
	public static IRegex isOrAre = new RegexLeaf("(is|are)");
	public static IRegex isOrAreNamed = new RegexLeaf("(is|are)[%s]+named");
	public static IRegex just = new RegexLeaf("just");
	public static IRegex linksTo = new RegexLeaf("links[%s]+to");
	public static IRegex occurs = new RegexLeaf("occurs");
	public static IRegex pauses = new RegexLeaf("pauses");
	public static IRegex requires = new RegexLeaf("(lasts|requires)");
	public static IRegex starts = new RegexLeaf("starts");
	public static IRegex worksOn = new RegexLeaf("works[%s]+on");

}
