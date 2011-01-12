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
import java.util.List;

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

	private Collection<IEntity> last2 = new ArrayList<IEntity>();
	private ConditionalContext2 currentContext;
	private int futureLength = 2;

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

	public void newActivity(String display) {
		if (last2.size() == 0) {
			throw new IllegalStateException();
		}
		final Entity act = createEntity(getAutoCode(), display, EntityType.ACTIVITY);
		for (IEntity last : this.last2) {
			this.addLink(new Link(last, act, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), null, futureLength));
		}
		this.last2.clear();
		this.last2.add(act);
		this.futureLength = 2;

	}

	private String getAutoCode() {
		return "ac" + UniqueSequence.getValue();
	}

	public void start() {
		if (last2.size() != 0) {
			throw new IllegalStateException();
		}
		this.last2.add(createEntity("start", "start", EntityType.CIRCLE_START));
	}

	public void startIf(String test) {
		final IEntity br = createEntity(getAutoCode(), "", EntityType.BRANCH);
		currentContext = new ConditionalContext2(currentContext, br, Direction.DOWN);
		for (IEntity last : this.last2) {
			this.addLink(new Link(last, br, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), null, futureLength));
		}
		this.last2.clear();
		this.last2.add(br);
		this.futureLength = 1;
	}

	public Collection<IEntity> getLastEntityConsulted2() {
		return this.last2;
	}

	public void endif() {
		this.last2.add(currentContext.getPending());
		currentContext = currentContext.getParent();
	}

	public void else2() {
		this.currentContext.setPending(this.last2.iterator().next());
		this.last2.clear();
		this.last2.add(currentContext.getBranch());
	}

}
