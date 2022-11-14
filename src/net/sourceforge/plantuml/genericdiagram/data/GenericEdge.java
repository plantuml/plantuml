
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
package net.sourceforge.plantuml.genericdiagram.data;


import net.sourceforge.plantuml.genericdiagram.GenericEdgeType;
import net.sourceforge.plantuml.genericdiagram.GenericEntityType;
import net.sourceforge.plantuml.genericdiagram.IGenericEdge;
import net.sourceforge.plantuml.genericdiagram.genericprocessing.IGenericDiagramVisitor;
import static java.lang.String.format;

public class GenericEdge extends GenericModelElement implements IGenericEdge {

	int source;
	int target;
	int diagram;
	String pumlIdSource;
	String pumlIdTarget;
	GenericEdgeType edgeType;


	public GenericEdge() {
		setType(GenericEntityType.EDGE);
	}

	public GenericEdge(int elementCount) {
		super(elementCount);
		setType(GenericEntityType.EDGE);

	}

	@Override
	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	@Override
	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	@Override
	public GenericEdgeType getEdgeType() {
		return edgeType;
	}

	public void setEdgeType(GenericEdgeType edgeType) {
		this.edgeType = edgeType;
	}

	@Override
	public int getDiagram() {
		return diagram;
	}

	public void setDiagram(Integer diagram) {
		this.diagram = diagram;
	}


	@Override
	public boolean isEdge() {
		return true;
	}


	@Override
	public boolean isNode() {
		return false;
	}

	public String toString() {
		return format("(%s)-%s-(%s)", source, getLabel(), target);
	}

	public String getPumlIdSource() {
		return pumlIdSource;
	}


	public void setPumlIdSource(String pumlIdSource) {
		this.pumlIdSource = pumlIdSource;
	}


	public String getPumlIdTarget() {
		return pumlIdTarget;
	}


	public void setPumlIdTarget(String pumlIdTarget) {
		this.pumlIdTarget = pumlIdTarget;
	}


	@Override
	public void acceptVisitor(IGenericDiagramVisitor visitor) {
		visitor.visitEdge(this);
	}
}
