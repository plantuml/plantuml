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

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.util.Collection;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graph.MethodsOrFieldsArea;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class EntityImageClass2 extends AbstractEntityImage2 {

	final private TextBlock name;
	final private MethodsOrFieldsArea methods;
	final private MethodsOrFieldsArea fields;
	final private CircledCharacter circledCharacter;

	// private final int xMargin = 0;
	// private final int yMargin = 0;
	// public static final int MARGIN = 20;

	public EntityImageClass2(IEntity entity, Rose rose, ISkinParam skinParam, Collection<Link> links) {
		super(entity);
		this.name = TextBlockUtils.create(StringUtils.getWithNewlines(entity.getDisplay()), getFont14(), Color.BLACK,
				HorizontalAlignement.CENTER);
		this.methods = new MethodsOrFieldsArea(entity.methods2(), getFont14());
		this.fields = new MethodsOrFieldsArea(entity.fields2(), getFont14());

		circledCharacter = getCircledCharacter(entity);
	}

	private CircledCharacter getCircledCharacter(IEntity entity) {
		// if (entity.getStereotype() != null) {
		// return new CircledCharacter(entity.getStereotype().getCharacter(),
		// font, entity.getStereotype().getColor(),
		// red, Color.BLACK);
		// }
		final double radius = 10;
		if (entity.getType() == EntityType.ABSTRACT_CLASS) {
			return new CircledCharacter('A', radius, getFont17(), getBlue(), getRed(), Color.BLACK);
		}
		if (entity.getType() == EntityType.CLASS) {
			return new CircledCharacter('C', radius, getFont17(), getGreen(), getRed(), Color.BLACK);
		}
		if (entity.getType() == EntityType.INTERFACE) {
			return new CircledCharacter('I', radius, getFont17(), getViolet(), getRed(), Color.BLACK);
		}
		if (entity.getType() == EntityType.ENUM) {
			return new CircledCharacter('E', radius, getFont17(), getRose(), getRed(), Color.BLACK);
		}
		assert false;
		return null;
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dimName = getNameDimension(stringBounder);
		final Dimension2D dimMethods = methods.calculateDimension(stringBounder);
		final Dimension2D dimFields = fields.calculateDimension(stringBounder);
		final double width = Math.max(Math.max(dimMethods.getWidth(), dimFields.getWidth()), dimName.getWidth());
		final double height = dimMethods.getHeight() + dimFields.getHeight() + dimName.getHeight();
		return new Dimension2DDouble(width, height);
	}

	private Dimension2D getNameDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		if (circledCharacter == null) {
			return nameDim;
		}
		return new Dimension2DDouble(nameDim.getWidth() + getCircledWidth(stringBounder), Math.max(nameDim.getHeight(),
				circledCharacter.getPreferredHeight(stringBounder)));
	}

	private double getCircledWidth(StringBounder stringBounder) {
		if (circledCharacter == null) {
			return 0;
		}
		return circledCharacter.getPreferredWidth(stringBounder) + 3;
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, double marginWidth,
			double marginHeight) {
		final Dimension2D dimTotal = getDimension(ug.getStringBounder());
		final Dimension2D dimName = getNameDimension(ug.getStringBounder());
		final Dimension2D dimFields = fields.calculateDimension(ug.getStringBounder());

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final URectangle rect = new URectangle(widthTotal, heightTotal);

		ug.getParam().setColor(getRed());
		ug.getParam().setBackcolor(getYellow());

		double x = xTheoricalPosition;
		double y = yTheoricalPosition;
		ug.draw(x, y, rect);

		if (circledCharacter != null) {
			circledCharacter.drawU(ug, x, y);
			x += circledCharacter.getPreferredWidth(ug.getStringBounder());
		}
		name.drawU(ug, x, y);

		y += dimName.getHeight();

		x = xTheoricalPosition;
		ug.getParam().setColor(getRed());
		ug.draw(x, y, new ULine(widthTotal, 0));
		fields.draw(ug, x, y);

		y += dimFields.getHeight();
		ug.getParam().setColor(getRed());
		ug.draw(x, y, new ULine(widthTotal, 0));

		methods.draw(ug, x, y);
	}
}
