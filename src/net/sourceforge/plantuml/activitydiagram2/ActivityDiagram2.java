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

	private Collection<IEntity> waitings = new ArrayList<IEntity>();
	private ConditionalContext2 currentContext;
	private int futureLength = 2;

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
		for (IEntity last : this.waitings) {
			this.addLink(new Link(last, act, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), null, futureLength));
		}

		for (String p : pendingLabels) {
			labels.put(p, act);
		}
		pendingLabels.clear();

		this.waitings.clear();
		this.waitings.add(act);
		this.futureLength = 2;

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

	public void startIf(String test) {
		final IEntity br = createEntity(getAutoCode(), "", EntityType.BRANCH);
		currentContext = new ConditionalContext2(currentContext, br, Direction.DOWN);
		for (IEntity last : this.waitings) {
			this.addLink(new Link(last, br, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), null, futureLength));
		}
		this.waitings.clear();
		this.waitings.add(br);
		this.futureLength = 2;
	}

	public Collection<IEntity> getLastEntityConsulted2() {
		return this.waitings;
	}

	public void endif() {
		this.waitings.add(currentContext.getPending());
		currentContext = currentContext.getParent();
	}

	public void else2() {
		this.currentContext.setPending(this.waitings.iterator().next());
		this.waitings.clear();
		this.waitings.add(currentContext.getBranch());
	}

	public void label(String label) {
		pendingLabels.add(label);
		for (final Iterator<PendingLink> it = pendingLinks.iterator(); it.hasNext();) {
			final PendingLink pending = it.next();
			if (pending.getLabel().equals(label)) {
				waitings.add(pending.getEntityFrom());
				it.remove();
			}
		}
	}

	private final Collection<PendingLink> pendingLinks = new ArrayList<PendingLink>();

	public void callGoto(String label) {
		final IEntity dest = labels.get(label);
		for (IEntity last : this.waitings) {
			if (dest == null) {
				this.pendingLinks.add(new PendingLink(last, label));

			} else {
				this.addLink(new Link(last, dest, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), null, futureLength));
			}
		}
		this.waitings.clear();
	}
}
