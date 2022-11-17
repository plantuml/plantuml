
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
package net.sourceforge.plantuml.genericdiagram.cucaprocessing.impl;

import net.sourceforge.plantuml.genericdiagram.data.GenericDiagram;
import net.sourceforge.plantuml.genericdiagram.data.GenericEdge;
import net.sourceforge.plantuml.genericdiagram.data.GenericGroup;
import net.sourceforge.plantuml.genericdiagram.data.GenericLeaf;
import net.sourceforge.plantuml.genericdiagram.data.GenericLink;
import net.sourceforge.plantuml.genericdiagram.data.GenericMember;
import net.sourceforge.plantuml.genericdiagram.data.GenericStereotype;

import java.util.function.Supplier;

/**
 * The purpose of this factory is to create model elements with a unique id
 * edges and nodes/elements have different counts both starting at 1
 * following the conventions on graphs
 */
public class GenericModelElementFactory  {

	private int elementCount = 0;
	private int edgeCount = 0;


	public Supplier<GenericDiagram> genericDiagramSupplier = new Supplier<GenericDiagram>() {
		@Override
		public GenericDiagram get() {
			elementCount++;
			return new GenericDiagram(elementCount);
		}
	};

	public Supplier<GenericEdge> genericEdgeSupplier = new Supplier<GenericEdge>() {
		@Override
		public GenericEdge get() {
			edgeCount ++;
			return new GenericEdge(edgeCount);
		}
	};

	public Supplier<GenericGroup> genericGroupSupplier = new Supplier<GenericGroup>() {
		@Override
		public GenericGroup get() {
			elementCount ++;
			return new GenericGroup(elementCount);
		}
	};
  public Supplier<GenericLeaf> genericLeafSupplier = new Supplier<GenericLeaf>() {
		@Override
		public GenericLeaf get() {
			elementCount++;
			return new GenericLeaf(elementCount);
		}
	};

	public Supplier<GenericMember> genericMemberSupplier = new Supplier<GenericMember>() {
		@Override
		public GenericMember get() {
			elementCount++;
			return new GenericMember(elementCount);
		}
	};

	public Supplier<GenericStereotype> genericStereotypeSupplier = new Supplier<GenericStereotype>() {
		@Override
		public GenericStereotype get() {
			elementCount++;
			return new GenericStereotype(elementCount);
		}
	};

	public Supplier<GenericLink> genericLinkSupplier = new Supplier<GenericLink>() {
		@Override
		public GenericLink get() {
			elementCount++;
			return new GenericLink(elementCount);
		}
	};
}
