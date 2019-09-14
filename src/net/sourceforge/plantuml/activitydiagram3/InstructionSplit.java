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
package net.sourceforge.plantuml.activitydiagram3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;

public class InstructionSplit implements Instruction {

	private final List<InstructionList> splits = new ArrayList<InstructionList>();
	private final Instruction parent;
	private final LinkRendering inlinkRendering;
	private final Swimlane swimlaneIn;
	private Swimlane swimlaneOut;

	public InstructionSplit(Instruction parent, LinkRendering inlinkRendering, Swimlane swimlane) {
		this.parent = parent;
		this.swimlaneIn = swimlane;

		this.splits.add(new InstructionList(swimlane));
		this.inlinkRendering = inlinkRendering;
		if (inlinkRendering == null) {
			throw new IllegalArgumentException();
		}
	}

	public boolean containsBreak() {
		for (InstructionList split : splits) {
			if (split.containsBreak()) {
				return true;
			}
		}
		return false;
	}

	private InstructionList getLast() {
		return splits.get(splits.size() - 1);
	}

	public void add(Instruction ins) {
		getLast().add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		final List<Ftile> all = new ArrayList<Ftile>();
		for (InstructionList list : splits) {
			all.add(list.createFtile(factory));
		}
		return factory.createParallel(all, ForkStyle.SPLIT, null, swimlaneIn, swimlaneOut);
	}

	public Instruction getParent() {
		return parent;
	}

	public void splitAgain(LinkRendering inlinkRendering) {
		if (inlinkRendering != null) {
			getLast().setOutRendering(inlinkRendering);
		}
		final InstructionList list = new InstructionList(swimlaneIn);
		this.splits.add(list);
	}

	public void endSplit(LinkRendering inlinkRendering, Swimlane endSwimlane) {
		if (inlinkRendering != null) {
			getLast().setOutRendering(inlinkRendering);
		}
		this.swimlaneOut = endSwimlane;

	}

	final public boolean kill() {
		return getLast().kill();
	}

	public LinkRendering getInLinkRendering() {
		return inlinkRendering;
	}

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		return getLast().addNote(note, position, type, colors, swimlaneNote);
	}

	public Set<Swimlane> getSwimlanes() {
		return InstructionList.getSwimlanes2(splits);
	}

	public Swimlane getSwimlaneIn() {
		return parent.getSwimlaneOut();
	}

	public Swimlane getSwimlaneOut() {
		return swimlaneOut;
		// return getLast().getSwimlaneOut();
	}

}
