/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.activitydiagram;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class ActivityDiagram extends CucaDiagram {

	private IEntity lastEntityConsulted;
	private IEntity lastEntityBrancheConsulted;
	private ConditionalContext currentContext;

	public ILeaf getOrCreateLeaf(Code code, LeafType type, USymbol symbol) {
		return getOrCreateLeafDefault(code, type, symbol);
	}

	private String getAutoBranch() {
		return "#" + UniqueSequence.getValue();
	}

	public IEntity getOrCreate(Code code, Display display, LeafType type) {
		final IEntity result;
		if (leafExist(code)) {
			result = getOrCreateLeafDefault(code, type, null);
			if (result.getLeafType() != type) {
				// throw new IllegalArgumentException("Already known: " + code + " " + result.getType() + " " + type);
				return null;
			}
		} else {
			result = createLeaf(code, display, type, null);
		}
		updateLasts(result);
		return result;
	}

	public void startIf(Code optionalCode) {
		final IEntity br = createLeaf(optionalCode == null ? Code.of(getAutoBranch()) : optionalCode,
				Display.create(""), LeafType.BRANCH, null);
		currentContext = new ConditionalContext(currentContext, br, Direction.DOWN);
	}

	public void endif() {
		currentContext = currentContext.getParent();
	}

	public ILeaf getStart() {
		return (ILeaf) getOrCreate(Code.of("start"), Display.getWithNewlines("start"), LeafType.CIRCLE_START);
	}

	public ILeaf getEnd(String suppId) {
		final Code code = suppId == null ? Code.of("end") : Code.of("end$" + suppId);
		return (ILeaf) getOrCreate(code, Display.getWithNewlines("end"), LeafType.CIRCLE_END);
	}

	private void updateLasts(final IEntity result) {
		if (result.getLeafType() == LeafType.NOTE) {
			return;
		}
		this.lastEntityConsulted = result;
		if (result.getLeafType() == LeafType.BRANCH) {
			lastEntityBrancheConsulted = result;
		}
	}

	@Override
	public ILeaf createLeaf(Code code, Display display, LeafType type, USymbol symbol) {
		final ILeaf result = super.createLeaf(code, display, type, symbol);
		updateLasts(result);
		return result;
	}

	public IEntity createNote(Code code, Display display) {
		return super.createLeaf(code, display, LeafType.NOTE, null);
	}

	final protected List<String> getDotStrings() {
		return Arrays.asList("nodesep=.20;", "ranksep=0.4;", "edge [fontsize=11,labelfontsize=11];",
				"node [fontsize=11];");
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(" + getLeafssize() + " activities)");
	}

	public IEntity getLastEntityConsulted() {
		return lastEntityConsulted;
	}

	@Deprecated
	public IEntity getLastEntityBrancheConsulted() {
		return lastEntityBrancheConsulted;
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.ACTIVITY;
	}

	public final ConditionalContext getCurrentContext() {
		return currentContext;
	}

	public final void setLastEntityConsulted(IEntity lastEntityConsulted) {
		this.lastEntityConsulted = lastEntityConsulted;
	}

	public IEntity createInnerActivity() {
		// Log.println("createInnerActivity A");
		final Code code = Code.of("##" + UniqueSequence.getValue());
		final IEntity g = getOrCreateGroup(code, Display.getWithNewlines(code), GroupType.INNER_ACTIVITY,
				getCurrentGroup());
		// g.setRankdir(Rankdir.LEFT_TO_RIGHT);
		lastEntityConsulted = null;
		lastEntityBrancheConsulted = null;
		// Log.println("createInnerActivity B "+getCurrentGroup());
		return g;
	}

	public void concurrentActivity(String name) {
		// Log.println("concurrentActivity A name=" + name+" "+getCurrentGroup());
		if (getCurrentGroup().getGroupType() == GroupType.CONCURRENT_ACTIVITY) {
			// getCurrentGroup().setRankdir(Rankdir.LEFT_TO_RIGHT);
			endGroup();
			// Log.println("endgroup");
		}
		// Log.println("concurrentActivity A name=" + name+" "+getCurrentGroup());
		final Code code = Code.of("##" + UniqueSequence.getValue());
		if (getCurrentGroup().getGroupType() != GroupType.INNER_ACTIVITY) {
			throw new IllegalStateException("type=" + getCurrentGroup().getGroupType());
		}
		getOrCreateGroup(code, Display.getWithNewlines("code"), GroupType.CONCURRENT_ACTIVITY, getCurrentGroup());
		lastEntityConsulted = null;
		lastEntityBrancheConsulted = null;
	}

}
