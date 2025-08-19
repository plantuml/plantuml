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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlanes;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.compress.CompressionMode;
import net.sourceforge.plantuml.klimt.compress.CompressionXorYBuilder;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockRecentred;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.url.Url;

public class ActivityDiagram3 extends UmlDiagram {

	enum SwimlaneStrategy {
		SWIMLANE_FORBIDDEN, SWIMLANE_ALLOWED;
	}

	private SwimlaneStrategy swimlaneStrategy;

	private final Swimlanes swimlanes = new Swimlanes(getSkinParam(), getPragma());

	public ActivityDiagram3(UmlSource source, Previous previous, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.ACTIVITY, previous, preprocessing);
	}

	private void manageSwimlaneStrategy() {
		if (swimlaneStrategy == null)
			swimlaneStrategy = SwimlaneStrategy.SWIMLANE_FORBIDDEN;

	}

	public CommandExecutionResult swimlane(String name, HColor color, Display label) {
		if (swimlaneStrategy == null)
			swimlaneStrategy = SwimlaneStrategy.SWIMLANE_ALLOWED;

		if (swimlaneStrategy == SwimlaneStrategy.SWIMLANE_FORBIDDEN)
			return CommandExecutionResult.error("This swimlane must be defined at the start of the diagram.");

		swimlanes.swimlane(name, color, label);
		return CommandExecutionResult.ok();
	}

	private void setCurrent(Instruction ins) {
		swimlanes.setCurrent(ins);
	}

	private Instruction current() {
		return swimlanes.getCurrent();
	}

	private LinkRendering nextLinkRenderer() {
		return swimlanes.nextLinkRenderer();
	}

	public CommandExecutionResult addActivity(Display activity, BoxStyle boxStyle, Url url, Colors colors,
			Stereotype stereotype) {
		manageSwimlaneStrategy();
		final InstructionSimple ins = new InstructionSimple(activity, nextLinkRenderer(),
				swimlanes.getCurrentSwimlane(), boxStyle, url, colors, stereotype, getCurrentStyleBuilder());
		final CommandExecutionResult added = current().add(ins);
		if (added.isOk() == false)
			return added;

		setNextLinkRendererInternal(LinkRendering.none());
		manageHasUrl(activity);
		if (url != null)
			hasUrl = true;

		return CommandExecutionResult.ok();

	}

	public void addSpot(String spot, HColor color) {
		final InstructionSpot ins = new InstructionSpot(spot, color, nextLinkRenderer(),
				swimlanes.getCurrentSwimlane());
		current().add(ins);
		setNextLinkRendererInternal(LinkRendering.none());
		manageSwimlaneStrategy();
	}

	public CommandExecutionResult addGoto(String name) {
		final InstructionGoto ins = new InstructionGoto(swimlanes.getCurrentSwimlane(), name);
		current().add(ins);
		setNextLinkRendererInternal(LinkRendering.none());
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult addLabel(String name) {
		final InstructionLabel ins = new InstructionLabel(swimlanes.getCurrentSwimlane(), name);
		current().add(ins);
		setNextLinkRendererInternal(LinkRendering.none());
		return CommandExecutionResult.ok();
	}

	public void start() {
		manageSwimlaneStrategy();
		current().add(new InstructionStart(swimlanes.getCurrentSwimlane(), nextLinkRenderer()));
		setNextLinkRendererInternal(LinkRendering.none());
	}

	public void stop() {
		manageSwimlaneStrategy();
		final InstructionStop ins = new InstructionStop(swimlanes.getCurrentSwimlane(), nextLinkRenderer());
		if (manageSpecialStopEndAfterEndWhile(ins))
			return;

		current().add(ins);
	}

	public void end() {
		manageSwimlaneStrategy();
		final InstructionEnd ins = new InstructionEnd(swimlanes.getCurrentSwimlane(), nextLinkRenderer());
		if (manageSpecialStopEndAfterEndWhile(ins))
			return;

		current().add(ins);
	}

	private boolean manageSpecialStopEndAfterEndWhile(Instruction special) {
		if (current() instanceof InstructionList == false)
			return false;

		final InstructionList current = (InstructionList) current();
		final Instruction last = current.getLast();
		if (last instanceof InstructionWhile == false)
			return false;

		final InstructionWhile instructionWhile = (InstructionWhile) last;
		if (instructionWhile.containsBreak())
			return false;

		instructionWhile.setSpecial(special);
		return true;
	}

	public void breakInstruction() {
		manageSwimlaneStrategy();
		current().add(new InstructionBreak(swimlanes.getCurrentSwimlane(), nextLinkRenderer()));
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("activity3");
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final StringBounder stringBounder = fileFormatOption.getDefaultStringBounder(getSkinParam());
		final TextBlock result = getTextBlock(stringBounder);
		return createImageBuilder(fileFormatOption).drawable(result).write(os);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		final StringBounder stringBounder = fileFormatOption.getDefaultStringBounder(getSkinParam());
		return getTextBlock(stringBounder);
	}

	private TextBlock getTextBlock(final StringBounder stringBounder) {
		swimlanes.computeSize(stringBounder);
		TextBlock result = swimlanes;

		// BUG42
		// COMMENT TO DISABLE COMPRESS
		result = CompressionXorYBuilder.build(CompressionMode.ON_X, result, stringBounder);
		result = CompressionXorYBuilder.build(CompressionMode.ON_Y, result, stringBounder);

		result = new TextBlockRecentred(result);
		return result;
	}

	public void fork() {
		manageSwimlaneStrategy();
		final InstructionFork instructionFork = new InstructionFork(current(), nextLinkRenderer(), getSkinParam(),
				swimlanes.getCurrentSwimlane());
		current().add(instructionFork);
		setNextLinkRendererInternal(LinkRendering.none());
		setCurrent(instructionFork);
	}

	public CommandExecutionResult forkAgain() {
		if (current() instanceof InstructionFork) {
			final InstructionFork currentFork = (InstructionFork) current();
			currentFork.manageOutRendering(nextLinkRenderer(), false);
			setNextLinkRendererInternal(LinkRendering.none());
			currentFork.forkAgain(swimlanes.getCurrentSwimlane());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find fork");
	}

	public CommandExecutionResult endFork(ForkStyle forkStyle, String label) {
		if (current() instanceof InstructionFork) {
			final InstructionFork currentFork = (InstructionFork) current();
			currentFork.setStyle(forkStyle, label, swimlanes.getCurrentSwimlane());
			currentFork.manageOutRendering(nextLinkRenderer(), true);
			setNextLinkRendererInternal(LinkRendering.none());
			setCurrent(currentFork.getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find fork");
	}

	public void split() {
		final InstructionSplit instructionSplit = new InstructionSplit(current(), nextLinkRenderer(),
				swimlanes.getCurrentSwimlane());
		setNextLinkRendererInternal(LinkRendering.none());
		current().add(instructionSplit);
		setCurrent(instructionSplit);
	}

	public CommandExecutionResult splitAgain() {
		if (current() instanceof InstructionSplit) {
			((InstructionSplit) current()).splitAgain(nextLinkRenderer());
			setNextLinkRendererInternal(LinkRendering.none());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find split");
	}

	public CommandExecutionResult endSplit() {
		if (current() instanceof InstructionSplit) {
			((InstructionSplit) current()).endSplit(nextLinkRenderer(), swimlanes.getCurrentSwimlane());
			setNextLinkRendererInternal(LinkRendering.none());
			setCurrent(((InstructionSplit) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find split");
	}

	public void startSwitch(Display test, HColor color) {
		manageSwimlaneStrategy();
		final InstructionSwitch instructionSwitch = new InstructionSwitch(swimlanes.getCurrentSwimlane(), current(),
				test, nextLinkRenderer(), color, getSkinParam());
		current().add(instructionSwitch);
		setNextLinkRendererInternal(LinkRendering.none());
		setCurrent(instructionSwitch);
	}

	public CommandExecutionResult switchCase(Display labelCase) {
		if (current() instanceof InstructionSwitch) {
			final boolean ok = ((InstructionSwitch) current()).switchCase(labelCase, nextLinkRenderer());
			if (ok == false)
				return CommandExecutionResult.error("You cannot put an elseIf here");

			setNextLinkRendererInternal(LinkRendering.none());
			return CommandExecutionResult.ok();

		}
		return CommandExecutionResult.error("Cannot find switch");
	}

	public CommandExecutionResult endSwitch() {
		if (current() instanceof InstructionSwitch) {
			((InstructionSwitch) current()).endSwitch(nextLinkRenderer());
			setNextLinkRendererInternal(LinkRendering.none());
			setCurrent(((InstructionSwitch) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find switch");
	}

	public void startIf(Display test, Display whenThen, HColor color, Url url, Stereotype stereotype) {
		manageSwimlaneStrategy();
		final InstructionIf instructionIf = new InstructionIf(swimlanes.getCurrentSwimlane(), current(), test,
				LinkRendering.none().withDisplay(whenThen), nextLinkRenderer(), color, getSkinParam(), url, stereotype);
		current().add(instructionIf);
		setNextLinkRendererInternal(LinkRendering.none());
		setCurrent(instructionIf);
	}

	public CommandExecutionResult elseIf(LinkRendering inlabel, Display test, LinkRendering whenThen, HColor color) {
		if (current() instanceof InstructionIf) {
			final boolean ok = ((InstructionIf) current()).elseIf(inlabel, test, whenThen, nextLinkRenderer(), color);
			if (ok == false)
				return CommandExecutionResult.error("You cannot put an elseIf here");

			setNextLinkRendererInternal(LinkRendering.none());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public CommandExecutionResult else2(LinkRendering whenElse) {
		if (current() instanceof InstructionIf) {
			final boolean result = ((InstructionIf) current()).swithToElse2(whenElse, nextLinkRenderer());
			if (result == false)
				return CommandExecutionResult.error("Cannot find if");

			setNextLinkRendererInternal(LinkRendering.none());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public CommandExecutionResult endif() {
		// System.err.println("Activity3::endif");
		if (current() instanceof InstructionIf) {
			((InstructionIf) current()).endif(nextLinkRenderer());
			setNextLinkRendererInternal(LinkRendering.none());
			setCurrent(((InstructionIf) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public void startRepeat(HColor color, Display label, BoxStyle boxStyleIn, Colors colors, Stereotype stereotype) {
		manageSwimlaneStrategy();
		final InstructionRepeat instructionRepeat = new InstructionRepeat(swimlanes, current(), nextLinkRenderer(),
				color, label, boxStyleIn, colors, stereotype);
		current().add(instructionRepeat);
		setCurrent(instructionRepeat);
		setNextLinkRendererInternal(LinkRendering.none());

	}

	public CommandExecutionResult repeatWhile(Display label, Display yes, Display out, Display linkLabel,
			Rainbow linkColor) {
		manageSwimlaneStrategy();
		if (current() instanceof InstructionRepeat) {
			final InstructionRepeat instructionRepeat = (InstructionRepeat) current();
			final LinkRendering back = LinkRendering.create(linkColor).withDisplay(linkLabel);
			instructionRepeat.setTest(label, yes, out, nextLinkRenderer(), back, swimlanes.getCurrentSwimlane());
			setCurrent(instructionRepeat.getParent());
			this.setNextLinkRendererInternal(LinkRendering.none());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find repeat");

	}

	public CommandExecutionResult backward(Display label, BoxStyle boxStyle, LinkRendering incoming1,
			LinkRendering incoming2, Stereotype stereotype) {
		manageSwimlaneStrategy();
		if (current() instanceof InstructionRepeat) {
			final InstructionRepeat instructionRepeat = (InstructionRepeat) current();
			instructionRepeat.setBackward(label, swimlanes.getCurrentSwimlane(), boxStyle, incoming1, incoming2,
					stereotype);
			return CommandExecutionResult.ok();
		}
		if (current() instanceof InstructionWhile) {
			final InstructionWhile instructionWhile = (InstructionWhile) current();
			instructionWhile.setBackward(label, boxStyle, incoming1, incoming2, stereotype);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find repeat");

	}

	public void doWhile(Display test, Display yes, HColor color) {
		manageSwimlaneStrategy();
		final InstructionWhile instructionWhile = new InstructionWhile(swimlanes.getCurrentSwimlane(), current(), test,
				nextLinkRenderer(), yes, color, getSkinParam().getCurrentStyleBuilder());
		current().add(instructionWhile);
		setCurrent(instructionWhile);
	}

	public CommandExecutionResult endwhile(Display out) {
		if (current() instanceof InstructionWhile) {
			((InstructionWhile) current()).incoming(nextLinkRenderer());
			((InstructionWhile) current()).outDisplay(out);
			setNextLinkRendererInternal(LinkRendering.none());
			setCurrent(((InstructionWhile) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find while");
	}

	final public CommandExecutionResult kill() {
		if (current().kill() == false)
			return CommandExecutionResult.error("kill cannot be used here");

		return CommandExecutionResult.ok();
	}

	public void startGroup(Display name, HColor backColor, USymbol type, Style style) {
		manageSwimlaneStrategy();
		final InstructionGroup instructionGroup = new InstructionGroup(current(), name, backColor,
				swimlanes.getCurrentSwimlane(), nextLinkRenderer(), type, style);
		current().add(instructionGroup);
		setCurrent(instructionGroup);
	}

	public CommandExecutionResult endGroup() {
		if (current() instanceof InstructionGroup) {
			setCurrent(((InstructionGroup) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find group");
	}

	private void setNextLinkRendererInternal(LinkRendering link) {
		// System.err.println("setNextLinkRendererInternal=" + link);
		swimlanes.setNextLinkRenderer(Objects.requireNonNull(link));
	}

	private void setNextLink(LinkRendering linkRenderer) {
		Objects.requireNonNull(linkRenderer);
		// System.err.println("setNextLink=" + linkRenderer);
		if (current() instanceof InstructionCollection) {
			final Instruction last = ((InstructionCollection) current()).getLast();
			if (last instanceof InstructionWhile)
				((InstructionWhile) last).outColor(linkRenderer.getRainbow());
			else if (last instanceof InstructionIf)
				((InstructionIf) last).outColor(linkRenderer);

		}
		this.setNextLinkRendererInternal(linkRenderer);
	}

	public void setLabelNextArrow(Display label) {
//		if (current() instanceof InstructionRepeat && ((InstructionRepeat) current()).hasBackward()) {
//			final InstructionRepeat instructionRepeat = (InstructionRepeat) current();
//			instructionRepeat.setBackwardArrowLabel(label);
//			return;
//		}
		if (current() instanceof InstructionWhile && ((InstructionWhile) current()).getLast() == null) {
			((InstructionWhile) current()).overwriteYes(label);
			return;
		}

		setNextLinkRendererInternal(nextLinkRenderer().withDisplay(label));
	}

	public void setColorNextArrow(Rainbow color) {
		if (color == null)
			return;

		final LinkRendering link = LinkRendering.create(color);
		setNextLink(link);
	}

	public CommandExecutionResult addNote(Display note, NotePosition position, NoteType type, Colors colors,
			Stereotype stereotype) {
		final boolean ok = current().addNote(note, position, type, colors, swimlanes.getCurrentSwimlane(), stereotype);
		if (ok == false)
			return CommandExecutionResult.error("Cannot add note here");

		manageHasUrl(note);
		return CommandExecutionResult.ok();
	}

	private boolean hasUrl = false;

	private void manageHasUrl(Display display) {
		if (display.hasUrl())
			hasUrl = true;

	}

	@Override
	public boolean hasUrl() {
		return hasUrl;
	}

}
