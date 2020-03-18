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

import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileWithNotes;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class InstructionGroup implements Instruction, InstructionCollection {

	private final InstructionList list;
	private final Instruction parent;
	private final HColor backColor;
	private final HColor borderColor;
	private final HColor titleColor;
	private final LinkRendering linkRendering;
	private final USymbol type;

	private final Display test;
	private final double roundCorner;
	private PositionedNote note = null;

	public boolean containsBreak() {
		return list.containsBreak();
	}

	public InstructionGroup(Instruction parent, Display test, HColor backColor, HColor titleColor,
			Swimlane swimlane, HColor borderColor, LinkRendering linkRendering, USymbol type, double roundCorner) {
		this.list = new InstructionList(swimlane);
		this.type = type;
		this.linkRendering = linkRendering;
		this.parent = parent;
		this.test = test;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.titleColor = titleColor;
		this.roundCorner = roundCorner;
	}

	public void add(Instruction ins) {
		list.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		Ftile tmp = list.createFtile(factory);
		if (note != null) {
			tmp = new FtileWithNotes(tmp, Collections.singleton(note), factory.skinParam());
		}
		return factory.createGroup(tmp, test, backColor, titleColor, null, borderColor, type, roundCorner);
	}

	public Instruction getParent() {
		return parent;
	}

	final public boolean kill() {
		return list.kill();
	}

	public LinkRendering getInLinkRendering() {
		return linkRendering;
	}

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		if (list.isEmpty()) {
			this.note = new PositionedNote(note, position, type, colors, swimlaneNote);
			return true;
		}
		return list.addNote(note, position, type, colors, swimlaneNote);
	}

	public Set<Swimlane> getSwimlanes() {
		return list.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return list.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return list.getSwimlaneOut();
	}

	public Instruction getLast() {
		return list.getLast();
	}

}
