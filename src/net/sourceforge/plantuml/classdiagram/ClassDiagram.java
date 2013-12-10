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
 * Revision $Revision: 11432 $
 *
 */
package net.sourceforge.plantuml.classdiagram;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;

public class ClassDiagram extends AbstractClassOrObjectDiagram {

	private String namespaceSeparator = ".";

	@Override
	public ILeaf getOrCreateLeaf(Code code, LeafType type) {
		if (type == null) {
			code = code.eventuallyRemoveStartingAndEndingDoubleQuote();
			if (getNamespaceSeparator() == null) {
				return getOrCreateLeafDefault(code, LeafType.CLASS);
			}
			code = code.getFullyQualifiedCode(getCurrentGroup(), getNamespaceSeparator());
			if (super.leafExist(code)) {
				return getOrCreateLeafDefault(code, LeafType.CLASS);
			}
			return createEntityWithNamespace(code,
					Display.getWithNewlines(code.getShortName(getLeafs(), getNamespaceSeparator())), LeafType.CLASS);
		}
		if (getNamespaceSeparator() == null) {
			return getOrCreateLeafDefault(code, LeafType.CLASS);
		}
		code = code.getFullyQualifiedCode(getCurrentGroup(), getNamespaceSeparator());
		if (super.leafExist(code)) {
			return getOrCreateLeafDefault(code, type);
		}
		return createEntityWithNamespace(code,
				Display.getWithNewlines(code.getShortName(getLeafs(), getNamespaceSeparator())), type);
	}

	@Override
	public ILeaf createLeaf(Code code, Display display, LeafType type) {
		if (type != LeafType.ABSTRACT_CLASS && type != LeafType.ANNOTATION && type != LeafType.CLASS && type != LeafType.INTERFACE
				&& type != LeafType.ENUM && type != LeafType.LOLLIPOP && type != LeafType.NOTE) {
			return super.createLeaf(code, display, type);
		}
		if (getNamespaceSeparator() == null) {
			return super.createLeaf(code, display, type);
		}
		code = code.getFullyQualifiedCode(getCurrentGroup(), getNamespaceSeparator());
		if (super.leafExist(code)) {
			throw new IllegalArgumentException("Already known: " + code);
		}
		return createEntityWithNamespace(code, display, type);
	}

	private ILeaf createEntityWithNamespace(Code fullyCode, Display display, LeafType type) {
		IGroup group = getCurrentGroup();
		final String namespace = fullyCode.getNamespace(getLeafs(), getNamespaceSeparator());
		if (namespace != null && (EntityUtils.groupRoot(group) || group.getCode().getCode().equals(namespace) == false)) {
			group = getOrCreateGroupInternal(Code.of(namespace), Display.getWithNewlines(namespace), namespace,
					GroupType.PACKAGE, getRootGroup());
		}
		return createLeafInternal(
				fullyCode,
				display == null ? Display.getWithNewlines(fullyCode.getShortName(getLeafs(),
				getNamespaceSeparator())) : display, type, group);
	}

	@Override
	public final boolean leafExist(Code code) {
		if (getNamespaceSeparator() == null) {
			return super.leafExist(code);
		}
		return super.leafExist(code.getFullyQualifiedCode(getCurrentGroup(), getNamespaceSeparator()));
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.CLASS;
	}

	private String getNamespaceSeparator() {
		return namespaceSeparator;
	}

	public void setNamespaceSeparator(String namespaceSeparator) {
		this.namespaceSeparator = namespaceSeparator;
	}

}
