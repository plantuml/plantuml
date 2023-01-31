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
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.descdiagram;

import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.USymbols;

public class DescriptionDiagram extends AbstractEntityDiagram {

	public DescriptionDiagram(UmlSource source, Map<String, String> skinParam) {
		super(source, UmlDiagramType.DESCRIPTION, skinParam);
	}

//	@Override
//	public Ident cleanIdent(Ident ident) {
//		String codeString = ident.getName();
//		if (codeString.startsWith("[") && codeString.endsWith("]")) {
//			return ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
//		}
//		if (codeString.startsWith(":") && codeString.endsWith(":")) {
//			return ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
//		}
//		if (codeString.startsWith("()")) {
//			codeString = StringUtils.trin(codeString.substring(2));
//			codeString = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeString);
//			return ident.parent().add(Ident.empty().add(codeString, null));
//		}
//		return ident;
//	}

	@Override
	public String cleanIdForQuark(String id) {
		if (id == null)
			return null;
		if (id.startsWith("()"))
			id = StringUtils.trin(id.substring(2));
		return super.cleanIdForQuark(id);
	}

//	@Override
//	protected ILeaf getOrCreateLeaf2(Quark ident, Quark code, LeafType type, USymbol symbol) {
//		Objects.requireNonNull(ident);
//		if (type == null) {
//			String codeString = code.getName();
//			if (codeString.startsWith("[") && codeString.endsWith("]")) {
//				final USymbol sym = getSkinParam().componentStyle().toUSymbol();
//				final Quark idNewLong = ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
//				if (idNewLong.getData() != null)
//					return (ILeaf) idNewLong.getData();
//				return reallyCreateLeaf(idNewLong, Display.getWithNewlines(idNewLong.getName()), LeafType.DESCRIPTION,
//						sym);
//			}
//			if (codeString.startsWith(":") && codeString.endsWith(":")) {
//				final Quark idNewLong = ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
//				if (idNewLong.getData() != null)
//					return (ILeaf) idNewLong.getData();
//				return reallyCreateLeaf(idNewLong, Display.getWithNewlines(idNewLong.getName()), LeafType.DESCRIPTION,
//						getSkinParam().actorStyle().toUSymbol());
//			}
//			if (codeString.startsWith("()")) {
//				codeString = StringUtils.trin(codeString.substring(2));
//				codeString = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeString);
//				final Quark idNewLong = buildFromName(
//						StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeString));
//				if (idNewLong.getData() != null)
//					return (ILeaf) idNewLong.getData();
//				return reallyCreateLeaf(idNewLong, Display.getWithNewlines(codeString), LeafType.DESCRIPTION,
//						USymbols.INTERFACE);
//			}
//			final String tmp4 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code.getName(), "\"([:");
//			final Quark idNewLong = ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
//			code = buildFromFullPath(tmp4);
//			if (idNewLong.getData() != null)
//				return (ILeaf) idNewLong.getData();
//			return reallyCreateLeaf(idNewLong, Display.getWithNewlines(tmp4), LeafType.STILL_UNKNOWN, symbol);
//		}
//		if (ident.getData() != null)
//			return (ILeaf) ident.getData();
//		return reallyCreateLeaf(ident, Display.getWithNewlines(code.getName()), type, symbol);
//	}

	private boolean isUsecase() {
		for (EntityImp leaf : getLeafsvalues()) {
			final LeafType type = leaf.getLeafType();
			final USymbol usymbol = leaf.getUSymbol();
			if (type == LeafType.USECASE || usymbol == getSkinParam().actorStyle().toUSymbol()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void makeDiagramReady() {
		super.makeDiagramReady();
		final LeafType defaultType = LeafType.DESCRIPTION;
		final USymbol defaultSymbol = isUsecase() ? getSkinParam().actorStyle().toUSymbol() : USymbols.INTERFACE;
		for (EntityImp leaf : getLeafsvalues()) {
			if (leaf.getLeafType() == LeafType.STILL_UNKNOWN) {
				leaf.muteToType(defaultType, defaultSymbol);
			}
		}
	}

	@Override
	public String checkFinalError() {
		this.applySingleStrategy();
		return super.checkFinalError();
	}

}
