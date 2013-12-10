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

import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public class InstructionWhile implements Instruction {

	private final InstructionList repeatList = new InstructionList();
	private final Instruction parent;
	private final LinkRendering nextLinkRenderer;

	private final Display test;
	private final Display yes;
	private Display out;
	private LinkRendering endInlinkRendering;
	private LinkRendering afterEndwhile;
	private final Swimlane swimlane;

	public InstructionWhile(Swimlane swimlane, Instruction parent, Display test, LinkRendering nextLinkRenderer, Display yes) {
		this.parent = parent;
		this.test = test;
		this.nextLinkRenderer = nextLinkRenderer;
		this.yes = yes;
		this.swimlane = swimlane;
	}

	public void add(Instruction ins) {
		repeatList.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		Ftile tmp = factory.decorateOut(repeatList.createFtile(factory), endInlinkRendering);
		tmp = factory.createWhile(swimlane, tmp, test, yes, out, afterEndwhile);
		// tmp = factory.decorateOut(tmp, afterEndwhile);
		return tmp;
	}

	public Instruction getParent() {
		return parent;
	}

	final public boolean kill() {
		return repeatList.kill();
	}

	public LinkRendering getInLinkRendering() {
		return nextLinkRenderer;
	}

	public void endwhile(LinkRendering nextLinkRenderer, Display out) {
		this.endInlinkRendering = nextLinkRenderer;
		this.out = out;
	}

	public void afterEndwhile(LinkRendering linkRenderer) {
		this.afterEndwhile = linkRenderer;
	}

	public void addNote(Display note, NotePosition position) {
		repeatList.addNote(note, position);
	}
	
	public Set<Swimlane> getSwimlanes() {
		return repeatList.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return parent.getSwimlaneOut();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}



}
