/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public class InstructionFork implements Instruction {

	private final List<InstructionList> forks = new ArrayList<InstructionList>();
	private final Instruction parent;
	private final LinkRendering inlinkRendering;

	public InstructionFork(Instruction parent, LinkRendering inlinkRendering) {
		this.parent = parent;
		this.inlinkRendering = inlinkRendering;
		this.forks.add(new InstructionList());
	}

	private InstructionList getLast() {
		return forks.get(forks.size() - 1);
	}

	public void add(Instruction ins) {
		getLast().add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		final List<Ftile> all = new ArrayList<Ftile>();
		for (InstructionList list : forks) {
			all.add(list.createFtile(factory));
		}
		return factory.createFork(getSwimlaneIn(), all);
	}

	public Instruction getParent() {
		return parent;
	}

	public void forkAgain() {
		this.forks.add(new InstructionList());
	}

	final public boolean kill() {
		return getLast().kill();
	}

	public LinkRendering getInLinkRendering() {
		return inlinkRendering;
	}

	public boolean addNote(Display note, NotePosition position) {
		return getLast().addNote(note, position);
	}

	public Set<Swimlane> getSwimlanes() {
		return InstructionList.getSwimlanes2(forks);
	}

	public Swimlane getSwimlaneIn() {
		// return parent.getSwimlaneOut();
		return forks.get(0).getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getLast().getSwimlaneOut();
	}

	public void manageOutRendering(LinkRendering nextLinkRenderer) {
		if (nextLinkRenderer == null) {
			return;
		}
		getLast().setOutRendering(nextLinkRenderer);
	}

}
