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

import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileKilled;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class InstructionRepeat implements Instruction {

	private final InstructionList repeatList = new InstructionList();
	private final Instruction parent;
	private final LinkRendering nextLinkRenderer;
	private final Swimlane swimlane;
	private Swimlane swimlaneOut;
	private BoxStyle boxStyle;
	private boolean killed = false;
	private final BoxStyle boxStyleIn;

	private Display backward = Display.NULL;
	private Display test = Display.NULL;
	private Display yes = Display.NULL;
	private Display out = Display.NULL;
	private final Display startLabel;
	private boolean testCalled = false;
	private LinkRendering endRepeatLinkRendering = LinkRendering.none();
	private LinkRendering backRepeatLinkRendering = LinkRendering.none();
	private final Colors colors;

	public boolean containsBreak() {
		return repeatList.containsBreak();
	}

	public InstructionRepeat(Swimlane swimlane, Instruction parent, LinkRendering nextLinkRenderer, HColor color,
			Display startLabel, BoxStyle boxStyleIn, Colors colors) {
		this.boxStyleIn = boxStyleIn;
		this.startLabel = startLabel;
		this.parent = parent;
		this.swimlane = swimlane;
		this.nextLinkRenderer = nextLinkRenderer;
		if (nextLinkRenderer == null) {
			throw new IllegalArgumentException();
		}
		this.colors = colors;
	}

	private boolean isLastOfTheParent() {
		if (parent instanceof InstructionList) {
			return ((InstructionList) parent).getLast() == this;
		}
		return false;
	}

	public void setBackward(Display label, Swimlane swimlaneOut, BoxStyle boxStyle) {
		this.backward = label;
		this.swimlaneOut = swimlaneOut;
		this.boxStyle = boxStyle;
	}

	public void add(Instruction ins) {
		repeatList.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		final Ftile back = Display.isNull(backward) ? null
				: factory.activity(backward, swimlane, boxStyle, Colors.empty());
		final Ftile decorateOut = factory.decorateOut(repeatList.createFtile(factory), endRepeatLinkRendering);
		final Ftile result = factory.repeat(boxStyleIn, swimlane, swimlaneOut, startLabel, decorateOut, test, yes, out,
				colors, backRepeatLinkRendering, back, isLastOfTheParent());
		if (killed) {
			return new FtileKilled(result);
		}
		return result;
	}

	public Instruction getParent() {
		return parent;
	}

	public void setTest(Display test, Display yes, Display out, LinkRendering endRepeatLinkRendering,
			LinkRendering backRepeatLinkRendering, Swimlane swimlaneOut) {
		this.swimlaneOut = swimlaneOut;
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

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		return repeatList.addNote(note, position, type, colors, swimlaneNote);
	}

	public Set<Swimlane> getSwimlanes() {
		return repeatList.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return parent.getSwimlaneOut();
	}

	public Swimlane getSwimlaneOut() {
		return parent.getSwimlaneOut();
	}

}
