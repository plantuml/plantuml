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
package net.sourceforge.plantuml.cute;

import java.util.Map;

import net.sourceforge.plantuml.StringUtils;

public class CuteShapeFactory {

	private final Map<String, Group> groups;

	public CuteShapeFactory(Map<String, Group> groups) {
		this.groups = groups;

	}

	public Positionned createCuteShapePositionned(String data) {
		final VarArgs varArgs = new VarArgs(data);
		return new PositionnedImpl(createCuteShape(data), varArgs);
	}

	private CuteShape createCuteShape(String data) {
		data = StringUtils.trin(data.toLowerCase());
		final VarArgs varArgs = new VarArgs(data);
		if (data.startsWith("circle ")) {
			return new Circle(varArgs);
		}
		if (data.startsWith("cheese ")) {
			return new Cheese(varArgs);
		}
		if (data.startsWith("stick ")) {
			return new Stick(varArgs);
		}
		if (data.startsWith("rectangle ") || data.startsWith("rect ")) {
			return new Rectangle(varArgs);
		}
		if (data.startsWith("triangle ")) {
			return new Triangle(varArgs);
		}
		final String first = data.split(" ")[0];
		// System.err.println("Looking for group " + first + " in " + groups.keySet());
		final Group group = groups.get(first);
		if (group == null) {
			throw new IllegalArgumentException("Cannot find group " + first + " in " + groups.keySet());
		}
		// System.err.println("Found group " + first + " in " + groups.keySet());
		return group;
	}

}
