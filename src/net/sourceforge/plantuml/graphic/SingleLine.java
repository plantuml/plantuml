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
 * Revision $Revision: 3834 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class SingleLine implements Line {

	private final List<Tile> blocs = new ArrayList<Tile>();
	private final HorizontalAlignement horizontalAlignement;

	public SingleLine(String text, Font font, Color paint, HorizontalAlignement horizontalAlignement) {
		this.horizontalAlignement = horizontalAlignement;
		final Splitter lineSplitter = new Splitter(text);

		FontConfiguration fontConfiguration = new FontConfiguration(font, paint);

		for (HtmlCommand cmd : lineSplitter.getHtmlCommands()) {
			if (cmd instanceof Text) {
				final String s = ((Text) cmd).getText();
				blocs.add(new TileText(s, fontConfiguration));
			} else if (cmd instanceof Img) {
				blocs.add(((Img) cmd).createMonoImage());
			} else if (cmd instanceof FontChange) {
				fontConfiguration = ((FontChange) cmd).apply(fontConfiguration);
			}
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (Tile b : blocs) {
			final Dimension2D size2D = b.calculateDimension(stringBounder);
			width += size2D.getWidth();
			height = Math.max(height, size2D.getHeight());
		}
		return new Dimension2DDouble(width, height);
	}

	private double maxDeltaY(Graphics2D g2d) {
		double result = 0;
		final Dimension2D dim = calculateDimension(StringBounderUtils.asStringBounder(g2d));
		for (Tile b : blocs) {
			if (b instanceof TileText == false) {
				continue;
			}
			final Dimension2D dimBloc = b.calculateDimension(StringBounderUtils.asStringBounder(g2d));
			final double deltaY = dim.getHeight() - dimBloc.getHeight() + ((TileText) b).getFontSize2D();
			result = Math.max(result, deltaY);
		}
		return result;
	}

	private double maxDeltaY(UGraphic ug) {
		double result = 0;
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		for (Tile b : blocs) {
			if (b instanceof TileText == false) {
				continue;
			}
			final Dimension2D dimBloc = b.calculateDimension(ug.getStringBounder());
			final double deltaY = dim.getHeight() - dimBloc.getHeight() + ((TileText) b).getFontSize2D();
			result = Math.max(result, deltaY);
		}
		return result;
	}

	public void draw(Graphics2D g2d, double x, double y) {
		final double deltaY = maxDeltaY(g2d);
		for (Tile b : blocs) {
			if (b instanceof TileImage) {
				b.draw(g2d, x, y);
			} else {
				b.draw(g2d, x, y + deltaY);
			}
			x += b.calculateDimension(StringBounderUtils.asStringBounder(g2d)).getWidth();
		}
	}

	public void drawU(UGraphic ug, double x, double y) {
		final double deltaY = maxDeltaY(ug);
		for (Tile b : blocs) {
			if (b instanceof TileImage) {
				b.drawU(ug, x, y);
			} else {
				b.drawU(ug, x, y + deltaY);
			}
			x += b.calculateDimension(ug.getStringBounder()).getWidth();
		}
	}

	public HorizontalAlignement getHorizontalAlignement() {
		return horizontalAlignement;
	}
}
