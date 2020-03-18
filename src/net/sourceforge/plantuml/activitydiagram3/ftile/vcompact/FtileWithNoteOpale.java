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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.AlignmentParam;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.Styleable;
import net.sourceforge.plantuml.svek.image.Opale;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class FtileWithNoteOpale extends AbstractFtile implements Stencil, Styleable {

	private final Ftile tile;
	private final Opale opale;

	// private final HtmlColor arrowColor;
	private final NotePosition notePosition;
	private final double suppSpace = 20;
	private final Swimlane swimlaneNote;

	public StyleSignature getDefaultStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.note);
	}

	public Set<Swimlane> getSwimlanes() {
		if (swimlaneNote != null) {
			final Set<Swimlane> result = new HashSet<Swimlane>(tile.getSwimlanes());
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

	public static Ftile create(Ftile tile, Collection<PositionedNote> notes, ISkinParam skinParam, boolean withLink) {
		if (notes.size() > 1) {
			return new FtileWithNotes(tile, notes, skinParam);
		}
		if (notes.size() == 0) {
			throw new IllegalArgumentException();
		}
		return new FtileWithNoteOpale(tile, notes.iterator().next(), skinParam, withLink);
	}

	private FtileWithNoteOpale(Ftile tile, PositionedNote note, ISkinParam skinParam, boolean withLink) {
		super(tile.skinParam());
		this.swimlaneNote = note.getSwimlaneNote();
		if (note.getColors() != null) {
			skinParam = note.getColors().mute(skinParam);
		}
		this.tile = tile;
		this.notePosition = note.getNotePosition();
		if (note.getType() == NoteType.FLOATING_NOTE) {
			withLink = false;
		}

		final Rose rose = new Rose();

		final HColor noteBackgroundColor;
		final HColor borderColor;
		final FontConfiguration fc;

		final double shadowing;
		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinition().getMergedStyle(skinParam.getCurrentStyleBuilder()).eventuallyOverride(note.getColors());
			noteBackgroundColor = style.value(PName.BackGroundColor).asColor(getIHtmlColorSet());
			borderColor = style.value(PName.LineColor).asColor(getIHtmlColorSet());
			fc = style.getFontConfiguration(getIHtmlColorSet());
			shadowing = style.value(PName.Shadowing).asDouble();
		} else {
			noteBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.noteBackground);
			borderColor = rose.getHtmlColor(skinParam, ColorParam.noteBorder);
			fc = new FontConfiguration(skinParam, FontParam.NOTE, null);
			shadowing = skinParam.shadowing(null) ? 4 : 0;
		}

		final HorizontalAlignment align = skinParam.getHorizontalAlignment(AlignmentParam.noteTextAlignment, null,
				false);
		final Sheet sheet = new CreoleParser(fc, align, skinParam, CreoleMode.FULL).createSheet(note.getDisplay());
		final TextBlock text = new SheetBlock2(new SheetBlock1(sheet, skinParam.wrapWidth(), skinParam.getPadding()),
				this, new UStroke(1));
		opale = new Opale(shadowing, borderColor, noteBackgroundColor, text, withLink);

	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);
		final Dimension2D dimTile = tile.calculateDimension(stringBounder);
		final double yForFtile = (dimTotal.getHeight() - dimTile.getHeight()) / 2;
		final double marge;
		if (notePosition == NotePosition.LEFT) {
			marge = dimNote.getWidth() + suppSpace;
		} else {
			marge = 0;
		}

		return new UTranslate(marge, yForFtile);

	}

	private UTranslate getTranslateForOpale(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);

		final double yForNote = (dimTotal.getHeight() - dimNote.getHeight()) / 2;

		if (notePosition == NotePosition.LEFT) {
			return UTranslate.dy(yForNote);
		}
		final double dx = dimTotal.getWidth() - dimNote.getWidth();
		return new UTranslate(dx, yForNote);
	}

	public void drawU(UGraphic ug) {
		final Swimlane intoSw;
		if (ug instanceof UGraphicInterceptorOneSwimlane) {
			intoSw = ((UGraphicInterceptorOneSwimlane) ug).getSwimlane();
		} else {
			intoSw = null;
		}

		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);

		if (notePosition == NotePosition.LEFT) {
			final Direction strategy = Direction.RIGHT;
			final Point2D pp1 = new Point2D.Double(dimNote.getWidth(), dimNote.getHeight() / 2);
			final Point2D pp2 = new Point2D.Double(dimNote.getWidth() + suppSpace, dimNote.getHeight() / 2);
			opale.setOpale(strategy, pp1, pp2);
		} else {
			final Direction strategy = Direction.LEFT;
			final Point2D pp1 = new Point2D.Double(0, dimNote.getHeight() / 2);
			final Point2D pp2 = new Point2D.Double(-suppSpace, dimNote.getHeight() / 2);
			opale.setOpale(strategy, pp1, pp2);
		}
		if (swimlaneNote == null || intoSw == swimlaneNote) {
			opale.drawU(ug.apply(getTranslateForOpale(ug)));
		}
		ug.apply(getTranslate(stringBounder)).draw(tile);
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry orig = tile.calculateDimension(stringBounder);
		final UTranslate translate = getTranslate(stringBounder);
		if (orig.hasPointOut()) {
			return new FtileGeometry(dimTotal, orig.getLeft() + translate.getDx(), orig.getInY() + translate.getDy(),
					orig.getOutY() + translate.getDy());
		}
		return new FtileGeometry(dimTotal, orig.getLeft() + translate.getDx(), orig.getInY() + translate.getDy());
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);
		final Dimension2D dimTile = tile.calculateDimension(stringBounder);
		final double height = Math.max(dimNote.getHeight(), dimTile.getHeight());
		return new Dimension2DDouble(dimTile.getWidth() + 1 * dimNote.getWidth() + suppSpace, height);
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return -opale.getMarginX1();
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return opale.calculateDimension(stringBounder).getWidth() - opale.getMarginX1();
	}

}
