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
package net.sourceforge.plantuml.activitydiagram;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.baraye.CucaDiagram;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.baraye.Quark;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.utils.Direction;

public class ActivityDiagram extends CucaDiagram {

	private EntityImp lastEntityConsulted;
	private EntityImp lastEntityBrancheConsulted;
	private ConditionalContext currentContext;

	public ActivityDiagram(UmlSource source, Map<String, String> skinParam) {
		super(source, UmlDiagramType.ACTIVITY, skinParam);
		setNamespaceSeparator(null);
	}

	private String getAutoBranch() {
		return "#" + this.getUniqueSequence();
	}

//	public final IEntity getOrCreateInActivity(Quark idNewLong, String codeString, Display display, LeafType type) {
//		final Quark code = buildFromFullPath(codeString);
//		final IEntity result;
//		if (code.getData() == null) {
//			final Quark quark = getPlasma().getIfExistsFromName(code.getName());
//			if (quark != null && quark.getData() != null)
//				result = getFromName(code.getName());
//			else
//				result = reallyCreateLeaf(idNewLong, display, type, null);
//		} else {
//			result = (ILeaf) code.getData();
//			if (result.getLeafType() != type)
//				return null;
//		}
//		updateLasts(result);
//		return result;
//	}

	public void startIf(String optionalCodeString) {
		final String idShort = optionalCodeString == null ? getAutoBranch() : optionalCodeString;
		final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
		// final Quark code = buildCode(idShort);
		final EntityImp br = reallyCreateLeaf(quark, Display.create(""), LeafType.BRANCH, null);
		currentContext = new ConditionalContext(currentContext, br, Direction.DOWN);
	}

	public void endif() {
		currentContext = currentContext.getParent();
	}

	public EntityImp getStart() {
		final Quark quark = quarkInContext("start", false);
		if (quark.getData() == null) {
			quark.setData(reallyCreateLeaf(quark, Display.getWithNewlines("start"), LeafType.CIRCLE_START, null));
		}
		return (EntityImp) quark.getData();
	}

	public EntityImp getEnd(String suppId) {
		final String tmp = suppId == null ? "end" : "end$" + suppId;
		final Quark quark = quarkInContext(tmp, false);
		if (quark.getData() == null) {
			quark.setData(reallyCreateLeaf(quark, Display.getWithNewlines("end"), LeafType.CIRCLE_END, null));
		}
		return (EntityImp) quark.getData();
	}

	@Override
	protected void updateLasts(EntityImp result) {
		if (result == null || result.getLeafType() == LeafType.NOTE)
			return;

		// System.err.println("updateLasts " + result);
		this.lastEntityConsulted = result;
		if (result.getLeafType() == LeafType.BRANCH)
			lastEntityBrancheConsulted = result;

	}

	public EntityImp createNote(Quark idNewLong, String code__, Display display) {
		return reallyCreateLeaf(Objects.requireNonNull(idNewLong), display, LeafType.NOTE, null);
	}

	final protected List<String> getDotStrings() {
		return Arrays.asList("nodesep=.20;", "ranksep=0.4;", "edge [fontsize=11,labelfontsize=11];",
				"node [fontsize=11];");
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(" + getLeafssize() + " activities)");
	}

	public EntityImp getLastEntityConsulted() {
		return lastEntityConsulted;
	}

	@Deprecated
	public EntityImp getLastEntityBrancheConsulted() {
		return lastEntityBrancheConsulted;
	}

	public final ConditionalContext getCurrentContext() {
		return currentContext;
	}

	public final void setLastEntityConsulted(EntityImp lastEntityConsulted) {
		// System.err.println("setLastEntityConsulted " + lastEntityConsulted);
		this.lastEntityConsulted = lastEntityConsulted;
	}

	public EntityImp createInnerActivity() {

		final String idShort = "##" + this.getUniqueSequence();

		final Quark quark = quarkInContext(idShort, false);
		gotoGroup(quark, Display.getWithNewlines(quark.getName()), GroupType.INNER_ACTIVITY);
		final EntityImp g = getCurrentGroup();

		lastEntityConsulted = null;
		lastEntityBrancheConsulted = null;

		return g;
	}

	public void concurrentActivity(String name) {
		if (getCurrentGroup().getGroupType() == GroupType.CONCURRENT_ACTIVITY)
			endGroup();

		final String idShort = "##" + this.getUniqueSequence();

		if (getCurrentGroup().getGroupType() != GroupType.INNER_ACTIVITY)
			throw new IllegalStateException("type=" + getCurrentGroup().getGroupType());

		final Quark idNewLong = quarkInContext(idShort, false);
		gotoGroup(idNewLong, Display.getWithNewlines("code"), GroupType.CONCURRENT_ACTIVITY);
		lastEntityConsulted = null;
		lastEntityBrancheConsulted = null;
	}

}
