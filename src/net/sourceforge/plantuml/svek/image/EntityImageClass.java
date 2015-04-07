/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineConfigurable;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizVersion;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class EntityImageClass extends AbstractEntityImage implements Stencil {

	final private TextBlock body;
	final private int shield;
	final private EntityImageClassHeader2 header;
	final private Url url;
	final private TextBlock mouseOver;
	final private double roundCorner;

	final private LineConfigurable lineConfig;

	public EntityImageClass(GraphvizVersion version, ILeaf entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);
		this.lineConfig = entity;
		this.roundCorner = skinParam.getRoundCorner();
		this.shield = version != null && version.useShield() && entity.hasNearDecoration() ? 16 : 0;
		this.body = entity.getBody(portionShower).asTextBlock(FontParam.CLASS_ATTRIBUTE, skinParam);

		header = new EntityImageClassHeader2(entity, skinParam, portionShower);
		this.url = entity.getUrl99();
		if (entity.getMouseOver() == null) {
			this.mouseOver = null;
		} else {
			this.mouseOver = entity.getMouseOver().asTextBlock(FontParam.CLASS_ATTRIBUTE, skinParam);
		}

	}

	// private int marginEmptyFieldsOrMethod = 13;

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

	final public void drawU(UGraphic ug) {
		if (url != null) {
			ug.startUrl(url);
		}
		drawInternal(ug);
		if (mouseOver != null) {
			// final UGroup g = ug.createGroup();
			// ug = ug.apply(new UChangeBackColor(SkinParamUtils.getColor(getSkinParam(), ColorParam.classBackground,
			// getStereo())));
			// final Dimension2D dim = mouseOver.calculateDimension(ug.getStringBounder());
			// final Shadowable rect = new URectangle(dim.getWidth(), dim.getHeight());
			// if (getSkinParam().shadowing()) {
			// rect.setDeltaShadow(4);
			// }
			//
			// final HtmlColor classBorder = SkinParamUtils.getColor(getSkinParam(), ColorParam.classBorder,
			// getStereo());
			// ug = ug.apply(
			// new UChangeBackColor(SkinParamUtils.getColor(getSkinParam(), ColorParam.classBackground,
			// getStereo()))).apply(new UChangeColor(classBorder));
			//
			// final double x = 30;
			// final double y = 30;
			// // ug.getParam().setStroke(new UStroke(1.5));
			// // g.draw(x, y, rect);
			// // ug.getParam().resetStroke();
			// final UGraphic ug2 = new UGraphicHorizontalLine(ug, x, x + dim.getWidth(), getStroke());
			// mouseOver.drawUNewWayINLINED(ug2.apply(new UTranslate(x, y)));
			// g.close();
		}

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
		final Shadowable rect = new URectangle(widthTotal, heightTotal, roundCorner, roundCorner);
		if (getSkinParam().shadowing()) {
			rect.setDeltaShadow(4);
		}

		HtmlColor classBorder = lineConfig.getSpecificLineColor();
		if (classBorder == null) {
			classBorder = SkinParamUtils.getColor(getSkinParam(), ColorParam.classBorder, getStereo());
		}
		ug = ug.apply(new UChangeColor(classBorder));
		HtmlColor backcolor = getEntity().getSpecificBackColor();
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), ColorParam.classBackground, getStereo());
		}
		ug = ug.apply(new UChangeBackColor(backcolor));

		final UStroke stroke = getStroke();
		ug.apply(stroke).draw(rect);

		header.drawU(ug, dimTotal.getWidth(), dimHeader.getHeight());

		if (body != null) {
			final UGraphic ug2 = new UGraphicStencil(ug, this, stroke);
			body.drawU(ug2.apply(new UTranslate(0, dimHeader.getHeight())));
		}
	}

	private UStroke getStroke() {
		UStroke stroke = lineConfig.getSpecificLineStroke();
		if (stroke == null) {
			stroke = getSkinParam().getThickness(LineParam.classBorder, getStereo());
		}
		if (stroke == null) {
			stroke = new UStroke(1.5);
		}
		return stroke;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return shield;
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return 0;
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return calculateDimension(stringBounder).getWidth();
	}

}
