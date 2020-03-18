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

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.WeldingPoint;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.Rainbow;
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
	private final Display labelPositive;
	private final Display inlabel;
	private final HColor color;
	private LinkRendering inlinkRendering = LinkRendering.none();

	private Ftile ftile;

	public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	public StyleSignature getDefaultStyleDefinitionDiamond() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	public boolean containsBreak() {
		return list.containsBreak();
	}

	public Branch(StyleBuilder styleBuilder, Swimlane swimlane, Display labelPositive, Display labelTest,
			HColor color, Display inlabel) {
		if (labelPositive == null) {
			throw new IllegalArgumentException();
		}
		if (labelTest == null) {
			throw new IllegalArgumentException();
		}
		if (inlabel == null) {
			throw new IllegalArgumentException();
		}
		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinitionDiamond().getMergedStyle(styleBuilder);
			this.color = color == null ? style.value(PName.BackGroundColor).asColor(
					styleBuilder.getSkinParam().getIHtmlColorSet()) : color;
		} else {
			this.color = color;
		}

		this.inlabel = inlabel;
		this.list = new InstructionList(swimlane);
		this.labelTest = labelTest;
		this.labelPositive = labelPositive;
	}

	public Collection<WeldingPoint> getWeldingPoints() {
		return ftile.getWeldingPoints();
	}

	public void add(Instruction ins) {
		list.add(ins);
	}

	public boolean kill() {
		return list.kill();
	}

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		return list.addNote(note, position, type, colors, swimlaneNote);
	}

	public final void setInlinkRendering(LinkRendering inlinkRendering) {
		if (inlinkRendering == null) {
			throw new IllegalArgumentException();
		}
		this.inlinkRendering = inlinkRendering;
	}

	public void updateFtile(FtileFactory factory) {
		this.ftile = factory.decorateOut(list.createFtile(factory), inlinkRendering);
	}

	public Collection<? extends Swimlane> getSwimlanes() {
		return list.getSwimlanes();
	}

	public final Display getLabelPositive() {
		final LinkRendering in = ftile.getInLinkRendering();
		if (in != null && Display.isNull(in.getDisplay()) == false) {
			return in.getDisplay();
		}
		return labelPositive;
	}

	public final Display getLabelTest() {
		return labelTest;
	}

	public final Rainbow getInlinkRenderingColorAndStyle() {
		return inlinkRendering == null ? null : inlinkRendering.getRainbow();
	}

	public Display getInlabel() {
		return inlabel;
	}

	public final Ftile getFtile() {
		return ftile;
	}

	public ISkinParam skinParam() {
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

	private LinkRendering special;

	public void setSpecial(LinkRendering link) {
		this.special = link;
	}

	public final LinkRendering getSpecial() {
		return special;
	}

}
