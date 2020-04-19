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
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Node;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageActivity extends AbstractEntityImage {

	private double shadowing = 0;
	public static final int CORNER = 25;
	final private TextBlock desc;
	final private static int MARGIN = 10;
	final private Url url;
	private final Bibliotekon bibliotekon;

	public EntityImageActivity(ILeaf entity, ISkinParam skinParam, Bibliotekon bibliotekon) {
		super(entity, skinParam);
		this.bibliotekon = bibliotekon;
		final Stereotype stereotype = entity.getStereotype();

		final FontConfiguration fontConfiguration;
		final HorizontalAlignment horizontalAlignment;
		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinition().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
			fontConfiguration = style.getFontConfiguration(skinParam.getIHtmlColorSet());
			horizontalAlignment = style.getHorizontalAlignment();
			shadowing = style.value(PName.Shadowing).asDouble();
		} else {
			fontConfiguration = new FontConfiguration(getSkinParam(), FontParam.ACTIVITY, stereotype);
			horizontalAlignment = HorizontalAlignment.CENTER;
			if (getSkinParam().shadowing(getEntity().getStereotype())) {
				shadowing = 4;
			}
		}
		this.desc = entity.getDisplay().create(fontConfiguration, horizontalAlignment, skinParam);
		this.url = entity.getUrl99();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = desc.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, MARGIN * 2);
	}

	final public void drawU(UGraphic ug) {
		if (url != null) {
			ug.startUrl(url);
		}
		if (getShapeType() == ShapeType.ROUND_RECTANGLE) {
			ug = drawNormal(ug);
		} else if (getShapeType() == ShapeType.OCTAGON) {
			ug = drawOctagon(ug);
		} else {
			throw new UnsupportedOperationException();
		}
		if (url != null) {
			ug.closeAction();
		}
	}

	private UGraphic drawOctagon(UGraphic ug) {
		final Node node = bibliotekon.getNode(getEntity());
		final Shadowable octagon = node.getOctagon();
		octagon.setDeltaShadow(shadowing);
		ug = applyColors(ug);
		ug.apply(new UStroke(1.5)).draw(octagon);
		desc.drawU(ug.apply(new UTranslate(MARGIN, MARGIN)));
		return ug;

	}

	private UGraphic drawNormal(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = new URectangle(widthTotal, heightTotal).rounded(CORNER);
		rect.setDeltaShadow(shadowing);

		ug = applyColors(ug);
		UStroke stroke = new UStroke(1.5);
		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinition().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
			stroke = style.getStroke();
		}
		ug.apply(stroke).draw(rect);

		desc.drawU(ug.apply(new UTranslate(MARGIN, MARGIN)));
		return ug;
	}

	public StyleSignature getDefaultStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity).with(getStereo());
	}

	private UGraphic applyColors(UGraphic ug) {
		HColor borderColor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.activityBorder);
		HColor backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.activityBackground);
		}

		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinition().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
			borderColor = style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
			backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
			if (backcolor == null) {
				backcolor = style.value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());
			}
		}
		ug = ug.apply(borderColor);
		ug = ug.apply(backcolor.bg());
		return ug;
	}

	public ShapeType getShapeType() {
		final Stereotype stereotype = getStereo();
		if (getSkinParam().useOctagonForActivity(stereotype)) {
			return ShapeType.OCTAGON;
		}
		return ShapeType.ROUND_RECTANGLE;
	}

}
