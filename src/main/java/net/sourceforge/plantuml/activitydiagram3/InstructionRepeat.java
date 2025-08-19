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
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileKilled;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlanes;
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileBox;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileRepeat;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.StyleBuilder;

public class InstructionRepeat extends AbstractInstruction implements Instruction {

	private final InstructionList repeatList;
	private final Instruction parent;
	private final LinkRendering nextLinkRenderer;
	private final Swimlane swimlane;
	private final Swimlanes swimlanes;
	private Swimlane swimlaneOut;
	private Swimlane swimlaneBackward;
	private BoxStyle boxStyle;
	private boolean killed = false;
	private final BoxStyle boxStyleIn;

	private Display backward = Display.NULL;

	private Stereotype stereotypeLoop;
	private Stereotype stereotypeBack;
	private LinkRendering incoming1 = LinkRendering.none();
	private LinkRendering incoming2 = LinkRendering.none();
	private List<PositionedNote> backwardNotes = new ArrayList<>();
	private Display test = Display.NULL;
	private Display yes = Display.NULL;
	private Display out = Display.NULL;
	private final Display startLabel;
	private boolean testCalled = false;
	private LinkRendering endRepeatLinkRendering = LinkRendering.none();

	private final Colors colors;
	private final StyleBuilder currentStyleBuilder;

	public boolean containsBreak() {
		return repeatList.containsBreak();
	}

	public InstructionRepeat(Swimlanes swimlanes, Instruction parent, LinkRendering nextLinkRenderer, HColor color,
			Display startLabel, BoxStyle boxStyleIn, Colors colors, Stereotype stereotype) {
		this.currentStyleBuilder = swimlanes.getCurrentStyleBuilder();
		this.swimlanes = swimlanes;
		this.swimlane = swimlanes.getCurrentSwimlane();
		this.repeatList = new InstructionList(this.swimlane);
		this.boxStyleIn = boxStyleIn;
		this.startLabel = startLabel;
		this.parent = parent;
		this.nextLinkRenderer = Objects.requireNonNull(nextLinkRenderer);
		this.colors = colors;
		this.stereotypeLoop = stereotype;
	}

	private boolean isLastOfTheParent() {
		if (parent instanceof InstructionList)
			return ((InstructionList) parent).getLast() == this;

		return false;
	}

	public void setBackward(Display label, Swimlane swimlaneBackward, BoxStyle boxStyle, LinkRendering incoming1,
			LinkRendering incoming2, Stereotype stereotype) {
		this.backward = label;
		this.swimlaneBackward = swimlaneBackward;
		this.boxStyle = boxStyle;
		this.incoming1 = incoming1;
		this.incoming2 = incoming2;
		this.stereotypeBack = stereotype;
	}

	public boolean hasBackward() {
		return this.backward != Display.NULL;
	}

	@Override
	public CommandExecutionResult add(Instruction ins) {
		return repeatList.add(ins);
	}

	// ::comment when __CORE__
	@Override
	public Gtile createGtile(ISkinParam skinParam, StringBounder stringBounder) {

		final Gtile tile = repeatList.createGtile(skinParam, stringBounder);
		final Gtile backward = getGtileBackward(skinParam, stringBounder);

		return new GtileRepeat(swimlane, tile, null, test, backward);
	}

	private Gtile getGtileBackward(ISkinParam skinParam, StringBounder stringBounder) {
		if (Display.isNull(backward))
			return null;

		GtileBox result = GtileBox.create(stringBounder, skinParam, backward, getSwimlaneIn(), boxStyle, null);
//		if (backwardNotes.size() > 0) {
//			result = factory.addNote(result, swimlaneOut, backwardNotes);
//		}
		return result;
	}
	// ::done

	public Ftile createFtile(FtileFactory factory) {
		final Ftile back = getFtileBackward(factory);
		Ftile tmp = repeatList.createFtile(factory);
		tmp = factory.decorateOut(tmp, endRepeatLinkRendering);
		if (this.testCalled == false && incoming1.isNone())
			incoming1 = swimlanes.nextLinkRenderer();
		tmp = factory.repeat(boxStyleIn, stereotypeLoop, swimlane, swimlaneOut, startLabel, tmp, test,
				yes, out, colors, back, isLastOfTheParent(), incoming1, incoming2, currentStyleBuilder);
		tmp = FtileUtils.withSwimlaneIn(tmp, swimlane);
		if (killed)
			return new FtileKilled(tmp);

		return tmp;
	}

	private Ftile getFtileBackward(FtileFactory factory) {
		if (Display.isNull(backward))
			return null;

		Ftile result = factory.activity(backward, swimlaneBackward, boxStyle, Colors.empty(), stereotypeBack, null);
		if (backwardNotes.size() > 0)
			result = factory.addNote(result, swimlaneBackward, backwardNotes, VerticalAlignment.CENTER);

		return result;
	}

	public Instruction getParent() {
		return parent;
	}

	public void setTest(Display test, Display yes, Display out, LinkRendering endRepeatLinkRendering,
			LinkRendering back, Swimlane swimlaneOut) {
		this.swimlaneOut = swimlaneOut;
		this.test = Objects.requireNonNull(test);
		this.yes = Objects.requireNonNull(yes);
		this.out = Objects.requireNonNull(out);
		this.endRepeatLinkRendering = endRepeatLinkRendering;
		if (back.isNone() == false)
			this.incoming1 = back;
		this.testCalled = true;
	}

	final public boolean kill() {
		if (testCalled) {
			this.killed = true;
			return true;
		}
		return repeatList.kill();
	}

	@Override
	public LinkRendering getInLinkRendering() {
		return nextLinkRenderer;
	}

	@Override
	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote, Stereotype stereotype) {
		if (Display.isNull(backward))
			return repeatList.addNote(note, position, type, colors, swimlaneNote, stereotype);

		this.backwardNotes.add(new PositionedNote(note, position, type, swimlaneNote, colors, stereotype));
		return true;

	}

	@Override
	public Set<Swimlane> getSwimlanes() {
		return repeatList.getSwimlanes();
	}

	@Override
	public Swimlane getSwimlaneIn() {
		return parent.getSwimlaneOut();
	}

	@Override
	public Swimlane getSwimlaneOut() {
		return parent.getSwimlaneOut();
	}

}
