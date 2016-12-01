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

import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;

public class InstructionGroup implements Instruction, InstructionCollection {

	private final InstructionList list;
	private final Instruction parent;
	private final HtmlColor backColor;
	private final HtmlColor borderColor;
	private final HtmlColor titleColor;
	private final LinkRendering linkRendering;

	private final Display test;
	private PositionedNote note = null;

	public InstructionGroup(Instruction parent, Display test, HtmlColor backColor, HtmlColor titleColor,
			Swimlane swimlane, HtmlColor borderColor, LinkRendering linkRendering) {
		this.list = new InstructionList(swimlane);
		this.linkRendering = linkRendering;
		this.parent = parent;
		this.test = test;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.titleColor = titleColor;
	}

	public void add(Instruction ins) {
		list.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		return factory.createGroup(list.createFtile(factory), test, backColor, titleColor, note, borderColor);
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

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors) {
		if (list.isEmpty()) {
			this.note = new PositionedNote(note, position, type, colors);
			return true;
		}
		return list.addNote(note, position, type, colors);
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
