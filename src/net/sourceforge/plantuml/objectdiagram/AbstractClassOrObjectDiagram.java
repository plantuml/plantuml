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
 * Revision $Revision: 4159 $
 *
 */
package net.sourceforge.plantuml.objectdiagram;

import java.util.List;

import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;

public abstract class AbstractClassOrObjectDiagram extends AbstractEntityDiagram {

	final public boolean insertBetween(IEntity entity1, IEntity entity2, IEntity node) {
		final Link link = foundLink(entity1, entity2);
		if (link == null) {
			return false;
		}
		final Link l1 = new Link(entity1, node, link.getType(), link.getLabel(), link.getLength(),
				link.getQualifier1(), null, link.getLabeldistance(), link.getLabelangle());
		final Link l2 = new Link(node, entity2, link.getType(), link.getLabel(), link.getLength(), null, link
				.getQualifier2(), link.getLabeldistance(), link.getLabelangle());
		addLink(l1);
		addLink(l2);
		removeLink(link);
		return true;
	}

	private Link foundLink(IEntity entity1, IEntity entity2) {
		Link result = null;
		final List<Link> links = getLinks();
		for (int i = links.size() - 1; i >= 0; i--) {
			final Link l = links.get(i);
			if (l.isBetween(entity1, entity2)) {
				if (result != null) {
					return null;
				}
				result = l;
			}
		}
		return result;
	}

	public int getNbOfHozizontalLollipop(IEntity entity) {
		if (entity.getType() == EntityType.LOLLIPOP) {
			throw new IllegalArgumentException();
		}
		int result = 0;
		for (Link link : getLinks()) {
			if (link.getLength() == 1 && link.contains(entity) && link.containsType(EntityType.LOLLIPOP)) {
				result++;
			}

		}
		return result;
	}
}
