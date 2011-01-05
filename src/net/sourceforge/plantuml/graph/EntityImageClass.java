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
 * Revision $Revision: 5798 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

class EntityImageClass extends AbstractEntityImage {

	final private TextBlock name;
	final private MethodsOrFieldsArea methods;
	final private MethodsOrFieldsArea fields;
	final private CircledCharacter circledCharacter;

	private final int xMargin = 10;
	private final int yMargin = 6;

	public EntityImageClass(Entity entity) {
		super(entity);
		this.name = TextBlockUtils.create(StringUtils.getWithNewlines(entity.getDisplay()), getFont14(), Color.BLACK,
				HorizontalAlignement.CENTER);
		this.methods = new MethodsOrFieldsArea(entity.methods2(), getFont14());
		this.fields = new MethodsOrFieldsArea(entity.fields2(), getFont14());

		circledCharacter = getCircledCharacter(entity);

	}

	private CircledCharacter getCircledCharacter(Entity entity) {
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
		final double width = Math.max(Math.max(dimMethods.getWidth(), dimFields.getWidth()), dimName.getWidth()) + 2
				* xMargin;
		final double height = dimMethods.getHeight() + dimFields.getHeight() + dimName.getHeight() + 6 * yMargin;
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

	@Override
	public void draw(Graphics2D g2d) {
		final Dimension2D dimTotal = getDimension(StringBounderUtils.asStringBounder(g2d));
		final Dimension2D dimName = getNameDimension(StringBounderUtils.asStringBounder(g2d));
		final Dimension2D dimFields = fields.calculateDimension(StringBounderUtils.asStringBounder(g2d));

		final int width = (int) dimTotal.getWidth();
		final int height = (int) dimTotal.getHeight();
		g2d.setColor(getYellow());
		g2d.fillRect(0, 0, width, height);

		g2d.setColor(getRed());
		g2d.drawRect(0, 0, width - 1, height - 1);

		final double line1 = dimName.getHeight() + 2 * yMargin;
		final double line2 = dimName.getHeight() + dimFields.getHeight() + 4 * yMargin;

		g2d.drawLine(0, (int) line1, width, (int) line1);
		g2d.drawLine(0, (int) line2, width, (int) line2);

		final double circledWidth = getCircledWidth(StringBounderUtils.asStringBounder(g2d));
		g2d.setColor(Color.BLACK);
		name.drawTOBEREMOVED(g2d, xMargin + circledWidth, yMargin);
		fields.drawTOBEREMOVED(g2d, xMargin, line1 + yMargin);
		methods.drawTOBEREMOVED(g2d, xMargin, line2 + yMargin);

		if (circledCharacter != null) {
			circledCharacter.draw(g2d, xMargin, yMargin, 1.0);
		}

	}
}
