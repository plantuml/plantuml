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

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.url.Url;

final class EntityImageChenCompactEntity extends AbstractEntityImage {

	private static final int WEAK_INSET = 3;

	private final boolean weak;
	private final TextBlock title;
	private final TextBlock rows;
	private final Url url;

	EntityImageChenCompactEntity(Entity entity) {
		super(entity);
		this.weak = hasStereotype("<<weak>>");

		final FontConfiguration titleFontConfiguration = getStyleStateTitle(entity, getSkinParam())
				.getFontConfiguration(getSkinParam().getIHtmlColorSet(), entity.getColors());
		this.title = entity.getDisplay().create8(titleFontConfiguration, HorizontalAlignment.CENTER, getSkinParam(),
				CreoleMode.FULL, getStyleState().wrapWidth());
		this.rows = ChenCompactRows.create(entity);
		this.url = entity.getUrl99();
	}

	private boolean hasStereotype(String stereotype) {
		return getEntity().getStereotype() != null && getEntity().getStereotype().toString().contains(stereotype);
	}

	private Style getStyleState() {
		return getStyleState(getEntity(), getSkinParam());
	}

	@Override
	public StyleSignature getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chenEerDiagram, SName.chenEntity);
	}

	private static Style getStyleState(Entity entity, ISkinParam skinParam) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chenEerDiagram, SName.chenEntity)
				.withTOBECHANGED(entity.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private static Style getStyleStateTitle(Entity entity, ISkinParam skinParam) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chenEerDiagram, SName.chenEntity, SName.title)
				.withTOBECHANGED(entity.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	@Override
	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		final XDimension2D dimTitle = title.calculateDimension(stringBounder);
		final XDimension2D dimRows = rows.calculateDimension(stringBounder);
		final double width = Math.max(dimTitle.getWidth(), dimRows.getWidth()) + 2 * (MARGIN + MARGIN_LINE);
		final double height = dimTitle.getHeight() + dimRows.getHeight() + 4 * (MARGIN + MARGIN_LINE);
		return new XDimension2D(width, height);
	}

	@Override
	public void drawU(UGraphic ug) {
		ug.startGroup(UGroup.singletonMap(UGroupType.ID, getEntity().getQuark().toStringPoint()));
		if (url != null)
			ug.startUrl(url);

		final XDimension2D dimTotal = calculateDimension(ug.getStringBounder());
		final XDimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final UStroke stroke = getStyleState().getStroke(getEntity().getColors());
		final HColor border = getBorderColor();
		final HColor background = getBackgroundColor();
		final HColor titleBackground = getTitleBackgroundColor(background);
		final double dividerY = dimTitle.getHeight() + 2 * (MARGIN + MARGIN_LINE);

		if (titleBackground != null && titleBackground.equals(background) == false) {
			ug.apply(HColors.none()).apply(background.bg()).draw(getShape(dimTotal));
			final double titleInset = weak ? WEAK_INSET : 0;
			ug.apply(HColors.none()).apply(titleBackground.bg()).apply(new UTranslate(titleInset, titleInset))
					.draw(URectangle.build(dimTotal.getWidth() - 2 * titleInset, dividerY - titleInset));
			ug = ug.apply(border).apply(HColors.none().bg()).apply(stroke);
		} else {
			ug = ug.apply(border).apply(background.bg()).apply(stroke);
		}

		ug.draw(getShape(dimTotal));
		if (weak)
			ug.apply(new UTranslate(WEAK_INSET, WEAK_INSET)).draw(getShape(dimTotal.delta(-2 * WEAK_INSET)));

		final double xTitle = (dimTotal.getWidth() - dimTitle.getWidth()) / 2;
		final double yTitle = MARGIN + MARGIN_LINE;
		title.drawU(ug.apply(new UTranslate(xTitle, yTitle)));

		final double dividerInset = weak ? WEAK_INSET : 0;
		ug.apply(new UTranslate(dividerInset, dividerY)).draw(ULine.hline(dimTotal.getWidth() - 2 * dividerInset));
		rows.drawU(ug.apply(new UTranslate(MARGIN + MARGIN_LINE, dividerY + MARGIN + MARGIN_LINE)));

		if (url != null)
			ug.closeUrl();
		ug.closeGroup();
	}

	private HColor getBorderColor() {
		final Colors colors = getEntity().getColors();
		HColor border = colors.getColor(ColorType.LINE);
		if (border == null)
			border = getStyleState().value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
		return border;
	}

	private HColor getBackgroundColor() {
		final Colors colors = getEntity().getColors();
		HColor background = colors.getColor(ColorType.BACK);
		if (background == null)
			background = getStyleState().value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());
		return background;
	}

	private HColor getTitleBackgroundColor(HColor background) {
		final HColor explicitBackground = getEntity().getColors().getColor(ColorType.BACK);
		if (explicitBackground != null)
			return explicitBackground;

		final HColor titleBackground = getStyleStateTitle(getEntity(), getSkinParam()).value(PName.BackGroundColor)
				.asColor(getSkinParam().getIHtmlColorSet());
		return titleBackground == null ? background : titleBackground;
	}

	private URectangle getShape(XDimension2D dimTotal) {
		return URectangle.build(dimTotal);
	}

}
