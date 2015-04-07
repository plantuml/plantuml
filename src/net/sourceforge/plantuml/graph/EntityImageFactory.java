/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 12235 $
 *
 */
package net.sourceforge.plantuml.graph;

import net.sourceforge.plantuml.cucadiagram.IEntity;

public class EntityImageFactory {

	public AbstractEntityImage createEntityImage(IEntity entity) {
		throw new UnsupportedOperationException();
//		if (entity.getEntityType() == LeafType.CLASS || entity.getEntityType() == LeafType.ANNOTATION
//				|| entity.getEntityType() == LeafType.ABSTRACT_CLASS || entity.getEntityType() == LeafType.INTERFACE
//				|| entity.getEntityType() == LeafType.ENUM) {
//			return new EntityImageClass(entity);
//		}
//		if (entity.getEntityType() == LeafType.ACTIVITY) {
//			return new EntityImageActivity(entity);
//		}
//		if (entity.getEntityType() == LeafType.NOTE) {
//			return new EntityImageNote(entity);
//		}
//		if (entity.getEntityType() == LeafType.POINT_FOR_ASSOCIATION) {
//			return new EntityImageActivityCircle(entity, 4, 4);
//		}
//		if (entity.getEntityType() == LeafType.CIRCLE_START) {
//			return new EntityImageActivityCircle(entity, 18, 18);
//		}
//		if (entity.getEntityType() == LeafType.CIRCLE_END) {
//			return new EntityImageActivityCircle(entity, 18, 11);
//		}
//		if (entity.getEntityType() == LeafType.BRANCH) {
//			return new EntityImageActivityBranch(entity);
//		}
//		if (entity.getEntityType() == LeafType.SYNCHRO_BAR) {
//			return new EntityImageActivityBar(entity);
//		}
//		if (entity.getEntityType() == LeafType.USECASE) {
//			return new EntityImageUsecase(entity);
//		}
//		if (entity.getEntityType() == LeafType.ACTOR) {
//			return new EntityImageActor(entity);
//		}
//		if (entity.getEntityType() == LeafType.CIRCLE_INTERFACE) {
//			return new EntityImageCircleInterface(entity);
//		}
//		if (entity.getEntityType() == LeafType.COMPONENT) {
//			return new EntityImageComponent(entity);
//		}
//		return new EntityImageDefault(entity);
	}

}
