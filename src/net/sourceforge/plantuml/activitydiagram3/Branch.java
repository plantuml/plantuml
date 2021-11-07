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

import java.util.Collection;
import java.util.Objects;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.WeldingPoint;
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class Branch {

	private final InstructionList list;
	private final Display labelTest;

	private final LinkRendering labelPositive;

	private LinkRendering inlinkRendering = LinkRendering.none();
	private final LinkRendering inlabel;
	private LinkRendering special;

	private final HColor color;

	private Ftile ftile;
	private Gtile gtile;

	public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	public StyleSignature getDefaultStyleDefinitionDiamond() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	public boolean containsBreak() {
		return list.containsBreak();
	}

	public Branch(StyleBuilder styleBuilder, Swimlane swimlane, LinkRendering labelPositive, Display labelTest,
			HColor color, LinkRendering inlabel) {
		this.inlabel = Objects.requireNonNull(inlabel);
		this.labelTest = Objects.requireNonNull(labelTest);
		this.labelPositive = Objects.requireNonNull(labelPositive);
		if (UseStyle.useBetaStyle()) {
			final Style style = getDefaultStyleDefinitionDiamond().getMergedStyle(styleBuilder);
			this.color = color == null
					? style.value(PName.BackGroundColor).asColor(styleBuilder.getSkinParam().getThemeStyle(),
							styleBuilder.getSkinParam().getIHtmlColorSet())
					: color;
		} else {
			this.color = color;
		}

		this.list = new InstructionList(swimlane);
	}

	public Collection<WeldingPoint> getWeldingPoints() {
		return ftile.getWeldingPoints();
	}

	public CommandExecutionResult add(Instruction ins) {
		list.add(ins);
		return CommandExecutionResult.ok();
	}

	public boolean kill() {
		return list.kill();
	}

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		return list.addNote(note, position, type, colors, swimlaneNote);
	}

	public final void setInlinkRendering(LinkRendering inlinkRendering) {
		this.inlinkRendering = Objects.requireNonNull(inlinkRendering);
	}

	public void updateFtile(FtileFactory factory) {
		this.ftile = factory.decorateOut(list.createFtile(factory), inlinkRendering);
	}

	public void updateGtile(ISkinParam skinParam, StringBounder stringBounder) {
		this.gtile = list.createGtile(skinParam, stringBounder);
	}

	public Collection<? extends Swimlane> getSwimlanes() {
		return list.getSwimlanes();
	}

	public final Display getLabelTest() {
		return labelTest;
	}

	public final Rainbow getOut() {
		if (special != null) {
			return special.getRainbow();
		}
//		if (labelPositive.getRainbow().size() > 0) {
//			return labelPositive.getRainbow();
//		}
		if (inlinkRendering == null) {
			return null;
		}
		return inlinkRendering.getRainbow();
	}

	public Rainbow getInColor(Rainbow arrowColor) {
		if (isEmpty()) {
			return getFtile().getOutLinkRendering().getRainbow(arrowColor);
		}
		if (labelPositive.getRainbow().size() > 0) {
			return labelPositive.getRainbow();
		}
		final LinkRendering linkIn = getFtile().getInLinkRendering();
		final Rainbow color = linkIn.getRainbow(arrowColor);
		if (color.size() == 0) {
			return arrowColor;
		}
		return color;
	}

	public Display getInlabel() {
		return inlabel.getDisplay();
	}

	public Rainbow getInRainbow(Rainbow defaultColor) {
		return inlabel.getRainbow(defaultColor);
	}

	public Rainbow getLabelPositiveRainbow(Rainbow defaultColor) {
		return labelPositive.getRainbow(defaultColor);
	}

	public final Ftile getFtile() {
		return ftile;
	}

	public Gtile getGtile() {
		return gtile;
	}

	public ISkinParam skinParam() {
		if (gtile != null)
			return gtile.skinParam();
		return ftile.skinParam();
	}

	public final HColor getColor() {
		return color;
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public Instruction getLast() {
		return list.getLast();
	}

	public boolean isOnlySingleStopOrSpot() {
		return list.isOnlySingleStopOrSpot();
	}

	public void setSpecial(LinkRendering link) {
		this.special = link;
	}

	public final LinkRendering getSpecial() {
		return special;
	}

	public final Display getDisplayPositive() {
		if (ftile != null) {
			final LinkRendering in = ftile.getInLinkRendering();
			if (in != null && Display.isNull(in.getDisplay()) == false) {
				return in.getDisplay();
			}
		}
		return labelPositive.getDisplay();
	}

	public Display getSpecialDisplay() {
		if (special != null && Display.isNull(special.getDisplay()) == false) {
			return special.getDisplay();
		}
		return null;
	}

	private TextBlock getTextBlock(Display display) {
		if (display == null)
			return TextBlockUtils.EMPTY_TEXT_BLOCK;

		LineBreakStrategy lineBreak = LineBreakStrategy.NONE;
		final FontConfiguration fcArrow;
		if (UseStyle.useBetaStyle()) {
			final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
			lineBreak = style.wrapWidth();
			fcArrow = style.getFontConfiguration(skinParam().getThemeStyle(), skinParam().getIHtmlColorSet());
		} else {
			fcArrow = new FontConfiguration(skinParam(), FontParam.ARROW, null);
		}

		return display.create0(fcArrow, HorizontalAlignment.LEFT, skinParam(), lineBreak, CreoleMode.SIMPLE_LINE, null,
				null);
	}

	public final TextBlock getTextBlockPositive() {
		return getTextBlock(getDisplayPositive());
	}

	public final TextBlock getTextBlockSpecial() {
		return getTextBlock(getSpecialDisplay());
	}

}
