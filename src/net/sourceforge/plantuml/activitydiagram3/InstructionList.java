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
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileDecorateWelding;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.WeldingPoint;
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileAssembly;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileEmpty;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.style.ISkinParam;

public class InstructionList extends WithNote implements Instruction, InstructionCollection {

	private final List<Instruction> all = new ArrayList<>();
	private final Swimlane defaultSwimlane;

	@Override
	public boolean containsBreak() {
		for (Instruction ins : all)
			if (ins.containsBreak())
				return true;

		return false;
	}

	public static InstructionList empty() {
		return new InstructionList(null);
	}

	public InstructionList(Swimlane defaultSwimlane) {
		this.defaultSwimlane = defaultSwimlane;
	}

	public boolean isEmpty() {
		return all.isEmpty();
	}

	public boolean isOnlySingleStopOrSpot() {
		if (all.size() != 1)
			return false;

		if (getLast() instanceof InstructionSpot)
			return true;

		if (getLast() instanceof InstructionStop)
			return ((InstructionStop) getLast()).hasNotes() == false;

		if (getLast() instanceof InstructionEnd)
			return ((InstructionEnd) getLast()).hasNotes() == false;

		return false;
	}

	@Override
	public CommandExecutionResult add(Instruction ins) {
		all.add(ins);
		return CommandExecutionResult.ok();
	}

	// ::comment when __CORE__
	@Override
	public Gtile createGtile(ISkinParam skinParam, StringBounder stringBounder) {
		if (all.size() == 0)
			return new GtileEmpty(stringBounder, skinParam, defaultSwimlane);

		Gtile result = null;
		for (Instruction ins : all) {
			final Gtile cur = ins.createGtile(skinParam, stringBounder);

			if (result == null)
				result = cur;
			else
				result = new GtileAssembly(result, cur, ins.getInLinkRendering());

		}
		return result;
	}
	// ::done

	@Override
	public Ftile createFtile(FtileFactory factory) {
		if (all.size() == 0) {
			Ftile result = new FtileEmpty(factory.skinParam(), defaultSwimlane);
			// Not a typo, in that case, we decide to decorate the entry link.
			if (outlinkRendering != null)
				result = factory.decorateIn(result, outlinkRendering);
			return result;
		}

		final List<WeldingPoint> breaks = new ArrayList<>();
		Ftile result = eventuallyAddNote(factory, null, getSwimlaneIn(), VerticalAlignment.CENTER);
		for (Instruction ins : all) {
			Ftile cur = ins.createFtile(factory);
			breaks.addAll(cur.getWeldingPoints());
			if (ins.getInLinkRendering().isNone() == false)
				cur = factory.decorateIn(cur, ins.getInLinkRendering());

			if (result == null)
				result = cur;
			else
				result = factory.assembly(result, cur);
		}

		if (outlinkRendering != null)
			result = factory.decorateOut(result, outlinkRendering);

		if (breaks.size() > 0)
			result = new FtileDecorateWelding(result, breaks);

		return result;
	}

	@Override
	final public boolean kill() {
		if (all.size() == 0)
			return false;

		return getLast().kill();
	}

	@Override
	public LinkRendering getInLinkRendering() {
		return all.iterator().next().getInLinkRendering();
	}

	@Override
	public Instruction getLast() {
		if (all.size() == 0)
			return null;

		return all.get(all.size() - 1);
	}

	@Override
	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		if (getLast() == null)
			return super.addNote(note, position, type, colors, swimlaneNote);

		return getLast().addNote(note, position, type, colors, swimlaneNote);
	}

	@Override
	public Set<Swimlane> getSwimlanes() {
		return getSwimlanes2(all);
	}

	@Override
	public Swimlane getSwimlaneIn() {
		return defaultSwimlane;
	}

	@Override
	public Swimlane getSwimlaneOut() {
		final Set<Swimlane> swimlanes = getSwimlanes();
		if (swimlanes.size() == 0)
			return null;

		if (swimlanes.size() == 1)
			return swimlanes.iterator().next();

		return getLast().getSwimlaneOut();
	}

	public static Set<Swimlane> getSwimlanes2(List<? extends Instruction> list) {
		final Set<Swimlane> result = new HashSet<>();
		for (Instruction ins : list)
			result.addAll(ins.getSwimlanes());

		return Collections.unmodifiableSet(result);
	}

	private LinkRendering outlinkRendering;

	public void setOutRendering(LinkRendering outlinkRendering) {
		this.outlinkRendering = outlinkRendering;
	}

}
