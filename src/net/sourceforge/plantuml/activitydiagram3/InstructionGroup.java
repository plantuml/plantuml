/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileGroup;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

public class InstructionGroup extends AbstractInstruction implements Instruction, InstructionCollection {

	private final InstructionList list;
	private final Instruction parent;
	private final HColor backColor;
	private final LinkRendering linkRendering;
	private final USymbol type;

	private final Display title;
	private PositionedNote note = null;
	private final Style style;

	@Override
	public boolean containsBreak() {
		return list.containsBreak();
	}

	public InstructionGroup(Instruction parent, Display title, HColor backColor, Swimlane swimlane,
			LinkRendering linkRendering, USymbol type, Style style) {
		this.list = new InstructionList(swimlane);
		this.type = type;
		this.linkRendering = linkRendering;
		this.parent = parent;
		this.title = title;
		this.style = style;
		this.backColor = backColor;
	}

	@Override
	public CommandExecutionResult add(Instruction ins) {
		return list.add(ins);
	}

	// ::comment when __CORE__
	@Override
	public Gtile createGtile(ISkinParam skinParam, StringBounder stringBounder) {
		Gtile tmp = list.createGtile(skinParam, stringBounder);
		return new GtileGroup(tmp, title, null, HColors.BLUE, backColor, tmp.skinParam(), type, style);
	}
	// ::done

	@Override
	public Ftile createFtile(FtileFactory factory) {
		Ftile tmp = list.createFtile(factory);
		if (note != null)
			tmp = new FtileWithNotes(tmp, Collections.singleton(note), VerticalAlignment.CENTER);

		return factory.createGroup(tmp, title, backColor, null, type, style);
	}

	public Instruction getParent() {
		return parent;
	}

	@Override
	final public boolean kill() {
		return list.kill();
	}

	@Override
	public LinkRendering getInLinkRendering() {
		return linkRendering;
	}

	@Override
	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		if (list.isEmpty()) {
			this.note = new PositionedNote(note, position, type, swimlaneNote, colors);
			return true;
		}
		return list.addNote(note, position, type, colors, swimlaneNote);
	}

	@Override
	public Set<Swimlane> getSwimlanes() {
		return list.getSwimlanes();
	}

	@Override
	public Swimlane getSwimlaneIn() {
		return list.getSwimlaneIn();
	}

	@Override
	public Swimlane getSwimlaneOut() {
		return list.getSwimlaneOut();
	}

	@Override
	public Instruction getLast() {
		return list.getLast();
	}

}
