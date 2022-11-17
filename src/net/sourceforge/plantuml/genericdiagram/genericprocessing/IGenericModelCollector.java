
/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
 * Original Author:  Thomas Woyke, Robert Bosch GmbH
 *
 */
package net.sourceforge.plantuml.genericdiagram.genericprocessing;

import net.sourceforge.plantuml.genericdiagram.IGenericDiagram;
import net.sourceforge.plantuml.genericdiagram.IGenericEdge;
import net.sourceforge.plantuml.genericdiagram.IGenericGroup;
import net.sourceforge.plantuml.genericdiagram.IGenericLeaf;
import net.sourceforge.plantuml.genericdiagram.IGenericLink;
import net.sourceforge.plantuml.genericdiagram.IGenericMember;
import net.sourceforge.plantuml.genericdiagram.IGenericStereotype;

/**
 * This interface allows for various export formats of the generic model
 * The converter uses this interface to provide the elements to an implementer
 * that may choose to use a special representation of the Generic Model Elements
 */
public interface IGenericModelCollector {
	void addDiagram(IGenericDiagram diagram);

	void addLeaf(IGenericLeaf leaf);

	void addGroup(IGenericGroup group);

	void addStereotype(IGenericStereotype stereotype);

	void addMember(IGenericMember member);

	void addLink(IGenericLink link);

	void addEdge(IGenericEdge edge);

	void reset();
}
