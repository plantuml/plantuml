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
package net.sourceforge.plantuml.classdiagram;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.svek.image.EntityImageClass;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

public class ClassDiagram extends AbstractClassOrObjectDiagram {

	public ClassDiagram(ISkinSimple skinParam) {
		super(skinParam);
	}

	private final String getNamespace(Code fullyCode) {
		return getNamespace(fullyCode, fullyCode.getSeparator());
	}

	private final String getNamespace(Code fullyCode, String separator) {
		String name = fullyCode.getFullName();
		if (separator == null) {
			throw new IllegalArgumentException(toString());
		}
		do {
			final int x = name.lastIndexOf(separator);
			if (x == -1) {
				return null;
			}
			name = name.substring(0, x);
		} while (entityFactory.getLeafsget(Code.of(name, separator)) != null);
		return name;
	}

	public final Code getShortName(Code code) {
		final String separator = code.getSeparator();
		if (separator == null) {
			throw new IllegalArgumentException();
		}
		final String codeString = code.getFullName();
		final String namespace = getNamespace(code);
		if (namespace == null) {
			return Code.of(codeString, separator);
		}
		return Code.of(codeString.substring(namespace.length() + separator.length()), separator);
	}

	@Override
	public ILeaf getOrCreateLeaf(Code code, LeafType type, USymbol symbol) {
		if (getNamespaceSeparator() != null) {
			code = code.withSeparator(getNamespaceSeparator());
		}
		if (type == null) {
			code = code.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
			if (getNamespaceSeparator() == null) {
				return getOrCreateLeafDefault(code, LeafType.CLASS, symbol);
			}
			code = getFullyQualifiedCode(code);
			if (super.leafExist(code)) {
				return getOrCreateLeafDefault(code, LeafType.CLASS, symbol);
			}
			return createEntityWithNamespace(code, Display.getWithNewlines(getShortName(code)), LeafType.CLASS, symbol);
		}
		if (getNamespaceSeparator() == null) {
			return getOrCreateLeafDefault(code, type, symbol);
		}
		code = getFullyQualifiedCode(code);
		if (super.leafExist(code)) {
			return getOrCreateLeafDefault(code, type, symbol);
		}
		return createEntityWithNamespace(code, Display.getWithNewlines(getShortName(code)), type, symbol);
	}

	@Override
	public ILeaf createLeaf(Code code, Display display, LeafType type, USymbol symbol) {
		if (getNamespaceSeparator() != null) {
			code = code.withSeparator(getNamespaceSeparator());
		}
		if (type != LeafType.ABSTRACT_CLASS && type != LeafType.ANNOTATION && type != LeafType.CLASS
				&& type != LeafType.INTERFACE && type != LeafType.ENUM && type != LeafType.LOLLIPOP_FULL
				&& type != LeafType.LOLLIPOP_HALF && type != LeafType.NOTE) {
			return super.createLeaf(code, display, type, symbol);
		}
		if (getNamespaceSeparator() == null) {
			return super.createLeaf(code, display, type, symbol);
		}
		code = getFullyQualifiedCode(code);
		if (super.leafExist(code)) {
			throw new IllegalArgumentException("Already known: " + code);
		}
		return createEntityWithNamespace(code, display, type, symbol);
	}

	private ILeaf createEntityWithNamespace(Code fullyCode, Display display, LeafType type, USymbol symbol) {
		final IGroup backupCurrentGroup = getCurrentGroup();
		final IGroup group = backupCurrentGroup;
		final String namespace = getNamespace(fullyCode, getNamespaceSeparator());
		if (namespace != null
				&& (EntityUtils.groupRoot(group) || group.getCode().getFullName().equals(namespace) == false)) {
			final Code namespace2 = Code.of(namespace);
			gotoGroupInternal(namespace2, Display.getWithNewlines(namespace), namespace2, GroupType.PACKAGE,
					getRootGroup());
		}
		final ILeaf result = createLeafInternal(
				fullyCode,
				Display.isNull(display) ? Display.getWithNewlines(getShortName(fullyCode)).withCreoleMode(
						CreoleMode.SIMPLE_LINE) : display, type, symbol);
		gotoThisGroup(backupCurrentGroup);
		return result;
	}

	@Override
	public final boolean leafExist(Code code) {
		if (getNamespaceSeparator() == null) {
			return super.leafExist(code);
		}
		code = code.withSeparator(getNamespaceSeparator());
		return super.leafExist(getFullyQualifiedCode(code));
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.CLASS;
	}

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
		if (useLayoutExplicit != 0) {
			return exportLayoutExplicit(os, index, fileFormatOption);
		}
		return super.exportDiagramInternal(os, index, fileFormatOption);
	}

	final protected ImageData exportLayoutExplicit(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final FullLayout fullLayout = new FullLayout();
		for (int i = 0; i <= useLayoutExplicit; i++) {
			final RowLayout rawLayout = getRawLayout(i);
			fullLayout.addRowLayout(rawLayout);
		}
		final ImageBuilder imageBuilder = new ImageBuilder(getSkinParam(), 1, null, null, 0, 10, null);
		imageBuilder.setUDrawable(fullLayout);
		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, seed(), os);
	}

	private RowLayout getRawLayout(int raw) {
		final RowLayout rawLayout = new RowLayout();
		for (ILeaf leaf : entityFactory.getLeafsvalues()) {
			if (leaf.getRawLayout() == raw) {
				rawLayout.addLeaf(getEntityImageClass(leaf));
			}
		}
		return rawLayout;
	}

	private TextBlock getEntityImageClass(ILeaf entity) {
		return new EntityImageClass(null, entity, getSkinParam(), this);
	}

	@Override
	public String checkFinalError() {
		for (Link link : this.getLinks()) {
			final int len = link.getLength();
			if (len == 1) {
				for (Link link2 : this.getLinks()) {
					if (link2.sameConnections(link) && link2.getLength() != 1) {
						link2.setLength(1);
					}
				}
			}
		}
		this.applySingleStrategy();
		return super.checkFinalError();
	}

}
