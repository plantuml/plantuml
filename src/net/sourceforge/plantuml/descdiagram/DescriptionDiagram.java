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
 * Revision $Revision: 8532 $
 *
 */
package net.sourceforge.plantuml.descdiagram;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.USymbol;

public class DescriptionDiagram extends AbstractEntityDiagram {

	@Override
	public ILeaf getOrCreateLeaf(Code code, LeafType type) {
		if (type == null) {
			String code2 = code.getCode();
			if (code2.startsWith("[") && code2.endsWith("]")) {
				return getOrCreateLeafDefault(code.eventuallyRemoveStartingAndEndingDoubleQuote(), LeafType.COMPONENT);
			}
			if (code2.startsWith(":") && code2.endsWith(":")) {
				return getOrCreateLeafDefault(code.eventuallyRemoveStartingAndEndingDoubleQuote(), LeafType.ACTOR);
			}
			if (code2.startsWith("()")) {
				code2 = code2.substring(2).trim();
				code2 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code2);
				return getOrCreateLeafDefault(Code.of(code2), LeafType.CIRCLE_INTERFACE);
			}
			code = code.eventuallyRemoveStartingAndEndingDoubleQuote();
			return getOrCreateLeafDefault(code, LeafType.STILL_UNKNOWN);
		}
		return getOrCreateLeafDefault(code, type);
	}

//	@Override
//	public ILeaf createLeaf(Code code, List<? extends CharSequence> display, LeafType type) {
//		if (type != LeafType.COMPONENT) {
//			return super.createLeaf(code, display, type);
//		}
//		code = code.getFullyQualifiedCode(getCurrentGroup());
//		if (super.leafExist(code)) {
//			throw new IllegalArgumentException("Already known: " + code);
//		}
//		return createEntityWithNamespace(code, display, type);
//	}

//	private ILeaf createEntityWithNamespace(Code fullyCode, List<? extends CharSequence> display, LeafType type) {
//		IGroup group = getCurrentGroup();
//		final String namespace = fullyCode.getNamespace(getLeafs());
//		if (namespace != null && (EntityUtils.groupRoot(group) || group.getCode().equals(namespace) == false)) {
//			group = getOrCreateGroupInternal(Code.of(namespace), StringUtils.getWithNewlines(namespace), namespace,
//					GroupType.PACKAGE, getRootGroup());
//		}
//		return createLeafInternal(fullyCode,
//				display == null ? StringUtils.getWithNewlines(fullyCode.getShortName(getLeafs())) : display, type,
//				group);
//	}

	private boolean isUsecase() {
		for (ILeaf leaf : getLeafs().values()) {
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
		final LeafType defaultType = isUsecase() ? LeafType.ACTOR : LeafType.CIRCLE_INTERFACE;
		for (ILeaf leaf : getLeafs().values()) {
			if (leaf.getEntityType() == LeafType.STILL_UNKNOWN) {
				leaf.muteToType(defaultType);
			}
		}
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.DESCRIPTION;
	}

}
