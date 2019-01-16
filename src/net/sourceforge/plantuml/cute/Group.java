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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Group implements Positionned {

	private final String groupName;
	private final List<Positionned> shapes;
	private final Group parent;
	private final Map<String, Group> children;

	// private final List<Group> children = new ArrayList<Group>();

	@Override
	public String toString() {
		return "Group " + groupName + " (" + shapes.size() + ") ";
	}

	// public static Group fromList(List<Positionned> shapes) {
	// return new Group("Automatic", shapes);
	// }

	public static Group createRoot() {
		return new Group(null, "ROOT");
	}

	private Group(Group parent, String groupName) {
		this.parent = parent;
		this.groupName = groupName;
		this.shapes = new ArrayList<Positionned>();
		this.children = new HashMap<String, Group>();
	}

	private Group(Group parent, String groupName, List<Positionned> shapes) {
		this.parent = parent;
		this.groupName = groupName;
		this.shapes = shapes;
		this.children = null;
	}

	public Group createChild(String childName) {
		final Group result = new Group(this, childName);
		this.children.put(childName, result);
		return result;
	}

	public void drawU(UGraphic ug) {
		for (Positionned shape : shapes) {
			shape.drawU(ug);
		}
	}

	public void add(Positionned shape) {
		shapes.add(shape);
	}

	public String getName() {
		return groupName;
	}

	public Positionned rotateZoom(RotationZoom rotationZoom) {
		if (rotationZoom.isNone()) {
			return this;
		}
		final List<Positionned> result = new ArrayList<Positionned>();
		for (Positionned shape : shapes) {
			result.add(shape.rotateZoom(rotationZoom));
		}
		return new Group(parent, groupName + "->" + rotationZoom, result);
	}

	public Positionned translate(UTranslate translation) {
		throw new UnsupportedOperationException();
	}

	public Group getParent() {
		return parent;
	}

	public Map<String, Group> getChildren() {
		return Collections.unmodifiableMap(children);
	}

}
