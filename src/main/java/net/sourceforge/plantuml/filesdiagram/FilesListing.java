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
 */
package net.sourceforge.plantuml.filesdiagram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Sheet;
import net.sourceforge.plantuml.klimt.creole.SheetBlock1;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockMemoized;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleQueries;
import net.sourceforge.plantuml.style.StyleQuery;
import net.sourceforge.plantuml.svek.image.Opale;

/**
 * Draws the tree: one row per entry, indented by depth, with connector lines
 * from each folder to its children.
 */
public class FilesListing extends TextBlockMemoized {

	/** Horizontal shift between a folder and its children. */
	private static final double INDENT = 21;
	/** Vertical space between two rows. */
	private static final double ROW_SPACING = 2;
	/** Space between the icon and the name. */
	private static final double ICON_GAP = 6;
	/** Space between the name and its description. */
	private static final double DESCRIPTION_GAP = 10;
	/** Where the connector stops, on the left of the child row. */
	private static final double CONNECTOR_GAP = 2;
	/** Margin of the highlight box around a row. */
	private static final double BOX_MARGIN = 2;

	private final ISkinParam skinParam;
	private final FEntry root;
	private final FilesIcons icons;
	private final Map<FEntry, Row> rows = new HashMap<>();

	public FilesListing(ISkinParam skinParam, FEntry root, FilesIcons icons) {
		this.skinParam = skinParam;
		this.root = root;
		this.icons = icons;
	}

	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		final double[] maxWidth = new double[1];
		final double height = walk(null, stringBounder, root, -INDENT, 0, maxWidth);
		return new XDimension2D(maxWidth[0] + BOX_MARGIN, height);
	}

	@Override
	public void drawU(UGraphic ug) {
		walk(ug, ug.getStringBounder(), root, -INDENT, 0, new double[1]);
	}

	/**
	 * Lays out (and draws, if {@code ug} is not null) an entry and its
	 * descendants.
	 *
	 * @return the y position under the last row
	 */
	private double walk(UGraphic ug, StringBounder stringBounder, FEntry entry, double x, double y,
			double[] maxWidth) {
		double iconCenter = 0;
		if (entry.isRoot() == false) {
			final Row row = getRow(entry);
			final XDimension2D dim = row.calculateDimension(stringBounder);
			if (ug != null)
				row.drawU(ug.apply(new UTranslate(x, y)));
			maxWidth[0] = Math.max(maxWidth[0], x + dim.getWidth());
			iconCenter = x + row.getIconCenter(stringBounder);
			y += dim.getHeight() + ROW_SPACING;
		}

		final double childX = x + INDENT;
		final double startY = y;
		double lastCenter = -1;
		final FEntry last = entry.getLastEntryChild();
		for (FEntry child : entry) {
			final double childHeight = getRow(child).calculateDimension(stringBounder).getHeight();
			final double center = y + childHeight / 2;
			if (child.isNote() == false && entry.isRoot() == false && ug != null)
				drawConnector(ug, iconCenter, childX - CONNECTOR_GAP, center);
			if (child == last)
				lastCenter = center;
			y = walk(ug, stringBounder, child, childX, y, maxWidth);
		}
		if (lastCenter != -1 && entry.isRoot() == false && ug != null)
			drawConnector(ug.apply(new UTranslate(iconCenter, startY - ROW_SPACING)),
					ULine.vline(lastCenter - startY + ROW_SPACING));
		return y;
	}

	private void drawConnector(UGraphic ug, double x1, double x2, double y) {
		if (x2 > x1)
			drawConnector(ug.apply(new UTranslate(x1, y)), ULine.hline(x2 - x1));
	}

	private void drawConnector(UGraphic ug, ULine line) {
		final Style style = skinParam.getCurrentStyleBuilder().getMergedStyle(StyleQueries.FILESDIAG);
		if (style.getStroke().getThickness() <= 0)
			return;
		style.applyStrokeAndLineColor(ug, skinParam.getIHtmlColorSet()).draw(line);
	}

	private Row getRow(FEntry entry) {
		Row result = rows.get(entry);
		if (result == null) {
			result = new Row(entry);
			rows.put(entry, result);
		}
		return result;
	}

	private StyleQuery withStereotypes(StyleQuery query, List<String> stereotypes) {
		for (String s : stereotypes)
			query = query.withStereotype(s);
		return query;
	}

	/**
	 * What is drawn for one entry: icon, name and description, or a note.
	 */
	class Row extends TextBlockMemoized {

		private final FEntry entry;
		private final TextBlock icon;
		private final TextBlock content;
		private final HColor background;
		private final HColor border;

		Row(FEntry entry) {
			this.entry = entry;
			if (entry.isNote()) {
				this.icon = null;
				this.content = createOpale(entry.getNote());
				this.background = null;
				this.border = null;
				return;
			}
			final StyleQuery query = withStereotypes(
					entry.isFolder() ? StyleQueries.FILESDIAG_FOLDER : StyleQueries.FILESDIAG_FILE,
					entry.getStereotypes());
			final Style style = skinParam.getCurrentStyleBuilder().getMergedStyle(query);
			final FontConfiguration fc = style.getFontConfiguration(skinParam.getIHtmlColorSet());

			final String iconText = icons.getIcon(entry);
			this.icon = iconText.length() == 0 ? null : text(iconText, fc, CreoleMode.NO_CREOLE);

			TextBlock tmp = text(entry.getName(), fc, CreoleMode.NO_CREOLE);
			if (this.icon != null)
				tmp = TextBlockUtils.mergeLR(this.icon,
						TextBlockUtils.mergeLR(TextBlockUtils.empty(ICON_GAP, 0), tmp, VerticalAlignment.CENTER),
						VerticalAlignment.CENTER);
			if (entry.getDescription() != null && entry.getDescription().length() > 0) {
				final Style descStyle = skinParam.getCurrentStyleBuilder()
						.getMergedStyle(withStereotypes(StyleQueries.FILESDIAG_DESCRIPTION, entry.getStereotypes()));
				final TextBlock desc = text(entry.getDescription(),
						descStyle.getFontConfiguration(skinParam.getIHtmlColorSet()), CreoleMode.FULL);
				tmp = TextBlockUtils.mergeLR(tmp, TextBlockUtils.mergeLR(TextBlockUtils.empty(DESCRIPTION_GAP, 0),
						desc, VerticalAlignment.CENTER), VerticalAlignment.CENTER);
			}
			this.content = tmp;

			if (entry.getColor() != null) {
				this.background = skinParam.getIHtmlColorSet().getColorOrNull(entry.getColor());
				this.border = this.background;
			} else {
				final HColor back = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
				this.background = back == null || back.isTransparent() ? null : back;
				this.border = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
			}
		}

		private TextBlock text(String s, FontConfiguration fc, CreoleMode mode) {
			return Display.getWithNewlines(skinParam.getPragma(), s).create7(fc, HorizontalAlignment.LEFT, skinParam,
					mode);
		}

		double getIconCenter(StringBounder stringBounder) {
			if (icon == null)
				return INDENT / 2 - CONNECTOR_GAP;
			return icon.calculateDimension(stringBounder).getWidth() / 2;
		}

		@Override
		public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
			return content.calculateDimension(stringBounder);
		}

		@Override
		public void drawU(UGraphic ug) {
			if (background != null) {
				final XDimension2D dim = calculateDimension(ug.getStringBounder());
				final URectangle rect = URectangle
						.build(dim.getWidth() + 2 * BOX_MARGIN, dim.getHeight() + ROW_SPACING).rounded(4);
				UGraphic ugBox = ug.apply(background.bg()).apply(new UTranslate(-BOX_MARGIN, -ROW_SPACING / 2));
				ugBox = ugBox.apply(border == null ? background : border);
				ugBox.draw(rect);
			}
			content.drawU(ug);
		}

		private Opale createOpale(List<String> note) {
			final Style style = skinParam.getCurrentStyleBuilder().getMergedStyle(StyleQueries.FILESDIAG_NOTE);

			final FontConfiguration fc = FontConfiguration.create(skinParam, style);
			final double shadowing = style.getShadowing();
			final HColor borderColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
			final HColor noteBackgroundColor = style.value(PName.BackGroundColor)
					.asColor(skinParam.getIHtmlColorSet());
			final UStroke stroke = style.getStroke();

			final Sheet sheet = skinParam
					.sheet(fc, skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT), CreoleMode.FULL)
					.createSheet(Display.create(note));
			final SheetBlock1 sheet1 = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam.getPadding());
			return new Opale(shadowing, borderColor, noteBackgroundColor, sheet1, false, stroke);
		}

	}

}
