/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public class InstructionIf1 implements Instruction {

	private final List<Branch> thens = new ArrayList<Branch>();
	private Branch elseBranch;

	private final Instruction parent;

	private Branch current;
	private final LinkRendering inlinkRendering;

	private final Swimlane swimlane;

	public InstructionIf1(Swimlane swimlane, Instruction parent, Display labelTest, Display whenThen,
			LinkRendering inlinkRendering) {
		this.parent = parent;

		this.inlinkRendering = inlinkRendering;
		this.swimlane = swimlane;
		this.thens.add(new Branch(swimlane, whenThen, labelTest));
		this.current = this.thens.get(0);
	}

	public void add(Instruction ins) {
		current.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		for (Branch branch : thens) {
			branch.updateFtile(factory);
		}
		if (elseBranch == null) {
			this.elseBranch = new Branch(swimlane, null, null);
		}
		elseBranch.updateFtile(factory);
		return factory.createIf(swimlane, thens, elseBranch);
	}

	public Instruction getParent() {
		return parent;
	}

	public void swithToElse(Display whenElse, LinkRendering nextLinkRenderer) {
		if (elseBranch != null) {
			throw new IllegalStateException();
		}
		this.current.setInlinkRendering(nextLinkRenderer);
		this.elseBranch = new Branch(swimlane, whenElse, null);
		// this.elseBranch.setLabelPositive(whenElse);
		this.current = elseBranch;
	}

	public void elseIf(Display test, Display whenThen, LinkRendering nextLinkRenderer) {
		if (elseBranch != null) {
			throw new IllegalStateException();
		}
		this.current.setInlinkRendering(nextLinkRenderer);
		this.current = new Branch(swimlane, whenThen, test);
		this.thens.add(current);

	}

	public void endif(LinkRendering nextLinkRenderer) {
		if (elseBranch == null) {
			this.elseBranch = new Branch(swimlane, null, null);
		}
		this.current.setInlinkRendering(nextLinkRenderer);
	}

	final public boolean kill() {
		return current.kill();
	}

	public LinkRendering getInLinkRendering() {
		return inlinkRendering;
	}

	public void addNote(Display note, NotePosition position) {
		current.addNote(note, position);
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		if (swimlane != null) {
			result.add(swimlane);
		}
		for (Branch branch : thens) {
			result.addAll(branch.getSwimlanes());
		}
		result.addAll(elseBranch.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

}
