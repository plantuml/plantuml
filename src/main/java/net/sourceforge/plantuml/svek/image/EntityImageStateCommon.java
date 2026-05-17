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
package net.sourceforge.plantuml.svek.image;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LineConfigurable;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.url.Url;

public abstract class EntityImageStateCommon extends AbstractEntityImage {

	public static final StyleSignatureBasic STYLE = StyleSignatureBasic.of(SName.root, SName.element,
			SName.stateDiagram, SName.state);

	final protected TextBlock name;
	final protected Url url;

	final protected LineConfigurable lineConfig;

	public EntityImageStateCommon(Entity entity) {
		super(entity);

		this.lineConfig = entity;

		final FontConfiguration nameFc = getStyleStateName(entity.getStereotype(),
				getSkinParam().getCurrentStyleBuilder())
				.getFontConfiguration(getSkinParam().getIHtmlColorSet(), entity.getColors());

		final HorizontalAlignment horizontalAlignment = getStyleState(entity.getStereotype(),
				getSkinParam().getCurrentStyleBuilder()).getHorizontalAlignment();
		this.name = entity.getDisplay().create8(nameFc, horizontalAlignment, getSkinParam(), CreoleMode.FULL,
				getStyleState().wrapWidth());
		this.url = entity.getUrl99();

	}

	@Override
	public StyleSignatureBasic getStyleSignature() {
		return STYLE;
	}

	public static Style getStyleStateName(Stereotype stereotype, StyleBuilder styleBuilder) {
		final StyleSignatureBasic toto1 = STYLE.addSName(SName.name);
		return toto1.withTOBECHANGED(stereotype).getMergedStyle(styleBuilder);
	}

	public static Style getStyleStateDescription(Stereotype stereotype, StyleBuilder styleBuilder) {
		return STYLE.addSName(SName.description).withTOBECHANGED(stereotype).getMergedStyle(styleBuilder);
	}

	public static Style getStyleState(Stereotype stereotype, StyleBuilder styleBuilder) {
		return STYLE.withTOBECHANGED(stereotype).getMergedStyle(styleBuilder);
	}

	public static Style getStyleStateBody(Stereotype stereotype, StyleBuilder styleBuilder) {
		return STYLE.addSName(SName.body).withTOBECHANGED(stereotype).getMergedStyle(styleBuilder);
	}

	final protected Style getStyleState() {
		return getStyleState(getEntity().getStereotype(), getSkinParam().getCurrentStyleBuilder());
	}

	final protected Style getStyleStateDescription() {
		return STYLE.addSName(SName.name).withTOBECHANGED(getEntity().getStereotype())
				.getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	final public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}

	final protected URectangle getShape(final XDimension2D dimTotal) {

		final double corner = getStyleState().value(PName.RoundCorner).asDouble();
		final double deltaShadow = getStyleState().getShadowing();

		final URectangle rect = URectangle.build(dimTotal).rounded(corner);
		rect.setDeltaShadow(deltaShadow);
		return rect;
	}

	final protected UGraphic applyColor(UGraphic ug, Style style) {

		HColor border = getBorderColor();

		ug = ug.apply(border);
		HColor backcolor = lineConfig.getColors().getColor(ColorType.BACK);
		if (backcolor == null)
			backcolor = style.value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());

		ug = ug.apply(backcolor.bg());

		return ug;
	}

	final protected HColor getBorderColor() {
		HColor border = lineConfig.getColors().getColor(ColorType.LINE);
		if (border == null)
			border = getStyleState().value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
		return border;
	}

}
