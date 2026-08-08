/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 */
package net.sourceforge.plantuml.cheneer;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

/** Internal bridge between the Chen model and its Svek renderers. */
public final class ChenCompactRows {

	private static final int INDENT = 10;
	private static final int ROW_GAP = 2;

	private ChenCompactRows() {
	}

	public static boolean hasRows(Entity entity) {
		return getRows(entity).isEmpty() == false;
	}

	public static TextBlock create(Entity entity) {
		final ChenEerDiagram diagram = (ChenEerDiagram) entity.getDiagram();
		final List<Row> rows = new ArrayList<>();
		for (CompactChenAttribute attribute : diagram.getCompactAttributes(entity)) {
			final Style style = StyleSignatureBasic
					.of(SName.root, SName.element, SName.chenEerDiagram, SName.chenAttribute, SName.title)
					.withTOBECHANGED(attribute.getStereotype())
					.getMergedStyle(entity.getSkinParam().getCurrentStyleBuilder());
			final FontConfiguration font = style.getFontConfiguration(entity.getSkinParam().getIHtmlColorSet());
			rows.add(new Row(entity, attribute, font, style.wrapWidth()));
		}
		return new RowsBlock(rows);
	}

	private static List<CompactChenAttribute> getRows(Entity entity) {
		return ((ChenEerDiagram) entity.getDiagram()).getCompactAttributes(entity);
	}

	private static final class RowsBlock implements TextBlock {
		private final List<Row> rows;

		private RowsBlock(List<Row> rows) {
			this.rows = rows;
		}

		@Override
		public XDimension2D calculateDimension(StringBounder stringBounder) {
			double width = 0;
			double height = 0;
			for (Row row : rows) {
				final XDimension2D dim = row.calculateDimension(stringBounder);
				width = Math.max(width, dim.getWidth());
				height += dim.getHeight() + ROW_GAP;
			}
			if (height > 0)
				height -= ROW_GAP;
			return new XDimension2D(width, height);
		}

		@Override
		public void drawU(UGraphic ug) {
			double y = 0;
			for (Row row : rows) {
				row.drawU(ug.apply(UTranslate.dy(y)));
				y += row.calculateDimension(ug.getStringBounder()).getHeight() + ROW_GAP;
			}
		}
	}

	private static final class Row implements TextBlock {
		private final int depth;
		private final boolean solidUnderline;
		private final boolean dashedUnderline;
		private final FontConfiguration font;
		private final TextBlock openBrace;
		private final TextBlock name;
		private final TextBlock derived;
		private final TextBlock closeBrace;
		private final TextBlock domain;

		private Row(Entity entity, CompactChenAttribute attribute, FontConfiguration font,
				LineBreakStrategy wrapWidth) {
			this.depth = attribute.getDepth();
			this.solidUnderline = attribute.getUnderlineStyle() == CompactChenAttribute.UnderlineStyle.SOLID;
			this.dashedUnderline = attribute.getUnderlineStyle() == CompactChenAttribute.UnderlineStyle.DASHED;
			this.font = font;
			final boolean multi = attribute.isMulti();
			this.openBrace = text(entity, multi ? "{" : "", font, wrapWidth);
			this.name = text(entity, attribute.getDisplayName(), font, wrapWidth);
			this.derived = text(entity, attribute.isDerived() ? "()" : "", font, wrapWidth);
			this.closeBrace = text(entity, multi ? "}" : "", font, wrapWidth);
			this.domain = text(entity, attribute.getDomain() == null ? "" : " : " + attribute.getDomain(), font,
					wrapWidth);
		}

		private static TextBlock text(Entity entity, String value, FontConfiguration font,
				LineBreakStrategy wrapWidth) {
			if (value.length() == 0)
				return TextBlockUtils.EMPTY_TEXT_BLOCK;

			return Display.getWithNewlines(((ChenEerDiagram) entity.getDiagram()).getPragma(), value).create8(font,
					HorizontalAlignment.LEFT, entity.getSkinParam(), CreoleMode.FULL, wrapWidth);
		}

		@Override
		public XDimension2D calculateDimension(StringBounder stringBounder) {
			final XDimension2D openDim = openBrace.calculateDimension(stringBounder);
			final XDimension2D nameDim = name.calculateDimension(stringBounder);
			final XDimension2D derivedDim = derived.calculateDimension(stringBounder);
			final XDimension2D closeDim = closeBrace.calculateDimension(stringBounder);
			final XDimension2D domainDim = domain.calculateDimension(stringBounder);
			final double width = depth * INDENT + openDim.getWidth() + nameDim.getWidth() + derivedDim.getWidth()
					+ closeDim.getWidth() + domainDim.getWidth();
			final double height = Math.max(nameDim.getHeight(), Math.max(openDim.getHeight(), Math.max(derivedDim.getHeight(),
					Math.max(closeDim.getHeight(), domainDim.getHeight()))));
			return new XDimension2D(width, height);
		}

		@Override
		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			double x = depth * INDENT;
			x = draw(openBrace, ug, stringBounder, x);
			final double nameStart = x;
			x = draw(name, ug, stringBounder, x);
			final double nameWidth = x - nameStart;
			x = draw(derived, ug, stringBounder, x);
			x = draw(closeBrace, ug, stringBounder, x);
			draw(domain, ug, stringBounder, x);

			if (solidUnderline || dashedUnderline) {
				final double y = name.calculateDimension(stringBounder).getHeight() - 1;
				final UStroke stroke = dashedUnderline ? new UStroke(2, 2, 1) : UStroke.withThickness(1);
				ug.apply(font.getColor()).apply(stroke).apply(new UTranslate(nameStart, y)).draw(ULine.hline(nameWidth));
			}
		}

		private static double draw(TextBlock block, UGraphic ug, StringBounder stringBounder, double x) {
			block.drawU(ug.apply(UTranslate.dx(x)));
			return x + block.calculateDimension(stringBounder).getWidth();
		}
	}

}
