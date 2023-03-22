/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.svek.image.EntityImageClass;

public class ClassDiagram extends AbstractClassOrObjectDiagram {

	public ClassDiagram(UmlSource source, Map<String, String> skinParam) {
		super(source, UmlDiagramType.CLASS, skinParam);
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
		for (Entity leaf : entityFactory.leafs())
			if (leaf.getRawLayout() == raw)
				rawLayout.addLeaf(getEntityImageClass(leaf));

		return rawLayout;
	}

	private TextBlock getEntityImageClass(Entity entity) {
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

		if (getPragma().useIntermediatePackages() == false)
			packSomePackage();

		this.applySingleStrategy();
		return super.checkFinalError();
	}

	private void packSomePackage() {
		String separator = getNamespaceSeparator();
		if (separator == null)
			separator = ".";

		while (true) {
			boolean changed = false;
			for (Entity group : this.entityFactory.groups()) {
				if (group.canBePacked()) {
					final Entity child = group.groups().iterator().next();
					final String appended = group.getDisplay().get(0) + separator;
					final Display newDisplay = child.getDisplay().appendFirstLine(appended);
					child.setDisplay(newDisplay);
					group.setPacked(true);
					changed = true;
				}
			}
			if (changed == false)
				return;
		}

	}

	public CommandExecutionResult checkIfPackageHierarchyIfOk(Entity entity) {
		Quark<Entity> current = entity.getQuark().getParent();
		while (current.isRoot() == false) {
			if (current.getData() != null && current.getData().isGroup() == false)
				return CommandExecutionResult.error("Bad hierarchy for class " + entity.getQuark().getQualifiedName());
			current = current.getParent();
		}
		return CommandExecutionResult.ok();
	}

}
