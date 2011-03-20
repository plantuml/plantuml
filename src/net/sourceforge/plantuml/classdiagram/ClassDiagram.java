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
 * Revision $Revision: 6167 $
 *
 */
package net.sourceforge.plantuml.classdiagram;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;

public class ClassDiagram extends AbstractClassOrObjectDiagram {

	@Override
	public IEntity getOrCreateEntity(String code, EntityType defaultType) {
		assert defaultType == EntityType.ABSTRACT_CLASS || defaultType == EntityType.CLASS
				|| defaultType == EntityType.INTERFACE || defaultType == EntityType.ENUM
				|| defaultType == EntityType.LOLLIPOP || defaultType == EntityType.POINT_FOR_ASSOCIATION;
		code = getFullyQualifiedCode(code);
		if (super.entityExist(code)) {
			return super.getOrCreateEntity(code, defaultType);
		}
		return createEntityWithNamespace(code, getShortName(code), defaultType);
	}

	@Override
	public Entity createEntity(String code, String display, EntityType type) {
		if (type != EntityType.ABSTRACT_CLASS && type != EntityType.CLASS && type != EntityType.INTERFACE
				&& type != EntityType.ENUM && type != EntityType.LOLLIPOP) {
			return super.createEntity(code, display, type);
		}
		code = getFullyQualifiedCode(code);
		if (super.entityExist(code)) {
			throw new IllegalArgumentException("Already known: " + code);
		}
		return createEntityWithNamespace(code, display, type);
	}

	private Entity createEntityWithNamespace(String fullyCode, String display, EntityType type) {
		Group group = getCurrentGroup();
		final String namespace = getNamespace(fullyCode);
		if (namespace != null && (group == null || group.getCode().equals(namespace) == false)) {
			group = getOrCreateGroupInternal(namespace, namespace, namespace, GroupType.PACKAGE, null);
			group.setBold(true);
		}
		return createEntityInternal(fullyCode, display == null ? getShortName(fullyCode) : display, type, group);
	}

	@Override
	public final boolean entityExist(String code) {
		return super.entityExist(getFullyQualifiedCode(code));
	}

	@Override
	public IEntity getOrCreateClass(String code) {
		return getOrCreateEntity(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code), EntityType.CLASS);
	}

	final public IEntity getOrCreateClass(String code, EntityType type) {
		if (type != EntityType.ABSTRACT_CLASS && type != EntityType.CLASS && type != EntityType.INTERFACE
				&& type != EntityType.ENUM && type != EntityType.LOLLIPOP) {
			throw new IllegalArgumentException();
		}
		return getOrCreateEntity(code, type);
	}

	private String getFullyQualifiedCode(String code) {
		if (code.startsWith("\\") || code.startsWith("~") || code.startsWith(".")) {
			return code.substring(1);
		}
		if (code.contains(".")) {
			return code;
		}
		final Group g = this.getCurrentGroup();
		if (g == null) {
			return code;
		}
		final String namespace = g.getNamespace();
		if (namespace == null) {
			return code;
		}
		return namespace + "." + code;
	}

	private String getShortName(String code) {
		// final int x = code.lastIndexOf('.');
		// if (x == -1) {
		// return code;
		// }
		// return code.substring(x + 1);
		final String namespace = getNamespace(code);
		if (namespace == null) {
			return code;
		}
		return code.substring(namespace.length() + 1);
	}

	private String getNamespace(String code) {
		assert code.startsWith("\\") == false;
		assert code.startsWith("~") == false;
		do {
			final int x = code.lastIndexOf('.');
			if (x == -1) {
				return null;
			}
			code = code.substring(0, x);
		} while (entityExist(code));
		return code;
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.CLASS;
	}


}
