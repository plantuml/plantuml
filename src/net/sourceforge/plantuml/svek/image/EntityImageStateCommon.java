/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineConfigurable;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.baraye.IEntity;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public abstract class EntityImageStateCommon extends AbstractEntityImage {

	final protected TextBlock title;
	final protected Url url;

	final protected LineConfigurable lineConfig;

	public EntityImageStateCommon(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);

		this.lineConfig = entity;

		final FontConfiguration titleFontConfiguration = getStyleStateTitle(entity, skinParam).getFontConfiguration(
				getSkinParam().getIHtmlColorSet(), entity.getColors());

		this.title = entity.getDisplay().create8(titleFontConfiguration, HorizontalAlignment.CENTER, skinParam,
				CreoleMode.FULL, getStyleState().wrapWidth());
		this.url = entity.getUrl99();

	}

	public static Style getStyleStateTitle(IEntity group, ISkinParam skinParam) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state, SName.title)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	public static Style getStyleStateHeader(IEntity group, ISkinParam skinParam) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state, SName.header)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	public static Style getStyleStateHeader(EntityImp group, ISkinParam skinParam) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state, SName.header)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	public static Style getStyleState(IEntity group, ISkinParam skinParam) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	public static Style getStyleStateBody(IEntity group, ISkinParam skinParam) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.stateBody)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	final protected Style getStyleState() {
		return getStyleState(getEntity(), getSkinParam());
	}

	final protected Style getStyleStateHeader() {
		return getStyleStateHeader(getEntity(), getSkinParam());
	}

	final public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}

	final protected URectangle getShape(final XDimension2D dimTotal) {

		final double corner = getStyleState().value(PName.RoundCorner).asDouble();
		final double deltaShadow = getStyleState().value(PName.Shadowing).asDouble();

		final URectangle rect = new URectangle(dimTotal).rounded(corner);
		rect.setDeltaShadow(deltaShadow);
		return rect;
	}

	final protected UGraphic applyColor(UGraphic ug) {

		HColor border = lineConfig.getColors().getColor(ColorType.LINE);
		if (border == null)
			border = getStyleState().value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());

		ug = ug.apply(border);
		HColor backcolor = lineConfig.getColors().getColor(ColorType.BACK);
		if (backcolor == null)
			backcolor = getStyleState().value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());

		ug = ug.apply(backcolor.bg());

		return ug;
	}

}
