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

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileKilled;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileWithNoteOpale;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class InstructionWhile extends WithNote implements Instruction, InstructionCollection {

	private final InstructionList repeatList = new InstructionList();
	private final Instruction parent;
	private final LinkRendering nextLinkRenderer;
	private final HColor color;
	private boolean killed = false;

	private final Display test;
	private Display yes;
	private Display out = Display.NULL;
	private boolean testCalled = false;
	private LinkRendering endInlinkRendering = LinkRendering.none();
	private LinkRendering afterEndwhile = LinkRendering.none();
	private final Swimlane swimlane;
	private final ISkinParam skinParam;

	private Instruction specialOut;

	public void overwriteYes(Display yes) {
		this.yes = yes;
	}

	public InstructionWhile(Swimlane swimlane, Instruction parent, Display test, LinkRendering nextLinkRenderer,
			Display yes, HColor color, ISkinParam skinParam) {
		if (test == null) {
			throw new IllegalArgumentException();
		}
		if (yes == null) {
			throw new IllegalArgumentException();
		}
		this.parent = parent;
		this.test = test;
		this.nextLinkRenderer = nextLinkRenderer;
		if (nextLinkRenderer == null) {
			throw new IllegalArgumentException();
		}
		this.yes = yes;
		this.swimlane = swimlane;
		this.color = color;
		this.skinParam = skinParam;
	}

	public void add(Instruction ins) {
		repeatList.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		final Ftile back = Display.isNull(backward) ? null
				: factory.activity(backward, swimlane, boxStyle, Colors.empty());
		Ftile tmp = factory.decorateOut(repeatList.createFtile(factory), endInlinkRendering);
		tmp = factory.createWhile(swimlane, tmp, test, yes, out, afterEndwhile, color, specialOut, back);
		if (getPositionedNotes().size() > 0) {
			tmp = FtileWithNoteOpale.create(tmp, getPositionedNotes(), skinParam, false);
		}
		if (killed || specialOut != null) {
			return new FtileKilled(tmp);
		}
		return tmp;
	}

	public Instruction getParent() {
		return parent;
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

	public void endwhile(LinkRendering nextLinkRenderer, Display out) {
		this.endInlinkRendering = nextLinkRenderer;
		this.out = out;
		if (out == null) {
			throw new IllegalArgumentException();
		}
		this.testCalled = true;
	}

	public void afterEndwhile(LinkRendering linkRenderer) {
		this.afterEndwhile = linkRenderer;
	}

	@Override
	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		if (repeatList.isEmpty()) {
			return super.addNote(note, position, type, colors, swimlaneNote);
		} else {
			return repeatList.addNote(note, position, type, colors, swimlaneNote);
		}
	}

	public Set<Swimlane> getSwimlanes() {
		return repeatList.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return parent.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return parent.getSwimlaneOut();
	}

	public Instruction getLast() {
		return repeatList.getLast();
	}

	public void setSpecial(Instruction special) {
		this.specialOut = special;
	}

	public boolean containsBreak() {
		return repeatList.containsBreak();
	}

	private BoxStyle boxStyle;
	private Swimlane swimlaneOut;
	private Display backward = Display.NULL;

	public void setBackward(Display label, Swimlane swimlaneOut, BoxStyle boxStyle) {
		this.backward = label;
		this.swimlaneOut = swimlaneOut;
		this.boxStyle = boxStyle;
	}

}
