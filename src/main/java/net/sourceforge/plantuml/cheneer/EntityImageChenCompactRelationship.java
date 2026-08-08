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

import java.util.Arrays;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.UShape;
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
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.url.Url;

final class EntityImageChenCompactRelationship extends AbstractEntityImage {

	private final boolean identifying;
	private final TextBlock title;
	private final Url url;

	EntityImageChenCompactRelationship(Entity entity) {
		super(entity);
		this.identifying = hasStereotype("<<identifying>>");
		final FontConfiguration titleFontConfiguration = getStyleTitle(entity, getSkinParam())
				.getFontConfiguration(getSkinParam().getIHtmlColorSet(), entity.getColors());
		this.title = entity.getDisplay().create8(titleFontConfiguration, HorizontalAlignment.CENTER, getSkinParam(),
				CreoleMode.FULL, getStyle().wrapWidth());
		this.url = entity.getUrl99();
	}

	private boolean hasStereotype(String stereotype) {
		return getEntity().getStereotype() != null && getEntity().getStereotype().toString().contains(stereotype);
	}

	private Style getStyle() {
		return getStyle(getEntity(), getSkinParam());
	}

	@Override
	public StyleSignature getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chenEerDiagram, SName.chenRelationship);
	}

	private Style getStyle(Entity entity, ISkinParam skinParam) {
		return getStyleSignature().withTOBECHANGED(entity.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private static Style getStyleTitle(Entity entity, ISkinParam skinParam) {
		return StyleSignatureBasic
				.of(SName.root, SName.element, SName.chenEerDiagram, SName.chenRelationship, SName.title)
				.withTOBECHANGED(entity.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	@Override
	public ShapeType getShapeType() {
		return ShapeType.DIAMOND;
	}

	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		final XDimension2D dimTitle = title.calculateDimension(stringBounder);
		final double diagonal = (dimTitle.getWidth() + 2 * dimTitle.getHeight()) / Math.sqrt(5) + 2 * MARGIN;
		return new XDimension2D(diagonal * Math.sqrt(5), diagonal * Math.sqrt(5) / 2);
	}

	@Override
	public void drawU(UGraphic ug) {
		ug.startGroup(UGroup.singletonMap(UGroupType.ID, getEntity().getQuark().toStringPoint()));
		if (url != null)
			ug.startUrl(url);

		final XDimension2D dimTotal = calculateDimension(ug.getStringBounder());
		final XDimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final UStroke stroke = getStyle().getStroke(getEntity().getColors());
		ug = applyColor(ug).apply(stroke);
		if (identifying) {
			ug.apply(HColors.WHITE.bg()).draw(getShape(dimTotal));
			ug.apply(new UTranslate(10, 5)).draw(getShape(dimTotal.delta(-20, -10)));
		} else {
			ug.draw(getShape(dimTotal));
		}

		final double xTitle = (dimTotal.getWidth() - dimTitle.getWidth()) / 2;
		final double yTitle = (dimTotal.getHeight() - dimTitle.getHeight()) / 2;
		title.drawU(ug.apply(new UTranslate(xTitle, yTitle)));

		if (url != null)
			ug.closeUrl();
		ug.closeGroup();
	}

	private UGraphic applyColor(UGraphic ug) {
		final Colors colors = getEntity().getColors();
		HColor border = colors.getColor(ColorType.LINE);
		if (border == null)
			border = getStyle().value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
		HColor background = colors.getColor(ColorType.BACK);
		if (background == null)
			background = getStyle().value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());
		return ug.apply(border).apply(background.bg());
	}

	private UShape getShape(XDimension2D dimTotal) {
		final double width = dimTotal.getWidth();
		final double height = dimTotal.getHeight();
		return new UPolygon(Arrays.asList(new XPoint2D(0, height / 2), new XPoint2D(width / 2, 0),
				new XPoint2D(width, height / 2), new XPoint2D(width / 2, height)));
	}

}
