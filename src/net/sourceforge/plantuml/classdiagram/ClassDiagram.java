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
package net.sourceforge.plantuml.classdiagram;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.svek.image.EntityImageClass;

public class ClassDiagram extends AbstractClassOrObjectDiagram {

	public ClassDiagram(UmlSource source, Map<String, String> skinParam) {
		super(source, UmlDiagramType.CLASS, skinParam);
	}

//	@Override
//	protected ILeaf getOrCreateLeaf2(Quark ident, Quark code, LeafType type, USymbol symbol) {
//		Objects.requireNonNull(ident);
//		if (type == null) {
//			code = code.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
//			if (code.getData() != null)
//				return (ILeaf) code.getData();
//			if (getNamespaceSeparator() == null)
//				return reallyCreateLeaf(ident, Display.getWithNewlines(code.getName()), LeafType.CLASS, symbol);
//			// return getOrCreateLeafDefault(ident, code.getName(), LeafType.CLASS, symbol);
//
//			if (ident.getData() != null)
//				return (ILeaf) ident.getData();
//			final ILeaf result = reallyCreateLeaf(ident, Display.getWithNewlines(ident.getName()), LeafType.CLASS, symbol);
//			this.lastEntity = (EntityImp) result;
//			return result;
//		}
//		if (code.getData() != null)
//			return (ILeaf) code.getData();
//		if (getNamespaceSeparator() == null)
//			return reallyCreateLeaf(ident, Display.getWithNewlines(code.getName()), type, symbol);
//		// return getOrCreateLeafDefault(ident, code.getName(), type, symbol);
//
//		final ILeaf result = reallyCreateLeaf(ident, Display.getWithNewlines(ident.getName()), type, symbol);
//		this.lastEntity = (EntityImp) result;
//		return result;
//	}

//	@Override
//	public ILeaf createLeaf(Quark idNewLong, String displayString, Display display, LeafType type, USymbol symbol) {
//		Objects.requireNonNull(idNewLong);
//		if (type != LeafType.ABSTRACT_CLASS && type != LeafType.ANNOTATION && type != LeafType.CLASS
//				&& type != LeafType.INTERFACE && type != LeafType.ENUM && type != LeafType.LOLLIPOP_FULL
//				&& type != LeafType.LOLLIPOP_HALF && type != LeafType.NOTE)
//			return super.createLeaf(idNewLong, displayString, display, type, symbol);
//
//		if (getNamespaceSeparator() == null)
//			return super.createLeaf(idNewLong, displayString, display, type, symbol);
//
//		final ILeaf result = createLeafInternal(idNewLong, display, type, symbol);
//		this.lastEntity = (EntityImp) result;
//		return result;
//	}

	private boolean allowMixing;

	public void setAllowMixing(boolean allowMixing) {
		this.allowMixing = allowMixing;
	}

	public boolean isAllowMixing() {
		return allowMixing;
	}

	private int useLayoutExplicit = 0;

	public void layoutNewLine() {
		useLayoutExplicit++;
		incRawLayout();
	}

	@Override
	final protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		if (useLayoutExplicit != 0)
			return exportLayoutExplicit(os, index, fileFormatOption);

		return super.exportDiagramInternal(os, index, fileFormatOption);
	}

	final protected ImageData exportLayoutExplicit(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final FullLayout fullLayout = new FullLayout();
		for (int i = 0; i <= useLayoutExplicit; i++) {
			final RowLayout rawLayout = getRawLayout(i);
			fullLayout.addRowLayout(rawLayout);
		}
		return createImageBuilder(fileFormatOption).annotations(false) // Backwards compatibility - this only applies
																		// when "layout_new_line" is used
				.drawable(fullLayout).write(os);
	}

	private RowLayout getRawLayout(int raw) {
		final RowLayout rawLayout = new RowLayout();
		for (EntityImp leaf : entityFactory.leafs())
			if (leaf.getRawLayout() == raw)
				rawLayout.addLeaf(getEntityImageClass(leaf));

		return rawLayout;
	}

	private TextBlock getEntityImageClass(EntityImp entity) {
		return new EntityImageClass(entity, getSkinParam(), this);
	}

	@Override
	public String checkFinalError() {
		for (Link link : this.getLinks()) {
			final int len = link.getLength();
			if (len == 1)
				for (Link link2 : this.getLinks())
					if (link2.sameConnections(link) && link2.getLength() != 1)
						link2.setLength(1);

		}
		this.applySingleStrategy();
		return super.checkFinalError();
	}

}
