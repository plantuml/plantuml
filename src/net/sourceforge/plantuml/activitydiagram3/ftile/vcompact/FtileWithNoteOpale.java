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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Sheet;
import net.sourceforge.plantuml.klimt.creole.SheetBlock1;
import net.sourceforge.plantuml.klimt.creole.SheetBlock2;
import net.sourceforge.plantuml.klimt.creole.Stencil;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.skin.AlignmentParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.style.Styleable;
import net.sourceforge.plantuml.svek.image.Opale;
import net.sourceforge.plantuml.utils.Direction;

public class FtileWithNoteOpale extends AbstractFtile implements Stencil, Styleable {

	private final Ftile tile;
	private final Opale opale;
	private final VerticalAlignment verticalAlignment;

	private final NotePosition notePosition;
	private final double suppSpace = 20;
	private final Swimlane swimlaneNote;

	public StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.note);
	}

	public Set<Swimlane> getSwimlanes() {
		if (swimlaneNote != null) {
			final Set<Swimlane> result = new HashSet<>(tile.getSwimlanes());
			result.add(swimlaneNote);
			return Collections.unmodifiableSet(result);
		}
		return tile.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return tile.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return tile.getSwimlaneOut();
	}

	@Override
	public Collection<Ftile> getMyChildren() {
		return Collections.singleton(tile);
	}

	public static Ftile create(Ftile tile, Collection<PositionedNote> notes, boolean withLink,
			VerticalAlignment verticalAlignment) {
		if (notes.size() > 1)
			return new FtileWithNotes(tile, notes, verticalAlignment);

		if (notes.size() == 0)
			throw new IllegalArgumentException();

		return new FtileWithNoteOpale(tile, notes.iterator().next(), withLink, verticalAlignment);
	}

	private FtileWithNoteOpale(Ftile tile, PositionedNote note, boolean withLink, VerticalAlignment verticalAlignment) {
		super(note.getColors() == null ? tile.skinParam() : note.getColors().mute(tile.skinParam()));
		this.verticalAlignment = verticalAlignment;
		this.swimlaneNote = note.getSwimlaneNote();

		this.tile = tile;
		this.notePosition = note.getNotePosition();
		if (note.getType() == NoteType.FLOATING_NOTE)
			withLink = false;

		final Style style = getStyleSignature().getMergedStyle(skinParam().getCurrentStyleBuilder())
				.eventuallyOverride(note.getColors());
		final HColor noteBackgroundColor = style.value(PName.BackGroundColor).asColor(getIHtmlColorSet());
		final HColor borderColor = style.value(PName.LineColor).asColor(getIHtmlColorSet());
		final FontConfiguration fc = style.getFontConfiguration(getIHtmlColorSet());
		final double shadowing = style.value(PName.Shadowing).asDouble();
		final LineBreakStrategy wrapWidth = style.wrapWidth();
		final UStroke stroke = style.getStroke();

		final HorizontalAlignment align = skinParam().getHorizontalAlignment(AlignmentParam.noteTextAlignment, null,
				false, null);
		final Sheet sheet = skinParam().sheet(fc, align, CreoleMode.FULL).createSheet(note.getDisplay());
		final TextBlock text = new SheetBlock2(new SheetBlock1(sheet, wrapWidth, skinParam().getPadding()), this,
				stroke);
		opale = new Opale(shadowing, borderColor, noteBackgroundColor, text, withLink, stroke);

	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		final XDimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final XDimension2D dimNote = opale.calculateDimension(stringBounder);
		final XDimension2D dimTile = tile.calculateDimension(stringBounder);
		final double yForFtile = (dimTotal.getHeight() - dimTile.getHeight()) / 2;
		final double marge;
		if (notePosition == NotePosition.LEFT)
			marge = dimNote.getWidth() + suppSpace;
		else
			marge = 0;

		return new UTranslate(marge, yForFtile);
	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == tile)
			return UTranslate.none();
		return super.getTranslateFor(child, stringBounder);

	}

	private UTranslate getTranslateForOpale(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimTotal = calculateDimension(stringBounder);
		final XDimension2D dimNote = opale.calculateDimension(stringBounder);

		final double yForNote;
		if (verticalAlignment == VerticalAlignment.CENTER)
			yForNote = (dimTotal.getHeight() - dimNote.getHeight()) / 2;
		else
			yForNote = 0;

		if (notePosition == NotePosition.LEFT)
			return UTranslate.dy(yForNote);

		final double dx = dimTotal.getWidth() - dimNote.getWidth();
		return new UTranslate(dx, yForNote);
	}

	public void drawU(UGraphic ug) {
		final Swimlane intoSw;
		if (ug instanceof UGraphicInterceptorOneSwimlane)
			intoSw = ((UGraphicInterceptorOneSwimlane) ug).getSwimlane();
		else
			intoSw = null;

		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimNote = opale.calculateDimension(stringBounder);

		if (notePosition == NotePosition.LEFT) {
			final Direction strategy = Direction.RIGHT;
			final XPoint2D pp1 = new XPoint2D(dimNote.getWidth(), dimNote.getHeight() / 2);
			final XPoint2D pp2 = new XPoint2D(dimNote.getWidth() + suppSpace, dimNote.getHeight() / 2);
			opale.setOpale(strategy, pp1, pp2);
		} else {
			final Direction strategy = Direction.LEFT;
			final XPoint2D pp1 = new XPoint2D(0, dimNote.getHeight() / 2);
			final XPoint2D pp2 = new XPoint2D(-suppSpace, dimNote.getHeight() / 2);
			opale.setOpale(strategy, pp1, pp2);
		}

		if (ug instanceof UGraphicInterceptorOneSwimlane == false || swimlaneNote == null || intoSw == swimlaneNote)
			opale.drawU(ug.apply(getTranslateForOpale(ug)));

		ug.apply(getTranslate(stringBounder)).draw(tile);
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final XDimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry orig = tile.calculateDimension(stringBounder);
		final UTranslate translate = getTranslate(stringBounder);
		if (orig.hasPointOut())
			return new FtileGeometry(dimTotal, orig.getLeft() + translate.getDx(), orig.getInY() + translate.getDy(),
					orig.getOutY() + translate.getDy());

		return new FtileGeometry(dimTotal, orig.getLeft() + translate.getDx(), orig.getInY() + translate.getDy());
	}

	private XDimension2D calculateDimensionInternal(StringBounder stringBounder) {
		final XDimension2D dimNote = opale.calculateDimension(stringBounder);
		final XDimension2D dimTile = tile.calculateDimension(stringBounder);
		final double height = Math.max(dimNote.getHeight(), dimTile.getHeight());
		return new XDimension2D(dimTile.getWidth() + 1 * dimNote.getWidth() + suppSpace, height);
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return -opale.getMarginX1();
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return opale.calculateDimension(stringBounder).getWidth() - opale.getMarginX1();
	}

	@Override
	final public LinkRendering getInLinkRendering() {
		return tile.getInLinkRendering();
	}

}
