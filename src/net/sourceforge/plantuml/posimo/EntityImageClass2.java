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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.util.Collection;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
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

public class EntityImageClass2 extends AbstractEntityImage2 {

	final private TextBlock name;
	final private TextBlock stereo;
	final private MethodsOrFieldsArea2 methods;
	final private MethodsOrFieldsArea2 fields;
	final private CircledCharacter circledCharacter;

	public EntityImageClass2(IEntity entity, ISkinParam skinParam, Collection<Link> links) {
		super(entity, skinParam);
		this.name = TextBlockUtils.create(entity.getDisplay2(), new FontConfiguration(
				getFont(FontParam.CLASS), HtmlColor.BLACK), HorizontalAlignement.CENTER);
		final Stereotype stereotype = entity.getStereotype();
		if (stereotype == null || stereotype.getLabel() == null) {
			this.stereo = null;
		} else {
			this.stereo = TextBlockUtils
					.create(StringUtils.getWithNewlines(stereotype.getLabel()), new FontConfiguration(
							getFont(FontParam.CLASS_STEREOTYPE), getFontColor(FontParam.CLASS_STEREOTYPE)),
							HorizontalAlignement.CENTER);
		}
		this.methods = new MethodsOrFieldsArea2(entity.getMethodsToDisplay(), FontParam.CLASS_ATTRIBUTE, skinParam);
		this.fields = new MethodsOrFieldsArea2(entity.getFieldsToDisplay(), FontParam.CLASS_ATTRIBUTE, skinParam);

		circledCharacter = getCircledCharacter(entity);
	}

	private CircledCharacter getCircledCharacter(IEntity entity) {
		final Stereotype stereotype = entity.getStereotype();
		if (stereotype != null && stereotype.getCharacter() != 0) {
			final HtmlColor classBorder = getColor(ColorParam.classBorder);
			final UFont font = getFont(FontParam.CIRCLED_CHARACTER);
			return new CircledCharacter(stereotype.getCharacter(), getSkinParam().getCircledCharacterRadius(), font,
					stereotype.getHtmlColor(), classBorder, getFontColor(FontParam.CIRCLED_CHARACTER));
		}
		if (entity.getType() == EntityType.ABSTRACT_CLASS) {
			return new CircledCharacter('A', getSkinParam().getCircledCharacterRadius(),
					getFont(FontParam.CIRCLED_CHARACTER), getColor(ColorParam.stereotypeABackground),
					getColor(ColorParam.classBorder), getFontColor(FontParam.CIRCLED_CHARACTER));
		}
		if (entity.getType() == EntityType.CLASS) {
			return new CircledCharacter('C', getSkinParam().getCircledCharacterRadius(),
					getFont(FontParam.CIRCLED_CHARACTER), getColor(ColorParam.stereotypeCBackground),
					getColor(ColorParam.classBorder), getFontColor(FontParam.CIRCLED_CHARACTER));
		}
		if (entity.getType() == EntityType.INTERFACE) {
			return new CircledCharacter('I', getSkinParam().getCircledCharacterRadius(),
					getFont(FontParam.CIRCLED_CHARACTER), getColor(ColorParam.stereotypeIBackground),
					getColor(ColorParam.classBorder), getFontColor(FontParam.CIRCLED_CHARACTER));
		}
		if (entity.getType() == EntityType.ENUM) {
			return new CircledCharacter('E', getSkinParam().getCircledCharacterRadius(),
					getFont(FontParam.CIRCLED_CHARACTER), getColor(ColorParam.stereotypeEBackground),
					getColor(ColorParam.classBorder), getFontColor(FontParam.CIRCLED_CHARACTER));
		}
		assert false;
		return null;
	}

	private int xMarginFieldsOrMethod = 5;
	private int marginEmptyFieldsOrMethod = 13;

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dimTitle = getTitleDimension(stringBounder);
		final Dimension2D dimMethods = methods.calculateDimension(stringBounder);
		final Dimension2D dimFields = fields.calculateDimension(stringBounder);
		final double width = Math.max(Math.max(dimMethods.getWidth() + 2 * xMarginFieldsOrMethod, dimFields.getWidth()
				+ 2 * xMarginFieldsOrMethod), dimTitle.getWidth() + 2 * xMarginCircle);
		final double height = getMethodOrFieldHeight(dimMethods) + getMethodOrFieldHeight(dimFields)
				+ dimTitle.getHeight();
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
	private int yMarginCircle = 5;

	private Dimension2D getTitleDimension(StringBounder stringBounder) {
		final Dimension2D nameAndStereo = getNameAndSteretypeDimension(stringBounder);
		if (circledCharacter == null) {
			return nameAndStereo;
		}
		return new Dimension2DDouble(nameAndStereo.getWidth() + getCircledWidth(stringBounder), Math.max(nameAndStereo
				.getHeight(), circledCharacter.getPreferredHeight(stringBounder) + 2 * yMarginCircle));
	}

	private Dimension2D getNameAndSteretypeDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo == null ? new Dimension2DDouble(0, 0) : stereo
				.calculateDimension(stringBounder);
		final Dimension2D nameAndStereo = new Dimension2DDouble(Math.max(nameDim.getWidth(), stereoDim.getWidth()),
				nameDim.getHeight() + stereoDim.getHeight());
		return nameAndStereo;
	}

	private double getCircledWidth(StringBounder stringBounder) {
		if (circledCharacter == null) {
			return 0;
		}
		return circledCharacter.getPreferredWidth(stringBounder) + 3;
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, double marginWidth,
			double marginHeight) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = getDimension(stringBounder);
		final Dimension2D dimTitle = getTitleDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final URectangle rect = new URectangle(widthTotal, heightTotal);

		ug.getParam().setColor(getColor(ColorParam.classBorder));
		ug.getParam().setBackcolor(getColor(ColorParam.classBackground));

		double x = xTheoricalPosition;
		double y = yTheoricalPosition;
		ug.draw(x, y, rect);

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
		ug.getParam().setColor(getColor(ColorParam.classBorder));
		ug.draw(x, y, new ULine(widthTotal, 0));
		fields.drawU(ug, x + xMarginFieldsOrMethod, y);

		y += getMethodOrFieldHeight(fields.calculateDimension(stringBounder));
		ug.getParam().setColor(getColor(ColorParam.classBorder));
		ug.draw(x, y, new ULine(widthTotal, 0));

		methods.drawU(ug, x + xMarginFieldsOrMethod, y);
	}
}
