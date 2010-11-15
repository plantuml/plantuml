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
 * Revision $Revision: 5019 $
 *
 */
package net.sourceforge.plantuml.componentdiagram;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public class ComponentDiagram extends AbstractEntityDiagram {

	@Override
	public IEntity getOrCreateClass(String code) {
		if (code.startsWith("[") && code.endsWith("]")) {
			return getOrCreateEntity(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code),
					EntityType.COMPONENT);
		}
		if (code.startsWith(":") && code.endsWith(":")) {
			return getOrCreateEntity(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code), EntityType.ACTOR);
		}
		if (code.startsWith("()")) {
			code = code.substring(2).trim();
			code = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code);
			return getOrCreateEntity(code, EntityType.CIRCLE_INTERFACE);
		}
		code = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code);
		return getOrCreateEntity(code, EntityType.CIRCLE_INTERFACE);
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.COMPONENT;
	}

}
