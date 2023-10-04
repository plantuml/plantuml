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
 * Contribution   :  Serge Wenger 
 *
 */
package net.sourceforge.plantuml.statediagram;

import java.util.Map;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class StateDiagram extends AbstractEntityDiagram {
	// ::remove folder when __HAXE__

	private static final String CONCURRENT_PREFIX = "CONC";

	public StateDiagram(UmlSource source, Map<String, String> skinParam) {
		super(source, UmlDiagramType.STATE, skinParam);
		setNamespaceSeparator(".");
	}

	public boolean checkConcurrentStateOk(Quark<Entity> code) {
		final boolean result = checkConcurrentStateOkInternal(code);
		return result;
	}

	private boolean checkConcurrentStateOkInternal(Quark<Entity> code) {
		if (code.getData() == null)
			return true;

		final Entity existing = code.getData();
		if (getCurrentGroup().getGroupType() == GroupType.CONCURRENT_STATE
				&& getCurrentGroup() != existing.getParentContainer())
			return false;

		if (existing.getParentContainer() != null
				&& existing.getParentContainer().getGroupType() == GroupType.CONCURRENT_STATE
				&& getCurrentGroup() != existing.getParentContainer())
			return false;

		return true;
	}

	public Entity getStart() {
		final Entity g = getCurrentGroup();
		if (g.isRoot()) {
			final String idShort = "*start*";
			final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));
			if (quark.getData() == null)
				reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.CIRCLE_START, null);
			return quark.getData();
		}
		final String idShort = "*start*" + g.getName();
		final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));
		if (quark.getData() == null)
			reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.CIRCLE_START, null);
		return quark.getData();
	}

	public Entity getEnd() {
		final Entity p = getCurrentGroup();
		if (p.isRoot()) {
			final String idShort = "*end*";
			final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));
			if (quark.getData() == null)
				reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.CIRCLE_END, null);
			return quark.getData();
		}
		final String idShort = "*end*" + p.getName();
		final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));
		if (quark.getData() == null)
			reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.CIRCLE_END, null);
		return quark.getData();
	}

	public Entity getHistorical() {
		final Entity g = getCurrentGroup();
		if (g.isRoot()) {
			final String idShort = "*historical*";
			final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));
			if (quark.getData() == null)
				reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.PSEUDO_STATE, null);
			return quark.getData();
		}
		final String idShort = "*historical*" + g.getName();
		final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));
		if (quark.getData() == null)
			reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.PSEUDO_STATE, null);
		return quark.getData();
	}

	public Entity getHistorical(String idShort) {
		final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));
		gotoGroup(quark, Display.getWithNewlines(quark), GroupType.STATE);
		final Entity g = getCurrentGroup();
		final String tmp = "*historical*" + g.getName();
		final Quark<Entity> ident = quarkInContext(true, tmp);
		final Entity result;
		if (ident.getData() == null)
			result = reallyCreateLeaf(ident, Display.getWithNewlines(ident), LeafType.PSEUDO_STATE, null);
		else
			result = ident.getData();
		endGroup();
		return result;
	}

	public Entity getDeepHistory() {
		final Entity g = getCurrentGroup();
		if (g.isRoot()) {
			final String idShort = "*deephistory*";
			final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));
			if (quark.getData() == null)
				reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.DEEP_HISTORY, null);
			return quark.getData();
		}

		final String idShort = "*deephistory*" + g.getName();
		final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));
		if (quark.getData() == null)
			reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.DEEP_HISTORY, null);
		return quark.getData();

	}

	public Entity getDeepHistory(String idShort) {
		final Quark<Entity> quark = quarkInContext(true, cleanId(idShort));

		gotoGroup(quark, Display.getWithNewlines(quark), GroupType.STATE);
		final Entity g = getCurrentGroup();
		final String tmp = "*deephistory*" + g.getName();
		final Quark<Entity> ident = quarkInContext(true, cleanId(tmp));
		final Entity result = reallyCreateLeaf(ident, Display.getWithNewlines(""), LeafType.DEEP_HISTORY, null);
		endGroup();
		return result;
	}

	public boolean concurrentState(char direction) {
		final Entity cur = getCurrentGroup();
		getCurrentGroup().setConcurrentSeparator(direction);

		if (cur.getGroupType() == GroupType.CONCURRENT_STATE)
			super.endGroup();

		final String tmp1 = this.getUniqueSequence(CONCURRENT_PREFIX);
		final Quark<Entity> ident1 = quarkInContext(true, cleanId(tmp1));

		gotoGroup(ident1, Display.create(""), GroupType.CONCURRENT_STATE);
		getCurrentGroup().setConcurrentSeparator(direction);
//		// final Entity conc1 = getCurrentGroup();
//		if (cur.getGroupType() == GroupType.STATE) {
//
////			moveAllChildOfToAnewFather(cur.getQuark(), conc1.getQuark());
////			super.endGroup();
//
//			final String tmp2 = this.getUniqueSequence(CONCURRENT_PREFIX);
//			final Quark<Entity> ident2 = quarkInContext(tmp2, false);
//			gotoGroup(ident2, Display.create(""), GroupType.CONCURRENT_STATE);
//		}

		return true;
	}

	@Override
	public boolean endGroup() {
		final Entity cur = getCurrentGroup();
		if (cur.getGroupType() == GroupType.CONCURRENT_STATE)
			super.endGroup();

		return super.endGroup();
	}

	private boolean hideEmptyDescription = false;

	@Override
	public final void setHideEmptyDescription(boolean hideEmptyDescription) {
		this.hideEmptyDescription = hideEmptyDescription;
	}

	public final boolean isHideEmptyDescriptionForState() {
		return hideEmptyDescription;
	}

	@Override
	public String checkFinalError() {
		for (Link link : this.getLinks()) {
			final Entity parent1 = getGroupParentIfItIsConcurrentState(link.getEntity1());
			final Entity parent2 = getGroupParentIfItIsConcurrentState(link.getEntity2());
			if (isCompatible(parent1, parent2) == false)
				return "State within concurrent state cannot be linked out of this concurrent state (between "
						+ link.getEntity1().getName() + " and " + link.getEntity2().getName() + ")";

		}
		return super.checkFinalError();
	}

	private static boolean isCompatible(Entity parent1, Entity parent2) {
		if (parent1 == null && parent2 == null)
			return true;

		if (parent1 != null ^ parent2 != null)
			return false;

		assert parent1 != null && parent2 != null;
		return parent1 == parent2;
	}

	private static Entity getGroupParentIfItIsConcurrentState(Entity ent) {
		Entity parent = ent.getParentContainer();
		while (parent != null) {
			if (parent.getGroupType() == GroupType.CONCURRENT_STATE)
				return parent;

			parent = parent.getParentContainer();
		}
		return null;

	}

}
