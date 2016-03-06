/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 19109 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.ColorMapper;

class EntityImageNote extends AbstractEntityImage {

	final private TextBlock text;

	private final int xMargin = 10;
	private final int yMargin = 10;

	public EntityImageNote(IEntity entity) {
		super(entity);
		this.text = entity.getDisplay().create(FontConfiguration.blackBlueTrue(getFont14()),
				HorizontalAlignment.CENTER, new SpriteContainerEmpty());
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dim = text.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, 2 * xMargin, 2 * yMargin);
	}

	@Override
	public void draw(ColorMapper colorMapper, Graphics2D g2d) {
		final Dimension2D dimTotal = getDimension(StringBounderUtils.asStringBounder(g2d));

		final int width = (int) dimTotal.getWidth();
		final int height = (int) dimTotal.getHeight();

		final Polygon p = new Polygon();
		p.addPoint(0, 0);
		p.addPoint(width - xMargin, 0);
		p.addPoint(width, yMargin);
		p.addPoint(width, height);
		p.addPoint(0, height);

		g2d.setColor(colorMapper.getMappedColor(getYellowNote()));
		g2d.fill(p);

		g2d.setColor(colorMapper.getMappedColor(getRed()));
		g2d.draw(p);
		g2d.drawLine(width - xMargin, 0, width - xMargin, yMargin);
		g2d.drawLine(width - xMargin, yMargin, width, yMargin);

		g2d.setColor(Color.BLACK);
		// text.drawTOBEREMOVED(colorMapper, g2d, xMargin, yMargin);

	}
}
