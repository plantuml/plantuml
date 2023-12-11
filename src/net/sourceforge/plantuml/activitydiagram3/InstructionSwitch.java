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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileIfHexagon;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.style.ISkinParam;

public class InstructionSwitch extends WithNote implements Instruction, InstructionCollection {
	// ::remove folder when __HAXE__

	private final List<Branch> switches = new ArrayList<>();
	private final ISkinParam skinParam;

	private final Instruction parent;

	private Branch current;
	private final LinkRendering topInlinkRendering;
	private LinkRendering afterEndwhile = LinkRendering.none();
	private final Display labelTest;

	private final Swimlane swimlane;

	@Override
	public boolean containsBreak() {
		for (Branch branch : switches)
			if (branch.containsBreak())
				return true;

		return false;
	}

	public InstructionSwitch(Swimlane swimlane, Instruction parent, Display labelTest, LinkRendering inlinkRendering,
			HColor color, ISkinParam skinParam) {
		this.topInlinkRendering = Objects.requireNonNull(inlinkRendering);
		this.parent = parent;
		this.skinParam = skinParam;
		this.labelTest = labelTest;
		this.swimlane = swimlane;
	}

	@Override
	public CommandExecutionResult add(Instruction ins) {
		if (current == null)
			return CommandExecutionResult.error("No 'case' in this switch");

		return current.add(ins);
	}

	// ::comment when __CORE__
	@Override
	public Gtile createGtile(ISkinParam skinParam, StringBounder stringBounder) {
		for (Branch branch : switches)
			branch.updateGtile(skinParam, stringBounder);

		final List<Gtile> gtiles = new ArrayList<>();
		final List<Branch> branches = new ArrayList<>();
		for (Branch branch : switches) {
			gtiles.add(branch.getGtile());
			branches.add(branch);
		}

		return GtileIfHexagon.build(swimlane, gtiles, switches);
	}
	// ::done

	public Ftile createFtile(FtileFactory factory) {
		for (Branch branch : switches)
			branch.updateFtile(factory);

		Ftile result = factory.createSwitch(swimlane, switches, afterEndwhile, topInlinkRendering, labelTest);
		result = eventuallyAddNote(factory, result, getSwimlaneIn(), VerticalAlignment.TOP);
		return result;
	}

	@Override
	final public boolean kill() {
		return current.kill();
	}

	@Override
	public LinkRendering getInLinkRendering() {
		return topInlinkRendering;
	}

	@Override
	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<>();
		if (swimlane != null)
			result.add(swimlane);

		for (Branch branch : switches)
			result.addAll(branch.getSwimlanes());

		return Collections.unmodifiableSet(result);
	}

	@Override
	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	@Override
	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	@Override
	public Instruction getLast() {
		return switches.get(switches.size() - 1).getLast();
	}

	public boolean switchCase(Display labelCase, LinkRendering nextLinkRenderer) {
		if (this.current != null)
			this.current.setSpecial(nextLinkRenderer);
		this.current = new Branch(skinParam.getCurrentStyleBuilder(), swimlane,
				LinkRendering.none().withDisplay(labelCase), labelCase, null,
				LinkRendering.none().withDisplay(labelCase), null);
		this.switches.add(this.current);
		return true;
	}

	public Instruction getParent() {
		return parent;
	}

	public void endSwitch(LinkRendering nextLinkRenderer) {
		if (this.current != null)
			this.current.setSpecial(nextLinkRenderer);
	}

	@Override
	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		if (current == null || current.isEmpty())
			return super.addNote(note, position, type, colors, swimlaneNote);
		else
			return current.addNote(note, position, type, colors, swimlaneNote);

	}

}
