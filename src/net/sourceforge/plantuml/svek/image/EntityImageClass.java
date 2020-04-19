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
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.CornerParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineConfigurable;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizVersion;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Margins;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UComment;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class EntityImageClass extends AbstractEntityImage implements Stencil, WithPorts {

	final private TextBlock body;
	final private Margins shield;
	final private EntityImageClassHeader header;
	final private Url url;
	final private double roundCorner;
	final private LeafType leafType;

	final private LineConfigurable lineConfig;

	public EntityImageClass(GraphvizVersion version, ILeaf entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, entity.getColors(skinParam).mute(skinParam));
		this.leafType = entity.getLeafType();
		this.lineConfig = entity;
		this.roundCorner = getSkinParam().getRoundCorner(CornerParam.DEFAULT, null);
		this.shield = version != null && version.useShield() && entity.hasNearDecoration() ? Margins.uniform(16)
				: Margins.NONE;
		final boolean showMethods = portionShower.showPortion(EntityPortion.METHOD, entity);
		final boolean showFields = portionShower.showPortion(EntityPortion.FIELD, entity);
		this.body = entity.getBodier().getBody(FontParam.CLASS_ATTRIBUTE, getSkinParam(), showMethods, showFields,
				entity.getStereotype());

		header = new EntityImageClassHeader(entity, getSkinParam(), portionShower);
		this.url = entity.getUrl99();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimHeader = header.calculateDimension(stringBounder);
		final Dimension2D dimBody = body == null ? new Dimension2DDouble(0, 0) : body.calculateDimension(stringBounder);
		double width = Math.max(dimBody.getWidth(), dimHeader.getWidth());
		if (width < getSkinParam().minClassWidth()) {
			width = getSkinParam().minClassWidth();
		}
		final double height = dimBody.getHeight() + dimHeader.getHeight();
		return new Dimension2DDouble(width, height);
	}

	@Override
	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		final Rectangle2D result = body.getInnerPosition(member, stringBounder, strategy);
		if (result == null) {
			return result;
		}
		final Dimension2D dimHeader = header.calculateDimension(stringBounder);
		final UTranslate translate = UTranslate.dy(dimHeader.getHeight());
		return translate.apply(result);
	}

	final public void drawU(UGraphic ug) {
		ug.draw(new UComment("class " + getEntity().getCodeGetName()));
		if (url != null) {
			ug.startUrl(url);
		}
		drawInternal(ug);

		if (url != null) {
			ug.closeAction();
		}
	}

	private void drawInternal(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dimHeader = header.calculateDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = new URectangle(widthTotal, heightTotal).rounded(roundCorner)
				.withComment(getEntity().getCodeGetName());
		if (getSkinParam().shadowing(getEntity().getStereotype())) {
			rect.setDeltaShadow(4);
		}

		HColor classBorder = lineConfig.getColors(getSkinParam()).getColor(ColorType.LINE);
		if (classBorder == null) {
			classBorder = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.classBorder);
		}
		ug = ug.apply(classBorder);
		HColor backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
		if (backcolor == null) {
			if (leafType == LeafType.ENUM) {
				backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.enumBackground,
						ColorParam.classBackground);
			} else {
				backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.classBackground);
			}
		}
		ug = ug.apply(backcolor.bg());

		final UStroke stroke = getStroke();

		HColor headerBackcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.HEADER);
		if (headerBackcolor == null) {
			headerBackcolor = getSkinParam().getHtmlColor(ColorParam.classHeaderBackground, getStereo(), false);
		}
		UGraphic ugHeader = ug;
		if (roundCorner == 0 && headerBackcolor != null && backcolor.equals(headerBackcolor) == false) {
			ug.apply(stroke).draw(rect);
			final Shadowable rect2 = new URectangle(widthTotal, dimHeader.getHeight());
			rect2.setDeltaShadow(0);
			ugHeader = ugHeader.apply(headerBackcolor.bg());
			ugHeader.apply(stroke).draw(rect2);
		} else if (roundCorner != 0 && headerBackcolor != null && backcolor.equals(headerBackcolor) == false) {
			ug.apply(stroke).draw(rect);
			final Shadowable rect2 = new URectangle(widthTotal, dimHeader.getHeight()).rounded(roundCorner);
			final URectangle rect3 = new URectangle(widthTotal, roundCorner / 2);
			rect2.setDeltaShadow(0);
			rect3.setDeltaShadow(0);
			ugHeader = ugHeader.apply(headerBackcolor.bg()).apply(headerBackcolor);
			ugHeader.apply(stroke).draw(rect2);
			ugHeader.apply(stroke).apply(UTranslate.dy(dimHeader.getHeight() - rect3.getHeight())).draw(rect3);
			rect.setDeltaShadow(0);
			ug.apply(stroke).apply(new HColorNone().bg()).draw(rect);
		} else {
			ug.apply(stroke).draw(rect);
		}
		header.drawU(ugHeader, dimTotal.getWidth(), dimHeader.getHeight());

		if (body != null) {
			final UGraphic ug2 = UGraphicStencil.create(ug, this, stroke);
			final UTranslate translate = UTranslate.dy(dimHeader.getHeight());
			body.drawU(ug2.apply(translate));
		}
	}

	public Ports getPorts(StringBounder stringBounder) {
		final Dimension2D dimHeader = header.calculateDimension(stringBounder);
		return ((WithPorts) body).getPorts(stringBounder).translateY(dimHeader.getHeight());
	}

	private UStroke getStroke() {
		UStroke stroke = lineConfig.getColors(getSkinParam()).getSpecificLineStroke();
		if (stroke == null) {
			stroke = getSkinParam().getThickness(LineParam.classBorder, getStereo());
		}
		if (stroke == null) {
			stroke = new UStroke(1.5);
		}
		return stroke;
	}

	public ShapeType getShapeType() {
		if (((ILeaf) getEntity()).getPortShortNames().size() > 0) {
			return ShapeType.RECTANGLE_HTML_FOR_PORTS;
		}
		return ShapeType.RECTANGLE;
	}

	@Override
	public Margins getShield(StringBounder stringBounder) {
		return shield;
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return 0;
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return calculateDimension(stringBounder).getWidth();
	}

}
