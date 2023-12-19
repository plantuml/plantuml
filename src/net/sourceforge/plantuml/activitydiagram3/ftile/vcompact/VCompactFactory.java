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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ForkStyle;
import net.sourceforge.plantuml.activitydiagram3.Instruction;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileAssemblySimple;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBox;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileCircleEndCross;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileCircleSpot;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileCircleStart;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileCircleStop;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDecorateIn;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDecorateOut;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.url.Url;

public class VCompactFactory implements FtileFactory {

	private final ISkinParam skinParam;
	private final StringBounder stringBounder;

	@Override
	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public StyleBuilder getCurrentStyleBuilder() {
		return skinParam.getCurrentStyleBuilder();
	}

	public VCompactFactory(ISkinParam skinParam, StringBounder stringBounder) {
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;
	}

	private StyleSignatureBasic getSignatureCircleEnd() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.circle, SName.end);
	}

	private StyleSignatureBasic getSignatureCircleStop() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.circle, SName.stop);
	}

	private StyleSignatureBasic getSignatureCircleSpot() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.circle, SName.spot);
	}

	private StyleSignatureBasic getSignatureCircleStart() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.circle, SName.start);
	}

	@Override
	public Ftile start(Swimlane swimlane) {
		final Style style = getSignatureCircleStart().getMergedStyle(skinParam.getCurrentStyleBuilder());
		return new FtileCircleStart(skinParam(), swimlane, style);
	}

	@Override
	public Ftile stop(Swimlane swimlane) {
		final Style style = getSignatureCircleStop().getMergedStyle(skinParam.getCurrentStyleBuilder());
		return new FtileCircleStop(skinParam(), swimlane, style);
	}

	@Override
	public Ftile spot(Swimlane swimlane, String spot, HColor color) {
		final UFont font = skinParam.getFont(null, false, FontParam.ACTIVITY);
		final Style style = getSignatureCircleSpot().getMergedStyle(skinParam.getCurrentStyleBuilder());
		return new FtileCircleSpot(skinParam(), swimlane, spot, font, color, style);
	}

	@Override
	public Ftile end(Swimlane swimlane) {
		final Style style = getSignatureCircleEnd().getMergedStyle(skinParam.getCurrentStyleBuilder());
		return new FtileCircleEndCross(skinParam(), swimlane, style);
	}

	@Override
	public Ftile activity(Display label, Swimlane swimlane, BoxStyle boxStyle, Colors colors, Stereotype stereotype) {
		return FtileBox.create(colors.mute(skinParam), label, swimlane, boxStyle, stereotype);
	}

	@Override
	public Ftile addNote(Ftile ftile, Swimlane swimlane, Collection<PositionedNote> notes,
			VerticalAlignment verticalAlignment) {
		return ftile;
	}

	@Override
	public Ftile addUrl(Ftile ftile, Url url) {
		return ftile;
	}

	@Override
	public Ftile assembly(Ftile tile1, Ftile tile2) {
		return new FtileAssemblySimple(tile1, tile2);
	}

	@Override
	public Ftile repeat(BoxStyle boxStyleIn, Swimlane swimlane, Swimlane swimlaneOut, Display startLabel, Ftile repeat,
			Display test, Display yes, Display out, Colors colors, Ftile backward, boolean noOut,
			LinkRendering incoming1, LinkRendering incoming2, StyleBuilder currentStyleBuilder) {
		return repeat;
	}

	@Override
	public Ftile createWhile(LinkRendering afterEndwhile, Swimlane swimlane, Ftile whileBlock, Display test,
			Display yes, HColor color, Instruction specialOut, Ftile back, LinkRendering incoming1,
			LinkRendering incoming2, StyleBuilder styleBuilder) {
		return whileBlock;
	}

	@Override
	public Ftile createIf(Swimlane swimlane, List<Branch> thens, Branch elseBranch, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Url url, Collection<PositionedNote> notes, Stereotype stereotype,
			StyleBuilder currentStyleBuilder) {
		final List<Ftile> ftiles = new ArrayList<>();
		for (Branch branch : thens)
			ftiles.add(branch.getFtile());

		ftiles.add(elseBranch.getFtile());
		return new FtileForkInner(ftiles);
	}

	@Override
	public Ftile createSwitch(Swimlane swimlane, List<Branch> branches, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Display labelTest) {
		final List<Ftile> ftiles = new ArrayList<>();
		for (Branch branch : branches)
			ftiles.add(branch.getFtile());

		return new FtileForkInner(ftiles);
	}

	@Override
	public Ftile createParallel(List<Ftile> all, ForkStyle style, String label, Swimlane in, Swimlane out) {
		return new FtileForkInner(all);
	}

	@Override
	public Ftile createGroup(Ftile list, Display name, HColor backColor, PositionedNote note, USymbol type,
			Style style) {
		return list;
	}

	@Override
	public Ftile decorateIn(final Ftile ftile, final LinkRendering linkRendering) {
		return new FtileDecorateIn(ftile, linkRendering);
	}

	@Override
	public Ftile decorateOut(final Ftile ftile, final LinkRendering linkRendering) {
		// if (ftile instanceof FtileWhile) {
		// if (linkRendering != null) {
		// ((FtileWhile) ftile).changeAfterEndwhileColor(linkRendering.getColor());
		// }
		// return ftile;
		// }
		return new FtileDecorateOut(ftile, linkRendering);
	}

	@Override
	public ISkinParam skinParam() {
		return skinParam;
	}
}
