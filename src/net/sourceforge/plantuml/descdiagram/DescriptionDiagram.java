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
 * Revision $Revision: 8532 $
 *
 */
package net.sourceforge.plantuml.descdiagram;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.StringUtils;

public class DescriptionDiagram extends AbstractEntityDiagram {

	private String namespaceSeparator = null;

	@Override
	public ILeaf getOrCreateLeaf(Code code, LeafType type, USymbol symbol) {
		if (namespaceSeparator != null) {
			code = code.withSeparator(namespaceSeparator);
		}
		if (namespaceSeparator != null && code.getFullName().contains(namespaceSeparator)) {
			// System.err.println("code=" + code);
			final Code fullyCode = code;
			// final String namespace = fullyCode.getNamespace(getLeafs());
			// System.err.println("namespace=" + namespace);
		}
		if (type == null) {
			String code2 = code.getFullName();
			if (code2.startsWith("[") && code2.endsWith("]")) {
				final USymbol sym = getSkinParam().useUml2ForComponent() ? USymbol.COMPONENT2 : USymbol.COMPONENT1;
				return getOrCreateLeafDefault(code.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:"),
						LeafType.DESCRIPTION, sym);
			}
			if (code2.startsWith(":") && code2.endsWith(":")) {
				return getOrCreateLeafDefault(code.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:"),
						LeafType.DESCRIPTION, USymbol.ACTOR);
			}
			if (code2.startsWith("()")) {
				code2 = StringUtils.trin(code2.substring(2));
				code2 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code2);
				return getOrCreateLeafDefault(Code.of(code2), LeafType.DESCRIPTION, USymbol.INTERFACE);
			}
			code = code.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
			return getOrCreateLeafDefault(code, LeafType.STILL_UNKNOWN, symbol);
		}
		return getOrCreateLeafDefault(code, type, symbol);
	}

	// @Override
	// public ILeaf createLeaf(Code code, List<? extends CharSequence> display, LeafType type) {
	// if (type != LeafType.COMPONENT) {
	// return super.createLeaf(code, display, type);
	// }
	// code = code.getFullyQualifiedCode(getCurrentGroup());
	// if (super.leafExist(code)) {
	// throw new IllegalArgumentException("Already known: " + code);
	// }
	// return createEntityWithNamespace(code, display, type);
	// }

	// private ILeaf createEntityWithNamespace(Code fullyCode, List<? extends CharSequence> display, LeafType type) {
	// IGroup group = getCurrentGroup();
	// final String namespace = fullyCode.getNamespace(getLeafs());
	// if (namespace != null && (EntityUtils.groupRoot(group) || group.getCode().equals(namespace) == false)) {
	// group = getOrCreateGroupInternal(Code.of(namespace), StringUtils.getWithNewlines(namespace), namespace,
	// GroupType.PACKAGE, getRootGroup());
	// }
	// return createLeafInternal(fullyCode,
	// display == null ? StringUtils.getWithNewlines(fullyCode.getShortName(getLeafs())) : display, type,
	// group);
	// }

	private boolean isUsecase() {
		for (ILeaf leaf : getLeafsvalues()) {
			final LeafType type = leaf.getEntityType();
			final USymbol usymbol = leaf.getUSymbol();
			if (type == LeafType.USECASE || usymbol == USymbol.ACTOR) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void makeDiagramReady() {
		super.makeDiagramReady();
		final LeafType defaultType = isUsecase() ? LeafType.DESCRIPTION : LeafType.DESCRIPTION;
		final USymbol defaultSymbol = isUsecase() ? USymbol.ACTOR : USymbol.INTERFACE;
		for (ILeaf leaf : getLeafsvalues()) {
			if (leaf.getEntityType() == LeafType.STILL_UNKNOWN) {
				leaf.muteToType(defaultType, defaultSymbol);
			}
		}
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.DESCRIPTION;
	}

	public void setNamespaceSeparator(String namespaceSeparator) {
		this.namespaceSeparator = namespaceSeparator;
	}
	
	public String getNamespaceSeparator() {
		return namespaceSeparator;
	}


}
