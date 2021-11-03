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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineConfigurable;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public abstract class EntityImageStateCommon extends AbstractEntityImage {

	final protected TextBlock desc;
	final protected Url url;

	final protected LineConfigurable lineConfig;

	public EntityImageStateCommon(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);

		this.lineConfig = entity;
		final Stereotype stereotype = entity.getStereotype();

		final FontConfiguration fontConfiguration;

		if (UseStyle.useBetaStyle())
			fontConfiguration = getStyleStateHeader().getFontConfiguration(getSkinParam().getThemeStyle(),
					getSkinParam().getIHtmlColorSet());
		else
			fontConfiguration = new FontConfiguration(getSkinParam(), FontParam.STATE, stereotype);

		this.desc = entity.getDisplay().create8(fontConfiguration, HorizontalAlignment.CENTER, skinParam,
				CreoleMode.FULL, skinParam.wrapWidth());
		this.url = entity.getUrl99();

	}

	private Style getStyleStateHeader() {
		return StyleSignature.of(SName.root, SName.element, SName.stateDiagram, SName.state, SName.header)
				.with(getEntity().getStereotype()).getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	final protected Style getStyleState() {
		return StyleSignature.of(SName.root, SName.element, SName.stateDiagram, SName.state)
				.with(getEntity().getStereotype()).getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	final protected UStroke getStroke() {
		UStroke stroke = lineConfig.getColors(getSkinParam()).getSpecificLineStroke();
		if (stroke == null) {
			stroke = new UStroke(1.5);
		}
		return stroke;
	}

	final public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}

	final protected URectangle getShape(final Dimension2D dimTotal) {
		double deltaShadow = 0;
		final double corner;
		if (UseStyle.useBetaStyle()) {
			corner = getStyleState().value(PName.RoundCorner).asDouble();
			deltaShadow = getStyleState().value(PName.Shadowing).asDouble();
		} else {
			corner = CORNER;
			if (getSkinParam().shadowing(getEntity().getStereotype()))
				deltaShadow = 4;
		}

		final URectangle rect = new URectangle(dimTotal).rounded(corner);
		rect.setDeltaShadow(deltaShadow);
		return rect;
	}

	final protected UGraphic applyColor(UGraphic ug) {

		HColor classBorder = lineConfig.getColors(getSkinParam()).getColor(ColorType.LINE);
		if (classBorder == null) {
			if (UseStyle.useBetaStyle())
				classBorder = getStyleState().value(PName.LineColor).asColor(getSkinParam().getThemeStyle(),
						getSkinParam().getIHtmlColorSet());
			else
				classBorder = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.stateBorder);
		}
		ug = ug.apply(getStroke()).apply(classBorder);
		HColor backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
		if (backcolor == null) {
			if (UseStyle.useBetaStyle())
				backcolor = getStyleState().value(PName.BackGroundColor).asColor(getSkinParam().getThemeStyle(),
						getSkinParam().getIHtmlColorSet());
			else
				backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.stateBackground);

		}
		ug = ug.apply(backcolor.bg());

		return ug;
	}

}
