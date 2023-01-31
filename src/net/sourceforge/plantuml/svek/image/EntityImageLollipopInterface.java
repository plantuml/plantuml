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

import java.util.EnumMap;
import java.util.Map;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGroupType;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageLollipopInterface extends AbstractEntityImage {

	private static final int SIZE = 10;

	private final TextBlock desc;
	private final SName sname;
	private final Url url;

	public StyleSignature getSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, sname, SName.circle).withTOBECHANGED(getStereo());
	}

	private UStroke getUStroke() {
		return new UStroke(1.5);
	}

	public EntityImageLollipopInterface(EntityImp entity, ISkinParam skinParam, SName sname) {
		super(entity, skinParam);
		this.sname = sname;

		final FontConfiguration fc = FontConfiguration.create(getSkinParam(),
				getSignature().getMergedStyle(skinParam.getCurrentStyleBuilder()));

		this.desc = entity.getDisplay().create(fc, HorizontalAlignment.CENTER, skinParam);
		this.url = entity.getUrl99();

	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(SIZE, SIZE);
	}

	final public void drawU(UGraphic ug) {

		final Style style = getSignature().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
		final HColor backgroundColor = style.value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());
		final HColor borderColor = style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
		final double shadow = style.value(PName.Shadowing).asDouble();

		final UEllipse circle;
		if (getEntity().getLeafType() == LeafType.LOLLIPOP_HALF) {
			circle = new UEllipse(SIZE, SIZE, angle - 90, 180);
		} else {
			circle = new UEllipse(SIZE, SIZE);
			if (getSkinParam().shadowing(getEntity().getStereotype()))
				circle.setDeltaShadow(shadow);
		}

		ug = ug.apply(backgroundColor.bg()).apply(borderColor);
		if (url != null)
			ug.startUrl(url);

		final Map<UGroupType, String> typeIDent = new EnumMap<>(UGroupType.class);
		typeIDent.put(UGroupType.CLASS, "elem " + getEntity().getCode() + " selected");
		typeIDent.put(UGroupType.ID, "elem_" + getEntity().getCode());
		ug.startGroup(typeIDent);
		ug.apply(getUStroke()).draw(circle);
		ug.closeGroup();

		final XDimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
		final double widthDesc = dimDesc.getWidth();

		final double x = SIZE / 2 - widthDesc / 2;
		final double y = SIZE;
		desc.drawU(ug.apply(new UTranslate(x, y)));
		if (url != null)
			ug.closeUrl();

	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}

	private double angle;

	public void addImpact(double angle) {
		this.angle = 180 - (angle * 180 / Math.PI);
	}

}
