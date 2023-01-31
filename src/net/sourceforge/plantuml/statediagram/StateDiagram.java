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
 * Contribution   :  Serge Wenger 
 *
 */
package net.sourceforge.plantuml.statediagram;

import java.util.Map;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.baraye.EntityUtils;
import net.sourceforge.plantuml.baraye.Quark;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;

public class StateDiagram extends AbstractEntityDiagram {

	private static final String CONCURRENT_PREFIX = "CONC";

	public StateDiagram(UmlSource source, Map<String, String> skinParam) {
		super(source, UmlDiagramType.STATE, skinParam);
		// setNamespaceSeparator(null);
	}

	public boolean checkConcurrentStateOk(Quark code) {
		final boolean result = checkConcurrentStateOkInternal(code);
		// System.err.println("checkConcurrentStateOk " + code + " " + ident + " " +
		// result);
		return result;
	}

	private boolean checkConcurrentStateOkInternal(Quark code) {
		if (code.getData() == null) {
			return true;
		}
		// final IEntity existing = this.getLeafFromName(code.getName());
		final EntityImp existing = (EntityImp) code.getData();
		if (getCurrentGroup().getGroupType() == GroupType.CONCURRENT_STATE
				&& getCurrentGroup() != existing.getParentContainer()) {
			return false;
		}
		if (existing.getParentContainer().getGroupType() == GroupType.CONCURRENT_STATE
				&& getCurrentGroup() != existing.getParentContainer()) {
			return false;
		}
		return true;
	}

//	@Override
//	protected IEntity getOrCreateLeaf2(Quark ident, Quark code, LeafType type, USymbol symbol) {
//		if (checkConcurrentStateOk(code) == false) {
//			throw new IllegalStateException("Concurrent State " + code);
//		}
//		if (type == null) {
//			if (code.getName().startsWith("[*]"))
//				throw new IllegalArgumentException();
//
//			if (isGroup(code.getName()))
//				return getGroup(code.getName());
//
//			if (code.getData() != null)
//				return (ILeaf) code.getData();
//
//			return reallyCreateLeaf(ident, Display.getWithNewlines(code.getName()), LeafType.STATE, null);
//		}
//		if (code.getData() != null)
//			return (ILeaf) code.getData();
//		return reallyCreateLeaf(ident, Display.getWithNewlines(code.getName()), type, symbol);
//	}

	public EntityImp getStart() {
		final EntityImp g = getCurrentGroup();
		if (g.getQuark().isRoot()) {
			final String idShort = "*start*";
			final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
			if (quark.getData() == null)
				quark.setData(reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.CIRCLE_START, null));
			return (EntityImp) quark.getData();
		}
		final String idShort = "*start*" + g.getCodeGetName();
		final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
		if (quark.getData() == null)
			quark.setData(reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.CIRCLE_START, null));
		return (EntityImp) quark.getData();
	}

	public EntityImp getEnd() {
		final EntityImp p = getCurrentGroup();
		if (p.getQuark().isRoot()) {
			final String idShort = "*end*";
			final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
			if (quark.getData() == null)
				quark.setData(reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.CIRCLE_END, null));
			return (EntityImp) quark.getData();
		}
		final String idShort = "*end*" + p.getCodeGetName();
		final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
		if (quark.getData() == null)
			quark.setData(reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.CIRCLE_END, null));
		return (EntityImp) quark.getData();
	}

	public EntityImp getHistorical() {
		final EntityImp g = getCurrentGroup();
		if (g.getQuark().isRoot()) {
			final String idShort = "*historical*";
			final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
			if (quark.getData() == null)
				quark.setData(reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.PSEUDO_STATE, null));
			return (EntityImp) quark.getData();
		}
		final String idShort = "*historical*" + g.getCodeGetName();
		final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
		if (quark.getData() == null)
			quark.setData(reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.PSEUDO_STATE, null));
		return (EntityImp) quark.getData();
	}

	public EntityImp getHistorical(String idShort) {
		final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
		gotoGroup(quark, Display.getWithNewlines(quark), GroupType.STATE);
		final EntityImp g = getCurrentGroup();
		final String tmp = "*historical*" + g.getCodeGetName();
		final Quark ident = quarkInContext(tmp, false);
		final EntityImp result = reallyCreateLeaf(ident, Display.getWithNewlines(ident), LeafType.PSEUDO_STATE, null);
		endGroup();
		return result;
	}

	public EntityImp getDeepHistory() {
		final EntityImp g = getCurrentGroup();
		if (g.getQuark().isRoot()) {
			final String idShort = "*deephistory*";
			final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
			if (quark.getData() == null)
				quark.setData(reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.DEEP_HISTORY, null));
			return (EntityImp) quark.getData();
		}

		final String idShort = "*deephistory*" + g.getCodeGetName();
		final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
		if (quark.getData() == null)
			quark.setData(reallyCreateLeaf(quark, Display.getWithNewlines(""), LeafType.DEEP_HISTORY, null));
		return (EntityImp) quark.getData();

	}

	public EntityImp getDeepHistory(String idShort) {
		final Quark quark = quarkInContext(cleanIdForQuark(idShort), false);
		// final Quark codeGroup = buildFromFullPath(idShort);
		gotoGroup(quark, Display.getWithNewlines(quark), GroupType.STATE);
		final EntityImp g = getCurrentGroup();
		final String tmp = "*deephistory*" + g.getCodeGetName();
		final Quark ident = quarkInContext(cleanIdForQuark(tmp), false);
		final EntityImp result = reallyCreateLeaf(ident, Display.getWithNewlines(""), LeafType.DEEP_HISTORY, null);
		endGroup();
		return result;
	}

	public boolean concurrentState(char direction) {
		final EntityImp cur = getCurrentGroup();
		// printlink("BEFORE");
		if (cur.getQuark().isRoot() == false && cur.getGroupType() == GroupType.CONCURRENT_STATE) {
			super.endGroup();
		}
		getCurrentGroup().setConcurrentSeparator(direction);
		final String tmp1 = this.getUniqueSequence(CONCURRENT_PREFIX);
		final Quark ident1 = quarkInContext(cleanIdForQuark(tmp1), false);

		gotoGroup(ident1, Display.create(""), GroupType.CONCURRENT_STATE);
		final EntityImp conc1 = getCurrentGroup();
		if (cur.getQuark().isRoot() == false && cur.getGroupType() == GroupType.STATE) {
			// cur.moveEntitiesTo(conc1);
			getPlasma().moveAllChildOfToAnewFather(cur.getQuark(), conc1.getQuark());
			super.endGroup();

			final String tmp2 = this.getUniqueSequence(CONCURRENT_PREFIX);

			final Quark ident2 = quarkInContext(tmp2, false);

			gotoGroup(ident2, Display.create(""), GroupType.CONCURRENT_STATE);
		}
		// printlink("AFTER");
		return true;
	}

	// private void printlink(String comment) {
	// Log.println("COMMENT="+comment);
	// for (Link l : getLinks()) {
	// Log.println(l);
	// }
	// }

	@Override
	public boolean endGroup() {
		final EntityImp cur = getCurrentGroup();
		if (cur.getQuark().isRoot() == false && cur.getGroupType() == GroupType.CONCURRENT_STATE)
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
			final EntityImp parent1 = getGroupParentIfItIsConcurrentState(link.getEntity1());
			final EntityImp parent2 = getGroupParentIfItIsConcurrentState(link.getEntity2());
			if (isCompatible(parent1, parent2) == false) {
				return "State within concurrent state cannot be linked out of this concurrent state (between "
						+ link.getEntity1().getCodeGetName() + " and " + link.getEntity2().getCodeGetName() + ")";
			}
		}
		return super.checkFinalError();
	}

	private static boolean isCompatible(EntityImp parent1, EntityImp parent2) {
		if (parent1 == null && parent2 == null) {
			return true;
		}
		if (parent1 != null ^ parent2 != null) {
			return false;
		}
		assert parent1 != null && parent2 != null;
		return parent1 == parent2;
	}

	private static EntityImp getGroupParentIfItIsConcurrentState(EntityImp ent) {
		EntityImp parent = ent.getParentContainer();
		while (parent != null) {
			if (parent.getGroupType() == GroupType.CONCURRENT_STATE) {
				return parent;
			}
			parent = parent.getParentContainer();
		}
		return null;

	}

}
