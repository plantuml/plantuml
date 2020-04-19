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
package net.sourceforge.plantuml.activitydiagram;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class ActivityDiagram extends CucaDiagram {

	private IEntity lastEntityConsulted;
	private IEntity lastEntityBrancheConsulted;
	private ConditionalContext currentContext;

	public ActivityDiagram(ISkinSimple skinParam) {
		super(skinParam);
		setNamespaceSeparator(null);
	}

	public ILeaf getOrCreateLeaf(Ident ident, Code code, LeafType type, USymbol symbol) {
		checkNotNull(ident);
		return getOrCreateLeafDefault(ident, code, type, symbol);
	}

	private String getAutoBranch() {
		return "#" + UniqueSequence.getValue();
	}

	public IEntity getOrCreate(Ident idNewLong, Code code, Display display, LeafType type) {
		final IEntity result;
		final boolean leafExist = this.V1972() ? leafExistSmart(idNewLong) : leafExist(code);
		if (leafExist) {
			result = getOrCreateLeafDefault(idNewLong, code, type, null);
			if (result.getLeafType() != type) {
				return null;
			}
		} else {
			result = createLeaf(idNewLong, code, display, type, null);
		}
		updateLasts(result);
		return result;
	}
	
	@Override
	public /*final*/ ILeaf getLeafVerySmart(Ident ident) {
		final ILeaf result = super.getLeafVerySmart(ident);
		updateLasts(result);
		return result;
	}


	public void startIf(String optionalCodeString) {
		final String idShort = optionalCodeString == null ? getAutoBranch() : optionalCodeString;
		final Ident idNewLong = buildLeafIdent(idShort);
		final Code code = this.V1972() ? idNewLong : buildCode(idShort);
		final IEntity br = createLeaf(idNewLong, code, Display.create(""), LeafType.BRANCH, null);
		currentContext = new ConditionalContext(currentContext, br, Direction.DOWN);
	}

	public void endif() {
		currentContext = currentContext.getParent();
	}

	public ILeaf getStart() {
		final Ident ident = buildLeafIdent("start");
		final Code code = this.V1972() ? ident : buildCode("start");
		return (ILeaf) getOrCreate(ident, code, Display.getWithNewlines("start"), LeafType.CIRCLE_START);
	}

	public ILeaf getEnd(String suppId) {
		final String tmp = suppId == null ? "end" : "end$" + suppId;
		final Ident ident = buildLeafIdent(tmp);
		final Code code = this.V1972() ? ident : buildCode(tmp);
		return (ILeaf) getOrCreate(ident, code, Display.getWithNewlines("end"), LeafType.CIRCLE_END);
	}

	private void updateLasts(final IEntity result) {
		if (result == null || result.getLeafType() == LeafType.NOTE) {
			return;
		}
		this.lastEntityConsulted = result;
		if (result.getLeafType() == LeafType.BRANCH) {
			lastEntityBrancheConsulted = result;
		}
	}

	@Override
	public ILeaf createLeaf(Ident idNewLong, Code code, Display display, LeafType type, USymbol symbol) {
		checkNotNull(idNewLong);
		final ILeaf result = super.createLeaf(idNewLong, code, display, type, symbol);
		updateLasts(result);
		return result;
	}

	public IEntity createNote(Ident idNewLong, Code code, Display display) {
		checkNotNull(idNewLong);
		return super.createLeaf(idNewLong, code, display, LeafType.NOTE, null);
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
		final String idShort = "##" + UniqueSequence.getValue();
		final Ident idNewLong = buildLeafIdent(idShort);
		final Code code = this.V1972() ? idNewLong : buildCode(idShort);
		gotoGroup(idNewLong, code, Display.getWithNewlines(code), GroupType.INNER_ACTIVITY, getCurrentGroup(),
				NamespaceStrategy.SINGLE);
		final IEntity g = getCurrentGroup();
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
		final String idShort = "##" + UniqueSequence.getValue();
		// Log.println("concurrentActivity A name=" + name+" "+getCurrentGroup());
		final Code code = buildCode(idShort);
		if (getCurrentGroup().getGroupType() != GroupType.INNER_ACTIVITY) {
			throw new IllegalStateException("type=" + getCurrentGroup().getGroupType());
		}
		final Ident idNewLong = buildLeafIdent(idShort);
		gotoGroup(idNewLong, code, Display.getWithNewlines("code"), GroupType.CONCURRENT_ACTIVITY, getCurrentGroup(),
				NamespaceStrategy.SINGLE);
		lastEntityConsulted = null;
		lastEntityBrancheConsulted = null;
	}

}
