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
package net.sourceforge.plantuml.graphic;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class TextBlockSimple implements TextBlock {

	private final List<Line> lines = new ArrayList<Line>();

	protected TextBlockSimple(List<? extends CharSequence> texts, FontConfiguration fontConfiguration,
			HorizontalAlignement horizontalAlignement) {
		for (CharSequence s : texts) {
			if (s instanceof Stereotype) {
				lines.add(createLineForStereotype(fontConfiguration, (Stereotype) s, horizontalAlignement));
			} else {
				lines.add(new SingleLine(s.toString(), fontConfiguration, horizontalAlignement));
			}
		}
	}

	private SingleLine createLineForStereotype(FontConfiguration fontConfiguration, Stereotype s,
			HorizontalAlignement horizontalAlignement) {
		assert s.getLabel() != null;
		return new SingleLine(s.getLabel(), fontConfiguration.add(FontStyle.ITALIC), horizontalAlignement);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return getTextDimension(stringBounder);
	}

	protected final Dimension2D getTextDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (Line line : lines) {
			final Dimension2D size2D = line.calculateDimension(stringBounder);
			height += size2D.getHeight();
			width = Math.max(width, size2D.getWidth());
		}
		return new Dimension2DDouble(width, height);
	}

	public void drawTOBEREMOVED(Graphics2D g2d, double x, double y) {
		final Dimension2D dimText = getTextDimension(StringBounderUtils.asStringBounder(g2d));

		for (Line line : lines) {
			final HorizontalAlignement lineHorizontalAlignement = line.getHorizontalAlignement();
			double deltaX = 0;
			if (lineHorizontalAlignement == HorizontalAlignement.CENTER) {
				final double diff = dimText.getWidth()
						- line.calculateDimension(StringBounderUtils.asStringBounder(g2d)).getWidth();
				deltaX = diff / 2.0;
			}
			line.draw(g2d, x + deltaX, y);
			y += line.calculateDimension(StringBounderUtils.asStringBounder(g2d)).getHeight();
		}
	}

	public void drawU(UGraphic ug, double x, double y) {
		final Dimension2D dimText = getTextDimension(ug.getStringBounder());

		for (Line line : lines) {
			final HorizontalAlignement lineHorizontalAlignement = line.getHorizontalAlignement();
			double deltaX = 0;
			if (lineHorizontalAlignement == HorizontalAlignement.CENTER) {
				final double diff = dimText.getWidth() - line.calculateDimension(ug.getStringBounder()).getWidth();
				deltaX = diff / 2.0;
			}
			line.drawU(ug, x + deltaX, y);
			y += line.calculateDimension(ug.getStringBounder()).getHeight();
		}
	}

}
