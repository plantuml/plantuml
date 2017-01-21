/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.activitydiagram3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileWithNoteOpale;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;

public class InstructionFork extends WithNote implements Instruction {

	private final List<InstructionList> forks = new ArrayList<InstructionList>();
	private final Instruction parent;
	private final LinkRendering inlinkRendering;
	private final ISkinParam skinParam;
	private ForkStyle style = ForkStyle.FORK;
	private String label;
	boolean finished = false;

	public InstructionFork(Instruction parent, LinkRendering inlinkRendering, ISkinParam skinParam) {
		this.parent = parent;
		this.inlinkRendering = inlinkRendering;
		this.skinParam = skinParam;
		this.forks.add(new InstructionList());
		if (inlinkRendering == null) {
			throw new IllegalArgumentException();
		}
	}

	private InstructionList getLastList() {
		return forks.get(forks.size() - 1);
	}

	public void add(Instruction ins) {
		getLastList().add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		final List<Ftile> all = new ArrayList<Ftile>();
		for (InstructionList list : forks) {
			all.add(list.createFtile(factory));
		}
		Ftile result = factory.createParallel(getSwimlaneIn(), all, style, label);
		if (getPositionedNotes().size() > 0) {
			result = FtileWithNoteOpale.create(result, getPositionedNotes(), skinParam, false);
		}
		return result;
	}

	public Instruction getParent() {
		return parent;
	}

	public void forkAgain() {
		this.forks.add(new InstructionList());
	}

	final public boolean kill() {
		return getLastList().kill();
	}

	public LinkRendering getInLinkRendering() {
		return inlinkRendering;
	}

	@Override
	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors) {
		if (finished) {
			return super.addNote(note, position, type, colors);
		}
		if (getLastList().getLast() == null) {
			return getLastList().addNote(note, position, type, colors);
		}
		return getLastList().addNote(note, position, type, colors);
	}

	public Set<Swimlane> getSwimlanes() {
		return InstructionList.getSwimlanes2(forks);
	}

	public Swimlane getSwimlaneIn() {
		// return parent.getSwimlaneOut();
		return forks.get(0).getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return forks.get(0).getSwimlaneOut();
		// return getLastList().getSwimlaneOut();
	}

	public void manageOutRendering(LinkRendering nextLinkRenderer, boolean endFork) {
		if (endFork) {
			this.finished = true;
		}
		if (nextLinkRenderer == null) {
			return;
		}
		getLastList().setOutRendering(nextLinkRenderer);
	}

	public void setStyle(ForkStyle style, String label) {
		this.style = style;
		this.label = label;
	}

}
