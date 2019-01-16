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
package net.sourceforge.plantuml.cucadiagram;


public abstract class EntityUtils {

	public static boolean groupRoot(IGroup g) {
		if (g == null) {
			throw new IllegalStateException();
		}
		return g instanceof GroupRoot;
	}

	private static boolean isParent(IGroup groupToBeTested, IGroup parentGroup) {
		if (groupToBeTested.isGroup() == false) {
			// Very strange!
			return false;
		}
		if (groupToBeTested.isGroup() == false) {
			throw new IllegalArgumentException();
		}
		while (EntityUtils.groupRoot(groupToBeTested) == false) {
			if (groupToBeTested == parentGroup) {
				return true;
			}
			groupToBeTested = groupToBeTested.getParentContainer();
			if (groupToBeTested.isGroup() == false) {
				throw new IllegalStateException();
			}
		}
		return false;
	}

	public static boolean isPureInnerLink12(IGroup group, Link link) {
		if (group.isGroup() == false) {
			throw new IllegalArgumentException();
		}
		final IEntity e1 = link.getEntity1();
		final IEntity e2 = link.getEntity2();
		final IGroup group1 = e1.getParentContainer();
		final IGroup group2 = e2.getParentContainer();
		if (isParent(group1, group) && isParent(group2, group)) {
			return true;
		}
		return false;
	}

	public static boolean isPureInnerLink3(IGroup group, Link link) {
		if (group.isGroup() == false) {
			throw new IllegalArgumentException();
		}
		final IEntity e1 = link.getEntity1();
		final IEntity e2 = link.getEntity2();
		final IGroup group1 = e1.getParentContainer();
		final IGroup group2 = e2.getParentContainer();
		if (isParent(group2, group) == isParent(group1, group)) {
			return true;
		}
		return false;
	}
}
