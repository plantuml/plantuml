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
 * Revision $Revision: 17068 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.ColorMapper;

class EntityImageUsecase extends AbstractEntityImage {

	final private TextBlock name;

	public EntityImageUsecase(IEntity entity) {
		super(entity);
		this.name = entity.getDisplay().create(FontConfiguration.blackBlueTrue(getFont14()),
				HorizontalAlignment.CENTER, new SpriteContainerEmpty());
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		// final double eps = Math.sqrt(nameDim.getWidth() /
		// nameDim.getHeight());
		// final double diag = Math.sqrt(nameDim.getWidth() * nameDim.getWidth()
		// + nameDim.getHeight()
		// * nameDim.getHeight());
		// return new Dimension2DDouble(diag * eps, diag / eps);
		final double eps = 1.7;
		return new Dimension2DDouble(nameDim.getWidth() * eps, nameDim.getHeight() * eps);
	}

	@Override
	public void draw(ColorMapper colorMapper, Graphics2D g2d) {
		final Dimension2D dimTotal = getDimension(StringBounderUtils.asStringBounder(g2d));

		// Shape ellipse = new Ellipse2D.Double(0, 0, dimTotal.getWidth(),
		// dimTotal.getHeight());
		final GeneralPath ellipse = new GeneralPath();
		final double h = dimTotal.getHeight();
		final double w = dimTotal.getWidth();
		ellipse.append(new QuadCurve2D.Double(0, h / 2, 0, 0, w / 2, 0), true);
		ellipse.append(new QuadCurve2D.Double(w / 2, 0, w, 0, w, h / 2), true);
		ellipse.append(new QuadCurve2D.Double(w, h / 2, w, h, w / 2, h), true);
		ellipse.append(new QuadCurve2D.Double(w / 2, h, 0, h, 0, h / 2), true);
		g2d.setColor(colorMapper.getMappedColor(getYellow()));
		g2d.fill(ellipse);

		g2d.setColor(colorMapper.getMappedColor(getRed()));
		g2d.draw(ellipse);

		// final Dimension2D nameDim = name.calculateDimension(StringBounderUtils.asStringBounder(g2d));
		// final double posx = (w - nameDim.getWidth()) / 2;
		// final double posy = (h - nameDim.getHeight()) / 2;
		// final Shape rect = new Rectangle2D.Double(posx, posy, nameDim.getWidth(), nameDim.getHeight());
		// g2d.draw(rect);

		g2d.setColor(Color.BLACK);
		// name.drawTOBEREMOVED(colorMapper, g2d, posx, posy);
	}
}
