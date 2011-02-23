/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 5721 $
 *
 */
package net.sourceforge.plantuml.activitydiagram2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class ActivityDiagram2 extends CucaDiagram {

	private Collection<IEntity> waitings = new LinkedHashSet<IEntity>();
	private ConditionalContext2 currentContext;
	private int futureLength = 2;
	private String futureLabel = null;

	private final Collection<String> pendingLabels = new HashSet<String>();
	private final Map<String, IEntity> labels = new HashMap<String, IEntity>();

	final protected List<String> getDotStrings() {
		return Arrays.asList("nodesep=.20;", "ranksep=0.4;", "edge [fontsize=11,labelfontsize=11];",
				"node [fontsize=11];");
	}

	public String getDescription() {
		return "(" + entities().size() + " activities)";
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.ACTIVITY;
	}

	public boolean isReachable() {
		return waitings.size() > 0;
	}

	public void newActivity(String display) {
		if (waitings.size() == 0) {
			throw new IllegalStateException();
		}
		final Entity act = createEntity(getAutoCode(), display, EntityType.ACTIVITY);
		afterAdd(act);

	}

	public void bar(String bar) {
		if (waitings.size() == 0) {
			// throw new IllegalStateException(bar);
		}
		label(bar);
		final Entity act = createEntity(getAutoCode(), bar, EntityType.SYNCHRO_BAR);
		afterAdd(act);
	}

	private void afterAdd(final Entity act) {
		for (IEntity last : this.waitings) {
			System.err.println("last=" + last);
			System.err.println("act=" + act);
			this.addLink(new Link(last, act, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), futureLabel, futureLength));
			futureLabel = null;
		}

		for (String p : pendingLabels) {
			labels.put(p, act);
		}
		pendingLabels.clear();

		this.waitings.clear();
		this.waitings.add(act);
		this.futureLength = 2;
	}
	
	public IEntity getLastEntityConsulted() {
		if (waitings.size()==1) {
			return waitings.iterator().next();
		}
		return null;
	}


	private String getAutoCode() {
		return "ac" + UniqueSequence.getValue();
	}

	public void start() {
		if (waitings.size() != 0) {
			throw new IllegalStateException();
		}
		this.waitings.add(createEntity("start", "start", EntityType.CIRCLE_START));
	}

	public void startIf(String test, String when) {
		final IEntity br = createEntity(getAutoCode(), "", EntityType.BRANCH);
		currentContext = new ConditionalContext2(currentContext, br, Direction.DOWN, when);
		for (IEntity last : this.waitings) {
			if (test == null) {
				// this.addLink(new Link(last, br, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), test, futureLength));
				throw new IllegalArgumentException();
			} else {
				this.addLink(new Link(last, br, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), this.futureLabel,
						futureLength, null, test, getLabeldistance(), getLabelangle()));
			}
			test = null;
		}
		this.waitings.clear();
		this.waitings.add(br);
		this.futureLength = 2;
		this.futureLabel = when;
	}

	// public Collection<IEntity> getWaitings() {
	// return this.waitings;
	// }

	public void endif() {
		final boolean hasElse = currentContext.isHasElse();
		System.err.println("CALL endif hasElse " + hasElse);
		this.waitings.addAll(currentContext.getPendings());
		currentContext = currentContext.getParent();
		if (currentContext == null) {
			System.err.println("after endif " + currentContext);
		} else {
			System.err.println("after endif " + currentContext.getPendings());
		}
	}

	public void else2(String when) {
		this.currentContext.executeElse(this.waitings);
		this.waitings.clear();
		this.waitings.add(currentContext.getBranch());
		this.futureLabel = when;
	}

	public void label(String label) {
		pendingLabels.add(label);
		for (final Iterator<PendingLink> it = pendingLinks.iterator(); it.hasNext();) {
			final PendingLink pending = it.next();
			if (pending.getGotoLabel().equals(label)) {
				if (pending.getLinkLabel() != null) {
					this.futureLabel = pending.getLinkLabel();
				}
				waitings.add(pending.getEntityFrom());
				it.remove();
			}
		}
	}

	private final Collection<PendingLink> pendingLinks = new ArrayList<PendingLink>();

	public void callGoto(String gotoLabel) {
		System.err.println("CALL goto " + gotoLabel);
		final IEntity dest = labels.get(gotoLabel);
		for (IEntity last : this.waitings) {
			if (dest == null) {
				this.pendingLinks.add(new PendingLink(last, gotoLabel, this.futureLabel));
			} else {
				this.addLink(new Link(last, dest, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), this.futureLabel,
						this.futureLength));
			}
		}
		System.err.println("Avant fin goto, waitings=" + waitings);
		this.waitings.clear();
		// currentContext.clearPendingsButFirst();
	}

	public void end() {
		// TODO Auto-generated method stub

	}

}
