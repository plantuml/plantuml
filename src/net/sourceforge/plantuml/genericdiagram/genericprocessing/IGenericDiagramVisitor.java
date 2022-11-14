
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

import net.sourceforge.plantuml.genericdiagram.data.GenericDiagram;
import net.sourceforge.plantuml.genericdiagram.data.GenericEdge;
import net.sourceforge.plantuml.genericdiagram.data.GenericGroup;
import net.sourceforge.plantuml.genericdiagram.data.GenericLeaf;
import net.sourceforge.plantuml.genericdiagram.data.GenericLink;
import net.sourceforge.plantuml.genericdiagram.data.GenericMember;
import net.sourceforge.plantuml.genericdiagram.data.GenericStereotype;

/**
 * This interface enables processing of the generic data model
 * a visitor implementor may
 * transform on incarnation of the model into another one
 * or process the generic model into a semantic model
 */
public interface IGenericDiagramVisitor {

	void visitDiagram(GenericDiagram diagram);
	void visitEdge(GenericEdge edge);
	void visitGroup(GenericGroup group);
	void visitLeaf(GenericLeaf leaf);
	void visitLink(GenericLink genericLink);
	void visitMember(GenericMember member);
	void visitStereotype(GenericStereotype stereotype);

}
