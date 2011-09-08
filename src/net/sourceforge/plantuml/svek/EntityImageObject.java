/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graph.MethodsOrFieldsArea2;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGroup;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageObject extends AbstractEntityImage {

	final private TextBlock name;
	final private TextBlock stereo;
	// final private MethodsOrFieldsArea2 methods;
	final private MethodsOrFieldsArea2 fields;

	public EntityImageObject(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();
		this.name = TextBlockUtils.create(entity.getDisplay2(), new FontConfiguration(getFont(FontParam.OBJECT,
				stereotype), getFontColor(FontParam.OBJECT, stereotype)), HorizontalAlignement.CENTER);
		if (stereotype == null || stereotype.getLabel() == null) {
			this.stereo = null;
		} else {
			this.stereo = TextBlockUtils.create(StringUtils.getWithNewlines(stereotype.getLabel()),
					new FontConfiguration(getFont(FontParam.OBJECT_STEREOTYPE, stereotype), getFontColor(
							FontParam.OBJECT_STEREOTYPE, stereotype)), HorizontalAlignement.CENTER);
		}
		// this.methods = new MethodsOrFieldsArea2(entity.getMethodsToDisplay(),
		// FontParam.OBJECT_ATTRIBUTE, skinParam);
		this.fields = new MethodsOrFieldsArea2(entity.getFieldsToDisplay(), FontParam.OBJECT_ATTRIBUTE, skinParam);
	}

	private int xMarginFieldsOrMethod = 5;
	private int marginEmptyFieldsOrMethod = 13;

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dimTitle = getTitleDimension(stringBounder);
		// final Dimension2D dimMethods =
		// methods.calculateDimension(stringBounder);
		final Dimension2D dimFields = fields.calculateDimension(stringBounder);
		final double width = Math.max(dimFields.getWidth() + 2 * xMarginFieldsOrMethod, dimTitle.getWidth() + 2
				* xMarginCircle);
		final double height = getMethodOrFieldHeight(dimFields) + dimTitle.getHeight();
		return new Dimension2DDouble(width, height);
	}

	private double getMethodOrFieldHeight(final Dimension2D dim) {
		final double fieldsHeight = dim.getHeight();
		if (fieldsHeight == 0) {
			return marginEmptyFieldsOrMethod;
		}
		return fieldsHeight;
	}

	private int xMarginCircle = 5;

	private Dimension2D getTitleDimension(StringBounder stringBounder) {
		return getNameAndSteretypeDimension(stringBounder);
	}

	private Dimension2D getNameAndSteretypeDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo == null ? new Dimension2DDouble(0, 0) : stereo
				.calculateDimension(stringBounder);
		final Dimension2D nameAndStereo = new Dimension2DDouble(Math.max(nameDim.getWidth(), stereoDim.getWidth()),
				nameDim.getHeight() + stereoDim.getHeight());
		return nameAndStereo;
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = getDimension(stringBounder);
		final Dimension2D dimTitle = getTitleDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = new URectangle(widthTotal, heightTotal);
		if (getSkinParam().shadowing()) {
			rect.setDeltaShadow(4);
		}

		ug.getParam().setColor(getColor(ColorParam.objectBorder, getStereo()));
		ug.getParam().setBackcolor(getColor(ColorParam.objectBackground, getStereo()));

		double x = xTheoricalPosition;
		double y = yTheoricalPosition;
		ug.getParam().setStroke(new UStroke(1.5));
		ug.draw(x, y, rect);
		ug.getParam().setStroke(new UStroke());

		final UGroup header = new UGroup(new PlacementStrategyY1Y2(ug.getStringBounder()));
		if (stereo != null) {
			header.add(stereo);
		}
		header.add(name);
		header.drawU(ug, x, y, dimTotal.getWidth(), dimTitle.getHeight());

		y += dimTitle.getHeight();

		x = xTheoricalPosition;
		ug.getParam().setColor(getColor(ColorParam.objectBorder, getStereo()));
		ug.getParam().setStroke(new UStroke(1.5));
		ug.draw(x, y, new ULine(widthTotal, 0));
		ug.getParam().setStroke(new UStroke());
		fields.drawU(ug, x + xMarginFieldsOrMethod, y);

	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
