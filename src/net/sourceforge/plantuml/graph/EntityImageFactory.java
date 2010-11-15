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
 * Revision $Revision: 3831 $
 *
 */
package net.sourceforge.plantuml.graph;

import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;

public class EntityImageFactory {

	public AbstractEntityImage createEntityImage(Entity entity) {
		if (entity.getType() == EntityType.CLASS || entity.getType() == EntityType.ABSTRACT_CLASS
				|| entity.getType() == EntityType.INTERFACE || entity.getType() == EntityType.ENUM) {
			return new EntityImageClass(entity);
		}
		if (entity.getType() == EntityType.ACTIVITY) {
			return new EntityImageActivity(entity);
		}
		if (entity.getType() == EntityType.NOTE) {
			return new EntityImageNote(entity);
		}
		if (entity.getType() == EntityType.POINT_FOR_ASSOCIATION) {
			return new EntityImageActivityCircle(entity, 4, 4);
		}
		if (entity.getType() == EntityType.CIRCLE_START) {
			return new EntityImageActivityCircle(entity, 18, 18);
		}
		if (entity.getType() == EntityType.CIRCLE_END) {
			return new EntityImageActivityCircle(entity, 18, 11);
		}
		if (entity.getType() == EntityType.BRANCH) {
			return new EntityImageActivityBranch(entity);
		}
		if (entity.getType() == EntityType.SYNCHRO_BAR) {
			return new EntityImageActivityBar(entity);
		}
		if (entity.getType() == EntityType.USECASE) {
			return new EntityImageUsecase(entity);
		}
		if (entity.getType() == EntityType.ACTOR) {
			return new EntityImageActor(entity);
		}
		if (entity.getType() == EntityType.CIRCLE_INTERFACE) {
			return new EntityImageCircleInterface(entity);
		}
		if (entity.getType() == EntityType.COMPONENT) {
			return new EntityImageComponent(entity);
		}
		return new EntityImageDefault(entity);
	}

}
