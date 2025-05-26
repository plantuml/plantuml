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

import java.util.Collection;
import java.util.Objects;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.WeldingPoint;
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class Branch {

	private final InstructionList list;
	private final Display labelTest;

	private final LinkRendering labelPositive;

	private LinkRendering inlinkRendering = LinkRendering.none();
	private final LinkRendering inlabel;
	private LinkRendering special;

	private final HColor color;

	private Ftile ftile;
	// ::comment when __CORE__
	private Gtile gtile;
	// ::done

	public StyleSignatureBasic getDefaultStyleDefinitionArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	public StyleSignatureBasic getDefaultStyleDefinitionDiamond() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	public boolean containsBreak() {
		return list.containsBreak();
	}

	public Branch(StyleBuilder styleBuilder, Swimlane swimlane, LinkRendering labelPositive, Display labelTest,
			HColor color, LinkRendering inlabel, Stereotype stereotype, HColorSet colorSet) {
		this.inlabel = Objects.requireNonNull(inlabel);
		this.labelTest = Objects.requireNonNull(labelTest);
		this.labelPositive = Objects.requireNonNull(labelPositive);

		final Style style = getDefaultStyleDefinitionDiamond().withTOBECHANGED(stereotype).getMergedStyle(styleBuilder);
		this.color = color == null ? style.value(PName.BackGroundColor).asColor(colorSet) : color;

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

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote,
			Stereotype stereotype) {
		return list.addNote(note, position, type, colors, swimlaneNote, stereotype);
	}

	public final void setInlinkRendering(LinkRendering inlinkRendering) {
		this.inlinkRendering = Objects.requireNonNull(inlinkRendering);
	}

	public void updateFtile(FtileFactory factory) {
		this.ftile = factory.decorateOut(list.createFtile(factory), inlinkRendering);
	}

	// ::comment when __CORE__
	public void updateGtile(ISkinParam skinParam, StringBounder stringBounder) {
		this.gtile = list.createGtile(skinParam, stringBounder);
	}
	// ::done

	public Collection<? extends Swimlane> getSwimlanes() {
		return list.getSwimlanes();
	}

	public final Display getLabelTest() {
		return labelTest;
	}

	public final Rainbow getOut() {
		if (special != null)
			return special.getRainbow();

//		if (labelPositive.getRainbow().size() > 0)
//			return labelPositive.getRainbow();

		if (inlinkRendering == null)
			return null;

		return inlinkRendering.getRainbow();
	}

	public Rainbow getInColor(Rainbow arrowColor) {
		if (isEmpty())
			return getFtile().getOutLinkRendering().getRainbow(arrowColor);

		if (labelPositive.getRainbow().size() > 0)
			return labelPositive.getRainbow();

		final LinkRendering linkIn = getFtile().getInLinkRendering();
		final Rainbow color = linkIn.getRainbow(arrowColor);
		if (color.size() == 0)
			return arrowColor;

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

	// ::comment when __CORE__
	public Gtile getGtile() {
		return gtile;
	}
	// ::done

	public ISkinParam skinParam() {
		// ::comment when __CORE__
		if (gtile != null)
			return gtile.skinParam();
		// ::done
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
			if (in != null && Display.isNull(in.getDisplay()) == false)
				return in.getDisplay();

		}
		return labelPositive.getDisplay();
	}

	public Display getSpecialDisplay() {
		if (special != null && Display.isNull(special.getDisplay()) == false)
			return special.getDisplay();

		return null;
	}

	private TextBlock getTextBlock(Display display) {
		if (display == null)
			return TextBlockUtils.EMPTY_TEXT_BLOCK;

		final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
		final LineBreakStrategy lineBreak = style.wrapWidth();
		final FontConfiguration fcArrow = style.getFontConfiguration(skinParam().getIHtmlColorSet());

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
