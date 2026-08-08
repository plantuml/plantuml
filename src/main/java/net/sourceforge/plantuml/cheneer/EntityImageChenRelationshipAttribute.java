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
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;

final class EntityImageChenRelationshipAttribute extends AbstractEntityImage {

	private static final int PADDING = 10;

	private final TextBlock rows;

	EntityImageChenRelationshipAttribute(Entity entity) {
		super(entity);
		this.rows = ChenCompactRows.create(entity);
	}

	@Override
	public StyleSignature getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chenEerDiagram, SName.chenRelationship);
	}

	private Style getStyle() {
		return getStyleSignature().withTOBECHANGED(getEntity().getStereotype())
				.getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	@Override
	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		return rows.calculateDimension(stringBounder).delta(PADDING * 2);
	}

	@Override
	public void drawU(UGraphic ug) {
		ug.startGroup(UGroup.singletonMap(UGroupType.ID, getEntity().getQuark().toStringPoint()));
		final XDimension2D dim = calculateDimension(ug.getStringBounder());
		ug = applyColor(ug).apply(getStyle().getStroke(getEntity().getColors()));
		ug.draw(URectangle.build(dim));
		rows.drawU(ug.apply(new UTranslate(PADDING, PADDING)));
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

}
