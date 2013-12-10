/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 3851 $
 *
 */
package net.sourceforge.plantuml.compositediagram;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;

public class CompositeDiagram extends AbstractEntityDiagram {

	public IEntity getOrCreateLeaf(Code code, LeafType type) {
		if (type == null) {
			if (isGroup(code)) {
				return getGroup(code);
			}
			return getOrCreateLeafDefault(code, LeafType.BLOCK);
		}
		return getOrCreateLeafDefault(code, type);
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.COMPOSITE;
	}

}
