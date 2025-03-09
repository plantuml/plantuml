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
package net.sourceforge.plantuml.activitydiagram;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;

public class ActivityDiagram extends CucaDiagram {

	private Entity lastEntityConsulted;
	private Entity lastEntityBrancheConsulted;
	private ConditionalContext currentContext;

	public ActivityDiagram(UmlSource source, Previous previous, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.ACTIVITY, previous, preprocessing);
		setNamespaceSeparator(null);
	}

	private String getAutoBranch() {
		return this.getUniqueSequence("#");
	}

	public void startIf(LineLocation location, String optionalCodeString) {
		final String idShort = optionalCodeString == null ? getAutoBranch() : optionalCodeString;
		final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));
		final Entity br = reallyCreateLeaf(location, quark, Display.create(""), LeafType.BRANCH, null);
		currentContext = new ConditionalContext(currentContext, br, Direction.DOWN);
	}

	public void endif() {
		currentContext = currentContext.getParent();
	}

	public Entity getStart(LineLocation location) {
		final Quark<Entity> quark = quarkInContext(true, "start");
		if (quark.getData() == null)
			reallyCreateLeaf(location, quark, Display.getWithNewlines(getPragma(), "start"), LeafType.CIRCLE_START, null);

		return quark.getData();
	}

	public Entity getEnd(LineLocation location, String suppId) {
		final String tmp = suppId == null ? "end" : "end$" + suppId;
		final Quark<Entity> quark = quarkInContext(true, tmp);
		if (quark.getData() == null)
			reallyCreateLeaf(location, quark, Display.getWithNewlines(getPragma(), "end"), LeafType.CIRCLE_END, null);

		return quark.getData();
	}

	@Override
	protected void updateLasts(Entity result) {
		if (result == null || result.getLeafType() == LeafType.NOTE)
			return;

		// System.err.println("updateLasts " + result);
		this.lastEntityConsulted = result;
		if (result.getLeafType() == LeafType.BRANCH)
			lastEntityBrancheConsulted = result;

	}

	public Entity createNote(LineLocation location, Quark<Entity> idNewLong, String code__, Display display) {
		return reallyCreateLeaf(location, Objects.requireNonNull(idNewLong), display, LeafType.NOTE, null);
	}

	final protected List<String> getDotStrings() {
		return Arrays.asList("nodesep=.20;", "ranksep=0.4;", "edge [fontsize=11,labelfontsize=11];",
				"node [fontsize=11];");
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(" + this.leafs().size() + " activities)");
	}

	public Entity getLastEntityConsulted() {
		return lastEntityConsulted;
	}

	@Deprecated
	public Entity getLastEntityBrancheConsulted() {
		return lastEntityBrancheConsulted;
	}

	public final ConditionalContext getCurrentContext() {
		return currentContext;
	}

	public final void setLastEntityConsulted(Entity lastEntityConsulted) {
		// System.err.println("setLastEntityConsulted " + lastEntityConsulted);
		this.lastEntityConsulted = lastEntityConsulted;
	}

	public Entity createInnerActivity(LineLocation location) {

		final String idShort = this.getUniqueSequence("##");

		final Quark<Entity> quark = quarkInContext(true, idShort);
		gotoGroup(location, quark, Display.getWithNewlines(getPragma(), quark.getName()), GroupType.INNER_ACTIVITY);
		final Entity g = getCurrentGroup();

		lastEntityConsulted = null;
		lastEntityBrancheConsulted = null;

		return g;
	}

	public void concurrentActivity(LineLocation location, String name) {
		if (getCurrentGroup().getGroupType() == GroupType.CONCURRENT_ACTIVITY)
			endGroup();

		final String idShort = this.getUniqueSequence("##");

		if (getCurrentGroup().getGroupType() != GroupType.INNER_ACTIVITY)
			throw new IllegalStateException("type=" + getCurrentGroup().getGroupType());

		final Quark<Entity> idNewLong = quarkInContext(true, idShort);
		gotoGroup(location, idNewLong, Display.getWithNewlines(getPragma(), "code"), GroupType.CONCURRENT_ACTIVITY);
		lastEntityConsulted = null;
		lastEntityBrancheConsulted = null;
	}

}
