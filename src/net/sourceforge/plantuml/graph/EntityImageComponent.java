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
 * Revision $Revision: 4959 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

class EntityImageComponent extends AbstractEntityImage {

	final private TextBlock name;
	private final float thickness = (float) 1.6;

	public EntityImageComponent(Entity entity) {
		super(entity);
		this.name = TextBlockUtils.create(StringUtils.getWithNewlines(entity.getDisplay()), getFont14(), Color.BLACK,
				HorizontalAlignement.CENTER);
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(nameDim, 20, 14);
	}

	private void drawRect(Graphics2D g2d, double x, double y, double width, double height) {
		g2d.setStroke(new BasicStroke(thickness));
		final Shape head = new Rectangle2D.Double(x, y, width, height);
		g2d.setColor(getYellow());
		g2d.fill(head);
		g2d.setColor(getRed());
		g2d.draw(head);

		g2d.setStroke(new BasicStroke());
	}

	@Override
	public void draw(Graphics2D g2d) {
		final Dimension2D dimTotal = getDimension(StringBounderUtils.asStringBounder(g2d));
		final Dimension2D nameDim = name.calculateDimension(StringBounderUtils.asStringBounder(g2d));

		drawRect(g2d, 6, 0, dimTotal.getWidth(), dimTotal.getHeight());
		drawRect(g2d, 0, 7, 12, 6);
		drawRect(g2d, 0, dimTotal.getHeight() - 7 - 6, 12, 6);

		g2d.setColor(Color.BLACK);
		name.drawTOBEREMOVED(g2d, 6 + (dimTotal.getWidth() - nameDim.getWidth()) / 2,
				(dimTotal.getHeight() - nameDim.getHeight()) / 2);
	}
}
