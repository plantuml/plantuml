/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.USymbol;

public class DescriptionDiagram extends AbstractEntityDiagram {

	public DescriptionDiagram(ISkinSimple skinParam) {
		super(skinParam);
	}

	@Override
	public Ident cleanIdent(Ident ident) {
		String codeString = ident.getName();
		if (codeString.startsWith("[") && codeString.endsWith("]")) {
			return ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
		}
		if (codeString.startsWith(":") && codeString.endsWith(":")) {
			return ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
		}
		if (codeString.startsWith("()")) {
			codeString = StringUtils.trin(codeString.substring(2));
			codeString = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeString);
			return ident.parent().add(Ident.empty().add(codeString, null));
		}
		return ident;
	}

	@Override
	public ILeaf getOrCreateLeaf(Ident ident, Code code, LeafType type, USymbol symbol) {
		checkNotNull(ident);
		if (type == null) {
			String codeString = code.getName();
			if (codeString.startsWith("[") && codeString.endsWith("]")) {
				final USymbol sym = getSkinParam().useUml2ForComponent() ? USymbol.COMPONENT2 : USymbol.COMPONENT1;
				final Ident idNewLong = ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
				return getOrCreateLeafDefault(idNewLong, idNewLong.toCode(this), LeafType.DESCRIPTION, sym);
			}
			if (codeString.startsWith(":") && codeString.endsWith(":")) {
				final Ident idNewLong = ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
				return getOrCreateLeafDefault(idNewLong, idNewLong.toCode(this), LeafType.DESCRIPTION, getSkinParam()
						.getActorStyle().getUSymbol());
			}
			if (codeString.startsWith("()")) {
				codeString = StringUtils.trin(codeString.substring(2));
				codeString = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeString);
				final Ident idNewLong = buildLeafIdent(codeString);
				final Code code99 = this.V1972() ? idNewLong : buildCode(codeString);
				return getOrCreateLeafDefault(idNewLong, code99, LeafType.DESCRIPTION, USymbol.INTERFACE);
			}
			final String tmp4 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code.getName(), "\"([:");
			final Ident idNewLong = ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
			code = this.V1972() ? idNewLong : buildCode(tmp4);
			return getOrCreateLeafDefault(idNewLong, code, LeafType.STILL_UNKNOWN, symbol);
		}
		return getOrCreateLeafDefault(ident, code, type, symbol);
	}

	private boolean isUsecase() {
		for (ILeaf leaf : getLeafsvalues()) {
			final LeafType type = leaf.getLeafType();
			final USymbol usymbol = leaf.getUSymbol();
			if (type == LeafType.USECASE || usymbol == getSkinParam().getActorStyle().getUSymbol()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void makeDiagramReady() {
		super.makeDiagramReady();
		final LeafType defaultType = isUsecase() ? LeafType.DESCRIPTION : LeafType.DESCRIPTION;
		final USymbol defaultSymbol = isUsecase() ? getSkinParam().getActorStyle().getUSymbol() : USymbol.INTERFACE;
		for (ILeaf leaf : getLeafsvalues()) {
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

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.DESCRIPTION;
	}

}
