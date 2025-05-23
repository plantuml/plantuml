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
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileDecorateWelding;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.WeldingPoint;
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileIfAlone;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileIfHexagon;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.url.Url;

public class InstructionIf extends WithNote implements Instruction, InstructionCollection {

	private final List<Branch> thens = new ArrayList<>();
	private Branch elseBranch;
	private boolean endifCalled = false;
	private final ISkinParam skinParam;
	private final Url url;

	private final Instruction parent;

	private Branch current;
	private final LinkRendering topInlinkRendering;
	private LinkRendering outColor = LinkRendering.none();
	private final Stereotype stereotype;

	private final Swimlane swimlane;
	private final StyleBuilder currentStyleBuilder;

	@Override
	public boolean containsBreak() {
		for (Branch branch : thens)
			if (branch.containsBreak())
				return true;

		if (elseBranch != null)
			return elseBranch.containsBreak();

		return false;
	}

	public InstructionIf(Swimlane swimlane, Instruction parent, Display labelTest, LinkRendering whenThen,
			LinkRendering inlinkRendering, HColor color, ISkinParam skinParam, Url url, Stereotype stereotype) {
		this.url = url;
		this.stereotype = stereotype;
		this.parent = parent;
		this.skinParam = skinParam;
		this.topInlinkRendering = Objects.requireNonNull(inlinkRendering);
		this.swimlane = swimlane;
		this.currentStyleBuilder = skinParam.getCurrentStyleBuilder();
		this.thens.add(new Branch(currentStyleBuilder, swimlane, whenThen, labelTest, color, LinkRendering.none(),
				stereotype, skinParam.getIHtmlColorSet()));
		this.current = this.thens.get(0);
	}

	@Override
	public CommandExecutionResult add(Instruction ins) {
		return current.add(ins);
	}

	// ::comment when __CORE__
	@Override
	public Gtile createGtile(ISkinParam skinParam, StringBounder stringBounder) {
		for (Branch branch : thens)
			branch.updateGtile(skinParam, stringBounder);

		final List<Gtile> gtiles = new ArrayList<>();
		final List<Branch> branches = new ArrayList<>();
		for (Branch branch : thens) {
			gtiles.add(branch.getGtile());
			branches.add(branch);
		}
		if (elseBranch != null && elseBranch.isEmpty() == false) {
			elseBranch.updateGtile(skinParam, stringBounder);
			gtiles.add(elseBranch.getGtile());
			branches.add(elseBranch);
		}

		if (branches.size() == 1)
			return new GtileIfAlone(swimlane, gtiles.get(0), branches.get(0));
		return GtileIfHexagon.build(swimlane, gtiles, branches);
	}
	// ::done

	@Override
	public Ftile createFtile(FtileFactory factory) {
		for (Branch branch : thens)
			branch.updateFtile(factory);

		if (elseBranch == null)
			this.elseBranch = new Branch(currentStyleBuilder, swimlane, LinkRendering.none(), Display.NULL, null,
					LinkRendering.none(), stereotype, skinParam.getIHtmlColorSet());

		elseBranch.updateFtile(factory);
		Ftile result = factory.createIf(swimlane, thens, elseBranch, outColor, topInlinkRendering, url,
				getPositionedNotes(), stereotype, currentStyleBuilder);
//		if (getPositionedNotes().size() > 0)
//			result = FtileWithNoteOpale.create(result, getPositionedNotes(), false, VerticalAlignment.CENTER);

		final List<WeldingPoint> weldingPoints = new ArrayList<>();
		for (Branch branch : thens)
			weldingPoints.addAll(branch.getWeldingPoints());

		weldingPoints.addAll(elseBranch.getWeldingPoints());
		if (weldingPoints.size() > 0)
			result = new FtileDecorateWelding(result, weldingPoints);

		return result;
	}

	public Instruction getParent() {
		return parent;
	}

	public boolean swithToElse2(LinkRendering whenElse, LinkRendering nextLinkRenderer) {
		this.current.setSpecial(nextLinkRenderer);

		if (elseBranch != null)
			return false;

		this.current.setInlinkRendering(nextLinkRenderer);
		this.elseBranch = new Branch(skinParam.getCurrentStyleBuilder(), swimlane, whenElse, Display.NULL, null,
				LinkRendering.none(), stereotype, skinParam.getIHtmlColorSet());
		this.current = elseBranch;
		return true;
	}

	public boolean elseIf(LinkRendering inlabel, Display test, LinkRendering whenThen, LinkRendering nextLinkRenderer,
			HColor color) {
		if (elseBranch != null)
			return false;

		this.current.setSpecial(nextLinkRenderer);
		this.current = new Branch(skinParam.getCurrentStyleBuilder(), swimlane, whenThen, test, color, inlabel,
				stereotype, skinParam.getIHtmlColorSet());
		this.thens.add(current);
		return true;

	}

	public void endif(LinkRendering nextLinkRenderer) {
		endifCalled = true;
		if (elseBranch == null)
			this.elseBranch = new Branch(skinParam.getCurrentStyleBuilder(), swimlane, LinkRendering.none(),
					Display.NULL, null, LinkRendering.none(), stereotype, skinParam.getIHtmlColorSet());

		this.elseBranch.setSpecial(nextLinkRenderer);
		this.current.setInlinkRendering(nextLinkRenderer);
	}

	@Override
	final public boolean kill() {
		if (endifCalled)
			for (Branch branch : thens) {
				if (branch.getLast() != null && branch.getLast().kill() == false)
					return false;

				if (elseBranch != null && elseBranch.getLast() != null && elseBranch.getLast().kill() == false)
					return false;

				return true;
			}

		return current.kill();
	}

	@Override
	public LinkRendering getInLinkRendering() {
		return topInlinkRendering;
	}

	@Override
	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote,
			Stereotype stereotype) {
		if (endifCalled || current.isEmpty())
			return super.addNote(note, position, type, colors, swimlaneNote, stereotype);
		else
			return current.addNote(note, position, type, colors, swimlaneNote, stereotype);

	}

	@Override
	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<>();
		if (swimlane != null)
			result.add(swimlane);

		for (Branch branch : thens)
			result.addAll(branch.getSwimlanes());

		if (elseBranch != null)
			result.addAll(elseBranch.getSwimlanes());

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
		if (elseBranch == null)
			return thens.get(thens.size() - 1).getLast();

		return elseBranch.getLast();
	}

	public void outColor(LinkRendering outColor) {
		this.outColor = outColor;
	}

}
