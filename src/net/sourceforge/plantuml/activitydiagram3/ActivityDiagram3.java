/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3;

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.CMapData;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlanes;
import net.sourceforge.plantuml.api.ImageDataComplex;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockCompressed;
import net.sourceforge.plantuml.graphic.TextBlockRecentred;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.DecorateTextBlock;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class ActivityDiagram3 extends UmlDiagram {

	enum SwimlaneStrategy {
		SWIMLANE_FORBIDDEN, SWIMLANE_ALLOWED;
	}

	private SwimlaneStrategy swimlaneStrategy;

	private final Swimlanes swinlanes = new Swimlanes(getSkinParam());

	private void manageSwimlaneStrategy() {
		if (swimlaneStrategy == null) {
			swimlaneStrategy = SwimlaneStrategy.SWIMLANE_FORBIDDEN;
		}
	}

	public CommandExecutionResult swimlane(String name, HtmlColor color, Display label) {
		if (swimlaneStrategy == null) {
			swimlaneStrategy = SwimlaneStrategy.SWIMLANE_ALLOWED;
		}
		if (swimlaneStrategy == SwimlaneStrategy.SWIMLANE_FORBIDDEN) {
			return CommandExecutionResult.error("This swimlane must be defined at the start of the diagram.");
		}

		swinlanes.swimlane(name, color, label);
		return CommandExecutionResult.ok();
	}

	private void setCurrent(Instruction ins) {
		swinlanes.setCurrent(ins);
	}

	private Instruction current() {
		return swinlanes.getCurrent();
	}

	private LinkRendering nextLinkRenderer() {
		return swinlanes.nextLinkRenderer();
	}

	public void addActivity(Display activity, HtmlColor color, BoxStyle style) {
		manageSwimlaneStrategy();
		current()
				.add(new InstructionSimple(activity, color, nextLinkRenderer(), swinlanes.getCurrentSwimlane(), style));
		setNextLinkRendererInternal(null);
		manageHasUrl(activity);
	}

	public void start() {
		manageSwimlaneStrategy();
		current().add(new InstructionStart(swinlanes.getCurrentSwimlane()));
	}

	public void stop() {
		manageSwimlaneStrategy();
		current().add(new InstructionStop(swinlanes.getCurrentSwimlane()));
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("activity3", getClass());
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.ACTIVITY;
	}

	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption,
			List<BufferedImage> flashcodes) throws IOException {
		// BUG42
		// TextBlock result = swinlanes;
		TextBlock result = new TextBlockCompressed(swinlanes);
		result = new TextBlockRecentred(result);
		result = addTitle(result);
		result = addHeaderAndFooter(result);
		final ISkinParam skinParam = getSkinParam();
		final Dimension2D dim = TextBlockUtils.getMinMax(result).getDimension();
		final double margin = 10;
		final double dpiFactor = getDpiFactor(fileFormatOption, Dimension2DDouble.delta(dim, 2 * margin, 0));

		final UGraphic ug = TextBlockUtils.getPrinted(result, fileFormatOption, skinParam.getColorMapper(), dpiFactor,
				getSkinParam().getBackgroundColor(), margin);

		ug.writeImage(os, fileFormatOption.isWithMetadata() ? getMetadata() : null, getDpi(fileFormatOption));

		if (ug instanceof UGraphicG2d) {
			final Set<Url> urls = ((UGraphicG2d) ug).getAllUrlsEncountered();
			if (urls.size() > 0) {
				final CMapData cmap = CMapData.cmapString(urls, dpiFactor);
				return new ImageDataComplex(dim, cmap, getWarningOrError());
			}
		}

		return new ImageDataSimple(dim);
	}

	private final double getDpiFactor(FileFormatOption fileFormatOption, final Dimension2D dim) {
		final double dpiFactor;
		final Scale scale = getScale();
		if (scale == null) {
			dpiFactor = getDpiFactor(fileFormatOption);
		} else {
			dpiFactor = scale.getScale(dim.getWidth(), dim.getHeight());
		}
		return dpiFactor;
	}

	private TextBlock addTitle(TextBlock original) {
		final Display title = getTitle();
		if (title == null) {
			return original;
		}
		final TextBlock text = TextBlockUtils.create(title, new FontConfiguration(getFont(FontParam.TITLE),
				getFontColor(FontParam.TITLE, null)), HorizontalAlignment.CENTER, getSkinParam());

		return new DecorateTextBlock(original, text, HorizontalAlignment.CENTER);
	}

	private TextBlock addHeaderAndFooter(TextBlock original) {
		final Display footer = getFooter();
		final Display header = getHeader();
		if (footer == null && header == null) {
			return original;
		}
		final TextBlock textFooter = footer == null ? null : TextBlockUtils.create(footer, new FontConfiguration(
				getFont(FontParam.FOOTER), getFontColor(FontParam.FOOTER, null)), getFooterAlignment(), getSkinParam());
		final TextBlock textHeader = header == null ? null : TextBlockUtils.create(header, new FontConfiguration(
				getFont(FontParam.HEADER), getFontColor(FontParam.HEADER, null)), getHeaderAlignment(), getSkinParam());

		return new DecorateTextBlock(original, textHeader, getHeaderAlignment(), textFooter, getFooterAlignment());
	}

	private final UFont getFont(FontParam fontParam) {
		final ISkinParam skinParam = getSkinParam();
		return skinParam.getFont(fontParam, null);
	}

	private final HtmlColor getFontColor(FontParam fontParam, String stereo) {
		final ISkinParam skinParam = getSkinParam();
		return skinParam.getFontHtmlColor(fontParam, stereo);
	}

	public void fork() {
		final InstructionFork instructionFork = new InstructionFork(current());
		current().add(instructionFork);
		setCurrent(instructionFork);
	}

	public CommandExecutionResult forkAgain() {
		if (current() instanceof InstructionFork) {
			((InstructionFork) current()).forkAgain();
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find fork");
	}

	public CommandExecutionResult endFork() {
		if (current() instanceof InstructionFork) {
			setCurrent(((InstructionFork) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find fork");
	}

	public void split() {
		final InstructionSplit instructionSplit = new InstructionSplit(current());
		current().add(instructionSplit);
		setCurrent(instructionSplit);
	}

	public CommandExecutionResult splitAgain() {
		if (current() instanceof InstructionSplit) {
			((InstructionSplit) current()).splitAgain();
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find split");
	}

	public CommandExecutionResult endSplit() {
		if (current() instanceof InstructionSplit) {
			setCurrent(((InstructionSplit) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find split");
	}

	public void startIf(Display test, Display whenThen) {
		manageSwimlaneStrategy();
		final InstructionIf1 instructionIf = new InstructionIf1(swinlanes.getCurrentSwimlane(), current(), test,
				whenThen, nextLinkRenderer());
		current().add(instructionIf);
		setCurrent(instructionIf);
	}

	public CommandExecutionResult elseIf(Display test, Display whenThen) {
		if (current() instanceof InstructionIf1) {
			((InstructionIf1) current()).elseIf(test, whenThen, nextLinkRenderer());
			setNextLinkRendererInternal(null);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public CommandExecutionResult else2(Display whenElse) {
		if (current() instanceof InstructionIf1) {
			((InstructionIf1) current()).swithToElse(whenElse, nextLinkRenderer());
			setNextLinkRendererInternal(null);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public CommandExecutionResult endif() {
		if (current() instanceof InstructionIf1) {
			((InstructionIf1) current()).endif(nextLinkRenderer());
			setNextLinkRendererInternal(null);
			setCurrent(((InstructionIf1) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public void startRepeat() {
		manageSwimlaneStrategy();
		final InstructionRepeat instructionRepeat = new InstructionRepeat(swinlanes.getCurrentSwimlane(), current(),
				nextLinkRenderer());
		current().add(instructionRepeat);
		setCurrent(instructionRepeat);

	}

	public CommandExecutionResult repeatWhile(Display label) {
		manageSwimlaneStrategy();
		if (current() instanceof InstructionRepeat) {
			final InstructionRepeat instructionRepeat = (InstructionRepeat) current();
			instructionRepeat.setTest(label, nextLinkRenderer());
			setCurrent(instructionRepeat.getParent());
			this.setNextLinkRendererInternal(null);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find repeat");

	}

	public void doWhile(Display test, Display yes) {
		manageSwimlaneStrategy();
		final InstructionWhile instructionWhile = new InstructionWhile(swinlanes.getCurrentSwimlane(), current(), test,
				nextLinkRenderer(), yes);
		current().add(instructionWhile);
		setCurrent(instructionWhile);
	}

	public CommandExecutionResult endwhile(Display out) {
		if (current() instanceof InstructionWhile) {
			((InstructionWhile) current()).endwhile(nextLinkRenderer(), out);
			setNextLinkRendererInternal(null);
			setCurrent(((InstructionWhile) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find while");
	}

	final public CommandExecutionResult kill() {
		if (current().kill() == false) {
			return CommandExecutionResult.error("kill cannot be used here");
		}
		return CommandExecutionResult.ok();
	}

	public void startGroup(Display name) {
		manageSwimlaneStrategy();
		final InstructionGroup instructionGroup = new InstructionGroup(current(), name);
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
		swinlanes.setNextLinkRenderer(link);
	}

	private void setNextLink(LinkRendering linkRenderer) {
		if (current() instanceof InstructionList) {
			final Instruction last = ((InstructionList) current()).getLast();
			if (last instanceof InstructionWhile) {
				((InstructionWhile) last).afterEndwhile(linkRenderer);
			}
		}
		this.setNextLinkRendererInternal(linkRenderer);
	}

	private final Rose rose = new Rose();

	public void setLabelNextArrow(Display label) {
		if (nextLinkRenderer() == null) {
			final HtmlColor arrowColor = rose.getHtmlColor(getSkinParam(), ColorParam.activityArrow);
			this.setNextLink(new LinkRendering(arrowColor));
		}
		nextLinkRenderer().setDisplay(label);
	}

	public void setColorNextArrow(HtmlColor color) {
		if (color == null) {
			return;
		}
		final LinkRendering link = new LinkRendering(color);
		setNextLink(link);
	}

	public CommandExecutionResult addNote(Display note, NotePosition position) {
		current().addNote(note, position);
		manageHasUrl(note);
		return CommandExecutionResult.ok();
	}

	private boolean hasUrl = false;

	private void manageHasUrl(Display display) {
		if (display.hasUrl()) {
			hasUrl = true;
		}
	}

	@Override
	public boolean hasUrl() {
		return hasUrl;
	}

}
