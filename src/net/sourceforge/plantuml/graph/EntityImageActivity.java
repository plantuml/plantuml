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
 * Revision $Revision: 6009 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

class EntityImageActivity extends AbstractEntityImage {

	final private TextBlock text;

	private final int xMargin = 10;
	private final int yMargin = 6;

	public EntityImageActivity(Entity entity) {
		super(entity);
		this.text = TextBlockUtils.create(StringUtils.getWithNewlines(entity.getDisplay()), new FontConfiguration(
				getFont14(), Color.BLACK), HorizontalAlignement.CENTER);
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dim = text.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, 2 * xMargin, 2 * yMargin);
	}

	@Override
	public void draw(Graphics2D g2d) {
		final Dimension2D dimTotal = getDimension(StringBounderUtils.asStringBounder(g2d));

		final int width = (int) dimTotal.getWidth();
		final int height = (int) dimTotal.getHeight();

		final Polygon p = new Polygon();
		p.addPoint(0, yMargin * 2);
		p.addPoint(xMargin * 2, 0);

		p.addPoint(width - 2 * xMargin, 0);
		p.addPoint(width, 2 * yMargin);

		p.addPoint(width, height - 2 * yMargin);
		p.addPoint(width - 2 * xMargin, height);

		p.addPoint(xMargin * 2, height);
		p.addPoint(0, height - 2 * yMargin);

		g2d.setColor(getYellow());
		g2d.fill(p);
		// g2d.fillRect(0, 0, width, height);

		g2d.setColor(getRed());
		g2d.draw(p);
		// g2d.drawRect(0, 0, width - 1, height - 1);
		g2d.setColor(Color.BLACK);
		text.drawTOBEREMOVED(g2d, xMargin, yMargin);

	}
}
