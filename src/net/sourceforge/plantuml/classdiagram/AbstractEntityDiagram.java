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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.PragmaKey;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public abstract class AbstractEntityDiagram extends CucaDiagram {
	// ::remove folder when __HAXE__

	public AbstractEntityDiagram(UmlSource source, UmlDiagramType type, Previous previous, PreprocessingArtifact preprocessing) {
		super(source, type, previous, preprocessing);
	}

	final protected List<String> getDotStrings() {
		final List<String> def = Arrays.asList("nodesep=.35;", "ranksep=0.8;", "edge [fontsize=11,labelfontsize=11];",
				"node [fontsize=11,height=.35,width=.55];");
		if (getPragma().isDefine(PragmaKey.GRAPH_ATTRIBUTES) == false)
			return def;

		final String attribute = getPragma().getValue(PragmaKey.GRAPH_ATTRIBUTES);
		final List<String> result = new ArrayList<>(def);
		result.add(attribute);
		return Collections.unmodifiableList(result);
	}

	final public DiagramDescription getDescription() {
		final StringBuilder result = new StringBuilder("(" + this.leafs().size() + " entities");
		if (getSource() != null) {
			final String id = getSource().getId();
			if (id != null) {
				result.append(", ");
				result.append(id);
			}
		}
		result.append(")");
		return new DiagramDescription(result.toString());
	}

	protected final void packSomePackage() {
		String separator = getNamespaceSeparator();
		if (separator == null)
			separator = ".";

		while (true) {
			boolean changed = false;
			for (Entity group : this.groups())
				if (group.canBePacked()) {
					final Entity child = group.groups().iterator().next();
					final String appended = group.getDisplay().get(0) + separator;
					final Display newDisplay = child.getDisplay().appendFirstLine(appended);
					child.setDisplay(newDisplay);
					group.setPacked(true);
					changed = true;
				}

			if (changed == false)
				return;
		}

	}

}
