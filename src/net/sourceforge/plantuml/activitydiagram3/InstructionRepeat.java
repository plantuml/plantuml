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
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileKilled;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public class InstructionRepeat implements Instruction {

	private final InstructionList repeatList = new InstructionList();
	private final Instruction parent;
	private final LinkRendering nextLinkRenderer;
	private final Swimlane swimlane;
	private final HtmlColor color;
	private boolean killed = false;

	private Display test = Display.NULL;
	private Display yes = Display.NULL;
	private Display out = Display.NULL;
	private boolean testCalled = false;
	private LinkRendering endRepeatLinkRendering = LinkRendering.none();
	private LinkRendering backRepeatLinkRendering = LinkRendering.none();

	public InstructionRepeat(Swimlane swimlane, Instruction parent, LinkRendering nextLinkRenderer, HtmlColor color) {
		this.parent = parent;
		this.swimlane = swimlane;
		this.nextLinkRenderer = nextLinkRenderer;
		if (nextLinkRenderer == null) {
			throw new IllegalArgumentException();
		}
		this.color = color;
	}

	public void add(Instruction ins) {
		repeatList.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		final Ftile result = factory.repeat(swimlane, repeatList.getSwimlaneOut(),
				factory.decorateOut(repeatList.createFtile(factory), endRepeatLinkRendering), test, yes, out, color,
				backRepeatLinkRendering);
		if (killed) {
			return new FtileKilled(result);
		}
		return result;
	}

	public Instruction getParent() {
		return parent;
	}

	public void setTest(Display test, Display yes, Display out, LinkRendering endRepeatLinkRendering,
			LinkRendering backRepeatLinkRendering) {
		this.test = test;
		this.yes = yes;
		this.out = out;
		if (test == null) {
			throw new IllegalArgumentException();
		}
		if (yes == null) {
			throw new IllegalArgumentException();
		}
		if (out == null) {
			throw new IllegalArgumentException();
		}
		this.endRepeatLinkRendering = endRepeatLinkRendering;
		this.backRepeatLinkRendering = backRepeatLinkRendering;
		this.testCalled = true;
	}

	final public boolean kill() {
		if (testCalled) {
			this.killed = true;
			return true;
		}
		return repeatList.kill();
	}

	public LinkRendering getInLinkRendering() {
		return nextLinkRenderer;
	}

	public boolean addNote(Display note, NotePosition position) {
		return repeatList.addNote(note, position);
	}

	public Set<Swimlane> getSwimlanes() {
		return repeatList.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return parent.getSwimlaneOut();
	}

	public Swimlane getSwimlaneOut() {
		return repeatList.getSwimlaneOut();
	}

}
