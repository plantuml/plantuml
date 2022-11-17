
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

import static java.lang.String.format;
import net.sourceforge.plantuml.genericdiagram.GenericEntityType;
import net.sourceforge.plantuml.genericdiagram.IGenericModelElement;
import net.sourceforge.plantuml.genericdiagram.genericprocessing.IGenericDiagramVisitor;

public class GenericModelElement implements IGenericModelElement {

	int id;
	String pumlId;
	String pumlRootPath;
	String label;
	GenericEntityType type;

	public GenericModelElement(int id) {
		this.id = id;
	}

	public GenericModelElement() {

	}

	@Override
	public String toString() {
		return format("(%d)(%s)(%s)", id, pumlId, label);
	}

	public String getPumlId() {
		return pumlId;
	}

	public void setPumlId(String pumlId) {
		this.pumlId = pumlId;
	}

	@Override
	public String getFullyQualifiedPumlId() {
		return this.pumlRootPath + this.pumlId;
	}

	public String getPumlRootPath() {
		return pumlRootPath;
	}

	public void setPumlRootPath(String pumlRootPath) {
		this.pumlRootPath = pumlRootPath;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isNode() {
		return true;
	}

	@Override

	public boolean isDiagram() {
		return false;
	}

	@Override

	public boolean isEdge() {
		return false;
	}

	@Override

	public boolean isGroup() {
		return false;
	}

	@Override

	public boolean isLeaf() {
		return false;
	}

	@Override
	public boolean isLink() {
		return false;
	}

	@Override

	public boolean isMember() {
		return false;
	}

	@Override

	public boolean isStereotype() {
		return false;
	}

	@Override
	public void acceptVisitor(IGenericDiagramVisitor visitor) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GenericEntityType getType() {
		return type;
	}

	public void setType(GenericEntityType type) {
		this.type = type;
	}

	public String getImplementationType() {
		String[] subs = this.getClass().toString().split("\\.");
		String implType = subs[subs.length - 1].replace("Generic", "");
		return implType;
	}
}
