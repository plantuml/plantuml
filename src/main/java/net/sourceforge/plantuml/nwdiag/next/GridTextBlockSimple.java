/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.nwdiag.next;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorderNone;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;

public class GridTextBlockSimple implements TextBlock {

	public static final double MINIMUM_WIDTH = 70;

	protected final NwArray data;
	private final ISkinParam skinParam;

	public GridTextBlockSimple(int lines, int cols, ISkinParam skinParam) {
		this.skinParam = skinParam;
		this.data = new NwArray(lines, cols);
	}

	protected void drawGrid(UGraphic ug) {
	}

	public void drawU(UGraphic ug) {
		drawGrid(ug);
		final StringBounder stringBounder = ug.getStringBounder();
		double y = 0;
		for (int i = 0; i < data.getNbLines(); i++) {
			final double lineHeight = lineHeight(stringBounder, i);
			double x = 0;
			for (int j = 0; j < data.getNbCols(); j++) {
				final double colWidth = colWidth(stringBounder, j);
				if (data.get(i, j) != null)
					data.get(i, j).drawMe(ug.apply(new UTranslate(x, y)), colWidth, lineHeight);

				x += colWidth;
			}
			y += lineHeight;
		}
	}

	protected double colWidth(StringBounder stringBounder, final int j) {
		double width = 0;
		for (int i = 0; i < data.getNbLines(); i++)
			if (data.get(i, j) != null)
				width = Math.max(width, data.get(i, j).naturalDimension(stringBounder).getWidth());

		return width;
	}

	public double lineHeight(StringBounder stringBounder, final int i) {
		double height = 50;
		for (int j = 0; j < data.getNbCols(); j++)
			if (data.get(i, j) != null)
				height = Math.max(height, data.get(i, j).naturalDimension(stringBounder).getHeight());

		return height;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		if (data.getNbLines() == 0)
			return new XDimension2D(0, 0);

		double height = 0;
		for (int i = 0; i < data.getNbLines(); i++)
			height += lineHeight(stringBounder, i);

		double width = 0;
		for (int j = 0; j < data.getNbCols(); j++)
			width += colWidth(stringBounder, j);

		return new XDimension2D(Math.max(MINIMUM_WIDTH, width), height);
	}

	@Override
	public XRectangle2D getInnerPosition(CharSequence member, StringBounder stringBounder) {
		throw new UnsupportedOperationException("member=" + member + " " + getClass().toString());
	}

	public MinMax getMinMax(StringBounder stringBounder) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void add(int i, int j, NServerDraw value) {
		data.set(i, j, value);
	}

	protected final ISkinParam getSkinParam() {
		return skinParam;
	}

	@Override
	public MagneticBorder getMagneticBorder() {
		return new MagneticBorderNone();
	}

	@Override
	public HColor getBackcolor() {
		return null;
	}

}
