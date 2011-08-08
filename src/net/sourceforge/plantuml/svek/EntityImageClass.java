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
import net.sourceforge.plantuml.MathUtils;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graph.MethodsOrFieldsArea2;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyX1Y2Y3;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGroup;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageClass extends AbstractEntityImage {

	final private TextBlock name;
	final private TextBlock stereo;
	final private MethodsOrFieldsArea2 methods;
	final private MethodsOrFieldsArea2 fields;
	final private CircledCharacter circledCharacter;

	public EntityImageClass(IEntity entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);

		final boolean italic = entity.getType() == EntityType.ABSTRACT_CLASS
				|| entity.getType() == EntityType.INTERFACE;

		final HtmlColor color = getFontColor(FontParam.CLASS, getStereo());
		final Stereotype stereotype = entity.getStereotype();
		FontConfiguration fontConfigurationName = new FontConfiguration(getFont(FontParam.CLASS, stereotype), color);
		if (italic) {
			fontConfigurationName = fontConfigurationName.italic();
		}
		this.name = TextBlockUtils.create(entity.getDisplay2(), fontConfigurationName,
				HorizontalAlignement.CENTER);

		if (stereotype == null || stereotype.getLabel() == null
				|| portionShower.showPortion(EntityPortion.STEREOTYPE, entity) == false) {
			this.stereo = null;
		} else {
			this.stereo = TextBlockUtils.create(StringUtils.getWithNewlines(stereotype.getLabel()),
					new FontConfiguration(getFont(FontParam.CLASS_STEREOTYPE, stereotype), getFontColor(
							FontParam.CLASS_STEREOTYPE, stereotype)), HorizontalAlignement.CENTER);
		}

		// see LabelBuilderHtmlHeaderTableForObjectOrClass for colors

		final boolean showMethods = portionShower.showPortion(EntityPortion.METHOD, getEntity());
		if (showMethods) {
			this.methods = new MethodsOrFieldsArea2(entity.getMethodsToDisplay(), FontParam.CLASS_ATTRIBUTE, skinParam);
		} else {
			this.methods = null;
		}

		final boolean showFields = portionShower.showPortion(EntityPortion.FIELD, getEntity());
		if (showFields) {
			this.fields = new MethodsOrFieldsArea2(entity.getFieldsToDisplay(), FontParam.CLASS_ATTRIBUTE, skinParam);
		} else {
			this.fields = null;
		}

		if (portionShower.showPortion(EntityPortion.CIRCLED_CHARACTER, getEntity())) {
			circledCharacter = getCircledCharacter(entity);
		} else {
			circledCharacter = null;
		}
	}

	private CircledCharacter getCircledCharacter(IEntity entity) {
		final Stereotype stereotype = entity.getStereotype();
		if (stereotype != null && stereotype.getCharacter() != 0) {
			final HtmlColor classBorder = getColor(ColorParam.classBorder, stereotype);
			final UFont font = getFont(FontParam.CIRCLED_CHARACTER, null);
			return new CircledCharacter(stereotype.getCharacter(), getSkinParam().getCircledCharacterRadius(), font,
					stereotype.getHtmlColor(), classBorder, getFontColor(FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getType() == EntityType.ABSTRACT_CLASS) {
			return new CircledCharacter('A', getSkinParam().getCircledCharacterRadius(), getFont(
					FontParam.CIRCLED_CHARACTER, null), getColor(ColorParam.stereotypeABackground, stereotype),
					getColor(ColorParam.classBorder, stereotype), getFontColor(FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getType() == EntityType.CLASS) {
			return new CircledCharacter('C', getSkinParam().getCircledCharacterRadius(), getFont(
					FontParam.CIRCLED_CHARACTER, null), getColor(ColorParam.stereotypeCBackground, stereotype),
					getColor(ColorParam.classBorder, stereotype), getFontColor(FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getType() == EntityType.INTERFACE) {
			return new CircledCharacter('I', getSkinParam().getCircledCharacterRadius(), getFont(
					FontParam.CIRCLED_CHARACTER, null), getColor(ColorParam.stereotypeIBackground, stereotype),
					getColor(ColorParam.classBorder, stereotype), getFontColor(FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getType() == EntityType.ENUM) {
			return new CircledCharacter('E', getSkinParam().getCircledCharacterRadius(), getFont(
					FontParam.CIRCLED_CHARACTER, null), getColor(ColorParam.stereotypeEBackground, stereotype),
					getColor(ColorParam.classBorder, stereotype), getFontColor(FontParam.CIRCLED_CHARACTER, null));
		}
		assert false;
		return null;
	}

	private int xMarginFieldsOrMethod = 5;
	private int marginEmptyFieldsOrMethod = 13;

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dimTitle = getTitleDimension(stringBounder);
		final Dimension2D dimMethods = methods == null ? new Dimension2DDouble(0, 0) : methods
				.calculateDimension(stringBounder);
		final Dimension2D dimFields = fields == null ? new Dimension2DDouble(0, 0) : fields
				.calculateDimension(stringBounder);
		final double width = MathUtils.max(dimMethods.getWidth() + 2 * xMarginFieldsOrMethod, dimFields.getWidth() + 2
				* xMarginFieldsOrMethod, dimTitle.getWidth() + 2 * xMarginCircle);
		final double height = getMethodOrFieldHeight(dimMethods, EntityPortion.METHOD)
				+ getMethodOrFieldHeight(dimFields, EntityPortion.FIELD) + dimTitle.getHeight();
		return new Dimension2DDouble(width, height);
	}

	private double getMethodOrFieldHeight(final Dimension2D dim, EntityPortion portion) {
		if (methods == null && portion == EntityPortion.METHOD) {
			return 0;
		}
		if (fields == null && portion == EntityPortion.FIELD) {
			return 0;
		}
		final double fieldsHeight = dim.getHeight();
		if (fieldsHeight == 0) {
			return marginEmptyFieldsOrMethod;
		}
		return fieldsHeight;
	}

	private int xMarginCircle = 5;
	private int yMarginCircle = 5;

	private Dimension2D getTitleDimension(StringBounder stringBounder) {
		final Dimension2D nameAndStereo = getNameAndSteretypeDimension(stringBounder);
		if (circledCharacter == null) {
			return Dimension2DDouble.atLeast(nameAndStereo, 4 * xMarginCircle, 6 * yMarginCircle);
		}
		return new Dimension2DDouble(nameAndStereo.getWidth() + getCircledWidth(stringBounder), Math.max(nameAndStereo
				.getHeight(), circledCharacter.getPreferredHeight(stringBounder) + 2 * yMarginCircle));
	}

	private Dimension2D getNameAndSteretypeDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo == null ? new Dimension2DDouble(0, 0) : stereo
				.calculateDimension(stringBounder);
		return Dimension2DDouble.mergeTB(nameDim, stereoDim);
	}

	private double getCircledWidth(StringBounder stringBounder) {
		if (circledCharacter == null) {
			return 0;
		}
		return circledCharacter.getPreferredWidth(stringBounder) + 3;
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = getDimension(stringBounder);
		final Dimension2D dimTitle = getTitleDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final URectangle rect = new URectangle(widthTotal, heightTotal);

		ug.getParam().setColor(getColor(ColorParam.classBorder, getStereo()));
		ug.getParam().setBackcolor(getColor(ColorParam.classBackground, getStereo()));

		double x = xTheoricalPosition;
		double y = yTheoricalPosition;
		ug.getParam().setStroke(new UStroke(1.5));
		ug.draw(x, y, rect);
		ug.getParam().setStroke(new UStroke());

		final UGroup header;
		if (circledCharacter == null) {
			header = new UGroup(new PlacementStrategyY1Y2(ug.getStringBounder()));
		} else {
			header = new UGroup(new PlacementStrategyX1Y2Y3(ug.getStringBounder()));
			header.add(circledCharacter);
		}
		if (stereo != null) {
			header.add(stereo);
		}
		header.add(name);
		header.drawU(ug, x, y, dimTotal.getWidth(), dimTitle.getHeight());

		y += dimTitle.getHeight();

		x = xTheoricalPosition;
		if (fields != null) {
			ug.getParam().setColor(getColor(ColorParam.classBorder, getStereo()));
			ug.getParam().setStroke(new UStroke(1.5));
			ug.draw(x, y, new ULine(widthTotal, 0));
			ug.getParam().setStroke(new UStroke());
			fields.draw(ug, x + xMarginFieldsOrMethod, y);
			y += getMethodOrFieldHeight(fields.calculateDimension(stringBounder), EntityPortion.FIELD);
		}

		if (methods != null) {
			ug.getParam().setColor(getColor(ColorParam.classBorder, getStereo()));
			ug.getParam().setStroke(new UStroke(1.5));
			ug.draw(x, y, new ULine(widthTotal, 0));
			ug.getParam().setStroke(new UStroke());
			methods.draw(ug, x + xMarginFieldsOrMethod, y);
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

}
