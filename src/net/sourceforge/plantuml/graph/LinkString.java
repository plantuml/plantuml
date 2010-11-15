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
 * Revision $Revision: 3833 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkString {

	final private static Pattern p = Pattern.compile("(.*)->(.*)");

	final private String node1;
	final private String node2;

	public LinkString(String desc) {
		final Matcher m = p.matcher(desc);
		if (m.find() == false) {
			throw new IllegalArgumentException();
		}
		node1 = m.group(1);
		node2 = m.group(2);

	}

	public final String getNode1() {
		return node1;
	}

	public final String getNode2() {
		return node2;
	}
}
