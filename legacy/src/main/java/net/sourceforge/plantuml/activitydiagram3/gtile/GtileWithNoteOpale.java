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
package net.sourceforge.plantuml.activitydiagram3.gtile;

import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
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
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.skin.AlignmentParam;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.style.Styleable;
import net.sourceforge.plantuml.svek.image.Opale;

public class GtileWithNoteOpale extends AbstractGtile implements Stencil, Styleable {

	private final Gtile tile;
	private final Opale opale;

	private final NotePosition notePosition;
	private final double suppSpace = 20;
	private final Swimlane swimlaneNote;

	private final UTranslate positionNote;
	private final UTranslate positionTile;

	private final XDimension2D dimNote;
	private final XDimension2D dimTile;

	public StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.note);
	}

	@Override
	public Swimlane getSwimlane(String point) {
		return tile.getSwimlane(point);
	}

	@Override
	public Set<Swimlane> getSwimlanes() {
		return tile.getSwimlanes();
	}

	public GtileWithNoteOpale(Gtile tile, PositionedNote note, ISkinParam skinParam, boolean withLink) {
		super(tile.getStringBounder(), tile.skinParam());
		this.swimlaneNote = note.getSwimlaneNote();
		if (note.getColors() != null)
			skinParam = note.getColors().mute(skinParam);

		this.tile = tile;
		this.notePosition = note.getNotePosition();
		if (note.getType() == NoteType.FLOATING_NOTE)
			withLink = false;

		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder())
				.eventuallyOverride(note.getColors());
		final HColor noteBackgroundColor = style.value(PName.BackGroundColor).asColor(getIHtmlColorSet());
		final HColor borderColor = style.value(PName.LineColor).asColor(getIHtmlColorSet());
		final FontConfiguration fc = style.getFontConfiguration(getIHtmlColorSet());
		final double shadowing = style.value(PName.Shadowing).asDouble();
		final LineBreakStrategy wrapWidth = style.wrapWidth();
		final UStroke stroke = style.getStroke();

		final HorizontalAlignment align = skinParam.getHorizontalAlignment(AlignmentParam.noteTextAlignment, null,
				false, null);
		final Sheet sheet = skinParam.sheet(fc, align, CreoleMode.FULL).createSheet(note.getDisplay());
		final TextBlock text = new SheetBlock2(new SheetBlock1(sheet, wrapWidth, skinParam.getPadding()), this,
				UStroke.withThickness(1));
		this.opale = new Opale(shadowing, borderColor, noteBackgroundColor, text, withLink, stroke);

		this.dimNote = opale.calculateDimension(stringBounder);
		this.dimTile = tile.calculateDimension(stringBounder);

		final XDimension2D dimTotal = calculateDimension(stringBounder);

		if (note.getNotePosition() == NotePosition.LEFT) {
			this.positionNote = new UTranslate(0, (dimTotal.getHeight() - dimNote.getHeight()) / 2);
			this.positionTile = new UTranslate(dimNote.getWidth() + suppSpace,
					(dimTotal.getHeight() - dimTile.getHeight()) / 2);
		} else {
			this.positionNote = new UTranslate(dimTile.getWidth() + suppSpace,
					(dimTotal.getHeight() - dimNote.getHeight()) / 2);
			this.positionTile = new UTranslate(0, (dimTotal.getHeight() - dimTile.getHeight()) / 2);
		}
	}

	@Override
	protected UTranslate getCoordImpl(String name) {
		return tile.getCoord(name).compose(positionTile);
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final double height = Math.max(dimNote.getHeight(), dimTile.getHeight());
		return new XDimension2D(dimTile.getWidth() + dimNote.getWidth() + suppSpace, height);
	}

	@Override
	protected void drawUInternal(UGraphic ug) {
		opale.drawU(ug.apply(positionNote));
		tile.drawU(ug.apply(positionTile));
	}

	@Override
	public double getStartingX(StringBounder stringBounder, double y) {
		return -opale.getMarginX1();
	}

	@Override
	public double getEndingX(StringBounder stringBounder, double y) {
		return opale.calculateDimension(stringBounder).getWidth() - opale.getMarginX1();
	}

}
